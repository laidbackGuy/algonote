package com.ssafy.grading.service;

import com.ssafy.grading.dto.ExecutionResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public interface LanguageExecutorService {

    ExecutionResult execute(String code, String input, String expected);

    String getActualOutput(String output);

    double getUsedTime(String output);

    double getUsedMemory(String output);

    default void deleteFiles(Path path) {
        try (var paths = Files.walk(path)) {
            // 내부 요소부터 외부 요소 순으로 정렬 후 삭제
            paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 문제 발생");
        }
    }
}
