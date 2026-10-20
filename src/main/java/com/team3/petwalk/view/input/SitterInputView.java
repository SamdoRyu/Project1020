package com.team3.petwalk.view.input;

import com.team3.petwalk.common.InputUtil;

// 형식 오류(문자, 범위 밖, Y/N 외 입력)는 InputUtil이 그 항목만 다시 묻는다 (NFR-06)
// Scanner는 InputUtil 하나만 쓴다
public class SitterInputView {
    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        return InputUtil.readLine("선택> ");
    }

    // 회원번호와 시터번호는 다른 값이므로 안내 문구에서 구분한다 (NFR-09)
    public int readMemberId() {
        return InputUtil.readId("회원번호> ");
    }

    // F-09 등록 입력. 단가는 0 이하 불가, 수용량 1~3
    public int readPricePerKm() {
        return InputUtil.readInt("km당 단가(원)> ", 1, Integer.MAX_VALUE);
    }

    public int readPricePerMin() {
        return InputUtil.readInt("분당 단가(원)> ", 1, Integer.MAX_VALUE);
    }

    public boolean readCertified() {
        return InputUtil.readYesNo("자격증 보유(Y/N)> ");
    }

    public int readMaxCapacity() {
        return InputUtil.readInt("최대수용량(1~3)> ", 1, 3);
    }
}
