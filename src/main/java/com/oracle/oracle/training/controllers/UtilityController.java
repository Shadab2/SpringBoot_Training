package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.consumingwebservice.rest.AddressProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utility")
public class UtilityController {
    @Autowired
    AddressProviderClient addressProviderClient;
    @GetMapping("/countries")
    public ResponseEntity<String> getCountries(){
        return new ResponseEntity<>(addressProviderClient.getCountries().getBody(),HttpStatus.OK);
    }
    @GetMapping("/states/{countryName}")
    public ResponseEntity<String> getStates(@PathVariable("countryName") String Country){
        return new ResponseEntity<>(addressProviderClient.getStates(Country).getBody(), HttpStatus.OK);
    }
    @GetMapping("/cities/{stateName}")
    public ResponseEntity<String> getCities(@PathVariable("stateName") String City){
        return new ResponseEntity<>(addressProviderClient.getCities(City).getBody(),HttpStatus.OK);
    }
}
