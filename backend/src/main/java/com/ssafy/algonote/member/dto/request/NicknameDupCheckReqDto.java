package com.ssafy.algonote.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
public record NicknameDupCheckReqDto(
   @NotBlank String nickname
) {
    public NicknameDupCheckReqDto(String nickname) {
        this.nickname = nickname.trim();
    }
}