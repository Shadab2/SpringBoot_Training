package com.oracle.oracle.training.services;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jBCrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String mobileNo) throws BadRequestException {
        if(firstName.trim().length()==0 || lastName.trim().length()==0 || email.trim().length()==0 || password.trim().length()==0) {
            log.info("Empty fields received in creating user with email {}",email);
            throw new BadRequestException("Request Invalid");
        }
        Integer countOfEmail = userRepository.countByEmail(email);
        if(countOfEmail==1) {
            log.info("A user with email {} is already present",email);
            throw new BadRequestException("Email Id is already in used");
        }
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(hashedPassword);
        user.setMobileNo(mobileNo);
        user.setRole(1);
        User createdUser = userRepository.save(user);
        log.info("User Created : {}",user);
        return createdUser;
    }

    @Override
    public UserDto verifyLogin(String email, String password) throws ResourceNotFound {
        UserDto resUser;
        try{
            User user = userRepository.findByEmail(email);
            if(!BCrypt.checkpw(password,user.getPassword())) {
                log.info("Password {} does not match",password);
                throw new Exception();
            }
           resUser = UserDto.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .mobileNo(user.getMobileNo())
                    .profileImage(user.getProfileImage())
                    .role(user.getRole())
                    .build();
            System.out.println(resUser.getRole()+" user Role");
        }catch (Exception e){
            log.error("Exception : Resource Not Found  , Email/password is invalid ");
            e.printStackTrace();
            throw  new ResourceNotFound("Email/password is invalid");
        }
        return resUser;
    }

    @Override
    public boolean editUser(User user) throws ResourceNotFound {
        User savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser==null) {
            log.info("Exception : Resource Not Found , No user with email {} found!",user.getEmail());
            throw new ResourceNotFound("No such user");
        }
        if(user.getFirstName() == null) user.setFirstName(savedUser.getFirstName());
        if(user.getLastName()==null) user.setLastName(savedUser.getLastName());
        if(user.getPassword()==null) {
            user.setPassword(savedUser.getPassword());
        } else {
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        }
        if(user.getMobileNo() == null) user.setMobileNo(savedUser.getMobileNo());
        log.info("User updated : {}",user);
        userRepository.updateUser(user.getFirstName(),user.getLastName(),user.getMobileNo(),user.getPassword(),user.getEmail());
        return true;
    }

    @Override
    public String uploadProfileImage(String email, MultipartFile file) throws BadRequestException {
        User user = userRepository.findByEmail(email);
        Set<String> allowedFileTypes = new HashSet<>(Arrays.asList("image/jpeg","image/png"));
        String profileImage = null;
        if(user==null) {
            log.info("Exception : Bad Request Exception, No user with email {} found!",email);
            throw new BadRequestException("No such user");
        }
        if(!allowedFileTypes.contains(file.getContentType())) {
            log.info("Exception : Bad Request Exception , Invalid file type : {}",file.getContentType());
            throw new BadRequestException("Invalid file type");
        }
        try {
            profileImage = Base64.getEncoder().encodeToString(file.getBytes());
            userRepository.updateProfileImage(email,profileImage);
        } catch (IOException e) {
            log.error("IOException : Error Converting to base64");
            e.printStackTrace();
            throw new BadRequestException("Invalid File");
        }
        return  profileImage;
    }

    @Override
    public List<UserPublicDto> findAllRegisteredUsers() {
        List<User> users = userRepository.findAll();
        List<UserPublicDto> userPublicDtos = users.stream().map(user->mapToPublicDto(user)).toList();
        return  userPublicDtos;
    }

    @Override
    public void updateRole(String email, Integer role) {
        userRepository.updateRole(email,role);
    }

    private UserPublicDto mapToPublicDto(User user){
         return UserPublicDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImage(user.getProfileImage())
                .build();
    }
}
