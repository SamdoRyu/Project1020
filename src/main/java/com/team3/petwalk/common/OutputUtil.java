package com.team3.petwalk.common;

// (B) 공통 출력 (NFR-08). 금액·날짜 포맷, 표 출력은 #9에서 추가
public class OutputUtil {
    public static final String DB_ERROR = "데이터 처리 중 오류가 발생했습니다.";

    private OutputUtil() {
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
