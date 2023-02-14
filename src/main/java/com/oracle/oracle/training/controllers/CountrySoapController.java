package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.consumingwebservice.CountryClient;
import com.oracle.oracle.training.websamples.ArrayOftCountryCodeAndName;
import com.oracle.oracle.training.websamples.TCountryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/country")
public class CountrySoapController {
    @Autowired
    CountryClient countryClient;
    @GetMapping("")
    public ResponseEntity<ArrayOftCountryCodeAndName> getAllCountries(){
        ArrayOftCountryCodeAndName list = countryClient.getAllCountryWithCodes().getListOfCountryNamesByCodeResult();
        return  new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/{countryIso}")
    public ResponseEntity<TCountryInfo> getCountryInfo(@PathVariable("countryIso") String countryISO){
        TCountryInfo tCountryInfo = countryClient.getCountryInfo(countryISO.toUpperCase()).getFullCountryInfoResult();
        return new ResponseEntity<>(tCountryInfo, HttpStatus.OK);
    }

}
