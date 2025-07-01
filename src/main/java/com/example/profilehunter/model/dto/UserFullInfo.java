package com.example.profilehunter.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFullInfo extends UserInfo {

    private String email;

    private String birthday;

    private String phoneNumber;

    private List<String> profilePhotos = new ArrayList<>();

}
