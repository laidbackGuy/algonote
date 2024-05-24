package com.ssafy.algonote.code.dto.response;

import lombok.Builder;

@Builder
public record ComplexityResDto(
        String timeComplexity,
        String spaceComplexity
) {
}
