package com.ssafy.algonote.code.service;

import com.ssafy.algonote.code.dto.request.AnalyzeReqDto;
import com.ssafy.algonote.code.dto.request.ComplexityReqDto;
import com.ssafy.algonote.code.dto.response.AnalyzeResDto;
import com.ssafy.algonote.code.dto.response.ComplexityResDto;
import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.note.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final RestTemplate restTemplate;
    private final GptService gptService;

    @Value("${analyze.server.url}")
    private String serverUrl;

    public AnalyzeResDto analyzeCode(String lang, AnalyzeReqDto dto) {

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnalyzeReqDto> requestEntity = new HttpEntity<>(dto, headers);
        if("java".equals(lang)) {
            return restTemplate.postForObject(serverUrl+ "/java", requestEntity, AnalyzeResDto.class);
        } else if("py".equals(lang)) {
            return restTemplate.postForObject(serverUrl+ "/py", requestEntity, AnalyzeResDto.class);
        } else if("c".equals(lang)) {
            return restTemplate.postForObject(serverUrl+ "/c", requestEntity, AnalyzeResDto.class);
        } else if("cpp".equals(lang)) {
            return restTemplate.postForObject(serverUrl+ "/cpp", requestEntity, AnalyzeResDto.class);
        } else {
            throw new CustomException(ErrorCode.INVALID_QUERY_PARAM);
        }

    }

    public ComplexityResDto getComplexity(ComplexityReqDto dto) {
        return ComplexityResDto.builder()
            .timeComplexity(gptService.getTimeComplexity(dto))
            .spaceComplexity(gptService.getSpaceComplexity(dto))
            .build();
    }

}

