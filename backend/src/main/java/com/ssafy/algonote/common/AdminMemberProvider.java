package com.ssafy.algonote.common;

import com.ssafy.algonote.member.domain.Member;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdminMemberProvider {

    private Member adminMember = Member.builder()
        .id(49L)
        .nickname("시스템 알림")
        .profileImg("https://algonote.s3.ap-northeast-2.amazonaws.com/b65d8f33-b2c7-4c06-bb81-eb1bee914743.png")
        .build();

    public Member getAdminMember() {
        return adminMember;
    }

}