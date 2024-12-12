package com.ssafy.algonote.recommend.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RecommendResDto(
    int count,
    List<RecommendProblemResDto> recommendProblems
) {
    public static RecommendResDto of(int count, List<RecommendProblemResDto> recommendProblems) {
        return RecommendResDto.builder()
            .count(count)
            .recommendProblems(recommendProblems)
            .build();
    }
}
