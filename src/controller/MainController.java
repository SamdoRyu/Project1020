package controller;

import view.input.MainInputView;
import view.output.MainOutputView;

// 메인 메뉴 분기만 담당한다 (NFR-02)
public class MainController {
    private final MainInputView inputView = new MainInputView();
    private final MainOutputView outputView = new MainOutputView();

    private final MemberController memberController = new MemberController();
    private final PetController petController = new PetController();
    private final SitterController sitterController = new SitterController();
    private final WalkOrderController walkOrderController = new WalkOrderController();

    public void run() {
        while (true) {
            outputView.printMenu();
            // TODO NFR-10: DB 오류 시 "데이터 처리 중 오류가 발생했습니다." 출력 후 이 메뉴로 복귀
            switch (inputView.readMenu()) {
                case "1" -> memberController.run();           // 회원 관리 (A)
                case "2" -> petController.run();              // 반려동물 관리 (B)
                case "3" -> sitterController.run();           // 시터 관리 (C)
                case "4" -> walkOrderController.run();        // 주문·결제 관리 (D)
                case "5" -> memberController.runStatistics(); // 결제 통계 F-17 (A, 선택)
                case "0" -> {
                    outputView.printExit();
                    return;
                }
                default -> outputView.printInvalidMenu();
            }
        }
    }
}
