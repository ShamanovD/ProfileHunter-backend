package com.example.profilehunter.model.mapper.auth;

import com.example.profilehunter.model.database.User;
import com.example.profilehunter.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BaseUserMapper implements IUserMapper {

    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract User toUser(UserDto userDto);

}
