package com.ssafy.algonote.note.repository;

import com.ssafy.algonote.note.domain.NoteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NoteDocumentRepository extends ElasticsearchRepository<NoteDocument, Long> {

}
