package com.team3.petwalk.view.output;

import com.team3.petwalk.common.OutputUtil;
import com.team3.petwalk.model.dto.SitterDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 표 출력은 OutputUtil, 금액 15,000원·날짜 yyyy-MM-dd HH:mm 형식은 여기서 맞춘다 (NFR-08)
public class SitterOutputView {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // 회원번호와 시터번호를 열 이름에서 구분한다 (NFR-09)
    private static final String[] HEADERS =
            {"시터번호", "회원번호", "이름", "km당 단가", "분당 단가", "자격증", "수용량", "제재종료일"};

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
        OutputUtil.printMessage("메뉴 번호를 다시 입력해 주세요.");
    }

    // F-10 목록. 결과가 없으면 "조회 결과가 없습니다." (NFR-08)
    public void printSitterList(List<SitterDTO> sitters) {
        if (sitters.isEmpty()) {
            OutputUtil.printMessage(OutputUtil.NO_RESULT);
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (SitterDTO sitter : sitters) {
            rows.add(toRow(sitter));
        }
        OutputUtil.printTable(HEADERS, rows);
    }

    // F-11 수정 전 현재 값
    public void printSitter(SitterDTO sitter) {
        OutputUtil.printTable(HEADERS, List.<String[]>of(toRow(sitter)));
    }

    public void printRegistered(int sitterId) {
        OutputUtil.printMessage("시터가 등록되었습니다. (시터번호: " + sitterId + ")");
    }

    public void printUpdated() {
        OutputUtil.printMessage("수정되었습니다.");
    }

    public void printReleased() {
        OutputUtil.printMessage("제재가 해제되었습니다.");
    }

    public void printDeleted() {
        OutputUtil.printMessage("삭제되었습니다.");
    }

    public void printDeleteCanceled() {
        OutputUtil.printMessage("삭제를 취소했습니다.");
    }

    // 업무 규칙 위반 안내 (NFR-07)
    public void printError(String message) {
        OutputUtil.printMessage(message);
    }

    // NFR-10
    public void printDbError() {
        OutputUtil.printMessage(OutputUtil.DB_ERROR);
    }

    private String[] toRow(SitterDTO sitter) {
        return new String[]{
                String.valueOf(sitter.getSitterId()),
                String.valueOf(sitter.getMemberId()),
                sitter.getMemberName(),
                formatMoney(sitter.getPricePerKm()),
                formatMoney(sitter.getPricePerMin()),
                sitter.isCertified() ? "있음" : "없음",
                String.valueOf(sitter.getMaxCapacity()),
                sitter.getSuspendedUntil() == null ? "-" : formatDateTime(sitter.getSuspendedUntil())
        };
    }

    private String formatMoney(int amount) {
        return String.format("%,d원", amount);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME);
    }
}
