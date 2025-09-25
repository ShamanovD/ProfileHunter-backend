package com.example.profilehunter.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String username;

    private String email;

    private String phone;

    private String password;

}
