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
                System.out.println("연락처 형식이 올바르지 않습니다. 010-0000-0000 형식으로 입력해 주세요.");
            } else {
                return phone;
            }
        }
    }
}
