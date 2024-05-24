package com.ssafy.grading.controller;

import com.ssafy.grading.dto.ExecutionResult;
import com.ssafy.grading.dto.request.ExecutionReqDto;
import com.ssafy.grading.service.LanguageExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/execute")
public class ExecutionController {
    private final Map<String, LanguageExecutorService> executorServiceMap;

    @Autowired
    public ExecutionController(@Qualifier("executorServiceMap") Map<String, LanguageExecutorService> executorServiceMap) {
        this.executorServiceMap = executorServiceMap;
    }

    @PostMapping("/{language}")
    public ResponseEntity<ExecutionResult> execute(@PathVariable("language") String language, @RequestBody ExecutionReqDto request) {
        return ResponseEntity.ok(executorServiceMap.get(language).execute(request.sourceCode(), request.inputData(), request.expectedOutput()));
    }

}
