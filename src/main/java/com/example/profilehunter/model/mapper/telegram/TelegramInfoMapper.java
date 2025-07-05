package com.example.profilehunter.model.mapper.telegram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.TelegramInfoWrapper;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserInfoMapper;
import org.drinkless.tdlib.TdApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TelegramInfoMapper extends BaseUserInfoMapper<TdApi.User, TelegramInfoWrapper> {

    @Mapping(source = "usernames.editableUsername", target = "username")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getShortUserImage")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(TdApi.User user);

    @Mapping(target = "username", source = "user.usernames.editableUsername")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = "user", qualifiedByName = "getShortUserImage")
    @Mapping(target = "profilePhotos", source = "photos", qualifiedByName = "mapProfilePhotos")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(TelegramInfoWrapper telegramInfoWrapper);

    @Override
    public String getUrl() {
        return "https://t.me/";
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.TELEGRAM;
    }

    @Named("getShortUserImage")
    public String getUserImage(TdApi.User user) {
        return "data:image/jpg;base64,"
                .concat(Base64.getEncoder().encodeToString(user.profilePhoto.minithumbnail.data));
    }

    @Named("mapProfilePhotos")
    public List<String> mapProfilePhotos(TdApi.ChatPhotos photos) {
        return Arrays.stream(photos.photos).map(item -> "data:image/jpg;base64,"
                .concat(Base64.getEncoder().encodeToString(item.minithumbnail.data))).toList();
    }
}
