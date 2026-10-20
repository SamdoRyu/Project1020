package com.team3.petwalk.view.input;

import java.util.Scanner;

public class WalkOrderInputView {
    // TODO NFR-06: B의 InputUtil이 나오면 공통 입력으로 교체
    private final Scanner scanner = new Scanner(System.in);

    // TODO C의 상수 클래스가 나오면 상태 문자열을 상수로 교체
    private static final String[] STATUSES = {"대기", "수행중", "완료", "취소"};

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        System.out.print("선택> ");
        return scanner.nextLine().trim();
    }

    // F-14 조회 방식 (1.전체 2.상태별 3.보호자별 4.시터별)
    public int readListType() {
        return readNumberInRange("조회 방식> ", 1, 4);
    }

    // F-14 상태별: 번호를 받아 상태 문자열로 돌려준다
    public String readStatus() {
        return STATUSES[readNumberInRange("상태 번호> ", 1, STATUSES.length) - 1];
    }

    // F-14 보호자별 (NFR-09: 회원번호로 표기)
    public int readGuardianMemberId() {
        return readPositiveNumber("보호자 회원번호> ");
    }

    // F-14 시터별 (NFR-09: 시터번호로 표기)
    public int readSitterId() {
        return readPositiveNumber("시터번호> ");
    }

    // 형식 오류(숫자 아님, 범위 밖)는 안내 후 그 항목만 다시 입력받는다 (NFR-06)
    private int readNumberInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // 아래 안내 후 다시 입력
            }
            System.out.println(min + "~" + max + " 사이의 숫자를 입력해 주세요.");
        }
    }

    private int readPositiveNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // 아래 안내 후 다시 입력
            }
            System.out.println("1 이상의 숫자를 입력해 주세요.");
        }
    }
}
