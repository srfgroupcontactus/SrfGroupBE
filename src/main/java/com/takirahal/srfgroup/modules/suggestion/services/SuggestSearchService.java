package com.takirahal.srfgroup.modules.suggestion.services;

import com.takirahal.srfgroup.modules.suggestion.entities.SuggestSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestSearchService {

    /**
     *
     * @param suggestSearch
     * @return
     */
    SuggestSearch save(SuggestSearch suggestSearch);

    Page<SuggestSearch> getAllPosts(Pageable pageable);
}
