package com.takirahal.srfgroup.modules.suggestion.services.impl;

import com.takirahal.srfgroup.modules.suggestion.entities.SuggestSearch;
import com.takirahal.srfgroup.modules.suggestion.repositories.SuggestSearchRepository;
import com.takirahal.srfgroup.modules.suggestion.services.SuggestSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value="elasticsearch.available",
        havingValue = "true",
        matchIfMissing = false)
public class SuggestSearchServiceImpl implements SuggestSearchService {

    @Autowired
    SuggestSearchRepository suggestSearchRepository;

    @Override
    public SuggestSearch save(SuggestSearch suggestSearch) {
        return suggestSearchRepository.save(suggestSearch);
    }

    @Override
    public Page<SuggestSearch> getAllPosts(Pageable pageable) {
        return suggestSearchRepository.findAll(pageable);
    }
}
