package com.ssafy.algonote.problem.domain;

import com.ssafy.algonote.common.BaseEntity;
import com.ssafy.algonote.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SolvedProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Setter
    @Enumerated(EnumType.STRING)
    private WritingStatus complete;

    @Setter
    private LocalDateTime uploadedAt;

    public static SolvedProblem of(Member member, Problem problem, LocalDateTime uploadedAt) {
        return SolvedProblem.builder()
                .member(member)
                .problem(problem)
                .complete(WritingStatus.NOT_YET)
                .uploadedAt(uploadedAt)
                .build();
    }
}
