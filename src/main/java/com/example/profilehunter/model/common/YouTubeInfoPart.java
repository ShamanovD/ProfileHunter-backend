package com.example.profilehunter.model.common;

import lombok.Getter;

@Getter
public enum YouTubeInfoPart {

    SNIPPET("snippet"),
    STATISTICS("statistics"),
    TOPIC_DETAILS("topicDetails");

    private final String partName;

    YouTubeInfoPart(String partName) {
        this.partName = partName;
    }
}
