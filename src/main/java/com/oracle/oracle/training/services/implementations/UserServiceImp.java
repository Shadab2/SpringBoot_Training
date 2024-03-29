package com.oracle.oracle.training.services.implementations;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.entity.Address;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.UserRepository;
import com.oracle.oracle.training.services.functional.AuthService;
import com.oracle.oracle.training.services.interfaces.UserService;
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
        List<String> emptyFeilds =new ArrayList<>();
        if(firstName==null || firstName.trim().length()==0) emptyFeilds.add("firstName");
        if(lastName==null || lastName.trim().length()==0) emptyFeilds.add("lastName");
        if(password==null || password.trim().length()==0) emptyFeilds.add("password");
        if(lastName==null || email.trim().length()==0) emptyFeilds.add("email");
        if(mobileNo==null || mobileNo.trim().length()==0) emptyFeilds.add("mobileNo");

        if(emptyFeilds.size()>0) {
            log.error("Empty fields received in creating user with email {}",email);
            log.debug("User Creation for email {} has failed since following feilds are empty {} ",email,emptyFeilds);
            throw new BadRequestException("Request Invalid");
        }
        Integer countOfEmail = userRepository.countByEmail(email);
        if(countOfEmail==1) {
            log.error("A user with email {} is already present",email);
            throw new BadRequestException("Email Id is already in used");
        }
        String hashedPassword = BCrypt.hashpw(email+password,BCrypt.gensalt(10));
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
            if(!BCrypt.checkpw(email+password,user.getPassword())) {
                log.error("Password {} does not match",password);
                throw new ResourceNotFound("Resouce not found");
            }
           resUser = UserDto.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .mobileNo(user.getMobileNo())
                    .profileImage(user.getProfileImage())
                    .role(user.getRole())
                    .token(AuthService.generateJWTToken(user))
                   .addressList(user.getAddresses())
                    .build();
        }catch (Exception e){
            log.error("Exception : Resource Not Found  , Email/password is invalid ");
            throw  new ResourceNotFound("Email/password is invalid");
        }
        return resUser;
    }

    @Override
    public boolean editUser(String email, User user) throws ResourceNotFound , AccessDeniedException {
        User savedUser = userRepository.findByEmail(email);
        if(savedUser==null) {
            log.error("Exception : Resource Not Found , No user with email {} found!",user.getEmail());
            throw new ResourceNotFound("No such user");
        }
        if(!email.equals(user.getEmail())){
            log.error("Cannot update as token mismatch");
            throw new AccessDeniedException("Can only update your account");
        }
        if(user.getFirstName() != null) savedUser.setFirstName(user.getFirstName());
        if(user.getLastName()!= null) savedUser.setLastName(user.getLastName());
        if(user.getPassword() != null) savedUser.setPassword(BCrypt.hashpw(user.getEmail()+user.getPassword(),BCrypt.gensalt(10)));
        if(user.getMobileNo() != null) savedUser.setMobileNo(user.getMobileNo());
        userRepository.save(savedUser);
        log.info("User updated : {}",savedUser);
        return true;
    }

    @Override
    public String uploadProfileImage(String email, MultipartFile file) throws BadRequestException {
        User user = userRepository.findByEmail(email);
        Set<String> allowedFileTypes = new HashSet<>(Arrays.asList("image/jpeg","image/png"));
        String profileImage = null;
        if(user==null) {
            log.error("Exception : Bad Request Exception, No user with email {} found!",email);
            throw new BadRequestException("No such user");
        }
        if(!allowedFileTypes.contains(file.getContentType())) {
            log.error("Exception : Bad Request Exception , Invalid file type : {}",file.getContentType());
            throw new BadRequestException("Invalid file type");
        }
        try {
            profileImage = Base64.getEncoder().encodeToString(file.getBytes());
            user.setProfileImage(profileImage);
            userRepository.save(user);
            log.info("Profile image updated successfully.");
        } catch (IOException e) {
            log.error("Exception : IOException : Error Converting to base64");
            throw new BadRequestException("Invalid File");
        }
        return userRepository.findByEmail(email).getProfileImage();
    }

    @Override
    public List<UserPublicDto> findAllRegisteredUsers(String email) throws AccessDeniedException {
//        Integer isAdmin = userRepository.findRoleByEmail(email);
        boolean  isAdmin = userRepository.existsByEmailAndRole(email,0);
        if(!isAdmin) throw new AccessDeniedException("Unauthorized email");
        List<User> users = userRepository.findAll();
        List<UserPublicDto> userPublicDtos = users.stream().map(user->mapToPublicDto(user)).toList();
        return  userPublicDtos;
    }

    @Override
    public List<Address> addAddress(String email, Address address) throws ResourceNotFound,BadRequestException {
        User user = userRepository.findByEmail(email);
        if(user==null) throw new ResourceNotFound("No such user");
        if(user.getAddresses().size()==3) {
            throw  new BadRequestException("Cannot add more than 3 address");
        }
        try{
        user.getAddresses().add(address);
        userRepository.save(user);
        }catch (Exception e){
            throw  new BadRequestException("Empty feilds are not allowed");
        }
        return  user.getAddresses();
    }

    @Override
    public boolean deleteAddress(String email, Integer addressId) throws  BadRequestException,ResourceNotFound{
        User user = userRepository.findByEmail(email);
        if(user==null) throw new ResourceNotFound("No such user available");
        user.getAddresses().removeIf(address -> address.getId().equals(addressId));
        userRepository.save(user);
        return true;
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
