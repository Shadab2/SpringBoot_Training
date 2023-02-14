package com.oracle.oracle.training.config;

import org.springframework.stereotype.Component;

@Component
public class ProxyConfig {
    public ProxyConfig() {
        System.setProperty("http.proxyHost", "www-proxy-adcq7-new.us.oracle.com");
        System.setProperty("http.proxyPort", "80");

        System.setProperty("https.proxyHost", "www-proxy-adcq7-new.us.oracle.com");
        System.setProperty("https.proxyPort", "80");
    }
}
