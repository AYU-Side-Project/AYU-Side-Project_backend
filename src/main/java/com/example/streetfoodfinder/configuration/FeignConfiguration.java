package com.example.streetfoodfinder.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    @Value("${mailgun.api-key}")
    private String APIKEY;

    @Qualifier(value = "mailgun")
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
        return new BasicAuthRequestInterceptor("api",APIKEY );
    }
}
