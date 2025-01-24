package org.example.expert.domain.search.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.annotation.DateValid;

@NoArgsConstructor
@Getter
public class SearchRequest {
    @JsonProperty("title")
    private String title = "__NULL__";
    @JsonProperty("startDate")
    @DateValid
    private String startDate = "0001-01-01";
    @JsonProperty("endDate")
    @DateValid
    private String endDate = "9999-12-31";
    @JsonProperty("nickname")
    private String nickname = "__NULL__";
    @JsonProperty("page")
    private int page = 1;
    @JsonProperty("size")
    private int size = 10;
}
