package com.ssafy.algonote.problem.consumer;

import com.ssafy.algonote.problem.dto.ConsumerProblemResDto;
import com.ssafy.algonote.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class ProblemConsumer {
    private final ProblemService problemService;

    @KafkaListener(topics = "${spring.kafka.topic.problem}")
    public void consumeBoj(ConsumerProblemResDto dto, Acknowledgment ack) {
        log.info("Consume the event {}", dto);
        problemService.saveProblem(dto);
        ack.acknowledge();
    }
}
