package com.ssafy.algonote.problem.dto.response;

import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public record ProblemResDto(
        Long id,
        String title,
        int tier,
        int acceptUserCount,
        double averageTries,
        Set<String> tags
) {
    public static ProblemResDto from(Problem entity) {
        return new ProblemResDto(
                entity.getId(),
                entity.getTitle(),
                entity.getTier(),
                entity.getAcceptedUserCount(),
                entity.getAverageTries(),
                entity.getTags().stream().map(Tag::getNameEn).collect(Collectors.toUnmodifiableSet())
        );
    }
}
