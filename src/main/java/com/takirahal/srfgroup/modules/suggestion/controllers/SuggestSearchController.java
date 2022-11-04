package com.takirahal.srfgroup.modules.suggestion.controllers;

import com.takirahal.srfgroup.modules.suggestion.entities.SuggestSearch;
import com.takirahal.srfgroup.modules.suggestion.services.SuggestSearchService;
import com.takirahal.srfgroup.utils.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suggest-search/")
@ConditionalOnProperty(
        value="elasticsearch.available",
        havingValue = "true",
        matchIfMissing = false)
public class SuggestSearchController {
    private final Logger log = LoggerFactory.getLogger(SuggestSearchController.class);

    @Autowired
    SuggestSearchService suggestSearchService;

    /**
     *
     * @param suggestSearch
     * @return
     */
    @PostMapping("public/create")
    public ResponseEntity<SuggestSearch> createPost(@RequestBody SuggestSearch suggestSearch){
        log.debug("REST request to save SuggestSearch : {}", suggestSearch);
        SuggestSearch result = suggestSearchService.save(suggestSearch);
        return new ResponseEntity<>(result, HeaderUtil.createAlert("add_offer.message_create_offer_succefull", ""), HttpStatus.CREATED);
    }

    /**
     *
     * @param pageable
     * @return
     */
    @GetMapping("public/fetchAll")
    public ResponseEntity<Page<SuggestSearch>> getAllSuggestSearch(Pageable pageable) {
        log.info("REST request to get SuggestSearch by criteria: {}", pageable);
        Page<SuggestSearch> page = suggestSearchService.getAllPosts(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
