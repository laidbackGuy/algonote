package com.ssafy.algonote.config.user;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(userId))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INVALID));

        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
            .userId(member.getId())
            .email(member.getEmail())
            .password(member.getPassword())
            .nickname(member.getNickname())
            .role(member.getRole())
            .build();

        return new CustomUserDetails(memberInfoDto);
    }

//    public boolean checkPassword(String password, String encodedPassword) {
//        return passwordEncoder.matches(password, encodedPassword);
//    }
}
