package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    Integer countByEmail(String email);

    User findByEmail(String email);

    @Modifying
    @Query(
            value = "update user_tbl set first_name = ?1, last_name = ?2," +
                    "mobile_no = ?3,password = ?4 where email = ?5",
            nativeQuery = true
    )
    void updateUser(String firstName,String lastName,
                    String mobileNo,String password,
                     String emailId);

    @Modifying
    @Query (
            value = "update user_tbl set profile_image = :profileImage where email = :email",
            nativeQuery = true
    )
    void updateProfileImage(String email,String profileImage);

}