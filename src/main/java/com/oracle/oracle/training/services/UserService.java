package com.oracle.oracle.training.services;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
     User createUser(String firstName, String lastName, String email, String password, String mobileNo) throws BadRequestException;

    UserDto verifyLogin(String email, String password) throws ResourceNotFound;

    boolean editUser(User user) throws ResourceNotFound;

    String uploadProfileImage(String email, MultipartFile file) throws BadRequestException;
}
