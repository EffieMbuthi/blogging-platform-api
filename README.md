# BlogApp2

I built this project as a Spring Boot web application for a blogging platform, layered as controller, service, and repository, exposing the same underlying data through both a REST API and a GraphQL API. It covers users, posts, comments, tags, and reviews, with validation, centralized exception handling, OpenAPI documentation, and AOP based logging and performance monitoring.

## Requirements

- Java 17
- Maven, though I use the included wrapper (`mvnw` or `mvnw.cmd`), so a separate Maven installation is not required
- PostgreSQL, running locally or reachable over the network
- Spring Boot 4.1.0, declared as the parent in `pom.xml`, along with Spring Framework's usual dependency management

## Setting up the database

I use PostgreSQL, and I keep three separate databases, one per profile, each owned by its own dedicated role rather than the PostgreSQL superuser, since an application should never hold more database privilege than it actually needs.

For local development and for running tests, create two databases and two matching roles in PostgreSQL, for example through pgAdmin or `psql`:

- A database named `blogapp2_dev`, owned by a role named `blogapp2_dev`.
- A database named `blogapp2_test`, owned by a role named `blogapp2_test`.

I have not created a production database as part of this project, since it has only ever run locally, but `application-prod.properties` is written so that a real deployment would supply its own database, username, and password entirely through environment variables, described below.

I also rely on one PostgreSQL feature that Hibernate cannot create for me automatically, an index that lets post title searches use the database's index rather than scanning every row. After the application has started at least once against `blogapp2_dev` (so the `posts` table exists), run this once, and again against `blogapp2_test`:

```sql
CREATE INDEX idx_posts_title_lower_pattern ON posts (LOWER(title) text_pattern_ops);
```

## Configuration profiles

I keep configuration split across four files in `src/main/resources`:

- `application.properties`, the values shared by every environment, and the line that selects `dev` as the default active profile.
- `application-dev.properties`, pointed at `blogapp2_dev`, with Hibernate allowed to evolve the schema automatically as I change entities (`ddl-auto=update`).
- `application-test.properties`, pointed at `blogapp2_test`, with the schema dropped and recreated on every run (`ddl-auto=create-drop`), so a test run never depends on leftover state from a previous one.
- `application-prod.properties`, which reads `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` from the environment with no fallback values at all, and refuses to alter the schema on its own (`ddl-auto=validate`). If those variables are not set, the application fails to start rather than silently connecting to the wrong place.

`dev` and `test` also accept `DB_USERNAME` and `DB_PASSWORD` as optional overrides, but fall back to `blogapp2_dev`/`devpassword` and `blogapp2_test`/`testpassword` respectively if those are not set, so the application runs immediately after the database setup above with no further configuration.

To run under a specific profile explicitly:

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

## Running the application

```
./mvnw spring-boot:run
```

On success, the log ends with a line resembling `Started BlogApp2Application in ... seconds`, and the application listens on port 8080.

## Testing the API

**REST**, through Swagger UI, generated automatically by Springdoc from the annotations on every controller:

```
http://localhost:8080/swagger-ui.html
```

Every endpoint is documented there, including expected status codes, and requests can be sent directly from that page.

**GraphQL**, through GraphiQL, an interactive in-browser query editor:

```
http://localhost:8080/graphiql
```

The schema is defined in `src/main/resources/graphql/schema.graphqls`, and covers `User`, `Post`, `Comment`, `Tag`, and `Review`, each with its own queries and mutations. A minimal example, fetching a post along with only the fields actually needed:

```graphql
query {
  getPost(id: "00000000-0000-0000-0000-000000000000") {
    title
    author {
      name
    }
    commentCount: comments { id }
  }
}
```

Both APIs run side by side on the same server and operate on the same service layer underneath, so a change made through one is immediately visible through the other.

## Response shape

Every REST response is wrapped consistently, carrying a status, a message, and the actual data:

```json
{
  "status": "SUCCESS",
  "message": "Post retrieved successfully",
  "data": { }
}
```

Errors, whether from a missing record, a conflicting business rule, or a failed validation, are handled centrally by `GlobalExceptionHandler` for REST, and by `GraphQLExceptionResolver` for GraphQL, so both surfaces report the same underlying problem in a form appropriate to each protocol, rather than one of them falling back to a raw, unhandled exception.

## Cross-cutting logging

`LoggingAspect`, in `com.BlogApp2.aop`, wraps every method in the service layer with logging and timing, using all three of Spring AOP's advice types for genuinely different purposes: `@Before` logs that a call has started, `@Around` measures and logs how long it actually took, including when the method throws rather than returns normally, and `@After` logs, at debug level, that a call reached completion at all, successfully or not, which is a distinct guarantee from either of the other two. These lines appear directly in the application's console output as the API is used.

## Project structure

```
controller/    REST controllers
graphql/       GraphQL resolvers and the GraphQL-specific exception resolver
service/       Service interfaces
service/impl/  Service implementations, where business rules and transactions live
repository/    Spring Data JPA repositories
model/         JPA entities
dto/request/   Incoming request bodies, carrying Bean Validation constraints
dto/response/  Outgoing response shapes, distinct from the entities themselves
mapper/        Entity to DTO conversions
exception/     Domain-specific exceptions
validation/    A custom Bean Validation constraint for unique emails
aop/           Cross-cutting logging and performance monitoring
```

See `PERFORMANCE_REPORT.md` for a closer look at how REST and GraphQL compare on this project, and where I found real room for further optimization.
