package com.ssafy.algonote.note.dto;

import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.Tag;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record BookmarkProblemResDto(
        Long id,
        String title,
        int tier,
        Set<String> tags
) {
    public static BookmarkProblemResDto from(Problem problem) {
        return BookmarkProblemResDto.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .tier(problem.getTier())
                .tags(problem.getTags().stream().map(Tag::getNameEn).collect(Collectors.toUnmodifiableSet()))
                .build();
    }
}
