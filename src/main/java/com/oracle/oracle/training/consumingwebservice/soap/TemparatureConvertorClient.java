package com.oracle.oracle.training.consumingwebservice.soap;

import com.oracle.oracle.training.websamples.wsdl.CelsiusToFahrenheit;
import com.oracle.oracle.training.websamples.wsdl.CelsiusToFahrenheitResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class TemparatureConvertorClient extends WebServiceGatewaySupport {

    public CelsiusToFahrenheitResponse convert(){
        CelsiusToFahrenheit celsiusToFahrenheit =  new CelsiusToFahrenheit();
        celsiusToFahrenheit.setCelsius("100");
        CelsiusToFahrenheitResponse celsiusToFahrenheitResponse = (CelsiusToFahrenheitResponse) getWebServiceTemplate().marshalSendAndReceive(celsiusToFahrenheit);
        return  celsiusToFahrenheitResponse;
    }
}
