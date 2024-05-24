package com.ssafy.algonote.member.dto.response;

import com.ssafy.algonote.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Builder
public record LoginReturnDto(String token,
                             Long memberId,
                             String email,
                             String nickname,
                             String profileImg) {

    public static LoginReturnDto from(String token, Member member) {
        return LoginReturnDto.builder()
            .token(token)
            .memberId(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .build();
    }
}