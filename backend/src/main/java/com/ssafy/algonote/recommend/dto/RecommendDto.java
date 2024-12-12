package com.ssafy.algonote.recommend.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record RecommendDto(
    String nickname,
    String tag,
    List<Long> solvedProblemIds) {

    public static RecommendDto of(String nickname, String tag, List<Long> solvedProblemIds) {
        return RecommendDto.builder()
            .nickname(nickname)
            .tag(tag)
            .solvedProblemIds(solvedProblemIds)
            .build();
    }
}
