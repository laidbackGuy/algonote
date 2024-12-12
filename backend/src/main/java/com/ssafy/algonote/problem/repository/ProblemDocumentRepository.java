package com.ssafy.algonote.problem.repository;

import com.ssafy.algonote.problem.domain.ProblemDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProblemDocumentRepository extends ElasticsearchRepository<ProblemDocument, String> {

}
