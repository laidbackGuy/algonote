package com.ssafy.algonote.problem.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record AnaylsisDto(
    String group,
    int score,
    LocalDateTime lastSolvedDate,
    int problemCount
) {

}
