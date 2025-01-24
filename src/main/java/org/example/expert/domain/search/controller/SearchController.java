package org.example.expert.domain.search.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.request.SearchRequest;
import org.example.expert.domain.search.dto.response.SearchResponse;
import org.example.expert.domain.search.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/search")
    public Page<SearchResponse>  todosSearch(@Valid @RequestBody SearchRequest searchRequest){

       return searchService.search(searchRequest);
    }
}
