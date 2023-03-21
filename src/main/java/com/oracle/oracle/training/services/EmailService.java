//package com.oracle.oracle.training.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import static com.oracle.oracle.training.constants.Constants.SENDER;
//
//@Service
//public class EmailService {
//    @Autowired
//    JavaMailSender javaMailSender;
//
//    public void sendEmail(String toEmail,String subject,String body){
//        SimpleMailMessage smm = new SimpleMailMessage();
//        smm.setFrom(SENDER);
//        smm.setTo(toEmail);
//        smm.setSubject(subject);
//        smm.setText(body);
//        javaMailSender.send(smm);
//        System.out.println("Mail sent succesfully to "+toEmail  );
//    }
//}
