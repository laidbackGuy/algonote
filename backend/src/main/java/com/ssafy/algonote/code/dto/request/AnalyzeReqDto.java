package com.ssafy.algonote.code.dto.request;

public record AnalyzeReqDto(
        String sourceCode,
        String inputData,
        String expectedOutput
) {
}
