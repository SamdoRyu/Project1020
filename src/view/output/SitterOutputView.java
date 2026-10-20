package view.output;

public class SitterOutputView {

    public void printMenu() {
        System.out.println("""

                [3. 시터 관리]
                1. 등록
                2. 목록
                3. 수정·제재 해제
                4. 삭제
                0. 뒤로
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }
}
