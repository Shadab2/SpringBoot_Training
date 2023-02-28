package com.oracle.oracle.training;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OracleTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleTrainingApplication.class, args);
    }

//    @Bean
//    CommandLineRunner lookup(CountryClient quoteClient) {
//        return args -> {
//            String country = "INDIA";
//
//            if (args.length > 0) {
//                country = args[0];
//            }
////            CapitalCityResponse response = quoteClient.getCapitalCity(country);
////            System.out.println(response.getCapitalCityResult());
//            FullCountryInfoResponse fullCountryInfoResponse = quoteClient.getCountryInfo(country);
//            TCountryInfo tCountryInfo = fullCountryInfoResponse.getFullCountryInfoResult();
//            System.out.println("Name :"+tCountryInfo.getSName());
//            System.out.println("Flag :"+tCountryInfo.getSCountryFlag());
//            System.out.println("CapitalCity :"+tCountryInfo.getSCapitalCity());
//            System.out.println("ISO code :"+tCountryInfo.getSISOCode());
//            System.out.println("Phone code :"+tCountryInfo.getSPhoneCode());
//            System.out.println("Languages :"+tCountryInfo.getLanguages());
//        };
//    }

}
