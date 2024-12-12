package com.ssafy.algonote.note.repository;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.note.domain.Heart;
import com.ssafy.algonote.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberAndNote(Member member, Note note);

    // 노트의 좋아요 전체 삭제
    void deleteAllByNote(Note note);

    // 좋아요 여부 확인
    boolean existsByMemberAndNote(Member member, Note note);

    // 좋아요 개수 카운트
    long countByNote(Note note);
}
