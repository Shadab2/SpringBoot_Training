package com.oracle.oracle.training.services;

import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class AdminService {

    @Autowired
    UserRepository userRepository;

    private boolean isAdmin(String email){
        return userRepository.existsByEmailAndRole(email,0);
    }

    public String getSalesData(String email){
        if(!isAdmin(email)) throw new AccessDeniedException("Unauthorized");
        try{
        String salesContent = Files.readString(Path.of("C:\\Users\\shada\\IdeaProjects\\oracle_training\\src\\main\\resources\\json\\admin-dashboard-data.json"), Charset.defaultCharset());
        return  salesContent;
        }catch (IOException e){
        }
        return null;
    }
}
