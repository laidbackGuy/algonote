package com.ssafy.algonote.member.repository;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.domain.MemberRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByRole(MemberRole memberRole);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
