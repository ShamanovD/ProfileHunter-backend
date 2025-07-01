package com.example.profilehunter.endpoint;

import com.example.profilehunter.service.search.telegram.config.ITelegramConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegram/config")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST }) // <-- ДОБАВЬ ЭТУ АННОТАЦИЮ
public class TelegramConfigurationController {

    private final ITelegramConfigService telegramConfigService;

    @PostMapping("/authCode")
    public void checkAuthCode(String authCode) {
        telegramConfigService.checkAuthCode(authCode);
    }

}
