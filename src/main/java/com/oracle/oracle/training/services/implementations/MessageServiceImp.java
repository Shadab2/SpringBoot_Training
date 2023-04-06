package com.oracle.oracle.training.services.implementations;

import com.oracle.oracle.training.dto.UserPublicDto;
import com.oracle.oracle.training.entity.Message;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.MessageRepository;
import com.oracle.oracle.training.repository.UserRepository;
import com.oracle.oracle.training.services.interfaces.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImp implements MessageService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Override
    public Message sendMessage(Message message) {
        User user = userRepository.findByEmail(message.getSenderEmail());
        if(user==null) throw  new ResourceNotFound("No such user");

        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d y H:m:s ZZZ");
        String dateString = formatter.format(new Date());

        message.setDateModified(dateString);
        message.setSenderName(user.getFirstName()+" "+user.getLastName());
        message.setSenderProfileImage(user.getProfileImage());

        messageRepository.save(message);
        return message;
    }

    private List<Message> getSortedResults(){
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }
    public Page<Message> getPaginationMessage(String email,int page){
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        int size = 10;
        return messageRepository.findAll(PageRequest.of(page,size).withSort(Sort.by(Sort.Direction.DESC,"id")));
    }
}
