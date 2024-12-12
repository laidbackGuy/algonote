package com.ssafy.algonote.problem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nameEn;

    private String group;

    @ToString.Exclude
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Problem> problems = new TreeSet<>(Comparator.comparingLong(Problem::getId));  // id 기준 오름차순 정렬

    private Tag(String nameEn) {
        this.nameEn = nameEn;
    }

    public static Tag of(String nameEn) {
        return new Tag(nameEn);
    }

}
