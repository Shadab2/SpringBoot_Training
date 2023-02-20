package com.oracle.oracle.training.dto;

import com.oracle.oracle.training.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileList {
    private List<UserProfile> userProfileList;
}
