package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.entity.Address;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
//    @Autowired
//    EmailService emailService;

    @PutMapping
    public ResponseEntity<String> updateUser(HttpServletRequest request,@RequestBody User user){
        String email = (String) request.getAttribute("email");
        userService.editUser(email,user);
        return new ResponseEntity<>("updated sucessfully",HttpStatus.OK);
    }

    @PostMapping("/upload-profile")
    public ResponseEntity<Map<String,String>> uploadProfile(HttpServletRequest request,@RequestParam("file") MultipartFile file){
        String email = (String)request.getAttribute("email");
        log.info("File Received , {} , {} , {} ",file.getContentType(),file.getOriginalFilename(),file.getName());
        String uploadedImage = userService.uploadProfileImage(email, file);
        return new ResponseEntity<>(Map.of("image",uploadedImage),HttpStatus.CREATED);
    }

    @PostMapping("/address")
    public ResponseEntity<List<Address>> addAddress(HttpServletRequest request, @RequestBody Address address){
        String email = (String) request.getAttribute("email");
        return  new ResponseEntity<>(userService.addAddress(email,address),HttpStatus.OK);
    }

    @DeleteMapping("/address/{addressId}")
    public  ResponseEntity<String> deleteAddress(HttpServletRequest request,@PathVariable("addressId") Integer addressId){
        String email = (String) request.getAttribute("email");
        userService.deleteAddress(email,addressId);
        return new ResponseEntity<>("deleted sucessfully",HttpStatus.OK);
    }
}
