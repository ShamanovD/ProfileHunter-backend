package com.example.profilehunter.model.mapper;

import com.example.profilehunter.model.common.SourceType;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class UserMapperFactory {

    private final Map<SourceType, IUserFullInfoMapper<?, ?>> userFullInfoMapperMap;

    public UserMapperFactory(List<IUserFullInfoMapper<?, ?>> userFullInfoMappers) {
        userFullInfoMapperMap = userFullInfoMappers.stream().collect(Collectors.toMap(IUserFullInfoMapper::getSourceType, item -> item));
    }

    public IUserFullInfoMapper<?, ?> getUserMapperBySourceType(SourceType sourceType) {
        return userFullInfoMapperMap.get(sourceType);
    }
}
