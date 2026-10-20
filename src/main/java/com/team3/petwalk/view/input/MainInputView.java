package com.team3.petwalk.view.input;

import com.team3.petwalk.common.InputUtil;

// 입력은 InputUtil로 받는다. System.in을 읽는 Scanner는 InputUtil 하나만 쓴다 (NFR-06)
public class MainInputView {

    // 메뉴 번호 입력. 검증 없이 문자열로 돌려주고, 없는 번호는 Controller switch의 default에서 처리
    public String readMenu() {
        return InputUtil.readLine("선택> ");
    }
}
