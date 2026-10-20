package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.model.dto.WalkOrderDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// (D) walk_order SQL 실행만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
// SQLException은 여기서 출력하지 않고 위로 던져, Controller가 NFR-10 안내 후 메인 메뉴로 돌아가게 한다
public class WalkOrderDAO {

    // F-14 목록 공통 SELECT: 주문 + 반려동물 + 보호자(member) + 시터 + 시터 이름(member)
    // 고정 문자열끼리만 이어 붙이고, 사용자 입력값은 모두 ? 로 받는다 (NFR-03)
    private static final String SELECT_LIST = """
            SELECT o.order_id, o.pet_id, o.sitter_id, o.walk_at, o.fee_type, o.applied_price,
                   o.status, o.canceled_by, o.distance_km, o.duration_min, o.amount, o.paid_at, o.created_at,
                   p.name AS pet_name, g.member_id AS guardian_id, g.name AS guardian_name, sm.name AS sitter_name
            FROM walk_order o
            JOIN pet p     ON p.pet_id = o.pet_id
            JOIN member g  ON g.member_id = p.member_id
            JOIN sitter s  ON s.sitter_id = o.sitter_id
            JOIN member sm ON sm.member_id = s.member_id
            """;
    private static final String ORDER_BY = " ORDER BY o.order_id";

    // F-14 1. 전체
    public List<WalkOrderDTO> findAll() throws SQLException {
        String sql = SELECT_LIST + ORDER_BY;
        List<WalkOrderDTO> orderList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                orderList.add(toDTO(rs));
            }
        }
        return orderList;
    }

    // F-14 2. 상태별
    public List<WalkOrderDTO> findByStatus(String status) throws SQLException {
        String sql = SELECT_LIST + "WHERE o.status = ?" + ORDER_BY;
        List<WalkOrderDTO> orderList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderList.add(toDTO(rs));
                }
            }
        }
        return orderList;
    }

    // F-14 3. 보호자별: walk_order에는 보호자 번호가 없으므로 pet의 member_id로 찾는다 (3NF)
    public List<WalkOrderDTO> findByGuardianId(int memberId) throws SQLException {
        String sql = SELECT_LIST + "WHERE p.member_id = ?" + ORDER_BY;
        List<WalkOrderDTO> orderList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderList.add(toDTO(rs));
                }
            }
        }
        return orderList;
    }

    // F-14 4. 시터별
    public List<WalkOrderDTO> findBySitterId(int sitterId) throws SQLException {
        String sql = SELECT_LIST + "WHERE o.sitter_id = ?" + ORDER_BY;
        List<WalkOrderDTO> orderList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderList.add(toDTO(rs));
                }
            }
        }
        return orderList;
    }

    // ResultSet 한 행 → DTO. NULL 가능 컬럼은 getInt 대신 getObject (getInt는 NULL을 0으로 바꿈)
    private WalkOrderDTO toDTO(ResultSet rs) throws SQLException {
        WalkOrderDTO order = new WalkOrderDTO();
        order.setOrderId(rs.getInt("order_id"));
        order.setPetId(rs.getInt("pet_id"));
        order.setSitterId(rs.getInt("sitter_id"));
        order.setWalkAt(rs.getObject("walk_at", LocalDateTime.class));
        order.setFeeType(rs.getString("fee_type"));
        order.setAppliedPrice(rs.getInt("applied_price"));
        order.setStatus(rs.getString("status"));
        order.setCanceledBy(rs.getString("canceled_by"));
        order.setDistanceKm(rs.getBigDecimal("distance_km"));
        order.setDurationMin(rs.getObject("duration_min", Integer.class));
        order.setAmount(rs.getObject("amount", Integer.class));
        order.setPaidAt(rs.getObject("paid_at", LocalDateTime.class));
        order.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        order.setPetName(rs.getString("pet_name"));
        order.setGuardianId(rs.getInt("guardian_id"));
        order.setGuardianName(rs.getString("guardian_name"));
        order.setSitterName(rs.getString("sitter_name"));
        return order;
    }
}
