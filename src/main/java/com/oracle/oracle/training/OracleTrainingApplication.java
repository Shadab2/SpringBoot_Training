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
//        };
//    }

}
