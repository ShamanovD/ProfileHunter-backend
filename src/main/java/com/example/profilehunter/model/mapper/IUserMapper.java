package com.example.profilehunter.model.mapper;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;

public interface IUserMapper<T, R> {

    UserInfo mapUser(T param);

    UserFullInfo mapUserWithFullInfo(R param);

    String getUrl();

    SourceType getSourceType();

}
