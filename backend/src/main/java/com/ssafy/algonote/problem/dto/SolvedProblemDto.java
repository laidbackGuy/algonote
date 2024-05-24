package com.ssafy.algonote.problem.dto;

import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.SolvedProblem;
import com.ssafy.algonote.problem.domain.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SolvedProblemDto {
    private Long problemId;
    private String problemTitle;
    private int tier;

    private Set<String> tags;
    private Set<String> groups;
    private LocalDateTime uploadedAt;

    public static SolvedProblemDto of(SolvedProblem solvedProblemProblem){
        Set<String> tags = new HashSet<>();
        Set<String> groups = new HashSet<>();
        Problem problem = solvedProblemProblem.getProblem();
        for(Tag tag : problem.getTags()){
            groups.add(tag.getGroup());
            tags.add(tag.getNameEn());
        }

        return SolvedProblemDto.builder()
                .problemId(problem.getId())
                .problemTitle(problem.getTitle())
                .tier(problem.getTier())
                .tags(tags)
                .groups(groups)
                .uploadedAt(solvedProblemProblem.getUploadedAt())
                .build();
    }
}
