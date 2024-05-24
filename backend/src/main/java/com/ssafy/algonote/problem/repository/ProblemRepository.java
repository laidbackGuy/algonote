package com.ssafy.algonote.problem.repository;

import com.ssafy.algonote.problem.domain.Problem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemCustomRepository {

    Optional<Problem> findById(Long id);
    Optional<Problem> findByTitle(String title);


}
