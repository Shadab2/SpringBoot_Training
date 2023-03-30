package com.oracle.oracle.training.entity.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments implements Serializable {
    private Integer id;
    private Integer userId;
    private String message;
    private String userProfileImage;
    private String userName;

    private String dateModified;

}
