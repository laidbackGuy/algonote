package com.ssafy.algonote.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record EmailDupCheckReqDto(
    @NotBlank @Email String email
) {

    public EmailDupCheckReqDto(String email) {
        this.email = email.trim();
    }
}
