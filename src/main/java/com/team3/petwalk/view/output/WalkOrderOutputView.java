package com.team3.petwalk.view.output;

public class WalkOrderOutputView {

    public void printMenu() {
        System.out.println("""

                [4. 주문·결제 관리]
                1. 주문 등록
                2. 주문 목록
                3. 산책 시작·완료
                4. 주문 취소
                0. 뒤로
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }
}
