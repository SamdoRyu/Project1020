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

    // F-01 등록 결과
    public void printRegistered(int memberId) {
        System.out.println("회원이 등록되었습니다. (회원번호: " + memberId + ")");
    }

    // 업무 규칙 위반 안내 (NFR-07)
    public void printMessage(String message) {
        System.out.println(message);
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
