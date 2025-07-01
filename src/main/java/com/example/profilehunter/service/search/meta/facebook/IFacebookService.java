package com.example.profilehunter.service.search.meta.facebook;

import com.example.profilehunter.model.dto.UserInfo;

public interface IFacebookService {

    UserInfo getUserInfoByUsername(String username);

}
