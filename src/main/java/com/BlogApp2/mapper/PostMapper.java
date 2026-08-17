package com.BlogApp2.mapper;

import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import com.BlogApp2.model.Comment;
import com.BlogApp2.model.Post;
import com.BlogApp2.model.Review;
import com.BlogApp2.model.Tag;
import com.BlogApp2.repository.CommentRepository;
import com.BlogApp2.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {
//fetching comments/reviews and passing them in.
// This is a real design choice with a trade-off:
// I chose it because "how do I compute a post's comment count and average rating" is
// squarely a mapping concern — turning a Post entity into its API representation
// — so the mapper owns it.
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final AuthorMapper authorMapper;
    private final CommentMapper commentMapper;

    // tagNames and commentCount are computed once for a whole page of posts by the
    // service layer, and passed in here, rather than this method querying for its own
    // post's tags and comment count individually, which is what used to make listing
    // posts issue several extra queries per post instead of a couple for the whole page.
    public PostSummaryDto toSummaryDto(Post post, Set<String> tagNames, long commentCount) {
        return new PostSummaryDto(
                post.getId(),
                post.getTitle(),
                buildPreview(post.getBody()),
                authorMapper.toAuthorSummary(post.getUser()),
                tagNames,
                (int) commentCount,
                post.getCreatedAt()
        );
    }

    public PostDetailDto toDetailDto(Post post) {
        List<Comment> comments = commentRepository.findByPostId(post.getId());
        List<Review> reviews = reviewRepository.findByPostId(post.getId());

        double averageRating = reviews.isEmpty()
                ? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        return new PostDetailDto(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                authorMapper.toAuthorSummary(post.getUser()),
                extractTagNames(post.getTags()),
                comments.stream().map(commentMapper::toResponse).toList(),
                averageRating,
                reviews.size(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private Set<String> extractTagNames(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

    private String buildPreview(String body) {
        if (body == null) return "";
        return body.length() <= 150 ? body : body.substring(0, 150) + "...";
    }
}