package com.BlogApp2.graphql;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.service.PostService;
import com.BlogApp2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class PostGraphQLController {
    private final PostService postService;
    private final ReviewService reviewService;

    //getPost(id: ID!): Post
    @QueryMapping //tells Spring this method implements one of the fields inside type Query in my schema."
    public PostDetailDto getPost(@Argument UUID id){
        return postService.getPostById(id);
    }

    // getAllPosts(search: String, page: Int = 0): [Post!]!
    @QueryMapping
    public List<PostSummaryDto> getAllPosts(@Argument String search, @Argument Integer page) {
        int pageNumber = (page != null) ? page : 0;
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());
        return postService.getAllPosts(search, pageable).getContent();
    }

    // getReviewsByPost(postId: ID!): [Review!]!
    @QueryMapping
    public List<ReviewResponse> getReviewsByPost(@Argument UUID postId) {
        return reviewService.getReviewsByPost(postId);
    }

    // createPost(input: CreatePostInput!): Post!
    @MutationMapping
    public PostDetailDto createPost(@Argument PostRequest input) {
        return postService.createPost(input);
    }

    // updatePost(id: ID!, input: UpdatePostInput!): Post!
    @MutationMapping
    public PostDetailDto updatePost(@Argument UUID id, @Argument PostRequest input) {
        return postService.updatePost(id, input);
    }

    // deletePost(id: ID!): Boolean!
    @MutationMapping
    public Boolean deletePost(@Argument UUID id) {
        postService.deletePost(id);
        return true;
    }
}
