package com.ssafy.algonote.note.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@Builder
public record SearchResDto(
    int noteCnt,
    @JsonProperty("notes") List<NoteSearchResDto> resDtos
) {
    public static SearchResDto from(List<NoteSearchResDto> resDtos) {
        return SearchResDto.builder()
            .noteCnt(resDtos.size())
            .resDtos(resDtos)
            .build();
    }
}
