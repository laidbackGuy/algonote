package com.ssafy.grading.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResult {
    private String output; // 실행결과
    private Double executionTime; // milliseconds
    private Double memoryUsage; // bytes
    private Boolean isCorrect; // 출력 결과가 기대와 일치하는지
}
