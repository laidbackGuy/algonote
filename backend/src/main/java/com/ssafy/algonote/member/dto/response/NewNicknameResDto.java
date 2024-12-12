package com.ssafy.algonote.member.dto.response;

import lombok.Builder;

@Builder
public record NewNicknameResDto(
        String updatedNickname
){
    public static NewNicknameResDto of(String updatedNickname) {
        return NewNicknameResDto.builder()
                .updatedNickname(updatedNickname)
                .build();
    }
}
