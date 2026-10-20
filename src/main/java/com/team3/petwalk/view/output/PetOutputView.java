package com.team3.petwalk.view.output;

import com.team3.petwalk.common.OutputUtil;

public class PetOutputView {

    public void printMenu() {
        System.out.println("""

                [2. 반려동물 관리]
                1. 등록
                2. 목록
                3. 수정
                4. 삭제
                0. 뒤로
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }

    // F-05
    public void printRegistered(int petId) {
        OutputUtil.printMessage("등록되었습니다. (동물번호: " + petId + ")");
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
