package com.oracle.oracle.training.config;

import com.oracle.oracle.training.consumingwebservice.soap.CountryClient;
import com.oracle.oracle.training.consumingwebservice.soap.TemparatureConvertorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebServicesConfig {
    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("com.oracle.oracle.training.websamples.wsdl","com.oracle.oracle.training.websamples");
        return  jaxb2Marshaller;
    }
    @Bean
    public CountryClient countryClient(Jaxb2Marshaller marshaller){
        CountryClient client = new CountryClient();
        client.setDefaultUri("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return  client;
    } @Bean
    public TemparatureConvertorClient temparatureConvertorClient(Jaxb2Marshaller marshaller){
        TemparatureConvertorClient client = new TemparatureConvertorClient();
        client.setDefaultUri("https://www.w3schools.com/xml/tempconvert.asmx?WSDL");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return  client;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
