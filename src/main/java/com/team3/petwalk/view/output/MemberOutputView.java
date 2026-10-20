package com.team3.petwalk.view.output;

public class MemberOutputView {

    public void printMenu() {
        System.out.println("""

                [1. 회원 관리]
                1. 등록
                2. 목록·검색
                3. 수정
                4. 삭제
                0. 뒤로
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }

    public void printStatisticsMenu() {
        System.out.println("""

                [5. 결제 통계]
                1. 시터별 수입
                2. 보호자별 지출
                0. 뒤로
                """);
    }
}
