package com.ssafy.algonote.problem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.algonote.problem.domain.Tag;
import com.ssafy.algonote.problem.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService sut;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("이미 존재하는 태그일 경우 저장 안함")
    void givenTag_whenSaveTag_thenNothingHappen() throws JsonProcessingException {
        // given
        Tag existTag = createTag(1, "tag1");
        given(tagRepository.findByNameEn("tag1")).willReturn(Optional.of(existTag));
        // when
        sut.saveTagIfNotExists("tag1");
        // then
        verify(tagRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 태그일 경우 저장")
    void givenTag_whenSaveTag_thenSaveTag() throws JsonProcessingException {
        // given
        Tag newTag = createTag(1, "tag1");
        given(tagRepository.findByNameEn("tag1")).willReturn(Optional.empty());
        // when
        sut.saveTagIfNotExists("tag1");
        // then
        verify(tagRepository, times(1)).save(any());
    }


    private Tag createTag(Integer id, String nameEn) {
        Tag tag = Tag.of(nameEn);
        ReflectionTestUtils.setField(tag, "id", id);
        return tag;
    }

}