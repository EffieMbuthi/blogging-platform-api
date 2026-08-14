package com.BlogApp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude= {"user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@Table(name= "posts")
public class Post extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name= "post_tags",
            joinColumns = @JoinColumn(name= "post_id"),
            inverseJoinColumns = @JoinColumn(name= "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}
