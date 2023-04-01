package com.oracle.oracle.training.entity.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "techstack_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true)
    private String name;
    private String description;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "techStacks")
    List<ResourcePost> resourcePostList;
}
