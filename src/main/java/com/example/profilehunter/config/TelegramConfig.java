package com.example.profilehunter.config;

import dev.voroby.springframework.telegram.client.updates.ClientAuthorizationState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    private final ClientAuthorizationState authorizationState;

    @Value("${spring.telegram.access.email}")
    private String email;
    @Value("${spring.telegram.access.password}")
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void configureTelegramClient() {
        if (authorizationState.isWaitEmailAddress()) {
            authorizationState.checkEmailAddress(email);
        }

        if (authorizationState.isWaitAuthenticationPassword()) {
            authorizationState.checkAuthenticationPassword(password);
        }
    }

}
