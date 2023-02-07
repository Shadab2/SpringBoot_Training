package com.oracle.oracle.training.services;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jBCrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String mobileNo) throws BadRequestException {
        if(firstName.trim().length()==0 || lastName.trim().length()==0 || email.trim().length()==0 || password.trim().length()==0)
            throw new BadRequestException("Request Invalid");
        Integer countOfEmail = userRepository.countByEmail(email);
        if(countOfEmail==1) throw new BadRequestException("Email Id is already in used");
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(hashedPassword);
        user.setMobileNo(mobileNo);

        return userRepository.save(user);
    }

    @Override
    public UserDto verifyLogin(String email, String password) throws ResourceNotFound {
        UserDto resUser;
        try{
            User user = userRepository.findByEmail(email);
            if(!BCrypt.checkpw(password,user.getPassword())) throw new Exception();
           resUser = UserDto.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .mobileNo(user.getMobileNo())
                    .profileImage(user.getProfileImage())
                    .build();
        }catch (Exception e){
            throw  new ResourceNotFound("Email/password is invalid");
        }
        return resUser;
    }

    @Override
    public boolean editUser(User user) throws ResourceNotFound {
        User savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser==null) throw new ResourceNotFound("No such user");
        if(user.getFirstName() == null) user.setFirstName(savedUser.getFirstName());
        if(user.getLastName()==null) user.setLastName(savedUser.getLastName());
        if(user.getPassword()==null) user.setPassword(savedUser.getPassword());
        else  user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        if(user.getMobileNo() == null) user.setMobileNo(savedUser.getMobileNo());
        System.out.println(user);
        userRepository.updateUser(user.getFirstName(),user.getLastName(),user.getMobileNo(),user.getPassword(),user.getEmail());
        return true;
    }

    @Override
    public String uploadProfileImage(String email, MultipartFile file) throws BadRequestException {
        User user = userRepository.findByEmail(email);
        Set<String> allowedFileTypes = new HashSet<>(Arrays.asList("image/jpeg","image/png"));
        String profileImage = null;
        if(user==null) throw new BadRequestException("No such user");
        if(!allowedFileTypes.contains(file.getContentType())) throw new BadRequestException("Invalid file:q type");
        try {
            profileImage = Base64.getEncoder().encodeToString(file.getBytes());
            userRepository.updateProfileImage(email,profileImage);
            return userRepository.findByEmail(email).getProfileImage();
        } catch (IOException e) {
            throw new BadRequestException("Invalid File");
        }
    }
}
