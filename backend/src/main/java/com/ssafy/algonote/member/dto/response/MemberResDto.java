package com.ssafy.algonote.member.dto.response;

import com.ssafy.algonote.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberResDto(
     Long memberId,
     String nickname,
     String profileImg
){
    public static MemberResDto from(Member member) {
        return MemberResDto.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .build();
    }
}
