package com.ssafy.grading.util;

public class CodeInputVerification {

    // 띄어쓰기 체크
    public static String normalizeNewlines(String input) {
        // 정규 표현식으로 개행 주변의 공백(스페이스, 탭 포함)을 찾아서 개행으로만 대체
        return input.replaceAll("[ \t]*\n[ \t]*", "\n").replaceAll("\n+", "\n");
    }
}
