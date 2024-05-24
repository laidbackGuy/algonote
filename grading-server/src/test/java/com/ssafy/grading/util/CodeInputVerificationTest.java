package com.ssafy.grading.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeInputVerificationTest {

    @ParameterizedTest
    @MethodSource("provideStringsForNormalization")
    void test(String input, String expected) {
        assertEquals(expected, normalizeNewlines(input));
    }

    private static String normalizeNewlines(String input) {
        return input.replaceAll("[ \t]*\n[ \t]*", "\n").replaceAll("\n+", "\n");
    }

    static Stream<Arguments> provideStringsForNormalization() {
        return Stream.of(
                Arguments.of("Hello \n     World \n this is \n a test \n    ", "Hello\nWorld\nthis is\na test\n"),
                Arguments.of("Hello\t\n\t\tWorld\t\n\tthis is\t\n\ta test\t\n\t\t", "Hello\nWorld\nthis is\na test\n"),
                Arguments.of("Hello \t\n \t   World \t \n \tthis is \t \n \ta test \t \n \t   ", "Hello\nWorld\nthis is\na test\n"),
                Arguments.of("\n     World \n this is \n a test \n    ", "\nWorld\nthis is\na test\n"),
                Arguments.of("Hello\nWorld\nthis is\na test\n", "Hello\nWorld\nthis is\na test\n")
        );
    }

}
