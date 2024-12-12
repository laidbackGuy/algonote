package com.ssafy.algonote.config.security;

import com.ssafy.algonote.config.user.CustomUserDetails;
import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class SecurityUtil {
    public static Long getMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new CustomException(ErrorCode.MEMBER_INVALID);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getMemberInfoDto().getUserId();
    }


}
