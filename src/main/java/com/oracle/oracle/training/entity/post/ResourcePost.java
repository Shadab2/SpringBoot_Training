package com.oracle.oracle.training.entity.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oracle.oracle.training.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourcePost {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(length = 1000)
    private String description;
    private String accessLevel = "PUBLIC";
    @ManyToMany(fetch = FetchType.EAGER)
    private List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "pc_piid", referencedColumnName = "post_id")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "pc_rpid", referencedColumnName = "post_id")
    private List<Link> resourceLinks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private PostFeedBack postFeedBack;
    private String dateModified;
}
