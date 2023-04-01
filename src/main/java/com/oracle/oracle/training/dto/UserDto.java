package com.oracle.oracle.training.dto;

import com.oracle.oracle.training.entity.Address;
import lombok.*;

import java.util.List;

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
    private Integer role;
    private String token;
    private List<Address> addressList;
}
