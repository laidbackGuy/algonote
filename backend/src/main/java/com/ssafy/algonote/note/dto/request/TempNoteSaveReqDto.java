package com.ssafy.algonote.note.dto.request;

public record TempNoteSaveReqDto(
        Long problemId,
        String title,
        String content
) {
}
