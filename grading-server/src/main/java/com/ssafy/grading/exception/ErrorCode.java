package com.ssafy.grading.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    COMPILE_EXCEPTION(HttpStatus.BAD_REQUEST, "Not Complete Compile"),
    CODE_ERROR(HttpStatus.BAD_REQUEST, "Occured Runtime Exception"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private HttpStatus status;
    private String message;
}
