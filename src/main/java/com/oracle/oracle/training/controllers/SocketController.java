package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.entity.Message;
import com.oracle.oracle.training.services.interfaces.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SocketController {
    @Autowired
    MessageService messageService;
    @MessageMapping("/message")
    @SendTo("/global/chat")
    public Message sendMessage(@RequestBody Message message){
       return messageService.sendMessage(message);
    }

}
