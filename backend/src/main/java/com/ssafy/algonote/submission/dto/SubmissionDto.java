package com.ssafy.algonote.submission.dto;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.submission.domain.Submission;
import com.ssafy.algonote.submission.dto.request.SubmissionReqDto;

import java.time.LocalDateTime;

public record SubmissionDto(
        Long submissionId,
        Problem problem,
        Member member,
        String code,
        String result,
        Integer length,
        LocalDateTime submissionTime,
        Long memorySize,
        Integer runningTime,
        String language
) {
    public static SubmissionDto fromEntity(Submission entity) {
        return new SubmissionDto(
                entity.getId(),
                entity.getProblem(),
                entity.getMember(),
                entity.getCode(),
                entity.getResult(),
                entity.getLength(),
                entity.getSubmissionTime(),
                entity.getMemorySize(),
                entity.getRunningTime(),
                entity.getLanguage()
        );
    }

    public static SubmissionDto fromReq(SubmissionReqDto req, Problem problem, Member member) {
        return new SubmissionDto(
                req.submissionId(),
                problem,
                member,
                req.code(),
                req.result(),
                req.length(),
                req.submissionTime(),
                req.memorySize(),
                req.runningTime(),
                req.language()
        );
    }
}
