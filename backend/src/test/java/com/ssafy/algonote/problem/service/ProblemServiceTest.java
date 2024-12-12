package com.ssafy.algonote.problem.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.repository.ProblemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ssafy.algonote.fixture.ProblemFixture.createProblem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {
    @InjectMocks
    private ProblemService sut;

    @Mock
    private ProblemRepository problemRepository;

    @Test
    @DisplayName("문제를 조회하면, 문제를 반환 - 단건 조회")
    void givenProblemId_whenSearchProblem_thenReturnProblem() {
        // given
        Long problemId = 1L;
        Problem problem = createProblem();
        given(problemRepository.findById(problemId)).willReturn(Optional.of(problem));

        // when
        Problem result = sut.getProblem(problemId);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("title", problem.getTitle())
                .hasFieldOrPropertyWithValue("tier", problem.getTier())
                .hasFieldOrPropertyWithValue("acceptedUserCount", problem.getAcceptedUserCount())
                .hasFieldOrPropertyWithValue("averageTries", problem.getAverageTries())
                .hasFieldOrPropertyWithValue("tags", problem.getTags());
        then(problemRepository).should().findById(problemId);
    }

    @Test
    @DisplayName("없는 문제를 조회하면, 에러를 반환 - 단건조회")
    void givenNonexistentProblemId_whenSearchProblem_thenThrowsException() {
        // given
        Long problemId = 0L;
        given(problemRepository.findById(problemId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.getProblem(problemId));

        // then
        assertThat(t)
                .isInstanceOf(CustomException.class)
                .hasMessage("해당하는 문제를 찾을 수 없습니다.");
        then(problemRepository).should().findById(problemId);
    }

}
