package com.ssafy.algonote.fixture;

import com.ssafy.algonote.submission.dto.request.SubmissionReqDto;

import java.time.LocalDateTime;

public class SubmissionReqFixture {

    public static SubmissionReqDto createSuccess(Long submissionId, Long problemId) {
        return new SubmissionReqDto(
                submissionId,
                problemId,
                "right code",
                "맞았습니다!!",
                403,
                LocalDateTime.now(),
                311332L,
                1776,
                "Java 11"
        );
    }

    public static SubmissionReqDto createFail(Long submissionId, Long problemId) {
        return new SubmissionReqDto(
                submissionId,
                problemId,
                "wrong code",
                "틀렸습니다",
                0,
                LocalDateTime.now(),
                0L,
                0,
                "Java 11"
        );
    }
}
