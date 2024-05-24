package com.ssafy.algonote.note.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.note.dto.request.ReviewReqDto;
import com.ssafy.algonote.note.dto.request.ReviewUpdateReqDto;
import com.ssafy.algonote.note.dto.response.ReviewResDto;
import com.ssafy.algonote.note.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review API", description = "리뷰 관련 API")
@AllArgsConstructor
@RestController
public class ReviewController {

    private ReviewService reviewService;

    @Operation(
        summary = "리뷰 생성",
        description = "리뷰를 생성합니다."
    )
    @PostMapping("/notes/{noteId}/reviews")
    public ResponseEntity<Void> create(@PathVariable("noteId") Long noteId, @RequestBody ReviewReqDto req) {
        Long memberId = SecurityUtil.getMemberId();
        reviewService.create( req, memberId, noteId);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "리뷰 목록 조회",
        description = "특정 노트에 연관된 리뷰 목록을 조회합니다."
    )
    @GetMapping("/notes/{noteId}/reviews")
    public ResponseEntity<List<ReviewResDto>> readList(@PathVariable("noteId") Long noteId) {
        return ResponseEntity.ok(reviewService.readList(noteId));
    }

    @Operation(
        summary = "리뷰 수정",
        description = "리뷰 내용을 수정합니다."
    )
    @PatchMapping("/notes/{noteId}/reviews/{reviewId}")
    public ResponseEntity<Void> update(
        @RequestBody ReviewUpdateReqDto req, @PathVariable("noteId") Long noteId, @PathVariable("reviewId") Long reviewId) {
        Long memberId = SecurityUtil.getMemberId();
        reviewService.update(req, memberId, noteId, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "리뷰 삭제",
        description = "리뷰를 삭제합니다."
    )
    @DeleteMapping("/notes/{noteId}/reviews/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable("noteId") Long noteId, @PathVariable("reviewId") Long reviewId) {
        Long memberId = SecurityUtil.getMemberId();
        reviewService.delete(memberId, noteId, reviewId);
        return ResponseEntity.ok().build();
    }

}
