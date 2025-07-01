package com.example.profilehunter.config;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookConfig {

    @Value("${spring.facebook.access.token}")
    private String accessToken;

    @Bean
    public FacebookClient getFacebookClient() {
        return new DefaultFacebookClient(accessToken, Version.LATEST);
    }
}
