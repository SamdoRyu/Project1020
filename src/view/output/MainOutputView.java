package view.output;

public class MainOutputView {

    public void printMenu() {
        System.out.println("""

                [메인 메뉴]
                1. 회원 관리
                2. 반려동물 관리
                3. 시터 관리
                4. 주문·결제 관리
                5. 결제 통계
                0. 종료
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }

    public void printExit() {
        System.out.println("프로그램을 종료합니다.");
    }
}
