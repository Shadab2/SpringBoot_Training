package com.oracle.oracle.training.services;

import com.oracle.oracle.training.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CaptchaService {
    private String CAPTCHA;
    private Random random = new Random();
    private Map values = new HashMap();

    public Map<String,String> generateCaptcha() {
        String x = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        StringBuilder captcha = new StringBuilder();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            captcha.append(x.charAt(random.nextInt(x.length())));
            id.append(x.charAt(random.nextInt(x.length())));
        }
        id.append(x.charAt(random.nextInt(x.length())));
        values.put(id.toString(),captcha.toString());
        return Map.of("captchaId",id.toString(),"captcha",captcha.toString());
    }


    public void verify(String id,String captcha) throws BadRequestException{
        if(!values.containsKey(id)) throw  new BadRequestException("Captcha Expired");
        if(!values.get(id).equals(captcha)) throw new BadRequestException("Invalid Captcha!");
    }

    public void finishValidation(String id){
        if(values.containsKey(id)) values.remove(id);
    }
}


