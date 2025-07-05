package com.example.profilehunter.model.mapper;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserInfo;

public interface IUserInfoMapper<T> {

    UserInfo mapUser(T param);

    String getUrl();

    SourceType getSourceType();

}
