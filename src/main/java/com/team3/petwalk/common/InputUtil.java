package com.team3.petwalk.common;

import java.math.BigDecimal;
import java.util.Scanner;

// (B) 공통 입력. 형식 오류는 안내 후 그 항목만 다시 입력받는다 (NFR-06)
// System.in을 읽는 Scanner는 프로그램 전체에서 이것 하나만 쓴다
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtil() {
    }

    // 검증 없이 한 줄 입력 (메뉴 번호 등)
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // 빈 값이 아닌 문자열
    public static String readString(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("값을 입력해 주세요.");
        }
    }

    // min~max 범위의 정수
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println(min + "~" + max + " 사이의 숫자를 입력해 주세요.");
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
            }
        }
    }

    // 0보다 큰 정수 (회원번호·동물번호 등)
    public static int readId(String prompt) {
        return readInt(prompt, 1, Integer.MAX_VALUE);
    }

    // 0보다 크고 소수 maxScale자리까지인 실수 (예: 무게 DECIMAL(4,1) → maxScale 1)
    public static BigDecimal readPositiveDecimal(String prompt, int maxScale) {
        while (true) {
            String line = readLine(prompt);
            BigDecimal value;
            try {
                value = new BigDecimal(line);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
                continue;
            }
            if (value.signum() <= 0) {
                System.out.println("0보다 큰 값을 입력해 주세요.");
            } else if (value.stripTrailingZeros().scale() > maxScale) {
                System.out.println("소수 " + maxScale + "자리까지만 입력해 주세요.");
            } else {
                return value;
            }
        }
    }
}
