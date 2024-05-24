package com.ssafy.grading.dto.request;

public record ExecutionReqDto(
        String sourceCode,
        String inputData,
        String expectedOutput
) {
}
