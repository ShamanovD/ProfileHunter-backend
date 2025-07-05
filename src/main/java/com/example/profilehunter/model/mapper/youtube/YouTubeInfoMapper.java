package com.example.profilehunter.model.mapper.youtube;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.dto.YouTubeInfoWrapper;
import com.example.profilehunter.model.mapper.BaseUserInfoMapper;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class YouTubeInfoMapper extends BaseUserInfoMapper<ChannelSnippet, YouTubeInfoWrapper> {

    @Mapping(target = "username", source = "customUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = ".", qualifiedByName = "getUserImage")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "description")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(ChannelSnippet snippet);

    @Mapping(target = "username", source = "snippet.customUrl")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "image", source = "snippet", qualifiedByName = "getUserImage")
    @Mapping(target = "country", source = "snippet.country")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "profilePhotos", source = "snippet", qualifiedByName = "getUserProfilePhoto")
    @Mapping(target = "metaInfoMap", source = ".", qualifiedByName = "mapMetaInfo")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(YouTubeInfoWrapper youTubeInfoWrapper);

    @AfterMapping
    public void mapTitle(@MappingTarget UserInfo userInfo, ChannelSnippet snippet) {
        setTitle(userInfo, snippet);
    }

    @AfterMapping
    public void mapTitle(@MappingTarget UserFullInfo userInfo, YouTubeInfoWrapper youTubeInfoWrapper) {
        if (Objects.nonNull(youTubeInfoWrapper.getSnippet())) {
            setTitle(userInfo, youTubeInfoWrapper.getSnippet());
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

    @Named("getUserProfilePhoto")
    public List<String> getUserProfilePhoto(ChannelSnippet snippet) {
        String image = null;

        if (!snippet.getThumbnails().isEmpty()) {
            image = snippet.getThumbnails().getMedium().getUrl();
        }

        return Objects.nonNull(image) ? List.of(image) : new ArrayList<>();
    }

    @Named("mapMetaInfo")
    public Map<String, Object> mapMetaInfo(YouTubeInfoWrapper user) {
        Map<String, Object> metaInfoMap = new HashMap<>();

        if (Objects.nonNull(user.getStatistics())) {
            ChannelStatistics statistics = user.getStatistics();

            metaInfoMap.put("videoCount", statistics.getVideoCount());
            metaInfoMap.put("commentCount", statistics.getCommentCount());
            metaInfoMap.put("viewCount", statistics.getViewCount());
            metaInfoMap.put("subscriberCount", statistics.getSubscriberCount());
            metaInfoMap.put("hiddenSubscriberCount", statistics.getHiddenSubscriberCount());
        }

        if (Objects.nonNull(user.getTopicDetails())) {
            metaInfoMap.put("topicCategories", user.getTopicDetails().getTopicCategories());
        }

        return metaInfoMap;
    }

    private void setTitle(UserInfo userInfo, ChannelSnippet snippet) {
        if (StringUtils.isNotEmpty(snippet.getTitle())) {
            String[] splitItems = snippet.getTitle().split(StringUtils.SPACE);

            userInfo.setFirstName(splitItems.length > 0 ? splitItems[0] : null);
            userInfo.setLastName(splitItems.length > 1 ? splitItems[1] : null);
        }

    }
}
