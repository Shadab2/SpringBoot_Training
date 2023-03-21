package com.oracle.oracle.training.consumingwebservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class FetchUserClient {
    @Autowired
    RestTemplate restTemplate;
    public String fetchAllUsers(){
         String url = "https://jsonplaceholder.typicode.com/users";
         return restTemplate.getForObject(url,String.class);
    }
}
