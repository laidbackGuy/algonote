package com.ssafy.algonote.problem.repository;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.SolvedProblem;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Long>, SolvedProblemCustomRepository {
    // 멤버와 문제로 풀었는지 체크
    Optional<SolvedProblem> findByMember_IdAndProblem_Id(Long memberId, Long problemId);
    Optional<SolvedProblem> findByMemberAndProblem(Member member, Problem problem);

    // 멤버로 푼 문제 조회
    Page<SolvedProblem> findAllByMember(Pageable pageable, Member member);
    List<SolvedProblem> findAllByMember(Member member);
}
