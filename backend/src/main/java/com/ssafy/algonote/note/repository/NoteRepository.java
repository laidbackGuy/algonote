package com.ssafy.algonote.note.repository;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, NoteCustomRepository {

    // 멤버가 작성한 노트 조회
    List<Note> findByMember(Member member);

    // 문제 id로 노트 조회
    List<Note> findByProblemId(Long problemId);

    List<Note> findByMember_IdAndProblem_Id(Long memberId, Long problemId);
}
