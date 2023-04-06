package com.oracle.oracle.training.entity.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oracle.oracle.training.services.functional.PostUtilityService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@Builder
@Table(name = "post_feedback_tbl")
public class PostFeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private Integer upvotesCount;
    private Integer  commentsCount;
    //Comma seperated user who liked the post
    @JsonIgnore
    private String likedUserList;

    @JsonIgnore
    @Lob
    private byte[] commentsData;

    @Transient
    private List<Comments> commentsList;

    public PostFeedBack(){
        this.setUpvotesCount(0);
        this.setCommentsCount(0);
        this.setLikedUserList("");
        this.setCommentsData(new PostUtilityService().getBytes(new ArrayList<>()));
    }

}
