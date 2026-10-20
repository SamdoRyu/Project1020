package controller;

import view.input.PetInputView;
import view.output.PetOutputView;

// (B) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class PetController {
    private final PetInputView inputView = new PetInputView();
    private final PetOutputView outputView = new PetOutputView();

    public void run() {
        while (true) {
            outputView.printMenu();
            switch (inputView.readMenu()) {
                case "1" -> register();           // F-05
                case "2" -> list();               // F-06
                case "3" -> update();             // F-07
                case "4" -> delete();             // F-08
                case "0" -> {
                    return;
                }
                default -> outputView.printInvalidMenu();
            }
        }
    }

    // F-05 반려동물 등록
    private void register() {
        // TODO
    }

    // F-06 반려동물 목록 (1.전체 2.보호자별)
    private void list() {
        // TODO
    }

    // F-07 반려동물 수정
    private void update() {
        // TODO
    }

    // F-08 반려동물 삭제
    private void delete() {
        // TODO
    }
}
