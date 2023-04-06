package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.services.functional.AdminService;
import com.oracle.oracle.training.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserPublicDto>> findRegisterdUsers(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(userService.findAllRegisteredUsers(email),HttpStatus.OK);
    }

    @GetMapping("/sales-data")
    public ResponseEntity<String> getSalesData(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        return  new ResponseEntity<>(adminService.getSalesData(email),HttpStatus.OK);
    }
}
