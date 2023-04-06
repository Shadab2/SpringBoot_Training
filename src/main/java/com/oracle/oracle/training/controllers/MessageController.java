package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.entity.Message;
import com.oracle.oracle.training.services.interfaces.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/message")
@RestController
public class MessageController {
    @Autowired
    MessageService messageService;
    @GetMapping()
    public ResponseEntity<Page<Message>> getPreviousMessage(HttpServletRequest request, @RequestParam("page") Integer page){
        String email = (String) request.getAttribute("email");
        return new ResponseEntity<>(messageService.getPaginationMessage(email,page), HttpStatus.OK);
    }
}
