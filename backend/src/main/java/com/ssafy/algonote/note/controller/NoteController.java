package com.ssafy.algonote.note.controller;

import com.ssafy.algonote.config.security.SecurityUtil;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.dto.BookmarkResDto;
import com.ssafy.algonote.note.dto.HeartResDto;
import com.ssafy.algonote.note.dto.request.NoteSaveReqDto;
import com.ssafy.algonote.note.dto.request.NoteUpdateReqDto;
import com.ssafy.algonote.note.dto.request.TempNoteSaveReqDto;
import com.ssafy.algonote.note.dto.response.*;
import com.ssafy.algonote.note.service.BookmarkService;
import com.ssafy.algonote.note.service.HeartService;
import com.ssafy.algonote.note.service.NoteService;
import com.ssafy.algonote.problem.dto.response.ProblemWithNoteResDto;
import com.ssafy.algonote.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Note API", description = "노트 관련 API")
@Slf4j
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final HeartService heartService;
    private final BookmarkService bookmarkService;
    private final ProblemService problemService;

    @Operation(
            summary = "노트의 좋아요",
            description = "노트의 좋아요 상태를 관리한다."
    )
    @PostMapping("/{noteId}/hearts")
    public ResponseEntity<HeartResDto> heart(@PathVariable("noteId") Long noteId) {
        log.info("noteId: {}", noteId);

        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(heartService.heart(memberId, noteId));
    }

    @Operation(
            summary = "노트 저장",
            description = "노트를 저장한다. 노트 저장은 제출이력이 있는 문제에 관해서만 노트를 생성할 수 있다."
    )
    @PostMapping
    public ResponseEntity<Void> saveNote(@RequestBody NoteSaveReqDto req) {
        Long memberId = SecurityUtil.getMemberId();
        noteService.saveNote(memberId, req.problemId(), req.title(), req.content(), req.tempNoteId());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "노트 수정",
            description = "노트를 수정한다."
    )
    @PatchMapping("/{noteId}")
    public ResponseEntity<NoteResDto> updateNote(@PathVariable("noteId") Long noteId, @RequestBody NoteUpdateReqDto req) {
        Long memberId = SecurityUtil.getMemberId();
        Note updatedNote = noteService.update(memberId, noteId, req.title(), req.content());
        return ResponseEntity.ok(
                NoteResDto.from(
                        updatedNote,
                        heartService.heartCnt(noteId),
                        heartService.heartStatus(memberId, noteId),
                        bookmarkService.bookmarkStatus(memberId, noteId)
                )
        );
    }

    @Operation(
            summary = "노트 삭제",
            description = "노트를 삭제한다. 삭제 시 노트 뿐만 아니라 좋아요, 북마크, es의 노트Doc도 모두 삭제한다."
    )
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable("noteId") Long noteId) {
        Long memberId = SecurityUtil.getMemberId();
        noteService.deleteNote(memberId, noteId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "노트 상세 조회",
            description = "노트를 상세조회한다."
    )
    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResDto> getNote(@PathVariable("noteId") Long noteId) {
        Long memberId = SecurityUtil.getMemberId();
        Note result = noteService.getNoteById(noteId);
        return ResponseEntity.ok(NoteResDto.from(
                        noteService.getNoteById(noteId),
                        heartService.heartCnt(result.getId()),
                        heartService.heartStatus(memberId, result.getId()),
                        bookmarkService.bookmarkStatus(memberId, noteId)
                )
        );
    }

    @Operation(
            summary="문제 번호로 사용자가 작성한 노트 전부 조회"
    )
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<NoteResDto>> getNotesByProblem(@PathVariable Long problemId) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("memberId: {}, problemId : {}", memberId, problemId);
        List<Note> notes = noteService.getNotesByProblem(memberId, problemId);

        return ResponseEntity.ok(notes.stream().map(NoteResDto::of).toList());
    }

    @Operation(
            summary = "사용자가 작성한 노트 조회",
            description = "노트 상세조회 이전 단계로 문제별로 그루핑하여 문제안의 노트들을 모두 전달한다."
    )
    @GetMapping
    public ResponseEntity<ProblemWithNoteResDto> searchNotesByMember(@RequestParam("memberId") Long requestMemberId) {
        Long memberId = SecurityUtil.getMemberId();
        List<Note> myNotes = noteService.getNotesByMember(requestMemberId);

        Map<Long, List<Note>> groupedNotesByProblemId = myNotes
                .stream()
                .collect(Collectors.groupingBy(
                                note -> note.getProblem().getId()
                        )
                );

        return ResponseEntity.ok(ProblemWithNoteResDto.from(groupedNotesByProblemId
                        .values()
                        .stream()
                        .map(notes -> {
                                    List<NoteWithoutContentResDto> noteWithoutContentResDtos = notes.stream()
                                            .map(it ->
                                                    NoteWithoutContentResDto.from(
                                                            it,
                                                            heartService.heartCnt(it.getId()),
                                                            heartService.heartStatus(memberId, it.getId())
                                                    )
                                            ).toList();
                                    return NoteGroupByProblemResDto.from(
                                            notes.get(0).getProblem(),
                                            noteWithoutContentResDtos
                                    );
                                }
                        )
                        .toList()
                )
        );
    }

    @Operation(
            summary = "임시 노트 저장",
            description = "임시저장 버튼을 누르면 임시 저장하고, 저장된 데이터를 반환한다."
    )
    @PostMapping("/temp")
    public ResponseEntity<TempNoteResDto> saveTempNote(@RequestBody TempNoteSaveReqDto req) {
        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(noteService.saveTempNote(memberId, req.problemId(), req.title(), req.content()));
    }
    @Operation(
            summary = "임시 노트 삭제",
            description = "임시노트를 삭제한다."
    )
    @DeleteMapping("/{tempNoteId}/temp")
    public ResponseEntity<Void> deleteTempNote(@PathVariable("tempNoteId") Long tempNoteId) {
        Long memberId = SecurityUtil.getMemberId();
        noteService.deleteTempNote(memberId, tempNoteId);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "임시 노트 조회",
            description = "내가 해당 문제에 대해 작성했던 임시노트를 전체 조회한다."
    )
    @GetMapping("/temp")
    public ResponseEntity<List<TempNoteResDto>> getListByProblem(@RequestParam("problemId") Long problemId) {
        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(noteService.getTempNoteList(memberId, problemId));
    }
    @Operation(
            summary = "임시 노트 수정",
            description = "임시저장 버튼을 누르면 임시 저장하고, 저장된 데이터를 반환한다."
    )
    @PatchMapping("/{tempNoteId}/temp")
    public ResponseEntity<TempNoteResDto> updateTempNote(@PathVariable("tempNoteId") Long tempNoteId, @RequestBody NoteUpdateReqDto req) {
        Long memberId = SecurityUtil.getMemberId();
        return ResponseEntity.ok(noteService.updateTempNote(memberId, tempNoteId, req.title(), req.content()));
    }

    @Operation(
            summary = "모든 노트 조회"
    )
    @GetMapping("/all")
    public ResponseEntity<SearchResDto> getAllNotes(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("page: {}, size:{}", pageable.getOffset(), pageable.getPageSize());
        List<NoteSearchDto> noteSearchResults = noteService.getAllNotes(pageable);
        List<NoteSearchResDto> resDtos = noteSearchResults.stream().map(resDto -> {
            List<String> tags = problemService.getTagOfProblem(resDto.problemId());

            return NoteSearchResDto.of(resDto,
                    heartService.heartCnt(resDto.noteId()),
                    heartService.heartStatus(memberId, resDto.noteId()),
                    bookmarkService.bookmarkStatus(memberId, resDto.noteId()),
                    tags);
        }).toList();

//        Long count = noteService.countAllNotes();
//        return ResponseEntity.ok(AllSearchRestDto.of(count, resDtos));
        return ResponseEntity.ok(SearchResDto.from(resDtos));
    }


    @Operation(
            summary = "노트 검색 - 엘라스틱서치",
            description = "엘라스틱서치를 통해 노트를 조회한다."
    )
    @GetMapping("/full-text")
    public ResponseEntity<SearchResDto> fullTextSearch(@RequestParam("keyword") String keyword,
                                                       @PageableDefault(size=20) Pageable pageable) {
        Long memberId = SecurityUtil.getMemberId();
        log.info("fullTextSearch keyword: {}, page: {}, size:{}", keyword, pageable.getOffset(), pageable.getPageSize());

        List<NoteSearchDto> noteSearchResults = noteService.fulltextNoteSearch(keyword, pageable);
        List<NoteSearchResDto> resDtos = noteSearchResults.stream().map(resDto -> {
            List<String> tags = problemService.getTagOfProblem(resDto.problemId());

            return NoteSearchResDto.of(resDto,
                    heartService.heartCnt(resDto.noteId()),
                    heartService.heartStatus(memberId, resDto.noteId()),
                    bookmarkService.bookmarkStatus(memberId, resDto.noteId()),
                    tags);
        }).toList();


        return ResponseEntity.ok(SearchResDto.from(resDtos));
    }

}
