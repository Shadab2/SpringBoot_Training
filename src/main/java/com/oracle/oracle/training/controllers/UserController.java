package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.dto.UserDto;
import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.services.EmailService;
import com.oracle.oracle.training.services.UserService;
import com.oracle.oracle.training.services.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CaptchaService captchaService;
    @Autowired
    EmailService emailService;

    @GetMapping
    public ResponseEntity<Map<String,String>> getCaptcha(){
        return new ResponseEntity<>(captchaService.generateCaptcha(),HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPublicDto>> findRegisterdUsers(){
        return new ResponseEntity<>(userService.findAllRegisteredUsers(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody Map<String,Object> requestMap){
        String firstName = (String) requestMap.get("firstName");
        String email = (String) requestMap.get("email");
        String lastName = (String) requestMap.get("lastName");
        String password = (String) requestMap.get("password");
        String mobileNo = (String) requestMap.get("mobileNo");
        String captchaId = (String) requestMap.get("captchaId");
        String captcha = (String) requestMap.get("captcha");
        captchaService.verify(captchaId,captcha);
        User user = userService.createUser(firstName,lastName,email,password,mobileNo);
        captchaService.finishValidation(captchaId);
//        emailService.sendEmail(email,"Account Created",
//                "Hello, "+firstName+" Your account to Oracle tranings has sucessfully  created");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody Map<String,String> requestMap){
        String email = requestMap.get("email");
        String password = requestMap.get("password");
        UserDto userDto = userService.verifyLogin(email,password);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user){
        userService.editUser(user);
        return new ResponseEntity<>("updated sucessfully",HttpStatus.OK);
    }

    @PostMapping("/upload-profile")
    public ResponseEntity<Map<String,String>> uploadProfile(@RequestParam("file") MultipartFile file,@RequestParam("email") String email){
        log.info("File Received , {} , {} , {} ",file.getContentType(),file.getOriginalFilename(),file.getName());
        String uploadedImage = userService.uploadProfileImage(email, file);
        return new ResponseEntity<>(Map.of("image",uploadedImage),HttpStatus.CREATED);
    }

}
