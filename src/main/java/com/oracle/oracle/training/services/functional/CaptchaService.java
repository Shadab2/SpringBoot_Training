package com.oracle.oracle.training.services.functional;

import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.utils.Captcha;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@Data
public class CaptchaService {
    private Random random = new Random();
    private Map<String,Captcha> captchaStore = new HashMap();

    public Map<String,String> generateCaptcha() {
        String x = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        StringBuilder cpt = new StringBuilder();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            cpt.append(x.charAt(random.nextInt(x.length())));
            id.append(x.charAt(random.nextInt(x.length())));
        }
        id.append(x.charAt(random.nextInt(x.length())));
        Captcha captcha = new Captcha(id.toString(),cpt.toString(),System.currentTimeMillis());
        captchaStore.put(captcha.getId(),captcha);
        return Map.of("captchaId",id.toString(),"captcha",captcha.getCAPTCHA());
    }


    public void verify(String id,String captcha) throws BadRequestException{
        if(!captchaStore.containsKey(id) || captchaStore.get(id).isExpired()) {
            log.error("{Captcha {} with id : {} has Expired",captcha,id);
            throw new BadRequestException("Invalid Captcha!");
        }
        if(!captchaStore.get(id).getCAPTCHA().equals(captcha)) {
            log.error("No such captcha with captcha {} found in  the directory",captcha);
            throw new BadRequestException("Invalid Captcha!");
        }
    }

    public void finishValidation(String id){
        if(captchaStore.containsKey(id)) captchaStore.remove(id);
    }

    private void clearBuffer(){
        for(Captcha captcha: captchaStore.values()) if(captcha.isExpired()) {
            log.info("captcha {} has been cleared : expired ",captcha.getCAPTCHA());
            captchaStore.remove(captcha);
        }
    }
}


