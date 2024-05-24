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
import com.ssafy.algonote.notification.domain.NotificationType;
import com.ssafy.algonote.notification.dto.request.NotificationReqDto;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class BookmarkService {

    private final ApplicationEventPublisher eventPublisher;

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final NoteRepository noteRepository;
    private final HeartRepository heartRepository;

    public BookmarkStatusResDto doBookmark(Long memberId, Long noteId) {
        Member member = getMemberOrElseThrow(memberId);
        Note note = getNoteOrElseThrow(noteId);

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByNoteIdAndMemberId(noteId, memberId);
        if (optionalBookmark.isEmpty()) {
            bookmarkRepository.save(Bookmark.builder()
                    .member(member)
                    .note(note)
                    .build());

            eventPublisher.publishEvent(
                new NotificationReqDto(
                    NotificationType.BOOKMARK,
                    note.getMember(),
                    member,
                    noteId,
                    null,
                    note.getTitle() + " 가 북마크 되었습니다."));

            return new BookmarkStatusResDto(true);
        } else {
            bookmarkRepository.delete(optionalBookmark.get());

            return new BookmarkStatusResDto(false);
        }
    }
    // 북마크 여부 확인
    public boolean bookmarkStatus(Long memberId, Long noteId) {
        Note note = getNoteOrElseThrow(noteId);
        Member member = getMemberOrElseThrow(memberId);
        return bookmarkRepository.existsByMemberAndNote(member, note);
    }

    private Member getMemberOrElseThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private Note getNoteOrElseThrow(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTE));
    }

    @Transactional(readOnly = true)
    public List<BookmarkResDto> getList(Long memberId) {
        getMemberOrElseThrow(memberId);
        return bookmarkRepository.findAllByMemberId(memberId)
                .stream()
                .map(
                        it -> BookmarkResDto.from(it, heartRepository.countByNote(it.getNote()))
                )
                .toList();
    }

}
