package com.oracle.oracle.training.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserPublicDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profileImage;
}
