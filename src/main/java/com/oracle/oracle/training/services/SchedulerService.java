package com.oracle.oracle.training.services;

import com.oracle.oracle.training.utils.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
public class SchedulerService {
    @Autowired
    CaptchaService captchaService;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void clearCaptchaBuffer(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = localDateTime.format(dateTimeFormatter);
        log.info("Scheduler running "+formattedTime);
        Map<String,Captcha> captchaStore = captchaService.getCaptchaStore();
        for(Captcha captcha:captchaStore.values()){
            if(captcha.isExpired()) {
                captchaStore.remove(captcha);
                log.info("captcha {} has been cleared : expired ",captcha.getCAPTCHA());
            }
        }
    }
}
