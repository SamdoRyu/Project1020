package com.team3.petwalk.controller;

import com.team3.petwalk.model.dto.WalkOrderDTO;
import com.team3.petwalk.model.service.WalkOrderService;
import com.team3.petwalk.view.input.WalkOrderInputView;
import com.team3.petwalk.view.output.WalkOrderOutputView;

import java.sql.SQLException;
import java.util.List;

// (D) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class WalkOrderController {
    private final WalkOrderInputView inputView = new WalkOrderInputView();
    private final WalkOrderOutputView outputView = new WalkOrderOutputView();
    private final WalkOrderService walkOrderService = new WalkOrderService();

    public void run() {
        while (true) {
            outputView.printMenu();
            try {
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
            } catch (IllegalArgumentException e) {
                // NFR-07: 업무 규칙 위반은 걸린 조건을 안내하고 이 관리 메뉴로 복귀
                outputView.printError(e.getMessage());
            } catch (SQLException e) {
                // NFR-10: 안내 후 run()을 빠져나가 메인 메뉴로 복귀
                outputView.printDbError();
                return;
            }
        }
    }

    // F-13 산책 주문 등록
    private void register() {
        // TODO
    }

    // F-14 주문 목록 (1.전체 2.상태별 3.보호자별 4.시터별)
    // 흐름: InputView 입력 → Service 조회·합계 계산 → OutputView 출력
    private void list() throws SQLException {
        outputView.printListMenu();
        List<WalkOrderDTO> orderList = switch (inputView.readListType()) {
            case 1 -> walkOrderService.getOrderList();
            case 2 -> {
                outputView.printStatusMenu();
                yield walkOrderService.getOrderListByStatus(inputView.readStatus());
            }
            case 3 -> walkOrderService.getOrderListByGuardian(inputView.readGuardianMemberId());
            default -> walkOrderService.getOrderListBySitter(inputView.readSitterId());
        };
        int completedTotal = walkOrderService.sumCompletedAmount(orderList);
        outputView.printOrderList(orderList, completedTotal);
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
