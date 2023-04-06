package com.oracle.oracle.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificaton {
    private NotificationType type;
    private String message;
    private String dateCreated;
    private String senderEmail;
    private String  senderName;
    private String profileImage;
    private String postTitle;
    private String content;
}
