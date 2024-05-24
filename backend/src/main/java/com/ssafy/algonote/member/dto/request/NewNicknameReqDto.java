package com.ssafy.algonote.member.dto.request;

import lombok.Getter;

@Getter
public class NewNicknameReqDto{
    String nickname;

    public NewNicknameReqDto(String nickname) {
        this.nickname = nickname.trim();
    }
}
