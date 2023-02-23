package com.oracle.oracle.training.services;

import com.oracle.oracle.training.entity.UserProfile;

import java.util.List;

public interface UserProfileService {
    List<UserProfile> saveIfNotExistsAndfetchAllUsers();
}
