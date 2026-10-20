package com.team3.petwalk.view.output;

import com.team3.petwalk.common.OutputUtil;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 표 출력·공통 문구는 OutputUtil을 쓴다 (NFR-08)
public class WalkOrderOutputView {
    // TODO NFR-08: OutputUtil에 금액·날짜 포맷이 생기면 formatWon·DATE_TIME을 교체
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // NFR-09: 주문번호를 열 이름에 표기
    private static final String[] ORDER_HEADERS =
            {"주문번호", "산책일시", "반려동물", "보호자", "시터", "요금방식", "상태", "금액"};

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
        OutputUtil.printMessage(OutputUtil.DB_ERROR);
    }

    // NFR-07: 어떤 조건에 걸렸는지 안내
    public void printError(String message) {
        System.out.println(message);
    }

    // F-13 요금방식 선택
    public void printFeeTypeMenu() {
        System.out.println("1) 거리  2) 시간");
    }

    public void printRegistered(int orderId) {
        System.out.println("주문이 접수되었습니다. (주문번호: " + orderId + ")");
    }

    // F-15 처리 선택
    public void printWalkActionMenu() {
        System.out.println("1) 시작  2) 완료");
    }

    public void printStarted() {
        System.out.println("산책을 시작합니다.");
    }

    public void printCompleted(int amount) {
        System.out.println("산책 완료. 결제 금액: " + formatWon(amount));
    }

    // F-16 취소 주체 선택
    public void printCanceledByMenu() {
        System.out.println("1) 보호자  2) 시터");
    }

    public void printCanceled() {
        System.out.println("취소되었습니다.");
    }

    public void printCancelStopped() {
        System.out.println("취소하지 않았습니다.");
    }

    // F-16 시터 측 취소 시 제재 안내 (BR-06)
    public void printSitterSuspended(String sitterName, int sitterId, LocalDateTime suspendedUntil) {
        System.out.println("시터 " + sitterName + "(시터번호 " + sitterId + ")은 "
                + suspendedUntil.format(DATE_TIME) + "까지 새 주문을 받을 수 없습니다.");
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
            OutputUtil.printMessage(OutputUtil.NO_RESULT);
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (WalkOrderDTO order : orderList) {
            rows.add(new String[]{
                    String.valueOf(order.getOrderId()),
                    order.getWalkAt().format(DATE_TIME),
                    order.getPetName(),
                    order.getGuardianName(),
                    order.getSitterName(),
                    order.getFeeType(),
                    order.getStatus(),
                    order.getAmount() == null ? "-" : formatWon(order.getAmount())});
        }
        OutputUtil.printTable(ORDER_HEADERS, rows);
        System.out.println("완료 주문 합계: " + formatWon(completedTotal));
    }

    // 금액: 천 단위 콤마 + 원 (NFR-08)
    private String formatWon(int amount) {
        return String.format("%,d원", amount);
    }
}
