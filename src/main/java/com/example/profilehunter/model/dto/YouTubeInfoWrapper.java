package com.example.profilehunter.model.dto;

import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;
import com.google.api.services.youtube.model.ChannelTopicDetails;
import lombok.Data;

@Data
public class YouTubeInfoWrapper {

    private ChannelSnippet snippet;

    private ChannelStatistics statistics;

    private ChannelTopicDetails topicDetails;

}
