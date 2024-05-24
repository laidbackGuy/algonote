package com.ssafy.grading.service;

import org.junit.jupiter.api.Test;

class CodeExecutorServiceTest {


    @Test
    void test() {
        String json = jsonCode1();
        int mainMethodIndex = json.indexOf("public static void main");
        int methodStartIndex = json.indexOf("{", mainMethodIndex) + 1;

        String memoryStartCode = "long startTime = System.nanoTime(); long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();";
        String memoryEndCode = "long endTime = System.nanoTime(); long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); System.out.println(\"Execution Time: \" + (endTime - startTime) + \" ns\"); System.out.println(\"Memory Used: \" + (endMemory - startMemory) + \" bytes\");";

        // 메모리 측정 코드를 main 메서드에 삽입
        StringBuilder builder = new StringBuilder(json);
        builder.insert(methodStartIndex, memoryStartCode);

        // 메인 메서드 종료 직전에 메모리 측정 종료 코드 삽입
        int methodEndIndex = json.lastIndexOf("}");
        builder.insert(methodEndIndex, memoryEndCode);
        System.out.println(builder);

    }

    @Test
    void findFirstBrace() {
        String case1 = jsonCode1();

        //int mainMethodIdx = case1.indexOf("public static void main");
        int firstBrace = findFirstBrace(case1);
        int lastBrace = findLastBrace(case1, firstBrace);
        String code = addMeasureCode(case1, firstBrace, lastBrace);
        System.out.println(code);
    }

    private int findFirstBrace(String code) {
        int mainMethodIdx = code.indexOf("public static void main");
        return code.indexOf("{", mainMethodIdx);
    }

    private int findLastBrace(String code, int firstBrace) {
        if (firstBrace == -1)
            return -1;

        int braceCnt = 1;
        int idx = firstBrace + 1; // 첫번째 중괄 호 다음부터
        while (idx < code.length() && braceCnt > 0) {
            char ch = code.charAt(idx++);
            if (ch == '{')
                braceCnt++;
            else if (ch == '}')
                braceCnt--;
        }

        return braceCnt == 0 ? idx : -1;
    }

    private String addMeasureCode(String code, int firstBrace, int lastBrace) {
        String startCode = "\n" +
                "long startTime = System.nanoTime();\n" +
                "long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();\n";
        String endCode = "\n" +
                "long endTime = System.nanoTime();" +
                "long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();\n" +
                "System.out.println(\"Execution Time: \" + (endTime - startTime) + \" ns\");\n" +
                "System.out.println(\"Memory Used: \" + (endMemory - startMemory) + \" bytes\");\n";
        StringBuilder builder = new StringBuilder(code);
        builder.insert(lastBrace - 1, endCode);
        builder.insert(firstBrace + 1, startCode);
        return builder.toString();
    }

    private String jsonCode1() {
        return "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int size = 10000000; // 큰 배열 크기\n" +
                "        int[] bigArray = new int[size];\n" +
                "        long sum = 0;\n\n" +
                "        // 배열에 데이터를 초기화\n" +
                "        for (int i = 0; i < size; i++) {\n" +
                "            bigArray[i] = i;\n" +
                "        }\n\n" +
                "        // 배열의 모든 요소의 합을 계산\n" +
                "        for (int value : bigArray) {\n" +
                "            sum += value;\n" +
                "        }\n\n" +
                "        System.out.println(\"Sum: \" + sum);\n" +
                "    }\n" +
                "}";
    }

    private String jsonCode2() {
        return "public class Main {\n\n" +
                "    public static void main(String[] args) {\n" +
                "        for(int i = 0; i < 10; i++) {\n" +
                "            System.out.println(i + \"Hello world\");\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    private String jsonCode3() {
        return "public class Solution\n" +
                "{\n" +
                "    public static void main(String[] args)\n" +
                "    {\n" +
                "        for(int i = 0; i < 10; i++)\n" +
                "        {\n" +
                "            System.out.println(i + \"Hello world\");\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}