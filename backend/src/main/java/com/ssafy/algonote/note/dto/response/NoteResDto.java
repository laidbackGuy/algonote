package com.ssafy.algonote.note.dto.response;

import com.ssafy.algonote.member.dto.response.MemberResDto;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.problem.dto.response.ProblemResDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoteResDto(
        Long noteId,
        MemberResDto member,
        ProblemResDto problem,
        String noteTitle,
        String content,
        Long heartCnt,
        Boolean hearted,
        Boolean bookmarked,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static NoteResDto of(Note note){
        return NoteResDto.builder()
                .noteId(note.getId())
                .noteTitle(note.getTitle())
                .problem(ProblemResDto.from(note.getProblem()))
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .modifiedAt(note.getModifiedAt())
                .build();
    }
    public static NoteResDto from(Note note, Long heartCnt, Boolean hearted, Boolean bookmarked) {
        return new NoteResDto(
                note.getId(),
                MemberResDto.from(note.getMember()),
                ProblemResDto.from(note.getProblem()),
                note.getTitle(),
                note.getContent(),
                heartCnt,
                hearted,
                bookmarked,
                note.getCreatedAt(),
                note.getModifiedAt()
        );
    }
}
