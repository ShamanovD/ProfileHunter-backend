package com.example.profilehunter.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFullInfo extends UserInfo {

    private List<String> profilePhotos = new ArrayList<>();

    private Map<String, Object> metaInfoMap = new HashMap<>();

}
