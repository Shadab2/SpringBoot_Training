package com.oracle.oracle.training.entity.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "post_feedback_tbl")
public class PostFeedBack {
    @Id
    Integer id;

    private Integer upvotesCount;
    private Integer  commentsCount;
    //Comma seperated user who liked the post
    @JsonIgnore
    private String likedUserList;

    @JsonIgnore
    @Lob
    private byte[] commentsData;

    public PostFeedBack(Integer id){
        this.setId(id);
        this.setUpvotesCount(0);
        this.setCommentsCount(0);
        this.setLikedUserList("");
    }

}
