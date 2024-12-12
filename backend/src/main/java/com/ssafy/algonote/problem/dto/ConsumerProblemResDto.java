package com.ssafy.algonote.problem.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerProblemResDto {
    Long problemId;
    String title;
    int acceptedUserCount;
    int level;
    double averageTries;
    Set<String> tags;

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    public static ConsumerProblemResDto fromJson(String jsonData) throws JsonProcessingException {
        return objectMapper.readValue(jsonData, ConsumerProblemResDto.class);
    }

}
