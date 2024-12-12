package com.ssafy.algonote.note.dto.response;

import com.ssafy.algonote.member.dto.response.MemberResDto;
import com.ssafy.algonote.note.domain.Review;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReviewResDto(
    Long reviewId,
    MemberResDto member,
    int startLine,
    int endLine,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {
    public static ReviewResDto from(Review review) {
        return ReviewResDto.builder()
            .reviewId(review.getId())
            .member(com.ssafy.algonote.member.dto.response.MemberResDto.from(review.getMember()))
            .startLine(review.getStartLine())
            .endLine(review.getEndLine())
            .content(review.getContent())
            .createdAt(review.getCreatedAt())
            .modifiedAt(review.getModifiedAt())
            .build();
    }
}