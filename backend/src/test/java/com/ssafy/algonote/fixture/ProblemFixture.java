package com.ssafy.algonote.fixture;

import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.domain.Tag;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

public class ProblemFixture {

    public static Problem createProblem() {
        return createProblem(1L);
    }

    public static Problem createProblem(Long id) {
        return new Problem(
                id,
                "title",
                1,
                1,
                1.12,
                Set.of(
                        createTag(1, "tag1"),
                        createTag(2, "tag2"),
                        createTag(3, "tag3")
                )
        );
    }

    public static Tag createTag(Integer id, String nameEn) {
        Tag tag = Tag.of(nameEn);
        ReflectionTestUtils.setField(tag, "id", id);
        return tag;
    }
}
