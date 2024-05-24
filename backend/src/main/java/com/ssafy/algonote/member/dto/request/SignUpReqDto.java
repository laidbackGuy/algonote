package com.ssafy.algonote.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.ToString;

@Builder
public record SignUpReqDto(
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String nickname
) {

    public SignUpReqDto(String email, String password, String nickname) {
        this.email = email.trim();
        this.password = password;
        this.nickname = nickname;
    }
}
