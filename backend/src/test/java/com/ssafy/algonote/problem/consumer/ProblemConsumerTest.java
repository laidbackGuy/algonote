package com.ssafy.algonote.problem.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.algonote.problem.dto.ConsumerProblemResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemConsumerTest {

    @Test
    @DisplayName("json으로 넘어올 시 DTO로 변환하기")
    void givenJsonData_thenConusmerProblemResDto() throws JsonProcessingException {
        String json = "{\n" +
                "\t\"problemId\": 1029,\n" +
                "\t\"title\": \"그림 교환\",\n" +
                "\t\"acceptedUserCount\": 1286.0,\n" +
                "\t\"level\": 15.0,\n" +
                "\t\"averageTries\": 3.5156,\n" +
                "\t\"tags\": [\n" +
                "\t\t\"bitmask\",\n" +
                "\t\t\"dp\",\n" +
                "\t\t\"dp_bitfield\"\n" +
                "\t]\n" +
                "}";
        ConsumerProblemResDto problem = ConsumerProblemResDto.fromJson(json);
        assertThat(problem.getProblemId()).isEqualTo(1029L);
        assertThat(problem.getTitle()).isEqualTo("그림 교환");
        assertThat(problem.getAcceptedUserCount()).isEqualTo(1286);
        assertThat(problem.getLevel()).isEqualTo(15);
        assertThat(problem.getAverageTries()).isEqualTo(3.5156);
        assertThat(problem.getTags()).contains("bitmask", "dp", "dp_bitfield");
    }

}