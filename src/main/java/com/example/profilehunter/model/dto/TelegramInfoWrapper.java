package com.example.profilehunter.model.dto;

import lombok.Data;
import org.drinkless.tdlib.TdApi;

@Data
public class TelegramInfoWrapper {

    private TdApi.User user;

    private TdApi.ChatPhotos photos;

}
