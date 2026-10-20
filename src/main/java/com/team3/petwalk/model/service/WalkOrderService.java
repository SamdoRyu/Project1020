package com.team3.petwalk.model.service;

import com.team3.petwalk.model.dao.WalkOrderDAO;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.sql.SQLException;
import java.util.List;

// (D) 업무 규칙(BR) 검사와 계산 담당. 입출력(Scanner, System.out)은 하지 않는다 (NFR-02)
public class WalkOrderService {
    private final WalkOrderDAO walkOrderDAO = new WalkOrderDAO();

    // F-14 1. 전체
    public List<WalkOrderDTO> getOrderList() throws SQLException {
        return walkOrderDAO.findAll();
    }

    // F-14 2. 상태별
    public List<WalkOrderDTO> getOrderListByStatus(String status) throws SQLException {
        return walkOrderDAO.findByStatus(status);
    }

    // F-14 3. 보호자별
    // TODO F-14 [복귀]: A의 회원 존재 확인 메서드가 나오면, 없는 회원번호는 조회 전에 막는다
    public List<WalkOrderDTO> getOrderListByGuardian(int memberId) throws SQLException {
        return walkOrderDAO.findByGuardianId(memberId);
    }

    // F-14 4. 시터별
    // TODO F-14 [복귀]: C의 시터 1명 조회 메서드가 나오면, 없는 시터번호는 조회 전에 막는다
    public List<WalkOrderDTO> getOrderListBySitter(int sitterId) throws SQLException {
        return walkOrderDAO.findBySitterId(sitterId);
    }

    // F-14 완료 주문 금액 합계. 완료 주문이 없으면 0 (Q14)
    public int sumCompletedAmount(List<WalkOrderDTO> orderList) {
        int total = 0;
        for (WalkOrderDTO order : orderList) {
            // TODO C의 상수 클래스가 나오면 '완료' 문자열을 상수로 교체
            if ("완료".equals(order.getStatus()) && order.getAmount() != null) {
                total += order.getAmount();
            }
        }
        return total;
    }
}
