package com.team3.petwalk.view.input;

import com.team3.petwalk.common.InputUtil;
import com.team3.petwalk.common.OrderConstants;

// 입력은 공통 InputUtil을 쓴다. Scanner는 InputUtil의 하나만 공유 (NFR-06, #8)
public class WalkOrderInputView {

    private static final String[] STATUSES = {
            OrderConstants.STATUS_WAITING, OrderConstants.STATUS_IN_PROGRESS,
            OrderConstants.STATUS_COMPLETED, OrderConstants.STATUS_CANCELED};

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

    // 형식 오류(숫자 아님, 범위 밖)는 안내 후 그 항목만 다시 입력받는다 (NFR-06)
    private int readNumberInRange(String prompt, int min, int max) {
        return InputUtil.readInt(prompt, min, max);
    }

    private int readPositiveNumber(String prompt) {
        return InputUtil.readId(prompt);
    }
}
