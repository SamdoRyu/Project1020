package com.team3.petwalk.model.service;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.WalkOrderDAO;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    // F-16 취소할 주문 확인. 걸리면 안내 문구를 담아 던지고, Controller가 출력 후 관리 메뉴로 복귀 (NFR-07)
    public WalkOrderDTO getCancelableOrder(int orderId) throws SQLException {
        WalkOrderDTO order = walkOrderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문번호입니다.");
        }
        // BR-05: 취소는 대기에서만
        if (!OrderConstants.STATUS_WAITING.equals(order.getStatus())) {
            throw new IllegalArgumentException("대기 상태인 주문만 취소할 수 있습니다. (현재 상태: " + order.getStatus() + ")");
        }
        return order;
    }

    // F-16 보호자 측 취소: 제재 없음 (BR-06)
    public void cancelByGuardian(int orderId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            walkOrderDAO.cancel(conn, orderId, OrderConstants.CANCELED_BY_OWNER);
        }
    }

    // F-16 시터 측 취소: 주문 UPDATE + 시터 제재 UPDATE를 한 트랜잭션으로 (NFR-05, BR-06)
    // 반환값: 바뀐 제재종료일 (출력용)
    public LocalDateTime cancelBySitter(int orderId, int sitterId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                walkOrderDAO.cancel(conn, orderId, OrderConstants.CANCELED_BY_SITTER);
                sitterService.suspend(conn, sitterId);   // C 제공 (#12): 현재 + 3일, 기존이 더 늦으면 유지
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();   // 둘 중 하나라도 실패하면 주문 UPDATE까지 되돌린다
                throw e;
            }
        }
        // suspend()는 반환값이 없으므로 커밋된 제재종료일을 다시 읽어 출력에 쓴다
        return sitterService.findById(sitterId).getSuspendedUntil();
    }
}
