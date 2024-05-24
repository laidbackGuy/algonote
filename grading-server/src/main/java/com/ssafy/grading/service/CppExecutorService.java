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
public class CppExecutorService implements LanguageExecutorService {

    private static final String C_COMPILER = "g++";
    private static final String FILE_EXTENSION = ".out";
    private static final String C_EXTENSION = ".c";

    @Override
    public ExecutionResult execute(String code, String input, String expected) {
        String dirPath = UUID.randomUUID() + "/";
        String fileName = "main";
        String cFile = dirPath + fileName;
        String inputFileName = dirPath + fileName + "Input.txt";

        try {
            File directory = new File(dirPath);
            if (!directory.exists())
                directory.mkdir();

            Files.write(Paths.get(cFile + C_EXTENSION), code.getBytes());
            Files.write(Paths.get(inputFileName), input.getBytes());
            compile(cFile); // .java 컴파일
            String output = run(dirPath, fileName, inputFileName);
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

    private void compile(String cFile) throws IOException, InterruptedException {
        // Java 파일 컴파일하기
        log.info(cFile);
        ProcessBuilder compileBuilder = new ProcessBuilder(C_COMPILER, cFile + C_EXTENSION, "-o", cFile + FILE_EXTENSION);
        log.info("{}", compileBuilder.command());
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

    private String run(String dirPath, String fileName, String inputFileName) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        if (!new File(dirPath + fileName + FILE_EXTENSION).exists()) {
            throw new FileNotFoundException(fileName + FILE_EXTENSION + "파일이 생성되지 않았습니다.");
        }
        ProcessBuilder runBuilder = new ProcessBuilder("/usr/bin/time", "-v", dirPath + fileName + FILE_EXTENSION);
        runBuilder.redirectErrorStream(true);
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
        String[] result = Arrays.copyOfRange(outputToArray, 0, outputToArray.length - 23);
        return String.join("\n", result);
    }

    @Override
    public double getUsedTime(String output) {
        String[] outputToArray = output.split("\n");
        String target = outputToArray[outputToArray.length - 19];
        target.replace("Elapsed (wall clock) time (h:mm:ss or m:ss): ", "");
        double milliseconds = 0;
        String[] parts = target.split(":");

        if (parts.length == 3) { // h:mm:ss format
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            double seconds = Double.parseDouble(parts[2]);
            milliseconds = ((hours * 3600 + minutes * 60 + seconds) * 1000);
        } else if (parts.length == 2) { // m:ss format
            int minutes = Integer.parseInt(parts[0]);
            double seconds = Double.parseDouble(parts[1]);
            milliseconds = ((minutes * 60 + seconds) * 1000);
        }
        return milliseconds;
    }

    @Override
    public double getUsedMemory(String output) {
        String[] outputToArray = output.split("\n");
        String target = outputToArray[outputToArray.length - 14];
        target.replace("Maximum resident set size (kbytes)", "");
        String[] parts = target.split(":");
        return Double.parseDouble(parts[1]);
    }

}
