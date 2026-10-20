package com.team3.petwalk.view.input;

import com.team3.petwalk.common.InputUtil;
import com.team3.petwalk.model.dto.PetDTO;

import java.math.BigDecimal;

public class PetInputView {

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        return InputUtil.readLine("선택> ");
    }

    // F-05 등록 정보 입력. 형식 오류는 InputUtil에서 그 항목만 재입력 (NFR-06)
    public PetDTO readRegisterInfo() {
        int memberId = InputUtil.readId("보호자 회원번호> ");
        String name = InputUtil.readString("이름> ");
        String breed = InputUtil.readString("견종> ");
        BigDecimal weight = InputUtil.readPositiveDecimal("무게(kg, 소수 첫째 자리까지)> ", 1);
        int activityLevel = InputUtil.readInt("활동지수(1~5)> ", 1, 5);
        return new PetDTO(memberId, name, breed, weight, activityLevel);
    }

    // F-06 목록 종류 (1.전체 2.보호자별)
    public int readListType() {
        return InputUtil.readInt("1) 전체  2) 보호자별 선택> ", 1, 2);
    }

    // F-06 보호자별 조회 회원번호 (NFR-09)
    public int readMemberId() {
        return InputUtil.readId("보호자 회원번호> ");
    }
}
