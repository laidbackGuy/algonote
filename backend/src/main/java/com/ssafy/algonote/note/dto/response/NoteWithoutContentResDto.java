package com.ssafy.algonote.note.dto.response;

import com.ssafy.algonote.note.domain.Note;

import java.time.LocalDateTime;

public record NoteWithoutContentResDto(
        Long noteId,
        String noteTitle,
        long heartCnt,
        boolean hearted,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {
    public static NoteWithoutContentResDto from(Note note, long heartCnt, boolean hearted) {
        return new NoteWithoutContentResDto(
                note.getId(),
                note.getTitle(),
                heartCnt,
                hearted,
                note.getCreatedAt(),
                note.getModifiedAt()
        );
    }
}
