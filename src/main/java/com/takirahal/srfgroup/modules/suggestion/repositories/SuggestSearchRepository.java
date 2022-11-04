package com.takirahal.srfgroup.modules.suggestion.repositories;

import com.takirahal.srfgroup.modules.suggestion.entities.SuggestSearch;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
        value="elasticsearch.available",
        havingValue = "true",
        matchIfMissing = false)
public interface SuggestSearchRepository  extends ElasticsearchRepository<SuggestSearch, String> {
}
