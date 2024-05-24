package com.ssafy.algonote.note.repository;

import com.ssafy.algonote.note.domain.TempNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempNoteRepository extends JpaRepository<TempNote, Long> {
    List<TempNote> findAllByMember_IdAndProblem_Id(Long memberId, Long problemId);
}
