package com.oracle.oracle.training.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProxyConfig {
    private String proxyHost;
    @Value("${proxyHost}")
    public void setProxyHost(String hostName){
        this.proxyHost = hostName;
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", "80");

        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", "80");
    }
}
