package com.oracle.oracle.training.services.interfaces;

import com.oracle.oracle.training.entity.Message;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    Page<Message> getPaginationMessage(String email,int page);
}
