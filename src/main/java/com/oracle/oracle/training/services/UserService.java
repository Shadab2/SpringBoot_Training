package com.oracle.oracle.training.services;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
     User createUser(String firstName, String lastName, String email, String password, String mobileNo) throws BadRequestException;

    UserDto verifyLogin(String email, String password) throws ResourceNotFound;

    boolean editUser(String email, User user) throws ResourceNotFound;

    String uploadProfileImage(String email, MultipartFile file) throws BadRequestException;

    List<UserPublicDto> findAllRegisteredUsers(String email) throws AccessDeniedException;

}
