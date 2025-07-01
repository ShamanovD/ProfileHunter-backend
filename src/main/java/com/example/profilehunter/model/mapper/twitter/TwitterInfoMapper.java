package com.example.profilehunter.model.mapper.twitter;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserMapper;
import com.example.profilehunter.model.mapper.IUserMapper;
import com.twitter.clientlib.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TwitterInfoMapper extends BaseUserMapper<User, User> {

    @Mapping(source = "name", target = "firstName")
    @Mapping(source = "profileImageUrl", target = "image")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(User user);

    @Mapping(source = "name", target = "firstName")
    @Mapping(source = "profileImageUrl", target = "image")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(User user);

    @Override
    public String getUrl() {
        return "https://x.com/";
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.TWITTER;
    }
}
