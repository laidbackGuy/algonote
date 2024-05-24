package com.ssafy.algonote.member.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.member.dto.request.NewNicknameReqDto;
import com.ssafy.algonote.member.dto.response.ProfileInfoResDto;
import com.ssafy.algonote.member.dto.response.UpdatedInfoResDto;
import com.ssafy.algonote.member.dto.response.NewNicknameResDto;
import com.ssafy.algonote.member.dto.response.UpdatedProfileImgResDto;
import com.ssafy.algonote.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "회원 관련 API")
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    @Operation(
        summary = "멤버 정보 조회",
        description = "회원 정보를 조회합니다."
    )
    @GetMapping()
    public ResponseEntity<ProfileInfoResDto> getProfileInfo(
        @RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(memberService.getProfileInfo(memberId));
    }

    @Operation(
        summary = "멤버 닉네임 변경"
    )
    @PatchMapping("/nicknames")
    public ResponseEntity<NewNicknameResDto> updateNickname(
        @RequestBody NewNicknameReqDto newNicknameReqDto) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("memberId : {}, updatedNickname: {}", memberId, newNicknameReqDto.getNickname());
        String updatedNickname = memberService.updateNickname(memberId, newNicknameReqDto.getNickname());
        return ResponseEntity.ok(NewNicknameResDto.of(updatedNickname));
    }

    @Operation(
        summary = "멤버 프로필 사진 변경"
    )
    @PatchMapping("/profileImgs")
    public ResponseEntity<UpdatedProfileImgResDto> updateProfileImg(
        @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("memberId : {} , profileImg :{}", memberId, profileImg.getOriginalFilename());
        String imgUrl = memberService.updateProfileImg(memberId, profileImg);
        return ResponseEntity.ok(UpdatedProfileImgResDto.of(imgUrl));
    }

    @Operation(
        summary = "멤버 정보 수정",
        description = "닉네임과 프로필이미지를 수정합니다."
    )
    @PutMapping("/update")
    public ResponseEntity<UpdatedInfoResDto> updateProfile(
        @RequestPart(value = "nickname", required = false) String newNickname,
        @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("memberId, nickname, profileImg : {}, {}, {}", memberId, newNickname, profileImg);
        return ResponseEntity.ok(memberService.update(memberId, newNickname, profileImg));
    }

}
