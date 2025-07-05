package com.example.profilehunter.service.search;

import com.example.profilehunter.model.common.SourceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SearchServiceFactory {

    private final Map<SourceType, IBaseSearchService<?, ?>> searchServiceMap;

    public SearchServiceFactory(List<IBaseSearchService<?, ?>> searchServices) {
        this.searchServiceMap = searchServices.stream().collect(Collectors.toMap(IBaseSearchService::getSourceType, item -> item));
    }

    public IBaseSearchService<?, ?> getSearchServiceBySearchType(SourceType sourceType) {
        return searchServiceMap.get(sourceType);
    }

    public List<IBaseSearchService<?, ?>> getServicesBySearchTypes(List<SourceType> types) {
        return types.stream().map(searchServiceMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<IBaseSearchService<?, ?>> getServicesFilteredByStartNodeSearchType(SourceType sourceType) {
        return searchServiceMap.entrySet().stream().filter(item -> !item.getKey().equals(sourceType)).map(Map.Entry::getValue).collect(Collectors.toList());
    }

}
