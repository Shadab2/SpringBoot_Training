package com.oracle.oracle.training.consumingwebservice;

import com.oracle.oracle.training.websamples.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Slf4j
public class CountryClient extends WebServiceGatewaySupport {
    public CapitalCityResponse getCapitalCity (String countryISOCode){
        CapitalCity capitalCityReq = new CapitalCity();
        capitalCityReq.setSCountryISOCode(countryISOCode);
        log.info("Request SOAP capitalCity for -> {}",countryISOCode);
        CapitalCityResponse capitalCityResponse =
                (CapitalCityResponse) getWebServiceTemplate()
                .marshalSendAndReceive(capitalCityReq);
        return  capitalCityResponse;
    }

    public FullCountryInfoResponse getCountryInfo(String countryISOCode){
        FullCountryInfo fullCountryInfoReq = new FullCountryInfo();
        fullCountryInfoReq.setSCountryISOCode(countryISOCode);
        log.info("Request SOAP CountryINFO for -> {}",countryISOCode);
        return (FullCountryInfoResponse)getWebServiceTemplate()
                .marshalSendAndReceive(fullCountryInfoReq);
    }

    public ListOfCountryNamesByCodeResponse getAllCountryWithCodes(){
        ListOfCountryNamesByCode listOfCountryNamesByCode = new ListOfCountryNamesByCode();
        return (ListOfCountryNamesByCodeResponse)getWebServiceTemplate().marshalSendAndReceive(listOfCountryNamesByCode);
    }
}
