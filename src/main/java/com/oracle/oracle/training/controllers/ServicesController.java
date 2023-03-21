package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.consumingwebservice.rest.FetchUserClient;
import com.oracle.oracle.training.consumingwebservice.soap.CountryClient;
import com.oracle.oracle.training.consumingwebservice.soap.TemparatureConvertorClient;
import com.oracle.oracle.training.dto.UserProfileList;
import com.oracle.oracle.training.entity.Company;
import com.oracle.oracle.training.entity.UserProfile;
import com.oracle.oracle.training.entity.purchase.PurchaseOrder;
import com.oracle.oracle.training.services.UserProfileService;
import com.oracle.oracle.training.websamples.ArrayOftCountryCodeAndName;
import com.oracle.oracle.training.websamples.TCountryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServicesController {
    @Autowired
    CountryClient countryClient;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    TemparatureConvertorClient temparatureConvertorClient;

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

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        return new ResponseEntity<>(userProfileService.getAllCompanies(),HttpStatus.OK);
    }

//    @GetMapping("/users/{id}")
//    public ResponseEntity<InputStreamResource> downloadUserInfoInExcel(@PathVariable("id") Integer id) throws IOException {
//        ByteArrayInputStream in = userProfileService.getUserDownloadInfo(id);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        String resposnse = Base64.getEncoder().encodeToString(in.readAllBytes());
//        httpHeaders.add("Content-Disposition","attachement; filename=User.xlsx");
//        return  ResponseEntity.ok().headers(httpHeaders)
//                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                .body(new InputStreamResource(in));
//    }

    @GetMapping("/temparature")
    public ResponseEntity<String> convertTemparature(){
        return new ResponseEntity<>(temparatureConvertorClient.convert().getCelsiusToFahrenheitResult(),HttpStatus.OK);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userProfileService.getUser(id),HttpStatus.OK);
    }


    @GetMapping("/users/{id}/download")
    public String downloadUserInfoInExcel(@PathVariable("id") Integer id) throws IOException {
        ByteArrayInputStream in = userProfileService.getUserDownloadInfo(id);
        HttpHeaders httpHeaders = new HttpHeaders();
       return Base64.getEncoder().encodeToString(in.readAllBytes());
    }

    @GetMapping("/purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders(){
        return new ResponseEntity<>(userProfileService.getPurchaseOrders(),HttpStatus.OK);
    }

}
