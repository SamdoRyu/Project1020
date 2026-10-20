package com.team3.petwalk.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

// (B) 공통 입력. 형식 오류는 안내 후 그 항목만 다시 입력받는다 (NFR-06)
// System.in을 읽는 Scanner는 프로그램 전체에서 이것 하나만 쓴다
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    // yyyy-MM-dd HH:mm. STRICT라서 2월 30일 같은 없는 날짜도 거부한다 (uuuu는 STRICT에서 연도 표기)
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final String PHONE_PATTERN = "^010-\\d{4}-\\d{4}$";

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

    // Y/N (대소문자 무관). Y면 true
    public static boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line.equalsIgnoreCase("Y")) {
                return true;
            }
            if (line.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Y 또는 N을 입력해 주세요.");
        }
    }

    // 날짜 yyyy-MM-dd HH:mm
    public static LocalDateTime readDateTime(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return LocalDateTime.parse(line, DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("날짜를 yyyy-MM-dd HH:mm 형식으로 입력해 주세요. (예: 2026-10-21 14:30)");
            }
        }
    }

    // 연락처 010-0000-0000
    public static String readPhone(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line.matches(PHONE_PATTERN)) {
                return line;
            }
            System.out.println("연락처를 010-0000-0000 형식으로 입력해 주세요.");
        }
    }

    // ---------------------------------------------------------
    // 수정 기능용 (F-03, F-07, F-11): 엔터만 누르면 기존 값을 그대로 돌려준다
    // 안내 문구 뒤에 현재 값을 [ ]로 붙여 보여 준다
    // ---------------------------------------------------------

    public static String readStringOrKeep(String prompt, String current) {
        String line = readLine(keepPrompt(prompt, current));
        return line.isEmpty() ? current : line;
    }

    public static int readIntOrKeep(String prompt, int min, int max, int current) {
        while (true) {
            String line = readLine(keepPrompt(prompt, current));
            if (line.isEmpty()) {
                return current;
            }
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

    public static BigDecimal readPositiveDecimalOrKeep(String prompt, int maxScale, BigDecimal current) {
        while (true) {
            String line = readLine(keepPrompt(prompt, current.toPlainString()));
            if (line.isEmpty()) {
                return current;
            }
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

    public static boolean readYesNoOrKeep(String prompt, boolean current) {
        while (true) {
            String line = readLine(keepPrompt(prompt, current ? "Y" : "N"));
            if (line.isEmpty()) {
                return current;
            }
            if (line.equalsIgnoreCase("Y")) {
                return true;
            }
            if (line.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Y 또는 N을 입력해 주세요.");
        }
    }

    public static String readPhoneOrKeep(String prompt, String current) {
        while (true) {
            String line = readLine(keepPrompt(prompt, current));
            if (line.isEmpty()) {
                return current;
            }
            if (line.matches(PHONE_PATTERN)) {
                return line;
            }
            System.out.println("연락처를 010-0000-0000 형식으로 입력해 주세요.");
        }
    }

    // "이름> " → "이름 [초코]> "
    private static String keepPrompt(String prompt, Object current) {
        String base = prompt.endsWith("> ") ? prompt.substring(0, prompt.length() - 2) : prompt;
        return base + " [" + current + "]> ";
    }
}
