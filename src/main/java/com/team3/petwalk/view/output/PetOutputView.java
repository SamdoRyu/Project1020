package com.team3.petwalk.view.output;

import com.team3.petwalk.common.OutputUtil;
import com.team3.petwalk.model.dto.PetDTO;

import java.util.ArrayList;
import java.util.List;

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

    // F-06 목록 표. 열 이름에 번호 종류를 구분해 쓴다 (NFR-09)
    public void printList(List<PetDTO> pets) {
        if (pets.isEmpty()) {
            OutputUtil.printMessage(OutputUtil.NO_RESULT);
            return;
        }
        String[] headers = {"동물번호", "이름", "견종", "무게(kg)", "활동지수", "보호자 회원번호", "보호자 이름"};
        List<String[]> rows = new ArrayList<>();
        for (PetDTO pet : pets) {
            rows.add(new String[]{
                    String.valueOf(pet.getPetId()),
                    pet.getName(),
                    pet.getBreed(),
                    pet.getWeight().toPlainString(),
                    String.valueOf(pet.getActivityLevel()),
                    String.valueOf(pet.getMemberId()),
                    pet.getMemberName()});
        }
        OutputUtil.printTable(headers, rows);
    }

    // F-07 수정 전 현재 값 출력
    public void printCurrent(PetDTO pet) {
        OutputUtil.printMessage("[현재 정보]");
        printList(List.of(pet));
    }

    // F-07
    public void printUpdated() {
        OutputUtil.printMessage("수정되었습니다.");
    }

    // F-08
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
}
