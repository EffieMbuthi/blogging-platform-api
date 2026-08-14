package com.BlogApp2.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"post", "user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name="comments")
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String body;
}
