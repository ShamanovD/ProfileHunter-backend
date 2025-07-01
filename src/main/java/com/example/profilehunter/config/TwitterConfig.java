package com.example.profilehunter.config;

import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterConfig {

    @Value("${spring.twitter.access.bearer}")
    private String accessToken;

    @Bean
    public TwitterApi getTwitterClient() {
        return new TwitterApi(new TwitterCredentialsBearer(accessToken));
    }

}
