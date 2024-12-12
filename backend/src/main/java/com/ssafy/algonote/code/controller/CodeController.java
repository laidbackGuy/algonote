package com.ssafy.algonote.code.controller;

import com.ssafy.algonote.code.dto.request.AnalyzeReqDto;
import com.ssafy.algonote.code.dto.request.ComplexityReqDto;
import com.ssafy.algonote.code.dto.response.AnalyzeResDto;
import com.ssafy.algonote.code.dto.response.ComplexityResDto;
import com.ssafy.algonote.code.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Code API", description = "코드 분석 관련 API")
@RestController
@RequestMapping("/codes")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @Operation(
            summary = "코드 분석 ",
            description = "코드에 대한 실행시간 및 메모리 사용량을 보여줍니다. 쿼리 파라미터는 lang=java / lang=py 둘 중에 하나 넣어주면 됩니다."
    )
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResDto> analyzeCode(@RequestBody AnalyzeReqDto dto, @RequestParam("lang") String lang) {
        return ResponseEntity.ok(codeService.analyzeCode(lang, dto));
    }

    @Operation(
        summary = "복잡도 계산",
        description = "코드에 대한 시간 복잡도와 공간 복잡도를 계산합니다."
    )
    @PostMapping("/complexity")
    public ResponseEntity<ComplexityResDto> getComplexity(@RequestBody ComplexityReqDto dto) {
        return ResponseEntity.ok(codeService.getComplexity(dto));
    }

}
