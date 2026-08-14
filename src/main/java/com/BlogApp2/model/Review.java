package com.BlogApp2.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude= {"user", "post"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@Table(
        name= "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
)
public class Review extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "user_id")
    private User user;

    @Column(nullable = false)
    private int rating;
}
