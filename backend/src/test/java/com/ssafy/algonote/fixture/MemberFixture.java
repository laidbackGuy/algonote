package com.ssafy.algonote.fixture;

import com.ssafy.algonote.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {
    public static Member createMember(Long memberId) {
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }
}
