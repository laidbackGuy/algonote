package com.ssafy.algonote.submission.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.submission.dto.request.SubmissionReqDto;
import com.ssafy.algonote.submission.dto.response.SubmissionResDto;
import com.ssafy.algonote.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Submission API", description = "제출이력 관련 API - for chrome Extension")
@Slf4j
@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(
            summary = "제출이력 저장하기",
            description = "백준의 제출이력을 리스트 형태로 받아 저장한다. 이미 저장된 제출이력의 경우 저장되지 않는다."
    )
    @PostMapping
    public ResponseEntity<Void> saveSubmission(@RequestBody List<SubmissionReqDto> dtos) {
//        log.info("사용자 제출 정보: {}", dtos);
        Long memberId = SecurityUtil.getMemberId();
        dtos.stream().forEach(dto -> submissionService.saveSubmission(dto, memberId));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "멤버와 문제로 제출이력 조회",
            description = "멤버 ID와 문제 번호로 제출이력을 조회한다. 해당 API는 노트 작성 페이지에서 사용한다."
    )
    @GetMapping
    public ResponseEntity<List<SubmissionResDto>> searchSubmissions(@RequestParam("memberId") Long memberId, @RequestParam("problemId") Long problemId) {
        return ResponseEntity.ok(submissionService.searchSubmissions(memberId, problemId)
                .stream()
                .map(SubmissionResDto::fromDto)
                .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "제출이력 ID로 제출이력 조회",
            description = "제출이력 ID로 제출이력을 조회한다."
    )
    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResDto> searchSubmission(@PathVariable("submissionId") Long submissionId) {
        return ResponseEntity.ok(
                SubmissionResDto.fromDto(submissionService.searchSubmissionById(submissionId))
        );
    }

}
