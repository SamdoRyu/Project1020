package com.team3.petwalk.view.input;

import java.util.Scanner;

public class WalkOrderInputView {
    // TODO NFR-06: B의 InputUtil이 나오면 공통 입력으로 교체
    private final Scanner scanner = new Scanner(System.in);

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        System.out.print("선택> ");
        return scanner.nextLine().trim();
    }
}
