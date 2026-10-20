package com.team3.petwalk.model.service;

import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.MemberDAO;
import com.team3.petwalk.model.dao.SitterDAO;
import com.team3.petwalk.model.dto.SitterDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// (C) 시터 업무 규칙 검사. 규칙 위반은 걸린 조건을 메시지로 담아 IllegalArgumentException을 던진다 (NFR-07)
// 팀 공통: Controller는 IllegalArgumentException을 잡아 안내 후 관리 메뉴로 복귀한다
public class SitterService {
    private final SitterDAO sitterDAO = new SitterDAO();
    // MemberService는 SitterService를 필드로 갖고 있어 서로 new 하면 무한 순환(StackOverflowError) → A의 DAO를 직접 사용
    private final MemberDAO memberDAO = new MemberDAO();

    // F-09 등록 전 확인. 회원번호만 먼저 받아 바로 검사한다
    public void checkRegistrable(int memberId) throws SQLException {
        // #7 A의 회원 존재 확인. member 테이블 SQL은 직접 쓰지 않는다
        if (!memberDAO.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원번호입니다.");
        }
        if (sitterDAO.existsByMemberId(memberId)) {
            throw new IllegalArgumentException("이미 시터로 등록된 회원입니다.");
        }
    }

    // F-09 저장 후 새 시터번호 반환
    public int register(SitterDTO sitter) throws SQLException {
        return sitterDAO.insert(sitter);
    }

    // F-10
    public List<SitterDTO> findAll() throws SQLException {
        return sitterDAO.findAll();
    }

    // F-10
    public List<SitterDTO> findAvailable() throws SQLException {
        return sitterDAO.findAvailable();
    }

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
