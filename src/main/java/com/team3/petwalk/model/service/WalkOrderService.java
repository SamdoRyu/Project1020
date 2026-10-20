package com.team3.petwalk.model.service;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.common.FeeCalculator;
import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.WalkOrderDAO;
import com.team3.petwalk.model.dto.SitterDTO;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// (D) 업무 규칙(BR) 검사와 계산 담당. 입출력(Scanner, System.out)은 하지 않는다 (NFR-02)
public class WalkOrderService {
    private final WalkOrderDAO walkOrderDAO = new WalkOrderDAO();
    private final MemberService memberService = new MemberService();   // A 제공 (#7)
    private final SitterService sitterService = new SitterService();   // C 제공 (#12)
    private final PetService petService = new PetService();            // B 제공 (#10)

    // F-13 산책 주문 등록. 1~5 중 걸린 조건을 안내하고 [복귀] (NFR-07). 반환값: 생성된 주문번호
    public int register(int petId, int sitterId, LocalDateTime walkAt, String feeType) throws SQLException {
        // 1. 시터 존재 확인. 없으면 SitterService가 안내 문구를 담아 던진다 (C 제공 #12)
        SitterDTO sitter = sitterService.findById(sitterId);
        // TODO #10: B의 반려동물 1마리 조회(findById)가 나오면 연결
        //   1. 반려동물 존재 확인
        //      PetDTO pet = petService.findById(petId);  → null이면 "존재하지 않는 동물번호입니다."

        // 2. 시터가 제재 중이 아닌지 (BR-03). 제재 검사는 등록 때만 한다
        if (sitterService.isSuspended(sitter)) {
            throw new IllegalArgumentException("제재 중인 시터라 새 주문을 받을 수 없습니다.");
        }

        // TODO #10: 3. 시터의 회원번호 ≠ 반려동물의 보호자 회원번호 (BR-03)
        //   if (sitter.getMemberId() == pet.getMemberId()) → "시터 본인의 반려동물은 주문할 수 없습니다."

        // 4. 그 반려동물에 진행 중(대기·수행중) 주문이 없는지 (BR-04, B 제공 #10)
        if (petService.hasActiveOrder(petId)) {
            throw new IllegalArgumentException("이미 진행 중(대기·수행중)인 주문이 있는 반려동물입니다.");
        }

        // 5. 산책일시는 현재 이후 (BR-03)
        if (!walkAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("산책일시는 현재 이후여야 합니다.");
        }

        // TODO #10: 1·3단계 연결 후 아래 throw를 지우고 저장
        //   반려동물 확인 없이 저장하면 없는 동물번호는 FK 오류(NFR-10)로 빠지고, BR-03 본인 검사도 빠진다
        //   6. 요금방식에 맞는 시터의 현재 단가를 주문에 복사해 대기 상태로 저장 (BR-02)
        //   int appliedPrice = OrderConstants.FEE_TYPE_DISTANCE.equals(feeType) ? sitter.getPricePerKm() : sitter.getPricePerMin();
        //   return walkOrderDAO.insert(petId, sitterId, walkAt, feeType, appliedPrice);
        throw new IllegalStateException("주문 등록은 반려동물 조회(#10 findById) 연결 후 사용할 수 있습니다.");
    }

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

    // F-15 주문 확인. 없는 주문번호면 [복귀] (NFR-07)
    public WalkOrderDTO getOrder(int orderId) throws SQLException {
        WalkOrderDTO order = walkOrderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문번호입니다.");
        }
        return order;
    }

    // F-15 산책 시작: 대기 → 수행중 (BR-05). 산책일시와는 비교하지 않는다
    public void start(WalkOrderDTO order) throws SQLException {
        if (!OrderConstants.STATUS_WAITING.equals(order.getStatus())) {
            throw new IllegalArgumentException("대기 상태인 주문만 시작할 수 있습니다. (현재 상태: " + order.getStatus() + ")");
        }
        // BR-07: 시터의 수행중 주문 수 < 최대수용량일 때만 시작 (C 제공 #12)
        SitterDTO sitter = sitterService.findById(order.getSitterId());
        if (sitterService.countInProgressOrders(order.getSitterId()) >= sitter.getMaxCapacity()) {
            throw new IllegalArgumentException("시터의 수행중 주문이 최대수용량(" + sitter.getMaxCapacity() + "건)에 도달해 시작할 수 없습니다.");
        }
        walkOrderDAO.start(order.getOrderId());
    }

    // F-15 완료할 수 있는 주문인지: 수행중 → 완료만 가능 (BR-05). 실측값 입력 전에 확인한다
    public void checkCompletable(WalkOrderDTO order) {
        if (!OrderConstants.STATUS_IN_PROGRESS.equals(order.getStatus())) {
            throw new IllegalArgumentException("수행중인 주문만 완료할 수 있습니다. (현재 상태: " + order.getStatus() + ")");
        }
    }

    // F-15 산책 완료: 요금방식 쪽 실측값으로 요금 계산 (BR-01) 후 한 번에 UPDATE. 반환값: 결제 금액
    public int complete(WalkOrderDTO order, BigDecimal distanceKm, int durationMin) throws SQLException {
        checkCompletable(order);
        // 단가는 시터의 현재 단가가 아니라 주문에 복사된 applied_price (BR-02). C 제공 #11
        int amount = FeeCalculator.calculate(order.getFeeType(), distanceKm, durationMin, order.getAppliedPrice());
        walkOrderDAO.complete(order.getOrderId(), distanceKm, durationMin, amount);
        return amount;
    }
}
