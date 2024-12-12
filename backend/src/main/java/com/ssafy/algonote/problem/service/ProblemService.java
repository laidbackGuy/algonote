package com.ssafy.algonote.problem.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.ProblemDocument;
import com.ssafy.algonote.problem.domain.Tag;
import com.ssafy.algonote.problem.dto.ConsumerProblemResDto;
import com.ssafy.algonote.problem.repository.ProblemDocumentRepository;
import com.ssafy.algonote.problem.repository.ProblemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TagService tagService;
    private final ProblemDocumentRepository problemDocumentRepository;


    @Transactional(readOnly = true)
    public Problem getProblem(Long id) {
        return getProblemOrException(id);
    }

    //문제 저장시점에 태그가 태그 레포지토리에 없으면 태그 레포에 저장
    public void saveProblem(ConsumerProblemResDto dto) {

        log.info("Consume the event {}", dto);
        problemDocumentRepository.save(ProblemDocument.from(dto));
        // 저장하려는 문제가 이미 있는 문제인지 확인
        problemRepository.findById(dto.getProblemId()).ifPresent(it -> {

            throw new CustomException(ErrorCode.DUPLICATE_PROBLEM);
        });
        // 없다면 문제를 저장
        Problem problem = Problem.of(dto.getProblemId(), dto.getTitle(), dto.getLevel(), dto.getAcceptedUserCount(), dto.getAverageTries());

        Set<Tag> tagSet = dto.getTags().stream().map(tagService::saveTagIfNotExists).collect(Collectors.toUnmodifiableSet());
        problem.addTags(tagSet);

        problemRepository.save(problem);
    }

    //문제 id로 조회하기
    public Problem getProblemById(Long id) {
        return getProblemOrException(id);
    }

    public List<String> getTagOfProblem(Long problemId){
        ProblemDocument problemDocument = problemDocumentRepository.findById(String.valueOf(problemId)).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROBLEM));

        return problemDocument.getTags().stream().map(ProblemDocument.Tag::getTag).toList();
    }

    // 문제 이름으로 조회하기
    public Problem getProblemByTitle(String title) {
        return problemRepository.findByTitle(title).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROBLEM));
    }

    private Problem getProblemOrException(Long problemId) {
        return problemRepository.findById(problemId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROBLEM));
    }

}
