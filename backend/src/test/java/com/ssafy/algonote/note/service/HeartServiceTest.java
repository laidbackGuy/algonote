package com.ssafy.algonote.note.service;

import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.note.domain.Heart;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.repository.HeartRepository;
import com.ssafy.algonote.note.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static com.ssafy.algonote.fixture.MemberFixture.createMember;
import static com.ssafy.algonote.fixture.NoteFixture.createNote;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeartServiceTest {

    @InjectMocks
    private HeartService sut;
    @Mock
    private HeartRepository heartRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private NoteRepository noteRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("하트가 존재 하지 않을 경우 하트 생성")
    void test1() {
        // Given
        Long noteId = 1000L;
        Long memberId = 1L;

        // mocking
        Member member = createMember(memberId);
        Note note = createNote(noteId, member);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));
        given(heartRepository.findByMemberAndNote(member, note)).willReturn(mock(Heart.class));

        // When
        sut.heart(memberId, noteId);

        // Then
        verify(heartRepository, never()).save(any());
        verify(heartRepository).delete(any());
    }

    @Test
    @DisplayName("하트가 존재할 경우 하트 삭제")
    void test2() {
        // Given
        Long noteId = 1000L;
        Long memberId = 1L;

        // mocking
        Member member = createMember(memberId);
        Note note = createNote(noteId, member);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));
        given(heartRepository.findByMemberAndNote(member, note)).willReturn(null);

        // When
        sut.heart(memberId, noteId);

        // Then
        verify(heartRepository, never()).delete(any());
        verify(heartRepository).save(any());
    }


}