package com.ssafy.algonote.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.repository.NoteRepository;
import com.ssafy.algonote.note.domain.Review;
import com.ssafy.algonote.note.dto.request.ReviewReqDto;
import com.ssafy.algonote.note.dto.request.ReviewUpdateReqDto;
import com.ssafy.algonote.note.dto.response.ReviewResDto;
import com.ssafy.algonote.note.repository.ReviewRepository;
import com.ssafy.algonote.note.service.ReviewService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    NoteRepository noteRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Nested
    @DisplayName("리뷰 생성")
    class testCreate {

        private Long noteId;
        private Long memberId;
        private Member member;
        private Note note;
        private ReviewReqDto reviewReqDto;

        @BeforeEach
        void setup() {
            noteId = 1L;
            memberId = 1L;
            member = getMember();
            note = getNote();
            reviewReqDto = getReviewReqDto();
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(note)).given(noteRepository).findById(any());

            // when
            reviewService.create(reviewReqDto, memberId, noteId);

            // then
            verify(reviewRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 memberId")
        void fail_member() {
            // given
            willReturn(Optional.empty()).given(memberRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.create(reviewReqDto, memberId, noteId));

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
                () -> reviewService.create(reviewReqDto, memberId, noteId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_NOTE, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] endLine이 startLine보다 크다")
        void fail_line() {
            // given
            ReviewReqDto invalidReviewReqDto = new ReviewReqDto(100, 1, "content");
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(note)).given(noteRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.create(invalidReviewReqDto, memberId, noteId));

            //then
            assertEquals(ErrorCode.INVALID_LINE_RANGE, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("리뷰 목록 조회")
    class testReadList {

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Member member = getMember();
            Note note = getNote();

            Review review1 = new Review(1L, member, note, 1, 3, "content1");
            Review review2 = new Review(2L, member, note, 2, 4, "content2");
            List<Review> reviews = Arrays.asList(review1, review2);

            willReturn(Optional.of(note)).given(noteRepository).findById(any());
            when(reviewRepository.findAllByNoteId(any())).thenReturn(reviews);

            // when
            List<ReviewResDto> resultList = reviewService.readList(any());

            // then
            assertEquals(2, resultList.size());
            assertEquals("content1", resultList.get(0).content());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 noteId")
        void fail_note() {
            // given
            Long noteId = 1L;
            willReturn(Optional.empty()).given(noteRepository).findById(any());

            // when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.readList(noteId));

            // then
            assertEquals(ErrorCode.NOT_FOUND_NOTE, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("리뷰 수정")
    class testUpdate {

        private Long noteId;
        private Long reviewId;
        private Long memberId;
        private Member member;
        private Review review;
        private ReviewUpdateReqDto reviewUpdateReqDto;

        @BeforeEach
        void setup() {
            noteId = 1L;
            reviewId = 1L;
            memberId = 1L;
            member = getMember();
            review = getReview();
            reviewUpdateReqDto = getReviewUpdateReqDto();
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            // when
            reviewService.update(reviewUpdateReqDto, memberId, noteId, reviewId);

            // then
//            verify(reviewRepository, times(1)).save(any());
            assertEquals(reviewUpdateReqDto.content(), review.getContent());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 경로")
        void fail_invalid_path() {
            // given
            Long wrongNoteId = 10L;
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(reviewUpdateReqDto, memberId, wrongNoteId, reviewId));

            //then
            assertEquals(ErrorCode.INVALID_PATH, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 memberId")
        void fail_member() {
            // given
            willReturn(Optional.empty()).given(memberRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(reviewUpdateReqDto, memberId, noteId, reviewId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 reviewId")
        void fail_review() {
            // given
            Long wrongReviewId = 10L;
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.empty()).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(reviewUpdateReqDto, memberId, noteId, wrongReviewId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_REVIEW, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 작성자가 아닌 멤버가 수정을 시도하는 경우")
        void fail_no_authority() {
            // given
            Member memberWithNoAuthority = Member.builder()
                .id(10L)
                .build();

            willReturn(Optional.of(memberWithNoAuthority)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(reviewUpdateReqDto, memberId, noteId, reviewId));

            //then
            assertEquals(ErrorCode.NO_AUTHORITY, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("리뷰 삭제")
    class testDelete {

        private Long noteId;
        private Long reviewId;
        private Long memberId;
        private Member member;
        private Review review;

        @BeforeEach
        void setup() {
            noteId = 1L;
            reviewId = 1L;
            memberId = 1L;
            member = getMember();
            review = getReview();
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            // when
            reviewService.delete(memberId, noteId, reviewId);

            // then
            verify(reviewRepository, times(1)).delete(any());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 경로")
        void fail_invalid_path() {
            // given
            Long wrongNoteId = 10L;
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.delete(memberId, wrongNoteId, reviewId));

            //then
            assertEquals(ErrorCode.INVALID_PATH, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 memberId")
        void fail_member() {
            // given
            willReturn(Optional.empty()).given(memberRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.delete(memberId, noteId, reviewId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 유효하지 않은 reviewId")
        void fail_review() {
            // given
            Long wrongReviewId = 10L;
            willReturn(Optional.of(member)).given(memberRepository).findById(any());
            willReturn(Optional.empty()).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.delete(memberId, noteId, wrongReviewId));

            //then
            assertEquals(ErrorCode.NOT_FOUND_REVIEW, exception.getErrorCode());
        }

        @Test
        @DisplayName("[실패] 작성자가 아닌 멤버가 삭제를 시도하는 경우")
        void fail_no_authority() {
            // given
            Member memberWithNoAuthority = Member.builder()
                .id(10L)
                .build();

            willReturn(Optional.of(memberWithNoAuthority)).given(memberRepository).findById(any());
            willReturn(Optional.of(review)).given(reviewRepository).findById(any());

            //when
            CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.delete(memberId, noteId, reviewId));

            //then
            assertEquals(ErrorCode.NO_AUTHORITY, exception.getErrorCode());
        }

    }

    ReviewReqDto getReviewReqDto() {
        return new ReviewReqDto(1, 3, "content");
    }

    ReviewUpdateReqDto getReviewUpdateReqDto() {
        return new ReviewUpdateReqDto("updated content");
    }

    Note getNote() {
        return Note.builder()
            .id(1L)
            .build();
    }

    Member getMember() {
        return Member.builder()
            .id(1L)
            .build();
    }

    Review getReview() {
        return Review.builder()
            .id(1L)
            .member(getMember())
            .note(getNote())
            .build();
    }

}