package com.example.profilehunter.model.mapper;

import com.example.profilehunter.model.dto.UserFullInfo;

public interface IUserFullInfoMapper<T, R> extends IUserInfoMapper<T> {

    UserFullInfo mapUserWithFullInfo(R param);

}
