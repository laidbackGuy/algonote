package com.ssafy.algonote.note.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record AllSearchRestDto(
        Long count,
        @JsonProperty("notes") List<NoteSearchResDto> resDtos
) {
    public static AllSearchRestDto of(Long count, List<NoteSearchResDto> resDtos) {
        return AllSearchRestDto.builder()
                .count(count)
                .resDtos(resDtos)
                .build();
    }
}
