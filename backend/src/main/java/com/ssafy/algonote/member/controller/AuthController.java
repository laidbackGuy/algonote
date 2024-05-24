package com.ssafy.algonote.member.controller;

import com.ssafy.algonote.member.dto.request.DestEmatilReqDto;
import com.ssafy.algonote.member.dto.request.EmailAuthReqDto;
import com.ssafy.algonote.member.dto.request.EmailDupCheckReqDto;
import com.ssafy.algonote.member.dto.request.LoginReqDto;
import com.ssafy.algonote.member.dto.request.NicknameDupCheckReqDto;
import com.ssafy.algonote.member.dto.request.SignUpReqDto;
import com.ssafy.algonote.member.dto.response.DupcheckResDto;
import com.ssafy.algonote.member.dto.response.EmailAuthResDto;
import com.ssafy.algonote.member.dto.response.LoginResDto;
import com.ssafy.algonote.member.dto.response.LoginReturnDto;
import com.ssafy.algonote.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }

    @Operation(
        summary = "회원가입",
        description = "회원가입 요청을 보냅니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        log.info("signUpReqDto : {}", signUpReqDto);
        memberService.signUp(signUpReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "로그인",
        description = "로그인 요청을 보냅니다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login (@RequestBody LoginReqDto loginReqDto) {
        log.info("loginReqDto : {}", loginReqDto);
        LoginReturnDto loginReturnDto = memberService.login(loginReqDto);


        LoginResDto loginResDto = LoginResDto.from(loginReturnDto);

        HttpHeaders header = new HttpHeaders();
        header.add("token", loginReturnDto.token());

        return new ResponseEntity<>(loginResDto, header, HttpStatus.OK);

    }

    @Operation(
        summary = "이메일 중복확인",
        description = "중복 여부를 반환합니다."
    )
    @PostMapping("/email-dupcheck")
    public ResponseEntity<DupcheckResDto> emailDupCheck(@RequestBody EmailDupCheckReqDto emailDupCheckReqDto) {
        log.info("emailDupCheckReqDto : {}", emailDupCheckReqDto);
        boolean duplicated = memberService.emailDupCheck(emailDupCheckReqDto);
        return ResponseEntity.ok(new DupcheckResDto(duplicated));
    }

    @Operation(
        summary = "닉네임 중복확인",
        description = "중복 여부를 반환합니다."
    )
    @PostMapping("/nickname-dupcheck")
    public ResponseEntity<DupcheckResDto> nicknameDupCheck(@RequestBody NicknameDupCheckReqDto nicknameDupCheckReqDto) {
        log.info("nicknameDupCheckReqDto : {}", nicknameDupCheckReqDto);
        boolean duplicated = memberService.nicknameDupCheck(nicknameDupCheckReqDto);
        return ResponseEntity.ok(new DupcheckResDto(duplicated));
    }

    @Operation(
        summary = "인증코드 발송",
        description = "입력된 이메일로 인증코드를 발송합니다."
    )
    @PostMapping("/verification-requests")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody DestEmatilReqDto destEmatilReqDto) {
        log.info("destEmatilReqDto : {}", destEmatilReqDto);
        memberService.sendCodeToEmail(destEmatilReqDto.email());

        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "인증코드 확인",
        description = "인증코드 일치 여부를 반환합니다."
    )
    @PostMapping("/verification")
    public ResponseEntity<EmailAuthResDto> verifyCode(@Valid @RequestBody EmailAuthReqDto emailAuthReqDto) {
        log.info("emailAuthReqDto : {}", emailAuthReqDto);
        EmailAuthResDto emailAuthResDto = memberService.verifyCode(emailAuthReqDto);

        return ResponseEntity.ok(emailAuthResDto);
    }

}
