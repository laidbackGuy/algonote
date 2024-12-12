package com.ssafy.algonote.member.service;

import com.ssafy.algonote.config.jwt.JwtUtil;
import com.ssafy.algonote.config.s3.AwsFileService;
import com.ssafy.algonote.config.user.MemberInfoDto;
import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.domain.MemberRole;
import com.ssafy.algonote.member.dto.request.EmailAuthReqDto;
import com.ssafy.algonote.member.dto.request.EmailDupCheckReqDto;
import com.ssafy.algonote.member.dto.request.LoginReqDto;
import com.ssafy.algonote.member.dto.request.NicknameDupCheckReqDto;
import com.ssafy.algonote.member.dto.request.SignUpReqDto;
import com.ssafy.algonote.member.dto.response.EmailAuthResDto;
import com.ssafy.algonote.member.dto.response.LoginReturnDto;
import com.ssafy.algonote.member.dto.response.ProfileInfoResDto;
import com.ssafy.algonote.member.dto.response.UpdatedInfoResDto;
import com.ssafy.algonote.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final AwsFileService awsFileService;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${cloud.aws.s3.prefix}")
    private String prefix;

    public Long signUp(SignUpReqDto signUpReqDto) {
        log.info("signUp dto : {}", signUpReqDto);
        if(checkDuplicated(signUpReqDto.email(), "email")){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (checkDuplicated(signUpReqDto.nickname(), "nickname")) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = Member.builder()
            .email(signUpReqDto.email())
//            .password(passwordEncoder.encode(signUpReqDto.password()))
            .password(signUpReqDto.password())
            .nickname(signUpReqDto.nickname())
            .role(MemberRole.USER)
            .profileImg(prefix+"/defaultProfile.png")
            .build();

        return memberRepository.save(member).getId();
    }

    public LoginReturnDto login(LoginReqDto loginReqDto){
        Member member = memberRepository.findByEmail(loginReqDto.email())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));


        if(!checkPassword(loginReqDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        MemberInfoDto memberInfoDto = MemberInfoDto.from(member);

        String token = jwtUtil.createAccessToken(memberInfoDto);

        return LoginReturnDto.from(token, member);
    }

    private boolean checkPassword(String password, String encodedPassword) {
//        return passwordEncoder.matches(password, encodedPassword);
        return password.equals(encodedPassword);
    }


    public boolean emailDupCheck(EmailDupCheckReqDto emailDupCheckReqDto) {
        return checkDuplicated(emailDupCheckReqDto.email(), "email");
    }

    public boolean nicknameDupCheck(NicknameDupCheckReqDto nicknameDupCheckReqDto) {
        return checkDuplicated(nicknameDupCheckReqDto.nickname(), "nickname");

    }

    public void sendCodeToEmail(String toEmail) {
        if(checkDuplicated(toEmail, "email")){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        String title = "Algonote 이메일 인증 번호";
        String authCode = createCode();

        mailService.sendEmail(toEmail, title, authCode);

        redisService.save(AUTH_CODE_PREFIX + toEmail, authCode, 5, TimeUnit.MINUTES);

    }

    public EmailAuthResDto verifyCode(EmailAuthReqDto emailAuthReqDto){
        String email = emailAuthReqDto.email();
        String code = emailAuthReqDto.authCode();
        if (checkDuplicated(email, "email")) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String redisCode = redisService.getData(AUTH_CODE_PREFIX + email);
        log.info("redisCode : {}", redisCode);
        boolean authenticated = redisCode.equals(code);

        return EmailAuthResDto.builder()
            .authenticated(authenticated)
            .build();
    }

    public ProfileInfoResDto getProfileInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return ProfileInfoResDto.from(member);
    }

    @Transactional
    public String updateNickname(Long memberId, String updatedNickname) {
        if(this.checkDuplicated(updatedNickname, "nickname")){
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));


        if (updatedNickname != null) {
            member.updateNickname(updatedNickname);
        }
        return member.getNickname();
    }

    @Transactional
    public String updateProfileImg(Long memberId, MultipartFile profileImg){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        String imgUrl = member.getProfileImg();

        if( !(profileImg == null || profileImg.getOriginalFilename() == null || profileImg.getOriginalFilename().isEmpty()) ){
            imgUrl = awsFileService.saveFile(profileImg);
        }

        member.updateProfileImg(imgUrl);

        return imgUrl;
    }

    @Transactional
    public UpdatedInfoResDto update(Long memberId, String updateNickname, MultipartFile profileImg) {
        updateNickname = this.updateNickname(memberId, updateNickname);
        String imgUrl = this.updateProfileImg(memberId, profileImg);

        return new UpdatedInfoResDto(updateNickname, imgUrl);
    }

    private String createCode(){
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new CustomException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

    private boolean checkDuplicated(String target,String type){
        if(type.equals("email")){

            return memberRepository.existsByEmail(target);
        }else{
            return memberRepository.existsByNickname(target);
        }


    }
}
