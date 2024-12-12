package com.ssafy.algonote.problem.dto.response;

import com.ssafy.algonote.problem.domain.SolvedProblem;
import com.ssafy.algonote.problem.domain.WritingStatus;

import java.time.LocalDateTime;

public record SolvedProblemResDto(
        ProblemResDto problem,
        WritingStatus complete,
        LocalDateTime uploadDate
) {
    public static SolvedProblemResDto from(SolvedProblem entity) {
        return new SolvedProblemResDto(
                ProblemResDto.from(entity.getProblem()),
                entity.getComplete(),
                entity.getUploadedAt()
        );
    }
}
