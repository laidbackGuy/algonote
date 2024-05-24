package com.ssafy.algonote.member.dto.response;

import lombok.Builder;

@Builder
public record EmailAuthResDto(
    boolean authenticated
) {


}
