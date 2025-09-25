package com.example.profilehunter.model.mapper.auth;

import com.example.profilehunter.model.database.User;
import com.example.profilehunter.model.dto.UserDto;

public interface IUserMapper {

    UserDto toDto(User user);

    User toUser(UserDto userDto);

}
