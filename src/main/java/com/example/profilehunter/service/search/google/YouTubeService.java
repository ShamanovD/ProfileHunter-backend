package com.example.profilehunter.service.search.google;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.common.YouTubeInfoPart;
import com.example.profilehunter.model.dto.YouTubeInfoWrapper;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import com.example.profilehunter.service.search.BaseSearchService;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSnippet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class YouTubeService extends BaseSearchService<ChannelSnippet, YouTubeInfoWrapper> {

    @Value("${spring.youtube.access.token}")
    private String accessToken;

    private final YouTube youTube;
    private final List<String> BASE_ACCOUNT_INFO = List.of(YouTubeInfoPart.SNIPPET.getPartName());
    private final List<String> FULL_ACCOUNT_INFO = Arrays.stream(YouTubeInfoPart.values()).map(YouTubeInfoPart::getPartName).toList();

    public YouTubeService(YouTube youTube, UserMapperFactory mapperFactory) {
        super(mapperFactory);
        this.youTube = youTube;
    }

    @Override
    public Function<String, Optional<ChannelSnippet>> searchUser() {
        return item -> {
            try {
                YouTube.Channels.List channelList = youTube.channels()
                        .list(BASE_ACCOUNT_INFO);

                channelList.setKey(accessToken);
                channelList.setForHandle(item.startsWith("@") ? item : "@" + item);

                ChannelListResponse response = channelList.execute();

                if (!response.isEmpty() && response.containsKey("items")) {
                    ChannelSnippet snippet = ((ArrayList<Channel>) response.get("items"))
                            .get(0)
                            .getSnippet();

                    return Optional.of(snippet);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        };
    }

    @Override
    public Function<String, Optional<YouTubeInfoWrapper>> searchUserFullInfo() {
        return item -> {
            try {
                YouTube.Channels.List channelList = youTube.channels()
                        .list(FULL_ACCOUNT_INFO);


                channelList.setKey(accessToken);
                channelList.setForHandle(item.startsWith("@") ? item : "@" + item);

                ChannelListResponse response = channelList.execute();

                if (!response.isEmpty() && response.containsKey("items")) {
                    YouTubeInfoWrapper infoWrapper = new YouTubeInfoWrapper();

                    Channel channel = ((ArrayList<Channel>) response.get("items"))
                            .get(0);

                    infoWrapper.setSnippet(channel.getSnippet());
                    infoWrapper.setStatistics(channel.getStatistics());
                    infoWrapper.setTopicDetails(channel.getTopicDetails());

                    return Optional.of(infoWrapper);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        };
    }

    @Override
    public Function<String, List<ChannelSnippet>> searchUsers() {
        return item -> {
            try {
                YouTube.Channels.List channelList = youTube.channels()
                        .list(BASE_ACCOUNT_INFO);

                channelList.setKey(accessToken);
                channelList.setForHandle(item.startsWith("@") ? item : "@" + item);

                ChannelListResponse response = channelList.execute();

                if (!response.isEmpty() && response.containsKey("items")) {
                    return ((ArrayList<Channel>) response.get("items"))
                            .stream()
                            .map(Channel::getSnippet)
                            .toList();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ArrayList<>();
        };
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.YOUTUBE;
    }
}
