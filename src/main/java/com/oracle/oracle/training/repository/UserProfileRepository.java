package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.Company;
import com.oracle.oracle.training.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    boolean existsById(Integer id);
    UserProfile findByUsername(String username);

}
