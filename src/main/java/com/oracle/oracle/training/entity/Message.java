package com.oracle.oracle.training.entity;

import com.oracle.oracle.training.dto.UserPublicDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false ,length = 1000)
    String message;

    @Column(nullable = false)
    private String senderEmail;
    private Integer receiverId;
    private String dateModified;

    @Lob
    private String senderProfileImage;
    private String senderName;
    private String type;
}
