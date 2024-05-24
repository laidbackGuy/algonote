package com.ssafy.algonote.note.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.note.domain.Bookmark;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.dto.BookmarkResDto;
import com.ssafy.algonote.note.dto.BookmarkStatusResDto;
import com.ssafy.algonote.note.repository.BookmarkRepository;
import com.ssafy.algonote.note.repository.HeartRepository;
import com.ssafy.algonote.note.repository.NoteRepository;
import com.ssafy.algonote.problem.domain.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    BookmarkService bookmarkService;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    NoteRepository noteRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    HeartRepository heartRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Nested
    @DisplayName("북마크 생성/삭제")
    class testBookmark {

        private Long noteId;
        private Long memberId;
        private Note note;
        private Member member;
        private Bookmark bookmark;

        @BeforeEach
        void setup() {
            noteId = 1L;
            memberId = 1L;
            note = getNote();
            member = getMember();
            bookmark = getBookmark();
        }

        @Test
        @DisplayName("[성공] 북마크가 없었던 경우 생성")
        void testDoBookmark_BookmarkDoesNotExist() {
            // given
            member = getMember();
            note = getNote();

            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(note)).given(noteRepository).findById(any());

            willReturn(Optional.empty()).given(bookmarkRepository).findByNoteIdAndMemberId(anyLong(), anyLong());

            // when
            BookmarkStatusResDto result = bookmarkService.doBookmark(1L, 1L);

            // then
            verify(bookmarkRepository, times(1)).findByNoteIdAndMemberId(1L, 1L);
            verify(bookmarkRepository, times(1)).save(any());

            assertTrue(result.bookmarked());
        }

        @Test
        @DisplayName("[성공] 북마크가 있었던 경우 삭제")
        void testDoBookmark_BookmarkExists() {
            // given
            member = getMember();
            note = getNote();

            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(note)).given(noteRepository).findById(any());

            willReturn(Optional.of(bookmark)).given(bookmarkRepository).findByNoteIdAndMemberId(anyLong(), anyLong());

            // when
            BookmarkStatusResDto result = bookmarkService.doBookmark(1L, 1L);

            // then
            verify(bookmarkRepository, times(1)).findByNoteIdAndMemberId(1L, 1L);
            verify(bookmarkRepository, times(1)).delete(any());

            // Asserting the result
            assertFalse(result.bookmarked());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 memberId")
        void fail_member() {
            // given
            willReturn(Optional.empty()).given(memberRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                    () -> bookmarkService.doBookmark(memberId, noteId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 noteId")
        void fail_note() {
            // given
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.empty()).given(noteRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                    () -> bookmarkService.doBookmark(memberId, noteId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_NOTE, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("북마크 목록 조회")
    class testGetList {

        private Long memberId;
        private Note note;
        private Member member;

        @BeforeEach
        void setup() {
            memberId = 1L;
            note = getNote();
            member = getMember();
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            member = getMember();
            note = getNote();

            Bookmark bookmark1 = new Bookmark(1L, member, note);
            Bookmark bookmark2 = new Bookmark(2L, member, note);
            List<Bookmark> bookmarks = Arrays.asList(bookmark1, bookmark2);

            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(bookmarkRepository.findAllByMemberId(any())).thenReturn(bookmarks);

            // when
            List<BookmarkResDto> resultList = bookmarkService.getList(memberId);

            // then
            assertEquals(2, resultList.size());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 memberId")
        void fail_member() {
            // given
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            //when
            CustomException exception = assertThrows(CustomException.class,
                    () -> bookmarkService.getList(memberId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
        }

    }

    Note getNote() {
        return Note.builder()
                .id(1L)
                .problem(new Problem(1L, "title", 1, 1, 1, new HashSet<>()))
                .build();
    }

    Member getMember() {
        return Member.builder()
                .id(1L)
                .build();
    }

    Bookmark getBookmark() {
        return Bookmark.builder()
                .id(1L)
                .build();
    }

}