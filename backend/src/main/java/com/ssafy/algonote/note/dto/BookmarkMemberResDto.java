package com.ssafy.algonote.note.dto;

import com.ssafy.algonote.member.domain.Member;
import lombok.Builder;

@Builder
public record BookmarkMemberResDto(
        Long id,
        String nickname
) {
    public static BookmarkMemberResDto from(Member member) {
        return BookmarkMemberResDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
