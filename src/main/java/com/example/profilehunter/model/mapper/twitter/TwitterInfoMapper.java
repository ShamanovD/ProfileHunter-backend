package com.example.profilehunter.model.mapper.twitter;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserInfoMapper;
import com.twitter.clientlib.model.User;
import org.mapstruct.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {List.class})
public abstract class TwitterInfoMapper extends BaseUserInfoMapper<User, User> {

    @Mapping(target = "firstName", source = "name")
    @Mapping(target = "image", source = "profileImageUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(User user);

    @Mapping(target = "firstName", source = "name")
    @Mapping(target = "image", source = "profileImageUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "metaInfoMap", source = ".", qualifiedByName = "mapMetaInfo")
    @Mapping(target = "profilePhotos", source = ".", qualifiedByName = "getProfileImageUrls")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(User user);

    @Override
    public SourceType getSourceType() {
        return SourceType.X;
    }

    @Named("mapMetaInfo")
    public Map<String, Object> mapMetaInfo(User user) {
        Map<String, Object> metaInfoMap = new HashMap<>();
        metaInfoMap.put("location", user.getLocation());
        metaInfoMap.put("verified", user.getVerified());

        if (Objects.nonNull(user.getPublicMetrics())) {
            metaInfoMap.put("followersCount", user.getPublicMetrics().getFollowersCount());
            metaInfoMap.put("listedCount", user.getPublicMetrics().getListedCount());
            metaInfoMap.put("followingCount", user.getPublicMetrics().getFollowingCount());
            metaInfoMap.put("tweetCount", user.getPublicMetrics().getTweetCount());
        }

        return metaInfoMap;
    }

    @Named("getProfileImageUrls")
    public List<String> getImages(User user) {
        if (Objects.nonNull(user.getProfileImageUrl())) {
            return List.of(user.getProfileImageUrl().toString());
        }

        return List.of();
    }
}
