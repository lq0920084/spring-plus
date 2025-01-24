package org.example.expert.domain.search.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.request.SearchRequest;
import org.example.expert.domain.search.dto.request.SearchServiceRequest;
import org.example.expert.domain.search.dto.response.SearchResponse;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final TodoRepository todoRepository;

    public Page<SearchResponse> search(SearchRequest searchRequest) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(searchRequest.getStartDate()+" 00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(searchRequest.getEndDate()+" 23:59:59", formatter);
        BooleanExpression title = !searchRequest.getTitle().equals("__NULL__") ? todo.title.contains(searchRequest.getTitle()) : null;
        BooleanExpression nickname = !searchRequest.getNickname().equals("__NULL__") ? manager.user.nickname.contains(searchRequest.getNickname()) : null;


        Pageable pageable = PageRequest.of(
                searchRequest.getPage()-1,
                searchRequest.getSize(),
                Sort.by(Sort.Order.desc("createdAt"))
        );
       return todoRepository.search(new SearchServiceRequest(title,startDate,endDate,nickname),pageable);

    }

}
