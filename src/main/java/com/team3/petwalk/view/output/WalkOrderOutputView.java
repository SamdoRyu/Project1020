package com.team3.petwalk.view.output;

import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WalkOrderOutputView {
    // TODO NFR-08: B의 OutputUtil이 나오면 금액·날짜 포맷과 표 정렬을 공통 출력으로 교체
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    // NFR-10
    public void printDbError() {
        System.out.println("데이터 처리 중 오류가 발생했습니다.");
    }

    // NFR-07: 어떤 조건에 걸렸는지 안내
    public void printError(String message) {
        System.out.println(message);
    }

    // F-14 조회 방식 선택
    public void printListMenu() {
        System.out.println("1) 전체  2) 상태별  3) 보호자별(회원번호)  4) 시터별(시터번호)");
    }

    // F-14 상태 선택
    public void printStatusMenu() {
        System.out.println("1) 대기  2) 수행중  3) 완료  4) 취소");
    }

    // F-14 주문 목록 표 + 완료 주문 합계
    public void printOrderList(List<WalkOrderDTO> orderList, int completedTotal) {
        if (orderList.isEmpty()) {
            System.out.println("조회 결과가 없습니다.");
            return;
        }
        System.out.println(pad("주문번호", 8) + pad("산책일시", 18) + pad("반려동물", 10) + pad("보호자", 8)
                + pad("시터", 8) + pad("요금방식", 9) + pad("상태", 7) + "금액");
        for (WalkOrderDTO order : orderList) {
            String amount = order.getAmount() == null ? "-" : formatWon(order.getAmount());
            System.out.println(pad(String.valueOf(order.getOrderId()), 8)
                    + pad(order.getWalkAt().format(DATE_TIME), 18)
                    + pad(order.getPetName(), 10)
                    + pad(order.getGuardianName(), 8)
                    + pad(order.getSitterName(), 8)
                    + pad(order.getFeeType(), 9)
                    + pad(order.getStatus(), 7)
                    + amount);
        }
        System.out.println("완료 주문 합계: " + formatWon(completedTotal));
    }

    // 금액: 천 단위 콤마 + 원 (NFR-08)
    private String formatWon(int amount) {
        return String.format("%,d원", amount);
    }

    // 한글은 콘솔에서 두 칸을 차지하므로 화면 너비 기준으로 공백을 채운다
    private String pad(String text, int width) {
        int displayWidth = 0;
        for (char c : text.toCharArray()) {
            displayWidth += (c >= '가' && c <= '힣') ? 2 : 1;
        }
        return text + " ".repeat(Math.max(1, width - displayWidth));
    }
}
