package com.ssafy.algonote.submission.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.algonote.submission.dto.SubmissionDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SubmissionResDto(
        Long submissionId,
        String code,
        String result,
        Integer length,
        LocalDateTime submissionTime,
        Long memorySize,
        Integer runningTime,
        String language
) {
    public static SubmissionResDto fromDto(SubmissionDto dto) {
        return SubmissionResDto.builder()
                .submissionId(dto.submissionId())
                .code(dto.code())
                .result(dto.result())
                .length(dto.length())
                .submissionTime(dto.submissionTime())
                .memorySize(dto.memorySize())
                .runningTime(dto.runningTime())
                .language(dto.language())
                .build();
    }
}
