package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    boolean existsById(Integer id);
}
