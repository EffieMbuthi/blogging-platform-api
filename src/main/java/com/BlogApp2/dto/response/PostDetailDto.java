package com.BlogApp2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;




@Getter
@Setter //the setter isn't there for your code to call. It's there for Jackson to call,
       // on your behalf, as its mechanism for populating an object it just created
@NoArgsConstructor //create an empty object first
                  // no-args constructor → Jackson has no way to construct the object at all. IT NEEDS THIS.
                  //needed for deserialization=> json- object
@AllArgsConstructor // entity(from the database)-→ DTO (building a response to send back out)
                    // Not "DTO to entity and vice versa" as one interchangeable thing
                    // — it's specifically the entity-to-DTO direction (outgoing).
                    //what about the dto--> entity? Instead, your Service layer reads the incoming DTO with GETTERS, and builds a brand-new Post entity

// has the full comments list, because this endpoint is specifically "give me everything about this one post."
public class PostDetailDto {
    private UUID id;
    private String title;
    private String body;
    private AuthorSummaryDto author;
    private Set<String> tagNames;
    private List<CommentResponse> comments;
    private double averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}