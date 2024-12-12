package com.ssafy.algonote.submission.dto.request;

import java.time.LocalDateTime;

public record SubmissionReqDto(
        Long submissionId,
        Long problemId,
        String code,
        String result,
        Integer length,
        LocalDateTime submissionTime,
        Long memorySize,
        Integer runningTime,
        String language
) {

}
