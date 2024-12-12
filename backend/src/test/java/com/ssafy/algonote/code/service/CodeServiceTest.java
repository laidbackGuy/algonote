package com.ssafy.algonote.code.service;

import com.ssafy.algonote.code.dto.request.AnalyzeReqDto;
import com.ssafy.algonote.code.dto.response.AnalyzeResDto;
import com.ssafy.algonote.note.service.GptService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@MockBean(JpaMetamodelMappingContext.class)
@RestClientTest(CodeService.class)
class CodeServiceTest {

    @Autowired
    private CodeService codeService;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private GptService gptService;

    @Test
    @DisplayName("[Rest Template] 응답 테스트")
    void analyzeCode() {
        // Given
        String expected = "60";
        Double executionTime = 0d;
        Double memoryUsage = 0d;
        String lang = "java";
        AnalyzeReqDto reqDto = getAnalyzeReqDto(expected);
        AnalyzeResDto expectedResponse = getAnalyzeResDto(expected, executionTime, memoryUsage, true);

        // Mock RestTemplate
        when(restTemplate.postForObject(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpEntity.class), ArgumentMatchers.eq(AnalyzeResDto.class)))
                .thenReturn(expectedResponse);

        // When
        AnalyzeResDto actualResponseDto = codeService.analyzeCode(lang, reqDto);

        // Then
        assertEquals(expectedResponse, actualResponseDto);

    }

    private String getSourceCode() {
        return "\"import java.util.Scanner;\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner scanner = new Scanner(System.in);\\n        int sum = 0;\\n        while (scanner.hasNextInt()) {\\n            sum += scanner.nextInt();\\n        }\\n        System.out.println(sum);\\n        scanner.close();\\n    }\\n}\"";
    }

    private String getInputData() {
        return "10\n20\n30";
    }

    private AnalyzeReqDto getAnalyzeReqDto(String expected) {
        return new AnalyzeReqDto(getSourceCode(), getInputData(), expected);
    }

    private AnalyzeResDto getAnalyzeResDto(String output, Double executionTime, Double memoryUsage, Boolean isCorrect) {
        return new AnalyzeResDto(output, executionTime, memoryUsage, isCorrect);
    }

}