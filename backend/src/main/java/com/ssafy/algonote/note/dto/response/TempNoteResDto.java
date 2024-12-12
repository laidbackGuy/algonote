package com.ssafy.algonote.note.dto.response;

import com.ssafy.algonote.member.dto.response.MemberResDto;
import com.ssafy.algonote.note.domain.TempNote;
import com.ssafy.algonote.problem.dto.response.ProblemResDto;

import java.time.LocalDateTime;

public record TempNoteResDto(
        Long tempNoteId,
        MemberResDto member,
        ProblemResDto problem,
        String noteTitle,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static TempNoteResDto from(TempNote tempNote) {
        return new TempNoteResDto(
                tempNote.getId(),
                MemberResDto.from(tempNote.getMember()),
                ProblemResDto.from(tempNote.getProblem()),
                tempNote.getTitle(),
                tempNote.getContent(),
                tempNote.getCreatedAt(),
                tempNote.getModifiedAt()
        );
    }
}
