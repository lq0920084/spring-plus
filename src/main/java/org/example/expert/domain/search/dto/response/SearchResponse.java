package org.example.expert.domain.search.dto.response;

import com.querydsl.core.types.dsl.NumberExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponse {
    private String title;
    private Long managerCount;
    private Long commentCount;

}
