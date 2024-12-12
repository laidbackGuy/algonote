package com.ssafy.algonote.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DestEmatilReqDto(
    @NotBlank @Email String email
) {

    public DestEmatilReqDto(String email) {
        this.email = email.trim();
    }
}
