package org.example.expert.domain.todo.repository;

import org.example.expert.domain.search.dto.request.SearchRequest;
import org.example.expert.domain.search.dto.request.SearchServiceRequest;
import org.example.expert.domain.search.dto.response.SearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TodoRepositoryCustom {
    Optional<Todo> findByIdWithUser(Long todoId);

    Page<SearchResponse> search(SearchServiceRequest searchServiceRequest, Pageable pageable);
}
