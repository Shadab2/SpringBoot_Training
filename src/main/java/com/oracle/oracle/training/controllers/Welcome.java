package com.oracle.oracle.training.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {

    @GetMapping("/")
    public String showWelcomeMessage(){
        return "Welcome to Oracle Spring Boot Traning!";
    }
}
