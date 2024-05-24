package com.ssafy.algonote.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EmailAuthReqDto(
    @NotBlank @Email String email,
    @NotBlank String authCode
) {

    public EmailAuthReqDto(@NotBlank @Email String email, @NotBlank String authCode) {
        this.email = email.trim();
        this.authCode = authCode;
    }
}
