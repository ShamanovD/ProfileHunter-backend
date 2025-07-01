package com.example.profilehunter.model.filter;

import com.example.profilehunter.model.common.SearchType;
import com.example.profilehunter.model.common.SourceType;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class LinkedUsersFilter {

    @NonNull
    private SearchType searchType;

    @NonNull
    private SourceType startNodeType;

    private List<SourceType> sourceTypes = new ArrayList<>();

}
