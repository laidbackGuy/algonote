package com.ssafy.algonote.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),
    NOT_FOUND_NOTE(HttpStatus.NOT_FOUND, "해당 노트를 찾을 수 없습니다."),
    NOT_FOUND_ES_NOTE(HttpStatus.NOT_FOUND, "ES에서 노트 정보를 찾을 수 없습니다."),
    NOT_FOUND_TEMP_NOTE(HttpStatus.NOT_FOUND, "해당 임시 노트를 찾을 수 없습니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    INVALID_PATH(HttpStatus.NOT_FOUND, "유효하지 않은 경로입니다."),
    NO_AUTHORITY(HttpStatus.UNAUTHORIZED, "해당 요청에 대한 권한이 없습니다."),
    WRONG_PASSWORD(HttpStatus.NOT_ACCEPTABLE, "비밀번호가 일치하지 않습니다."),
    BLANK_TOKEN_HEADER(HttpStatus.UNAUTHORIZED, "헤더에 토큰이 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "알고리즘이 존재하지 않습니다."),
    MEMBER_INVALID(HttpStatus.UNAUTHORIZED, "토큰에 해당하는 유저가 없습니다"),
    NOT_FOUND_PROBLEM(HttpStatus.NOT_FOUND, "해당하는 문제를 찾을 수 없습니다."),
    DUPLICATE_PROBLEM(HttpStatus.CONFLICT, "이미 존재하는 문제입니다."),
    NOT_FOUND_TAG(HttpStatus.NOT_FOUND, "해당하는 태그 정보를 찾을 수 없습니다."),
    DUPLICATE_TAG(HttpStatus.CONFLICT, "이미 존재하는 태그입니다."),
    FAILED_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 없습니다"),
    INVALID_LINE_RANGE(HttpStatus.BAD_REQUEST, "endLine은 startLine보다 작을 수 없습니다."),
    JSON_GENERATING_ERROR(HttpStatus.BAD_REQUEST, "JSON 생성 중 에러가 발생하였습니다."),
    JSON_PARSING_ERROR(HttpStatus.BAD_REQUEST, "JSON 파싱 중 에러가 발생하였습니다."),
    NOT_FOUND_SUBMISSION(HttpStatus.NOT_FOUND, "존재하지 않는 제출정보입니다."),
    RUNTIME_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    NOTIFICATION_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 연결 중 에러가 발생하였습니다."),
    NOTIFICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 전송 중 에러가 발생하였습니다."),
    JSON_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FastAPI 서버 오류"),
    NOT_SOLVED(HttpStatus.FORBIDDEN, "아직 풀지 않은 문제입니다."),
    INVALID_QUERY_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청 URL입니다."),
    NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}