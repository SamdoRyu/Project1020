package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.model.dto.SitterDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// (C) sitter 테이블 SQL만 실행한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class SitterDAO {

    // 이름은 member에만 있으므로 JOIN으로 가져온다
    private static final String SELECT_WITH_NAME = """
            SELECT s.sitter_id, s.member_id, m.name, s.price_per_km, s.price_per_min,
                   s.certified, s.max_capacity, s.suspended_until
              FROM sitter s
              JOIN member m ON s.member_id = m.member_id
            """;

    // 회원의 시터 여부 (sitter.member_id UNIQUE)
    public boolean existsByMemberId(int memberId) throws SQLException {
        String sql = "SELECT 1 FROM sitter WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // F-09 저장 후 새로 만들어진 시터번호를 돌려준다. suspended_until은 기본값 NULL
    public int insert(SitterDTO sitter) throws SQLException {
        String sql = """
                INSERT INTO sitter (member_id, price_per_km, price_per_min, certified, max_capacity)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, sitter.getMemberId());
            pstmt.setInt(2, sitter.getPricePerKm());
            pstmt.setInt(3, sitter.getPricePerMin());
            pstmt.setBoolean(4, sitter.isCertified());
            pstmt.setInt(5, sitter.getMaxCapacity());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // F-10 전체 시터
    public List<SitterDTO> findAll() throws SQLException {
        return findList(SELECT_WITH_NAME + " ORDER BY s.sitter_id");
    }

    // F-10 매칭 가능 시터 = 제재종료일이 없거나 이미 지난 시터
    public List<SitterDTO> findAvailable() throws SQLException {
        return findList(SELECT_WITH_NAME
                + " WHERE s.suspended_until IS NULL OR s.suspended_until <= NOW()"
                + " ORDER BY s.sitter_id");
    }

    // 시터 1명 조회. 없으면 null
    public SitterDTO findById(int sitterId) throws SQLException {
        String sql = SELECT_WITH_NAME + " WHERE s.sitter_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? toDTO(rs) : null;
            }
        }
    }

    // F-11 정보 수정 (단가·자격증·수용량)
    public int update(SitterDTO sitter) throws SQLException {
        String sql = """
                UPDATE sitter
                   SET price_per_km = ?, price_per_min = ?, certified = ?, max_capacity = ?
                 WHERE sitter_id = ?
                """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitter.getPricePerKm());
            pstmt.setInt(2, sitter.getPricePerMin());
            pstmt.setBoolean(3, sitter.isCertified());
            pstmt.setInt(4, sitter.getMaxCapacity());
            pstmt.setInt(5, sitter.getSitterId());
            return pstmt.executeUpdate();
        }
    }

    // F-11 제재 해제 (BR-09)
    public int clearSuspension(int sitterId) throws SQLException {
        String sql = "UPDATE sitter SET suspended_until = NULL WHERE sitter_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitterId);
            return pstmt.executeUpdate();
        }
    }

    // 시터의 특정 상태 주문 수 (F-15 수행중 주문 수, BR-07)
    public int countOrdersByStatus(int sitterId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM walk_order WHERE sitter_id = ? AND status = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitterId);
            pstmt.setString(2, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // 트랜잭션용(F-16): 호출한 쪽의 Connection을 쓰고 닫지 않는다 (NFR-05)
    public LocalDateTime findSuspendedUntil(Connection conn, int sitterId) throws SQLException {
        String sql = "SELECT suspended_until FROM sitter WHERE sitter_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sitterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getObject("suspended_until", LocalDateTime.class) : null;
            }
        }
    }

    // 트랜잭션용(F-16): 호출한 쪽의 Connection을 쓰고 닫지 않는다 (NFR-05)
    public int updateSuspendedUntil(Connection conn, int sitterId, LocalDateTime suspendedUntil) throws SQLException {
        String sql = "UPDATE sitter SET suspended_until = ? WHERE sitter_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, suspendedUntil);
            pstmt.setInt(2, sitterId);
            return pstmt.executeUpdate();
        }
    }

    private List<SitterDTO> findList(String sql) throws SQLException {
        List<SitterDTO> sitters = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                sitters.add(toDTO(rs));
            }
        }
        return sitters;
    }

    private SitterDTO toDTO(ResultSet rs) throws SQLException {
        SitterDTO sitter = new SitterDTO(
                rs.getInt("sitter_id"),
                rs.getInt("member_id"),
                rs.getInt("price_per_km"),
                rs.getInt("price_per_min"),
                rs.getBoolean("certified"),
                rs.getInt("max_capacity"),
                rs.getObject("suspended_until", LocalDateTime.class)); // NULL이면 null
        sitter.setMemberName(rs.getString("name"));
        return sitter;
    }
}
