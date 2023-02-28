package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import com.oracle.oracle.training.consumingwebservice.soap.CountryClient;
import com.oracle.oracle.training.dto.UserProfileList;
import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.entity.purchase.PurchaseOrder;
import com.oracle.oracle.training.services.UserProfileService;
import com.oracle.oracle.training.websamples.ArrayOftCountryCodeAndName;
import com.oracle.oracle.training.websamples.TCountryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServicesController {
    @Autowired
    CountryClient countryClient;
    @Autowired
    UserProfileService userProfileService;
    @GetMapping("/country")
    public ResponseEntity<ArrayOftCountryCodeAndName> getAllCountries(){
        ArrayOftCountryCodeAndName list = countryClient.getAllCountryWithCodes().getListOfCountryNamesByCodeResult();
        return  new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/country/{countryIso}")
    public ResponseEntity<TCountryInfo> getCountryInfo(@PathVariable("countryIso") String countryISO){
        TCountryInfo tCountryInfo = countryClient.getCountryInfo(countryISO.toUpperCase()).getFullCountryInfoResult();
        return new ResponseEntity<>(tCountryInfo, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfile>> getUsersInfo(){
        return new ResponseEntity<>(userProfileService.saveIfNotExistsAndfetchAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders(){
        return new ResponseEntity<>(userProfileService.getPurchaseOrders(),HttpStatus.OK);
    }

}
