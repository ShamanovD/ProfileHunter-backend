package com.example.profilehunter.service.search.google;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.youtube.YouTubeMapper;
import com.example.profilehunter.service.search.BaseSearchService;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSnippet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class YouTubeService extends BaseSearchService<ChannelSnippet, ChannelSnippet> {

    @Value("${spring.youtube.access.token}")
    private String accessToken;

    private final YouTube youTube;
    private final YouTubeMapper youTubeMapper;

    @Override
    public Function<String, Optional<ChannelSnippet>> searchUser() {
        return item -> {
            try {
                YouTube.Channels.List channelList = youTube.channels()
                        .list(List.of("snippet"));

                channelList.setKey(accessToken);
                channelList.setForHandle(item.startsWith("@") ? item : "@" + item);

                ChannelListResponse response = channelList.execute();

                if (!response.isEmpty() && response.containsKey("items")) {
                    ChannelSnippet snippet = (ChannelSnippet) ((ArrayList<Channel>) response.get("items")).get(0).get("snippet");
                    return Optional.of(snippet);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        };
    }

    @Override
    public Function<String, Optional<ChannelSnippet>> searchUserFullInfo() {
        return searchUser();
    }

    @Override
    public Function<String, List<ChannelSnippet>> searchUsers() {
        return item -> {
            try {
                YouTube.Channels.List channelList = youTube.channels()
                        .list(List.of("snippet"));

                channelList.setKey(accessToken);
                channelList.setForHandle(item.startsWith("@") ? item : "@" + item);

                ChannelListResponse response = channelList.execute();

                if (!response.isEmpty() && response.containsKey("items")) {
                    return ((ArrayList<Channel>) response.get("items")).stream().map(channel -> (ChannelSnippet) channel.get("snippet")).toList();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ArrayList<>();
        };
    }

    @Override
    public Function<ChannelSnippet, UserInfo> mapUser() {
        return youTubeMapper::mapUser;
    }

    @Override
    public Function<ChannelSnippet, UserFullInfo> mapUserWithFullInfo() {
        return youTubeMapper::mapUserWithFullInfo;
    }

    @Override
    public SourceType getSearchType() {
        return SourceType.YOUTUBE;
    }
}
