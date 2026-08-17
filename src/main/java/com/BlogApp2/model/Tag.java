package com.BlogApp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@Table(name= "tags")
public class Tag extends BaseEntity {
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Post> posts= new HashSet<>();

    @Column(nullable = false, unique = true)
    private String name;
}
