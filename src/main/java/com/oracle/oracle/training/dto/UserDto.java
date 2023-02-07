package com.oracle.oracle.training.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String profileImage;
}
