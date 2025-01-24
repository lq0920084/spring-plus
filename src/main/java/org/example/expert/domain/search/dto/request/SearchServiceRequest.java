package org.example.expert.domain.search.dto.request;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SearchServiceRequest {
    private BooleanExpression title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BooleanExpression nickname;
}
