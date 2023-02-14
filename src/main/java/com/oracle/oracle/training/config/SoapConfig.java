package com.oracle.oracle.training.config;

import com.oracle.oracle.training.consumingwebservice.CountryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapConfig {
    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.oracle.oracle.training.websamples");
        return  jaxb2Marshaller;
    }
    @Bean
    public CountryClient countryClient(Jaxb2Marshaller marshaller){
        CountryClient client = new CountryClient();
        client.setDefaultUri("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return  client;
    }
}
