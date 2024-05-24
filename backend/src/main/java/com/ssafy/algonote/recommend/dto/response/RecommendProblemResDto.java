package com.ssafy.algonote.recommend.dto.response;


import lombok.Builder;

@Builder
public record RecommendProblemResDto(
    Long problemId,
    int tier,
    String problemTitle,
    int acceptedUserCount,
    double averageTries){
}