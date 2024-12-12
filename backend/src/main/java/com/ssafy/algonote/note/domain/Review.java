package com.ssafy.algonote.note.domain;

import com.ssafy.algonote.common.BaseEntity;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.note.dto.request.ReviewReqDto;
import com.ssafy.algonote.note.dto.request.ReviewUpdateReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(nullable = false)
    private int startLine;

    @Column(nullable = false)
    private int endLine;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public static Review of(ReviewReqDto req, Member member, Note note) {
        return Review.builder()
            .startLine(req.startLine())
            .endLine(req.endLine())
            .content(req.content())
            .member(member)
            .note(note)
            .build();
    }

    public void update(ReviewUpdateReqDto dto) {
        this.content = dto.content();
    }

}
