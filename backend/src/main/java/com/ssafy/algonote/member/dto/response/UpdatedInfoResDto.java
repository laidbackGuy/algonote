package com.ssafy.algonote.member.dto.response;

import lombok.Builder;

@Builder
public record UpdatedInfoResDto(
        String updatedNickname,
        String profileImgUrl
) {
}
