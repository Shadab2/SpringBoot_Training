package com.oracle.oracle.training.consumingwebservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class AddressProviderClient {

    private String authToken;
    private HttpEntity httpEntity;
    private static final String BASE_URL = "https://www.universal-tutorial.com/api/";
    @Autowired
    RestTemplate restTemplate;

    @Value("${address.authtoken}")
    public void setHttpEntity(String authToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+authToken);
        httpEntity = new HttpEntity(httpHeaders);
    }

    public ResponseEntity<String> getCountries(){
        String url = BASE_URL + "countries";
        return  restTemplate.exchange(BASE_URL+"countries", HttpMethod.GET,httpEntity,String.class);
    }

    public ResponseEntity<String> getStates(String COUNTRY){
        return  restTemplate.exchange(BASE_URL+"states/"+COUNTRY, HttpMethod.GET,httpEntity,String.class);
    }
    public ResponseEntity<String> getCities(String STATE){
        return restTemplate.exchange(BASE_URL+"cities/"+STATE,HttpMethod.GET,httpEntity,String.class);
    }
}
