package com.oracle.oracle.training.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Embeddable
public class UserPublicDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profileImage;
}
