package com.ssafy.algonote.member.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginReqDto(
    @NotBlank @Email String email,
    @NotBlank String password
) {

    public LoginReqDto(String email, String password) {
        this.email = email.trim();
        this.password = password.trim();
    }
}