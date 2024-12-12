package com.ssafy.grading.service;

import com.ssafy.grading.dto.ExecutionResult;
import com.ssafy.grading.exception.ErrorCode;
import com.ssafy.grading.exception.GradeApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.ssafy.grading.util.CodeInputVerification.normalizeNewlines;

@Slf4j
@Service
public class JavaExecutorService implements LanguageExecutorService {

    private static final String JAVA_COMPILER = "javac";
    private static final String JAVA_RUNNER = "java";

    //TODO : 시간 초과 발생 시 txt 파일이 삭제 안됨.

    @Override
    public ExecutionResult execute(String code, String input, String expected) {
        String dirPath = UUID.randomUUID() + "/";
        String className = "Main";
        String javaFile = dirPath + className + ".java";
        String inputFileName = dirPath + className + "Input.txt";
        try {
            File directory = new File(dirPath);
            if (!directory.exists())
                directory.mkdir();

            // 소스 코드와 입력 데이터를 파일로 저장
            int firstBrace = findFirstBrace(code);
            int lastBrace = findLastBrace(code, firstBrace);

            code = addMeasureCode(code, firstBrace, lastBrace);
            Files.write(Paths.get(javaFile), code.getBytes());
            Files.write(Paths.get(inputFileName), input.getBytes());

            compile(javaFile); // .java 컴파일
            String output = run(dirPath, className, inputFileName);
            String result = normalizeNewlines(getActualOutput(output));
            double executionTime = getUsedTime(output);
            double memoryUsage = getUsedMemory(output);
            boolean isCorrect = result.trim().equals(normalizeNewlines(expected.trim()));
            return new ExecutionResult(result, executionTime, memoryUsage, isCorrect);
        } catch (Exception e) {
            return new ExecutionResult(e.getMessage(), null, null, null);
        } finally {
            deleteFiles(Paths.get(dirPath));
        }
    }

    private void compile(String javaFile) throws IOException, InterruptedException {
        // Java 파일 컴파일하기
        ProcessBuilder compileBuilder = new ProcessBuilder(JAVA_COMPILER, "-encoding", "UTF-8", javaFile);
        compileBuilder.redirectErrorStream(true);
        try {
            Process compileProcess = compileBuilder.start();
            String output = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            int exitCode = compileProcess.waitFor();
            if (exitCode != 0) {
                throw new GradeApplicationException(ErrorCode.COMPILE_EXCEPTION,
                        String.format("%s", output.split("error: ")[1])
                );
            }
        } catch (IOException e) {
            throw new IOException("파일을 읽거나 쓰는 중 문제가 발생했습니다: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 다시 설정
            throw new InterruptedException("컴파일 과정이 중단되었습니다: " + e.getMessage());
        }
    }

    private String run(String dirPath, String className, String inputFileName) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        if (!new File(dirPath + className + ".class").exists())
            throw new FileNotFoundException("Main.class 파일이 생성되지 않았습니다.");

        ProcessBuilder runBuilder = new ProcessBuilder(JAVA_RUNNER, "-cp", dirPath, className);
        runBuilder.redirectErrorStream(true);  // 표준 에러를 표준 출력에 합치기 위함
        runBuilder.redirectInput(ProcessBuilder.Redirect.from(new File(inputFileName)));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = null;
        Process runProcess = runBuilder.start();
        try {
            future = executor.submit(() -> {

                // 실행 결과 읽기
                String actualOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                int exitCode = runProcess.waitFor();
                if (exitCode != 0) {
                    throw new GradeApplicationException(ErrorCode.CODE_ERROR, actualOutput);
                }
                return actualOutput;
            });
            // 최대 10초 동안 기다리고, 시간 초과시 TimeoutException 발생
            return future.get(10, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            future.cancel(true);  // 작업을 취소하고 프로세스를 종료하기
            throw new TimeoutException("실행 시간 초과");
        } catch (InterruptedException e) {
            throw e;
        } finally {
            executor.shutdownNow();  // 자원 회수
        }
    }

    @Override
    public String getActualOutput(String output) {
        String[] outputToArray = output.split("\n");
        String[] result = Arrays.copyOfRange(outputToArray, 0, outputToArray.length - 2);
        return String.join("\n", result);
    }

    @Override
    public double getUsedTime(String output) {
        String[] outputToArray = output.split("\n");
        return Double.parseDouble(outputToArray[outputToArray.length - 2]) / 1000000;
    }

    @Override
    public double getUsedMemory(String output) {
        String[] outputToArray = output.split("\n");
        return Double.parseDouble(outputToArray[outputToArray.length - 1]) / 1024;
    }

    private String addMeasureCode(String code, int firstBrace, int lastBrace) {
        String startCode = "\n" +
                "Runtime.getRuntime().gc();" +
                "long startTime = System.nanoTime();\n";
        String endCode = "\n" +
                "long endTime = System.nanoTime();\n" +
                "long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();\n" +
                "System.out.println(endTime - startTime);\n" +
                "System.out.println(usedMemory);\n";
        StringBuilder builder = new StringBuilder(code);
        builder.insert(lastBrace - 1, endCode);
        builder.insert(firstBrace + 1, startCode);
        return builder.toString();
    }

    private int findFirstBrace(String code) {
        int mainMethodIdx = code.indexOf("public static void main");
        return code.indexOf("{", mainMethodIdx);
    }

    private int findLastBrace(String code, int firstBrace) {
        if (firstBrace == -1)
            return -1;
        int braceCnt = 1;
        int idx = firstBrace + 1; // 첫번째 중괄 호 다음부터
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;

        while (idx < code.length() && braceCnt > 0) {
            char ch = code.charAt(idx);
            char nextChar = idx + 1 < code.length() ? code.charAt(idx + 1) : '\0';

            if (!inMultiLineComment && !inSingleLineComment && ch == '/' && nextChar == '/') {  // 단일 주석인 경우
                inSingleLineComment = true;
                idx++;
            } else if (!inMultiLineComment && !inSingleLineComment && ch == '/' && nextChar == '*') { // 여러줄 주석인 경우
                inMultiLineComment = true;
                idx++;
            } else if (inSingleLineComment && ch == '\n') {
                inSingleLineComment = false;
            } else if (inMultiLineComment && ch == '*' && nextChar == '/') {
                inMultiLineComment = false;
                idx++;
            } else if (!inSingleLineComment && !inMultiLineComment) {
                if (ch == '{') braceCnt++;
                else if (ch == '}') braceCnt--;
            }
            idx++;
        }
        return braceCnt == 0 ? idx : -1;
    }

}

