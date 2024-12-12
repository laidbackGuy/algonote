package com.ssafy.grading.config;

import com.ssafy.grading.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    @Primary
    public Map<String, LanguageExecutorService> executorServiceMap(
            JavaExecutorService javaExecutorService,
            PythonExecutorService pythonExecutorService,
            CExecutorService cExecutorService,
            CppExecutorService cppExecutorService) {
        Map<String, LanguageExecutorService> map = new HashMap<>();
        map.put("java", javaExecutorService);
        map.put("py", pythonExecutorService);
        map.put("c", cExecutorService);
        map.put("cpp", cppExecutorService);
        return map;
    }
}
