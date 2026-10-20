package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dto.PetDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// (B) pet 테이블 SQL만 담당한다 (NFR-02~04)
public class PetDAO {

    // F-05 반려동물 등록. 생성된 동물번호를 돌려준다
    public int insert(PetDTO pet) throws SQLException {
        String sql = "INSERT INTO pet (member_id, name, breed, weight, activity_level) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, pet.getMemberId());
            pstmt.setString(2, pet.getName());
            pstmt.setString(3, pet.getBreed());
            pstmt.setBigDecimal(4, pet.getWeight());
            pstmt.setInt(5, pet.getActivityLevel());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // 목록 조회 공통 SELECT (pet JOIN member로 보호자 이름 포함)
    private static final String SELECT_WITH_MEMBER =
            "SELECT p.pet_id, p.member_id, p.name, p.breed, p.weight, p.activity_level, m.name AS member_name "
                    + "FROM pet p JOIN member m ON p.member_id = m.member_id ";

    // F-06 전체 목록
    public List<PetDTO> findAll() throws SQLException {
        String sql = SELECT_WITH_MEMBER + "ORDER BY p.pet_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return toList(rs);
        }
    }

    // F-06 보호자별 목록
    public List<PetDTO> findByMemberId(int memberId) throws SQLException {
        String sql = SELECT_WITH_MEMBER + "WHERE p.member_id = ? ORDER BY p.pet_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return toList(rs);
            }
        }
    }

    private List<PetDTO> toList(ResultSet rs) throws SQLException {
        List<PetDTO> pets = new ArrayList<>();
        while (rs.next()) {
            pets.add(new PetDTO(
                    rs.getInt("pet_id"),
                    rs.getInt("member_id"),
                    rs.getString("name"),
                    rs.getString("breed"),
                    rs.getBigDecimal("weight"),
                    rs.getInt("activity_level"),
                    rs.getString("member_name")));
        }
        return pets;
    }

    // 반려동물의 진행 중(대기·수행중) 주문 수 (F-13 BR-04, 다른 팀원 제공용)
    public int countActiveOrders(int petId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM walk_order WHERE pet_id = ? AND status IN (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, petId);
            pstmt.setString(2, OrderConstants.STATUS_WAITING);
            pstmt.setString(3, OrderConstants.STATUS_IN_PROGRESS);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // 회원의 반려동물 수 (F-04 BR-10, 다른 팀원 제공용)
    public int countByMemberId(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pet WHERE member_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
