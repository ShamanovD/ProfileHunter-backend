package com.example.profilehunter.model.dto;

import com.example.profilehunter.model.common.SourceType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserInfo {

    private String id = UUID.randomUUID().toString();

    private String username;

    private String firstName;

    private String lastName;

    private String url;

    private String image;

    private String description;

    private String country;

    private SourceType sourceType;

}
