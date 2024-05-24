package com.ssafy.algonote.note.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.note.dto.BookmarkResDto;
import com.ssafy.algonote.note.dto.BookmarkStatusResDto;
import com.ssafy.algonote.note.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bookmark API", description = "북마크 관련 API")
@AllArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(
        summary = "북마크 생성/삭제",
        description = "북마크를 생성하거나 삭제합니다."
    )
    @PostMapping("/notes/{noteId}/bookmarks")
    public ResponseEntity<BookmarkStatusResDto> doBookmark(@PathVariable("noteId") Long noteId) {
        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(bookmarkService.doBookmark(memberId, noteId));
    }

    @Operation(
        summary = "북마크된 노트 목록 조회",
        description = "특정 회원이 북마크한 노트 목록를 조회합니다."
    )
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkResDto>> getList(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(bookmarkService.getList(memberId));
    }



}
