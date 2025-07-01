package com.example.profilehunter.model.mapper.telegram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserMapper;
import org.drinkless.tdlib.TdApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TelegramInfoMapper extends BaseUserMapper<TdApi.User, TdApi.User> {

    @Mapping(source = "usernames.editableUsername", target = "username")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getShortUserImage")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(TdApi.User user);

    @Mapping(source = "usernames.editableUsername", target = "username")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getShortUserImage")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(TdApi.User user);

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
}
