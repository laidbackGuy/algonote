package com.ssafy.algonote.problem.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.Tag;
import com.ssafy.algonote.problem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag saveTagIfNotExists(String name) {
        Optional<Tag> existingTag = tagRepository.findByNameEn(name);

        // 태그가 존재하지 않으면 새 태그를 저장
        return existingTag.orElseGet(() -> tagRepository.save(Tag.of(name)));
    }

    //태그 검색 시 태그에 해당하는 문제 반환
    @Transactional(readOnly = true)
    public Set<Problem> findProblemsByTagName(String name) {
        Tag tag = getTagOrException(name);
        return tag.getProblems();
    }

    // 전체 태그 조회
    @Transactional(readOnly = true)
    public List<String> getTagNames() {
        return tagRepository.findAll().stream().map(Tag::getNameEn).collect(Collectors.toList());
    }

    private Tag getTagOrException(String name) {
        return tagRepository.findByNameEn(name).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TAG));
    }

}
