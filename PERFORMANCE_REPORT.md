# Performance Report: REST versus GraphQL

I wrote this report by reading through my own service and mapper layer closely, rather than by inventing numbers, since both APIs in this project sit on top of the exact same service methods. That fact turns out to matter more than it first appears, and it shapes most of what follows.

## The one fact that governs everything else here

`PostController` and `PostGraphQLController` both call `PostService.getAllPosts`. `UserController` and `UserGraphQLController` both call `UserService.getUserById`. This is true across every entity in the project. Neither API has its own separate business logic or its own separate database access, they are two different doors into the identical service layer, wrapped by the identical `LoggingAspect`, which already times every one of these calls and prints the result to the console as `Method X executed in Y ms`.

This means a claim like "GraphQL is faster than REST" or the reverse would be the wrong question to ask about this particular project. The service-layer execution time for, say, `getPostById`, is identical no matter which controller invoked it. Where REST and GraphQL genuinely differ here is not in that shared execution time, it is in two other things: how much data actually travels over the network, and how many round trips are needed to assemble a given view.

## Where GraphQL genuinely wins here: avoiding over-fetching

A REST client calling `GET /api/posts/{id}` always receives the complete `PostDetailDto` shape, title, full body, author, every tag name, every comment, the average rating, and both timestamps, whether it needs all of that or only the title and author. A GraphQL client asking only for those two fields:

```graphql
query {
  getPost(id: "...") {
    title
    author { name }
  }
}
```

receives only those two fields on the wire. For a post with many comments and reviews, that difference in payload size is not trivial. This is the textbook GraphQL advantage, and it applies directly here, since `PostDetailDto` is a genuinely large shape.

## A service-layer inefficiency GraphQL could not have fixed by itself, found and corrected

While writing this report I found a real, pre-existing inefficiency in `PostMapper`, worth being specific about rather than gesturing at "N+1 queries" abstractly. `PostMapper.toSummaryDto`, used by `PostService.getAllPosts`, and therefore by both `PostController`'s paginated post listing and `PostGraphQLController.getAllPosts`, used to do the following for every single post in a page:

- Call `commentRepository.findByPostId(post.getId()).size()` to compute a comment count, one query per post, purely to produce an integer.
- Read `post.getUser()`, which is `FetchType.LAZY`, triggering a separate query per post the first time it's touched.
- Read `post.getTags()`, also `FetchType.LAZY` and a `@ManyToMany`, triggering another separate query per post.

For a single page of ten posts, that was one query to fetch the page itself, plus up to thirty more, ten for comment counts, ten for authors, ten for tag sets, none of them batched. This inefficiency was hit identically whether the request arrived through REST or through GraphQL, because both call the same mapper, it was never a REST problem or a GraphQL problem, it was a service-layer data access problem both APIs inherited equally.

I fixed this rather than leave it documented. `PostRepository.findAll` and `findByTitleStartingWithIgnoreCase` now carry `@EntityGraph(attributePaths = "user")`, so the author comes back in the same query as the page itself, no more per-post query for it. Tag names for every post in the page are now fetched with one query, `PostRepository.findTagNamesForPostIds`, grouped in memory by post id afterward. Comment counts work the same way, one grouped query, `CommentRepository.countByPostIds`, using `GROUP BY c.post.id`, instead of one count query per post. `PostServiceImpl.getAllPosts` now runs this once per page rather than once per post, and `PostMapper.toSummaryDto` no longer queries anything at all, it just assembles a DTO from values it's handed. For a page of ten posts, this brings the total down from up to thirty-one queries to three, one for the page, one for tags, one for comment counts, regardless of the page size.

## A second finding, already corrected during this work

While comparing the two APIs field by field, I found that the GraphQL schema's `getAllPosts` query was declared to return the full `Post` type, including `body`, a full `comments` list, `averageRating`, and `reviewCount`, while the resolver behind it actually returned `PostSummaryDto`, which has none of those, only `bodyPreview` and a plain `commentCount`. A client asking `getAllPosts` for `body` or `comments` would have received either an error or a missing field at runtime, since this specific mismatch happens after Spring GraphQL's own startup validation, which checks nullability, not whether every possible returned Java type actually satisfies every field a schema type promises. I introduced a distinct `PostSummary` type to match what the resolver actually returns, mirroring the summary and detail split REST already had correctly. This is worth recording here specifically because it demonstrates a real risk of maintaining two parallel APIs over one service layer: a REST-side DTO split can silently drift out of sync with a GraphQL schema that was written to look like a single unified type, and nothing catches that until a client asks for the missing field.

## Round trips: the other real difference

Consider a view that needs a post's title, its author's name, and its three most recent comments' bodies. Over REST, as currently designed, that is two calls, `GET /api/posts/{id}` for the post and author, plus reading `comments` off the same response, since `PostDetailDto` already embeds full comment data. So REST is actually already reasonably efficient here, at the cost of also transmitting the review statistics and full body whether wanted or not. Over GraphQL, it is one call either way, and the client controls exactly which nested fields it receives, without the backend needing to have anticipated that particular combination in advance with a bespoke DTO.

## Measured results

I gathered these myself, through Postman against my own running instance, one real post with two tags, six comments, and one review. Two numbers are reported side by side wherever possible: Postman's total round trip time, and `LoggingAspect`'s own internal service-layer timing read from the console for the same stretch of calls.

**Single post, full detail (REST `GET /api/posts/{id}` versus GraphQL `getPost` requesting every field REST returns):**

| Call | REST round trip | REST internal (`getPostById`) | GraphQL round trip | GraphQL internal (`getPostById`) |
|---|---|---|---|---|
| 1st | 106 ms | 57 ms | 513 ms | 28 ms |
| 2nd | 31 ms | 20 ms | 35 ms | 18 ms |
| 3rd | 33 ms | 21 ms | 38 ms | 17 ms |
| 4th | 46 ms | 30 ms | 35 ms | 18 ms |
| 5th | 40 ms | 22 ms | 37 ms | 21 ms |

**Single post, GraphQL requesting only `title` and `author.name` (the narrow case GraphQL is meant for):**

| Call | Round trip | Internal (`getPostById`) |
|---|---|---|
| 1st | 35 ms | 16 ms |
| 2nd | 28 ms | 17 ms |
| 3rd | 25 ms | 14 ms |
| 4th | 30 ms | 17 ms |
| 5th | 25 ms | 14 ms |

**Paginated post list (REST `GET /api/posts` versus GraphQL `getAllPosts`, both against `PostSummary`/`PostSummaryDto`):**

| Call | REST round trip | REST internal (`getAllPosts`) | GraphQL round trip | GraphQL internal (`getAllPosts`) |
|---|---|---|---|---|
| 1st | 128 ms | 74 ms | 289 ms | 145 ms |
| 2nd | 26 ms | 18 ms | 38 ms | 18 ms |
| 3rd | 27 ms | 13 ms | 32 ms | 22 ms |
| 4th | 24 ms | 17 ms | 31 ms | 29 ms |
| 5th | 30 ms | 14 ms | — | 17 ms |

(The REST and GraphQL internal timings each come from the same `getAllPosts` service method, `LoggingAspect` cannot tell which controller called it, so the pairing above assumes the calls were made in the order I asked for them, REST's five first, then GraphQL's.)

**Write operations, for reference**: `createUser` 180 ms, `createPost` 685 ms, `createReview` 2880 ms round trip, 1764 ms internal.

## What the real numbers actually show

**The dominant pattern, visible in every single table above without exception**: the first call of any given kind is dramatically slower than every call after it, `getPostById` over REST goes 106 then settles to 31 to 46 ms, the identical query over GraphQL goes 513 then settles to 35 to 38 ms, and `getAllPosts` goes 128 then 24 to 30 ms over REST, 289 then 31 to 38 ms over GraphQL. This is not noise, it happens consistently across every distinct operation measured, including the write calls, `createReview`'s 1764 ms internal time on its first and only invocation is the most extreme example of the same effect. The cause is Hibernate and the JVM itself, not REST or GraphQL: Hibernate parses and prepares each distinct query shape lazily, the first time it's actually executed, and the JVM's JIT compiler has not yet optimized a code path it has never run. Both costs are paid exactly once per distinct operation, not once per request, which is exactly what these numbers show.

**Once warmed up, REST and GraphQL cost roughly the same for the identical full-detail request**, REST settling around 31 to 46 ms round trip, GraphQL asking for the equivalent full set of fields settling around 35 to 38 ms. That small gap is consistent with GraphQL's own query parsing and field-resolution machinery adding a little overhead on top of the same underlying service call, not with GraphQL being faster or slower in any way that matters here.

**Where GraphQL actually distinguishes itself is the narrow-fields case**: asking only for `title` and `author.name` settled around 25 to 30 ms, genuinely faster than either full-detail path, and this is the one difference in this whole dataset that isn't just warm-up noise, since it's the same warmed-up server, same post, same connection pool, the only thing that changed is how much data was requested and returned. That's GraphQL's real, demonstrated advantage on this project: not raw speed, but the ability to only pay for what was actually asked for.

**The internal service timings confirm the architectural point made earlier in this report**: REST and GraphQL's `getPostById` and `getAllPosts` internal numbers land in the same range as each other, because it's the same service method either way, `LoggingAspect` has no way to know which controller called it, and the numbers show exactly that, no systematic gap between the two once you account for warm-up.

## Summary

| Aspect | REST | GraphQL |
|---|---|---|
| Payload shape | Fixed per endpoint (`PostSummaryDto`, `PostDetailDto`) | Client-selected per query |
| Over-fetching | Present, `PostDetailDto` always sends everything | Avoided by design |
| Round trips for a custom view | Can require more than one, or a new endpoint | Usually one |
| Underlying service execution time | Identical, same service layer | Identical, same service layer |
| N+1 query risk in `getAllPosts` | Fixed | Fixed, identically, since it's the same call |
| Schema and DTO drift risk | Not applicable, DTOs are the contract | Real, and already caught once in this project |
