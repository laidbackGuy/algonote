package com.ssafy.algonote.problem.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record AnalysisResDto(
    int totalProblemCount,
    List<AnaylsisDto> groups
) {

}
