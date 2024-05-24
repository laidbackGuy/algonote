package com.ssafy.algonote.problem.dto.response;

import lombok.Builder;

@Builder
public record NotedProblemResDto(
    int solvedProblemCnt,
    int notedProblemCnt,
    int noteCnt
) {

}
