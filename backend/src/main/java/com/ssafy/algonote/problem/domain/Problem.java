package com.ssafy.algonote.problem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    private Long id;
    private String title;
    private int tier;
    private int acceptedUserCount;
    private double averageTries;

    @ToString.Exclude
    @JoinTable(
            name = "problem_tag",
            joinColumns = @JoinColumn(name = "problemId"),
            inverseJoinColumns = @JoinColumn(name = "tagId")
    )
    @ManyToMany
    private Set<Tag> tags;

    public static Problem of(Long id, String title, int tier, int acceptedUserCount, double averageTries) {
        return Problem.builder()
                .id(id)
                .title(title)
                .tier(tier)
                .acceptedUserCount(acceptedUserCount)
                .averageTries(averageTries)
                .tags(new LinkedHashSet<>())
                .build();
    }

    public void addTags(Collection<Tag> tags) {
        this.getTags().addAll(tags);
    }

}
