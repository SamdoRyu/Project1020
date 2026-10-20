package com.team3.petwalk.view.input;

import com.team3.petwalk.common.InputUtil;
import com.team3.petwalk.common.OrderConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

// 입력은 공통 InputUtil을 쓴다. Scanner는 InputUtil의 하나만 공유 (NFR-06, #8)
public class WalkOrderInputView {

    private static final String[] STATUSES = {
            OrderConstants.STATUS_WAITING, OrderConstants.STATUS_IN_PROGRESS,
            OrderConstants.STATUS_COMPLETED, OrderConstants.STATUS_CANCELED};

    private static final BigDecimal MAX_DISTANCE_KM = new BigDecimal("999.99");

    // STRICT: 2026-02-30처럼 없는 날짜를 거부한다 (STRICT에서는 연도를 uuuu로 써야 함)
    private static final DateTimeFormatter WALK_AT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        return InputUtil.readLine("선택> ");
    }

    // F-14 조회 방식 (1.전체 2.상태별 3.보호자별 4.시터별)
    public int readListType() {
        return readNumberInRange("조회 방식> ", 1, 4);
    }

    // F-14 상태별: 번호를 받아 상태 문자열로 돌려준다
    public String readStatus() {
        return STATUSES[readNumberInRange("상태 번호> ", 1, STATUSES.length) - 1];
    }

    // F-14 보호자별 (NFR-09: 회원번호로 표기)
    public int readGuardianMemberId() {
        return readPositiveNumber("보호자 회원번호> ");
    }

    // F-14 시터별 (NFR-09: 시터번호로 표기)
    public int readSitterId() {
        return readPositiveNumber("시터번호> ");
    }

    // F-13 (NFR-09: 동물번호로 표기)
    public int readPetId() {
        return readPositiveNumber("동물번호> ");
    }

    // F-13 산책일시. 없는 날짜(2월 30일 등)도 형식 오류로 본다
    public LocalDateTime readWalkAt() {
        while (true) {
            System.out.print("산책일시(yyyy-MM-dd HH:mm)> ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(input, WALK_AT_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("yyyy-MM-dd HH:mm 형식으로 입력해 주세요. (예: 2026-10-25 14:30)");
            }
        }
    }

    // F-13 요금방식: 번호를 받아 '거리' / '시간'으로 돌려준다
    // TODO C의 상수 클래스가 나오면 요금방식 문자열을 상수로 교체
    public String readFeeType() {
        return readNumberInRange("요금방식> ", 1, 2) == 1 ? "거리" : "시간";
    }

    // F-15·F-16 (NFR-09: 주문번호로 표기)
    public int readOrderId() {
        return readPositiveNumber("주문번호> ");
    }

    // F-16 취소 주체: 번호를 받아 '보호자' / '시터'로 돌려준다
    public String readCanceledBy() {
        return readNumberInRange("취소 주체> ", 1, 2) == 1
                ? OrderConstants.CANCELED_BY_OWNER : OrderConstants.CANCELED_BY_SITTER;
    }

    // 확인(Y/N). Y면 true
    // TODO NFR-06: InputUtil에 Y/N 입력이 생기면 교체
    public boolean readConfirm(String prompt) {
        while (true) {
            String input = InputUtil.readLine(prompt + " (Y/N)> ").toUpperCase();
            if (input.equals("Y") || input.equals("N")) {
                return input.equals("Y");
            }
            System.out.println("Y 또는 N을 입력해 주세요.");
        }
    }

    // F-15 처리 선택 (1.시작 2.완료)
    public int readWalkAction() {
        return readNumberInRange("처리> ", 1, 2);
    }

    // F-15 실측거리(km): 0보다 크고, 소수 둘째 자리까지, 최대 999.99 (DECIMAL(5,2))
    // InputUtil에는 최댓값 검사가 없어서 999.99 초과만 여기서 다시 묻는다 (넘으면 DB Out of range 오류)
    public BigDecimal readDistanceKm() {
        while (true) {
            BigDecimal distance = InputUtil.readPositiveDecimal("실측거리(km)> ", 2);
            if (distance.compareTo(MAX_DISTANCE_KM) <= 0) {
                return distance;
            }
            System.out.println("999.99 이하로 입력해 주세요.");
        }
    }

    // F-15 실측시간(분)
    public int readDurationMin() {
        return readPositiveNumber("실측시간(분)> ");
    }

    // 형식 오류(숫자 아님, 범위 밖)는 안내 후 그 항목만 다시 입력받는다 (NFR-06)
    private int readNumberInRange(String prompt, int min, int max) {
        return InputUtil.readInt(prompt, min, max);
    }

    private int readPositiveNumber(String prompt) {
        return InputUtil.readId(prompt);
    }
}
