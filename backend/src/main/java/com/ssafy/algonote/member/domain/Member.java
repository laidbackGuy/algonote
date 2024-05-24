package com.ssafy.algonote.member.domain;

import com.ssafy.algonote.common.BaseEntity;
import com.ssafy.algonote.member.dto.request.SignUpReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private String profileImg;

    public void update(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImg(String imgUrl) {
        this.profileImg = imgUrl;
    }

    public static Member from(SignUpReqDto signUpReqDto) {
        return Member.builder()
            .email(signUpReqDto.email())
            .password(signUpReqDto.password())
            .nickname(signUpReqDto.nickname())
            .role(MemberRole.USER)
            .build();
    }
}
