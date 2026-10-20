package com.team3.petwalk.view.input;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MemberInputView {
    // TODO NFR-06: B의 InputUtil이 나오면 공통 입력으로 교체
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern PHONE = Pattern.compile("^010-[0-9]{4}-[0-9]{4}$");
    private static final int NAME_MAX = 20;

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        System.out.print("선택> ");
        return scanner.nextLine().trim();
    }

    // F-03 회원번호 입력. 숫자가 아니거나 1 미만이면 안내 후 다시 입력 (NFR-06, NFR-09)
    // TODO NFR-06: InputUtil로 교체
    public int readMemberId() {
        while (true) {
            System.out.print("회원번호> ");
            String text = scanner.nextLine().trim();
            try {
                int memberId = Integer.parseInt(text);
                if (memberId >= 1) {
                    return memberId;
                }
            } catch (NumberFormatException e) {
                // 아래 안내 출력
            }
            System.out.println("회원번호는 1 이상의 숫자로 입력해 주세요.");
        }
    }

    // F-03 이름 입력. 엔터만 누르면 기존 값 유지
    // TODO NFR-06: InputUtil로 교체
    public String readNameOrKeep(String current) {
        while (true) {
            System.out.print("새 이름(엔터: 유지)> ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                return current;
            }
            if (name.length() <= NAME_MAX) {
                return name;
            }
            System.out.println("이름은 " + NAME_MAX + "자 이하로 입력해 주세요.");
        }
    }

    // F-03 연락처 입력. 엔터만 누르면 기존 값 유지, 형식 오류는 다시 입력
    // TODO NFR-06: InputUtil로 교체
    public String readPhoneOrKeep(String current) {
        while (true) {
            System.out.print("새 연락처(010-0000-0000, 엔터: 유지)> ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                return current;
            }
            if (PHONE.matcher(phone).matches()) {
                return phone;
            }
            System.out.println("연락처 형식이 올바르지 않습니다. 하이픈 추가해서 010-0000-0000 형식으로 입력해 주세요.");
        }
    }

    // F-02 검색어 입력. 엔터만 누르면 빈 문자열(전체)
    public String readSearchKeyword() {
        System.out.print("이름 검색어(엔터: 전체)> ");
        return scanner.nextLine().trim();
    }

    // F-01 이름 입력. 빈 값·길이 초과는 안내 후 다시 입력 (NFR-06)
    // TODO NFR-06: InputUtil로 교체
    public String readName() {
        while (true) {
            System.out.print("이름> ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("이름을 입력해 주세요.");
            } else if (name.length() > NAME_MAX) {
                System.out.println("이름은 " + NAME_MAX + "자 이하로 입력해 주세요.");
            } else {
                return name;
            }
        }
    }

    // F-01 연락처 입력. 빈 값·형식 오류는 안내 후 다시 입력 (NFR-06)
    // TODO NFR-06: InputUtil로 교체
    public String readPhone() {
        while (true) {
            System.out.print("연락처(010-0000-0000)> ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("연락처를 입력해 주세요.");
            } else if (!PHONE.matcher(phone).matches()) {
                System.out.println("연락처 형식이 올바르지 않습니다. 하이픈 추가해서 010-0000-0000 형식으로 입력해 주세요.");
            } else {
                return phone;
            }
        }
    }
}
