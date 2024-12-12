package com.ssafy.algonote.note.repository;

import com.ssafy.algonote.note.dto.response.NoteSearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteCustomRepository {
    List<NoteSearchDto> searchAll(Pageable pageable);
}
