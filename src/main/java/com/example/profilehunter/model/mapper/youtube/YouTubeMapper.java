package com.example.profilehunter.model.mapper.youtube;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserMapper;
import com.example.profilehunter.model.mapper.IUserMapper;
import com.google.api.services.youtube.model.ChannelSnippet;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class YouTubeMapper extends BaseUserMapper<ChannelSnippet, ChannelSnippet> {

    @Mapping(target = "username", source = "customUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getUserImage")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(ChannelSnippet snippet);

    @Mapping(target = "username", source = "customUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getUserImage")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(ChannelSnippet snippet);

    @AfterMapping
    public void mapTitle(@MappingTarget UserInfo userInfo, ChannelSnippet snippet) {
        if (StringUtils.isNotEmpty(snippet.getTitle())) {
            String[] splitItems = snippet.getTitle().split(StringUtils.SPACE);

            userInfo.setFirstName(splitItems.length > 0 ? splitItems[0] : null);
            userInfo.setLastName(splitItems.length > 1 ? splitItems[1] : null);
        }
    }

    @Override
    public String getUrl() {
        return "https://www.youtube.com/";
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.YOUTUBE;
    }

    @Named("getUserImage")
    public String getUserImage(ChannelSnippet snippet) {
        if (!snippet.getThumbnails().isEmpty()) {
           return snippet.getThumbnails().getDefault().getUrl();
        }

        return null;
    }
}
