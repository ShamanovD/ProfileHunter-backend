package com.example.profilehunter.service.search.telegram.config;

import dev.voroby.springframework.telegram.client.updates.ClientAuthorizationState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramConfigService implements ITelegramConfigService {

    private final ClientAuthorizationState authorizationState;

    @Override
    public void checkAuthCode(String authCode) {
        authorizationState.checkAuthenticationCode(authCode);
    }

}
