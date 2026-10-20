package com.team3.petwalk.model.service;

import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.WalkOrderDAO;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.sql.SQLException;
import java.util.List;

// (D) 업무 규칙(BR) 검사와 계산 담당. 입출력(Scanner, System.out)은 하지 않는다 (NFR-02)
public class WalkOrderService {
    private final WalkOrderDAO walkOrderDAO = new WalkOrderDAO();
    private final MemberService memberService = new MemberService();   // A 제공 (#7)
    private final SitterService sitterService = new SitterService();   // C 제공 (#12)

    // F-14 1. 전체
    public List<WalkOrderDTO> getOrderList() throws SQLException {
        return walkOrderDAO.findAll();
    }

    // F-14 2. 상태별
    public List<WalkOrderDTO> getOrderListByStatus(String status) throws SQLException {
        return walkOrderDAO.findByStatus(status);
    }

    // F-14 3. 보호자별. 없는 회원번호면 안내 후 [복귀] (NFR-07)
    public List<WalkOrderDTO> getOrderListByGuardian(int memberId) throws SQLException {
        if (!memberService.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원번호입니다.");
        }
        return walkOrderDAO.findByGuardianId(memberId);
    }

    // F-14 4. 시터별. 없는 시터번호면 SitterService가 안내 문구를 담아 던진다 → [복귀] (NFR-07)
    public List<WalkOrderDTO> getOrderListBySitter(int sitterId) throws SQLException {
        sitterService.findById(sitterId);
        return walkOrderDAO.findBySitterId(sitterId);
    }

    // F-14 완료 주문 금액 합계. 완료 주문이 없으면 0 (Q14)
    public int sumCompletedAmount(List<WalkOrderDTO> orderList) {
        int total = 0;
        for (WalkOrderDTO order : orderList) {
            if (OrderConstants.STATUS_COMPLETED.equals(order.getStatus()) && order.getAmount() != null) {
                total += order.getAmount();
            }
        }
        return total;
    }
}
