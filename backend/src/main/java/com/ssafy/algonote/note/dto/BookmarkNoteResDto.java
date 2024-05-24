package com.ssafy.algonote.note.dto;

import com.ssafy.algonote.note.domain.Note;
import lombok.Builder;

@Builder
public record BookmarkNoteResDto(
        Long id,
        String title,
        long heartCnt
) {
    public static BookmarkNoteResDto from(Note note, long heartCnt) {
        return BookmarkNoteResDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .heartCnt(heartCnt)
                .build();
    }
}
