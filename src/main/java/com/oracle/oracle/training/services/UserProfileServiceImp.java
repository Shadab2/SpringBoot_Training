package com.oracle.oracle.training.services;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserProfileServiceImp implements UserProfileService{
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    FetchUserClient fetchUserClient;

    @Override
    public List<UserProfile> saveIfNotExistsAndfetchAllUsers() {
        UserProfile[] userProfiles= fetchUserClient.fetchAllUsers();
        for(UserProfile u:userProfiles){
            if(userProfileRepository.existsById(u.getId())) continue;
            userProfileRepository.save(u);
        }
        return userProfileRepository.findAll();
    }
}
