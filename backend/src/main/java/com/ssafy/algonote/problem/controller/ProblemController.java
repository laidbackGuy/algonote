package com.ssafy.algonote.problem.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.problem.dto.response.AnalysisResDto;
import com.ssafy.algonote.problem.dto.response.NotedProblemResDto;
import com.ssafy.algonote.problem.dto.response.ProblemResDto;
import com.ssafy.algonote.problem.dto.response.SolvedProblemResDto;
import com.ssafy.algonote.problem.service.ProblemService;
import com.ssafy.algonote.problem.service.SolvedProblemService;
import com.ssafy.algonote.problem.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/problems")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Problem API", description = "문제 관련 API")
public class ProblemController {

    private final ProblemService problemService;
    private final SolvedProblemService solvedProblemService;
    private final TagService tagService;

    @GetMapping("/solved")
    @Operation(summary = "내가 푼 문제 조회")
    public ResponseEntity<Page<SolvedProblemResDto>> mySolvedProblem(@PageableDefault(size = 20, sort = "uploadedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(
                solvedProblemService.getSolvedProblemByMember(pageable, memberId)
                        .map(SolvedProblemResDto::from)
        );
    }

    @GetMapping("/search-tag")
    @Operation(summary = "태그로 문제 조회")
    public ResponseEntity<List<ProblemResDto>> findProblemsByTagName(@RequestParam("tag") String tagNameEn) {
        return ResponseEntity.ok(
                tagService.findProblemsByTagName(tagNameEn)
                        .stream()
                        .map(ProblemResDto::from)
                        .sorted(Comparator.comparingLong(ProblemResDto::id))
                        .toList()
        );
    }

    @GetMapping("/search-id")
    @Operation(summary = "문제 ID로 문제 조회")
    public ResponseEntity<ProblemResDto> findProblemsById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(ProblemResDto.from(problemService.getProblemById(id)));
    }

    @GetMapping("/search-name")
    @Operation(summary = "문제 이름으로 문제 조회")
    public ResponseEntity<ProblemResDto> findProblemsById(@RequestParam("name") String title) {
        return ResponseEntity.ok(ProblemResDto.from(problemService.getProblemByTitle(title)));
    }

    @GetMapping("/tags")
    @Operation(summary = "모든 태그 조회")
    public ResponseEntity<List<String>> findAllTags() {
        return ResponseEntity.of(Optional.ofNullable(tagService.getTagNames()));
    }


    @GetMapping("/analysis")
    @Operation(summary = "푼 문제 분석 결과")
    public ResponseEntity<AnalysisResDto> analysis() {
        Long memberId = SecurityUtil.getMemberId();
        log.info("memberId: {}", memberId);

        return ResponseEntity.ok(solvedProblemService.getAnalysis(memberId));
    }

    @GetMapping("/problem-note-count")
    @Operation(summary = "문제별 노트 개수 조회")
    public ResponseEntity<NotedProblemResDto> getProblemNoteCount() {
       Long memberId = SecurityUtil.getMemberId();

        return ResponseEntity.ok(solvedProblemService.getNotedProblem(memberId));
    }

    @PostMapping("/send-to-all-members")
    @Operation(summary = "모든 사용자에게 안푼지 오래된 알고리즘 유형에 대한 알림 전송")
    public ResponseEntity<Void> notifyTest() {
        solvedProblemService.sendNotificationsToAllMembersAboutTag();
        return ResponseEntity.ok().build();
    }

}
