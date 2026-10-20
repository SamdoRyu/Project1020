package com.team3.petwalk.model.service;

import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.SitterDAO;
import com.team3.petwalk.model.dto.SitterDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

// (C) 시터 업무 규칙 검사. 규칙 위반은 걸린 조건을 메시지로 담아 IllegalArgumentException을 던진다 (NFR-07)
// 팀 공통: Controller는 IllegalArgumentException을 잡아 안내 후 관리 메뉴로 복귀한다
public class SitterService {
    private final SitterDAO sitterDAO = new SitterDAO();

    // 시터 1명 조회 (단가·제재종료일·회원번호·이름·최대수용량). 없으면 예외
    // 사용처: F-11, F-12, F-13, F-14, F-15, F-16
    public SitterDTO findById(int sitterId) throws SQLException {
        SitterDTO sitter = sitterDAO.findById(sitterId);
        if (sitter == null) {
            throw new IllegalArgumentException("존재하지 않는 시터번호입니다.");
        }
        return sitter;
    }

    // 제재 중 = 제재종료일이 현재보다 뒤. F-10 매칭 가능 조건의 반대
    // 사용처: F-11 제재 해제(BR-09), F-13 주문 등록 시 제재 검사(BR-03)
    public boolean isSuspended(SitterDTO sitter) {
        return sitter.getSuspendedUntil() != null
                && sitter.getSuspendedUntil().isAfter(LocalDateTime.now());
    }

    // 회원의 시터 여부. 사용처: F-04 회원 삭제 (BR-10)
    public boolean isSitter(int memberId) throws SQLException {
        return sitterDAO.existsByMemberId(memberId);
    }

    // 수행중 주문 수. 사용처: F-15 산책 시작 시 최대수용량과 비교 (BR-07)
    public int countInProgressOrders(int sitterId) throws SQLException {
        return sitterDAO.countOrdersByStatus(sitterId, OrderConstants.STATUS_IN_PROGRESS);
    }

    // 시터 측 취소 제재: 현재 + 3일, 기존 제재종료일이 더 늦으면 유지 (BR-06)
    // 사용처: F-16. 주문 UPDATE와 같은 Connection으로 묶기 위해 Connection을 받는다 (NFR-05)
    // commit·rollback·close는 호출한 쪽에서 한다
    public void suspend(Connection conn, int sitterId) throws SQLException {
        LocalDateTime threeDaysLater = LocalDateTime.now().plusDays(3);
        LocalDateTime current = sitterDAO.findSuspendedUntil(conn, sitterId);
        LocalDateTime suspendedUntil =
                (current != null && current.isAfter(threeDaysLater)) ? current : threeDaysLater;
        sitterDAO.updateSuspendedUntil(conn, sitterId, suspendedUntil);
    }
}
