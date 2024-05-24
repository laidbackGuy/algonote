package com.ssafy.algonote.member.dto.response;

import lombok.Builder;

@Builder
public record UpdatedProfileImgResDto(
        String profileImgUrl
) {
    public static UpdatedProfileImgResDto of(String profileImgUrl) {
        return UpdatedProfileImgResDto.builder()
                .profileImgUrl(profileImgUrl).build();
    }
}
