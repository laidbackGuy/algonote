package com.ssafy.algonote.note.dto.request;

public record NoteSaveReqDto(
        Long problemId,
        String title,
        String content,
        Long tempNoteId
) {
}
