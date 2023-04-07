package com.oracle.oracle.training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="notification_tbl")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificaton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private NotificationType type;
    private String message;
    private String dateCreated;
    private String senderEmail;
    private String receiverEmail;
    private String  senderName;
    @Lob
    private String profileImage;
    private String postTitle;
    private String content;
}
