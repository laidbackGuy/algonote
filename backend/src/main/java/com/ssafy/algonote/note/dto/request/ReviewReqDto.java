package com.ssafy.algonote.note.dto.request;

public record ReviewReqDto(
    int startLine,
    int endLine,
    String content
) { }