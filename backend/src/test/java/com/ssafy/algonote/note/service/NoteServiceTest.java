package com.ssafy.algonote.note.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.domain.NoteDocument;
import com.ssafy.algonote.note.domain.TempNote;
import com.ssafy.algonote.note.repository.*;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.SolvedProblem;
import com.ssafy.algonote.problem.repository.SolvedProblemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.ssafy.algonote.fixture.MemberFixture.createMember;
import static com.ssafy.algonote.fixture.NoteFixture.createNote;
import static com.ssafy.algonote.fixture.NoteFixture.createTempNote;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteService sut;
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private TempNoteRepository tempNoteRepository;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private HeartRepository heartRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private SolvedProblemRepository solvedProblemRepository;
    @Mock
    private NoteDocumentRepository noteDocumentRepository;

    // 노트 생성
    @Test
    @DisplayName("[생성] 노트 작성이 성공한 경우 - 작성한 임시노트 없음")
    void givenNoteDataFromTempNote_whenSaveNote_thenNothing() {
        //given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;
        Long tempNoteId = 2L;
        Problem mockProblem = mock(Problem.class);
        SolvedProblem mockSolvedProblem = mock(SolvedProblem.class);

        when(mockProblem.getId()).thenReturn(problemId);
        when(mockSolvedProblem.getProblem()).thenReturn(mockProblem);

        //mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mock(Member.class)));
        given(solvedProblemRepository.findByMember_IdAndProblem_Id(memberId, problemId)).willReturn(Optional.of(mockSolvedProblem));
        given(noteRepository.save(any())).willReturn(mock(Note.class));
        given(tempNoteRepository.findById(any())).willReturn(Optional.of(mock(TempNote.class)));
        given(noteDocumentRepository.save(any())).willReturn(mock(NoteDocument.class));

        //when
        sut.saveNote(memberId, problemId, title, content, tempNoteId);

        //then
        verify(noteRepository).save(any());
        verify(tempNoteRepository).delete(any());
        verify(noteDocumentRepository).save(any());
    }

    @Test
    @DisplayName("[생성] 노트 작성이 성공한 경우 - 임시노트는 삭제")
    void givenNoteData_whenSaveNoteFromTempNote_thenNothing() {
        //given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;
        Problem mockProblem = mock(Problem.class);
        SolvedProblem mockSolvedProblem = mock(SolvedProblem.class);

        when(mockProblem.getId()).thenReturn(problemId);
        when(mockSolvedProblem.getProblem()).thenReturn(mockProblem);

        //mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mock(Member.class)));
        given(solvedProblemRepository.findByMember_IdAndProblem_Id(memberId, problemId)).willReturn(Optional.of(mockSolvedProblem));
        given(noteRepository.save(any())).willReturn(mock(Note.class));
        given(noteDocumentRepository.save(any())).willReturn(mock(NoteDocument.class));

        //when
        sut.saveNote(memberId, problemId, title, content, null);

        //then
        verify(noteRepository).save(any());
        verify(noteDocumentRepository).save(any());
    }


    @Test
    @DisplayName("[생성] 노트 작성 시 요청한 유저가 존재하지 않는 경우")
    void givenNoteDataWithoutMember_whenSaveNote_thenThrowException() {
        // given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        //when & then
        assertThrows(CustomException.class, () -> {
            sut.saveNote(memberId, problemId, title, content, null);
        });
    }

    @Test
    @DisplayName("[생성] 노트 작성 시 풀지 않은 문제를 등록한 경우")
    void givenNoteAndMemberInfo_whenSaveNoteWithNonExistProblem_thenThrowException() {
        // given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        given(memberRepository.findById(memberId)).willReturn(Optional.of(mock(Member.class)));
        given(solvedProblemRepository.findByMember_IdAndProblem_Id(memberId, problemId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
            sut.saveNote(memberId, problemId, title, content, null);
        });
    }

    @Test
    @DisplayName("[삭제] 내가 작성한 노트 삭제")
    void givenUserId_whenDeleteNote_thenNothing() {
        Long memberId = 1L;
        Long noteId = 1000L;
        Member member = createMember(memberId);
        Note note = createNote(noteId, member);
        NoteDocument noteDocument = createNoteDoc(noteId);
        // mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));
        given(noteDocumentRepository.findById(noteId)).willReturn(Optional.of(noteDocument));

        sut.deleteNote(memberId, noteId);
        verify(noteRepository).delete(any());
    }

    @Test
    @DisplayName("[삭제] 노트 삭제 시 권한이 없는 경우")
    void givenMemberAndNote_whenDeleteNoteWithoutAuthority_thenThrowException() {
        // given
        Long memberId = 1L;
        Long noteId = 1000L;

        Member writer = createMember(memberId);
        Member other = createMember(2L);
        Note note = createNote(noteId, other);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(writer));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));

        // when & then
        assertThrows(CustomException.class, () -> {
            sut.deleteNote(memberId, noteId);
        });
    }

    @Test
    @DisplayName("[삭제] 노트 삭제 시 노트가 존재하지 않는 경우")
    void givenMemberAndNote_whenDeleteNoteWithoutNote_thenThrowException() {
        // given
        Long memberId = 1L;
        Long noteId = 1000L;

        Member member = createMember(memberId);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
            sut.deleteNote(memberId, noteId);
        });
    }


    @Test
    @DisplayName("[수정] 노트 수정이 성공하는 경우")
    void givenTitleAndContent_whenUpdateNote_thenNothing() {
        // given
        Long memberId = 1L;
        Long noteId = 1000L;
        String title = "updatedTitle";
        String content = "updatedContent";

        Member member = createMember(memberId);
        Note note = createNote(noteId, member);
        NoteDocument noteDocument = createNoteDoc(noteId);

        // mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));
        given(noteDocumentRepository.findById(noteId)).willReturn(Optional.of(noteDocument));

        // when
        sut.update(memberId, noteId, title, content);

        //then
        verify(noteDocumentRepository).save(any());
        verify(noteRepository).saveAndFlush(any());
    }

    @Test
    @DisplayName("[생성] 임시 노트 작성이 성공한 경우")
    void givenNoteData_whenSaveTempNote_thenNothing() {
        //given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        //mocking
        Member mockMember = mock(Member.class);
        TempNote mockTempNote = mock(TempNote.class);
        SolvedProblem mockSolvedProblem = mock(SolvedProblem.class);
        when(mockTempNote.getMember()).thenReturn(mockMember);
        when(mockTempNote.getProblem()).thenReturn(mock(Problem.class));
        when(mockMember.getId()).thenReturn(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
        given(solvedProblemRepository.findByMember_IdAndProblem_Id(memberId, problemId)).willReturn(Optional.of(mockSolvedProblem));
        given(tempNoteRepository.save(any())).willReturn(mockTempNote);

        //when
        sut.saveTempNote(memberId, problemId, title, content);

        //then
        verify(tempNoteRepository).save(any());
    }

    @Test
    @DisplayName("[수정] 임시노트 수정이 성공하는 경우")
    void givenTitleAndContent_whenUpdateTempNote_thenNothing() {
        // given
        Long memberId = 1L;
        Long tempNoteId = 1000L;
        String title = "updatedTitle";
        String content = "updatedContent";

        // mocking
        Member mockMember = mock(Member.class);
        TempNote mockTempNote = mock(TempNote.class);
        when(mockTempNote.getId()).thenReturn(tempNoteId);
        when(mockTempNote.getMember()).thenReturn(mockMember);
        when(mockTempNote.getProblem()).thenReturn(mock(Problem.class));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
        given(tempNoteRepository.findById(tempNoteId)).willReturn(Optional.of(mockTempNote));
        given(tempNoteRepository.saveAndFlush(any())).willReturn(mockTempNote);
        // when
        sut.updateTempNote(memberId, tempNoteId, title, content);

        //then
        verify(tempNoteRepository).saveAndFlush(any());
    }


    @Test
    @DisplayName("[삭제] 내가 작성한 임시노트 삭제")
    void givenUserId_whenDeleteTempNote_thenNothing() {
        Long memberId = 1L;
        Long tempNoteId = 1000L;
        Member member = createMember(memberId);
        TempNote tempNote = createTempNote(tempNoteId, member);
        // mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(tempNoteRepository.findById(tempNoteId)).willReturn(Optional.of(tempNote));

        sut.deleteTempNote(memberId, tempNoteId);
        verify(tempNoteRepository).delete(any());
    }

    private NoteDocument createNoteDoc(Long noteId) {
        NoteDocument noteDocument = new NoteDocument();
        ReflectionTestUtils.setField(noteDocument, "id", noteId);
        return noteDocument;
    }

}
