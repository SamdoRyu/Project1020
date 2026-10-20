package controller;

import view.input.WalkOrderInputView;
import view.output.WalkOrderOutputView;

// (D) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class WalkOrderController {
    private final WalkOrderInputView inputView = new WalkOrderInputView();
    private final WalkOrderOutputView outputView = new WalkOrderOutputView();

    public void run() {
        while (true) {
            outputView.printMenu();
            switch (inputView.readMenu()) {
                case "1" -> register();           // F-13
                case "2" -> list();               // F-14
                case "3" -> startOrComplete();    // F-15
                case "4" -> cancel();             // F-16
                case "0" -> {
                    return;
                }
                default -> outputView.printInvalidMenu();
            }
        }
    }

    // F-13 산책 주문 등록
    private void register() {
        // TODO
    }

    // F-14 주문 목록 (1.전체 2.상태별 3.보호자별 4.시터별)
    private void list() {
        // TODO
    }

    // F-15 산책 시작·완료 (1.시작 2.완료)
    private void startOrComplete() {
        // TODO
    }

    // F-16 주문 취소 (1.보호자 2.시터), NFR-05 트랜잭션
    private void cancel() {
        // TODO
    }
}
