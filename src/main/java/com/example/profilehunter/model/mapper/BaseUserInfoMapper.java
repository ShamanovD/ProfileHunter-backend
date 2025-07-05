package com.example.profilehunter.model.mapper;

import com.example.profilehunter.model.dto.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public abstract class BaseUserInfoMapper<T, R> implements IUserFullInfoMapper<T, R> {

    @AfterMapping
    protected void generateUrlByUsername(@MappingTarget UserInfo userInfo) {
        if (StringUtils.isNotBlank(getUrl()) && StringUtils.isNotBlank(userInfo.getUsername())) {
            userInfo.setUrl(getUrl().concat(userInfo.getUsername()));
        }
    }

    @Override
    public String getUrl() {
        return null;
    }
}
