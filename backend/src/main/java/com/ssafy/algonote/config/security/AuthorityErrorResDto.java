package com.ssafy.algonote.config.security;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthorityErrorResDto {

    int httpStatus;
    String message;
    LocalDateTime dateTime;
}
