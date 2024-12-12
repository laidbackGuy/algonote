package com.ssafy.algonote.code.dto.response;

public record AnalyzeResDto(
        String output,
        Double executionTime,
        Double memoryUsage,
        Boolean isCorrect
) {
}
