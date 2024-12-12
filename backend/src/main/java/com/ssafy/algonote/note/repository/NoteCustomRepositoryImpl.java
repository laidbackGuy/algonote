package com.ssafy.algonote.note.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.domain.QNote;
import com.ssafy.algonote.note.dto.response.NoteSearchDto;
import com.ssafy.algonote.note.dto.response.SearchResDto;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ssafy.algonote.note.domain.QNote.note;

@Slf4j
public class NoteCustomRepositoryImpl implements NoteCustomRepository {

    private final JPAQueryFactory queryFactory;
    public NoteCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<NoteSearchDto> searchAll(Pageable pageable){


        return queryFactory.selectFrom(note)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream().map(NoteSearchDto::of).toList();
    }
}
