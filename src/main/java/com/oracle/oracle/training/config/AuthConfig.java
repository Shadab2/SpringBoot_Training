package com.oracle.oracle.training.config;

import com.oracle.oracle.training.filters.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    public FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean(){
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter authFilter = new AuthFilter();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/api/user/*");
        registrationBean.addUrlPatterns("/api/service/*");
        registrationBean.addUrlPatterns("/api/utility/*");
        registrationBean.addUrlPatterns("/api/admin/*");
        registrationBean.addUrlPatterns("/api/post/*");
        return registrationBean;
    }
}
