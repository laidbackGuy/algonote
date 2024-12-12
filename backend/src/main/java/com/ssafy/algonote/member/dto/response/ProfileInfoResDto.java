package com.ssafy.algonote.member.dto.response;


import com.ssafy.algonote.member.domain.Member;
import lombok.Builder;

// 사용자 프로필 정보를 리턴하는 DTO
@Builder
public record ProfileInfoResDto(
    Long memberId,
    String email,
    String nickname,
    String profileImg
) {
    public static ProfileInfoResDto from(Member member) {
        return ProfileInfoResDto.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .build();
    }
}
