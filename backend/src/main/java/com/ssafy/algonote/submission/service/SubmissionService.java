package com.ssafy.algonote.submission.service;

import com.ssafy.algonote.common.AdminMemberProvider;
import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.notification.domain.NotificationType;
import com.ssafy.algonote.notification.dto.request.NotificationReqDto;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.repository.ProblemRepository;
import com.ssafy.algonote.problem.service.SolvedProblemService;
import com.ssafy.algonote.submission.domain.Submission;
import com.ssafy.algonote.submission.dto.SubmissionDto;
import com.ssafy.algonote.submission.dto.request.SubmissionReqDto;
import com.ssafy.algonote.submission.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ApplicationEventPublisher eventPublisher;

    private final SolvedProblemService solvedProblemService;
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    // 제출 이력 저장
    @Transactional
    public void saveSubmission(SubmissionReqDto dto, Long memberId) {
        if (!submissionRepository.findById(dto.submissionId()).isPresent()) {
            Member member = getMemberOrException(memberId);
            Problem problem = getProblemOrException(dto.problemId());
            if (dto.result().equals("맞았습니다!!")) {
                solvedProblemService.saveSolvedProblem(member, problem, dto.submissionTime());

                eventPublisher.publishEvent(NotificationReqDto.builder()
                        .notificationType(NotificationType.SUBMISSION)
                        .receiver(member)
                        .provider(AdminMemberProvider.getAdminMember())
                        .relatedId(null)
                        .content(problem.getTitle() + " 문제를 푸셨군요! 노트를 작성해보세요.")
                        .build()
                    );
            }
            submissionRepository.save(Submission.fromDto(
                            SubmissionDto.fromReq(dto, problem, member)
                    )
            );
        }

    }

    // 제출 이력 조회(문제, 멤버로)
    public List<SubmissionDto> searchSubmissions(Long memberId, Long problemId) {
        Member member = getMemberOrException(memberId);
        Problem problem = getProblemOrException(problemId);

        return submissionRepository.findAllByMemberAndProblem(member, problem).stream()
                .map(SubmissionDto::fromEntity)
                .sorted(Comparator.comparing(SubmissionDto::submissionId).reversed())
                .collect(Collectors.toList());
    }

    // 제출 이력 단건 조회
    public SubmissionDto searchSubmissionById(Long submissionId) {
        return SubmissionDto.fromEntity(getSubmissionOrException(submissionId));
    }

    private Submission getSubmissionOrException(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SUBMISSION));
    }

    private Member getMemberOrException(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private Problem getProblemOrException(Long problemId) {
        return problemRepository.findById(problemId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PROBLEM));
    }

}
