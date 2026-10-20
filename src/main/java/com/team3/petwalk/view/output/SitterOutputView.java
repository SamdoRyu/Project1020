package com.team3.petwalk.view.output;

import com.team3.petwalk.common.OutputUtil;

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
        OutputUtil.printMessage("메뉴 번호를 다시 입력해 주세요.");
    }

    public void printRegistered(int sitterId) {
        OutputUtil.printMessage("시터가 등록되었습니다. (시터번호: " + sitterId + ")");
    }

    // 업무 규칙 위반 안내 (NFR-07)
    public void printError(String message) {
        OutputUtil.printMessage(message);
    }

    // NFR-10
    public void printDbError() {
        OutputUtil.printMessage(OutputUtil.DB_ERROR);
    }
}
