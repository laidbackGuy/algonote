package com.ssafy.algonote.member.dto.response;

import lombok.Builder;

@Builder
public record DupcheckResDto(boolean duplicated) {

    public DupcheckResDto(boolean duplicated) {
        this.duplicated = duplicated;
    }
}
