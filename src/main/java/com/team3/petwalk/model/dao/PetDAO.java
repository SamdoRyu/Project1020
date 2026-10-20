package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
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

    // 반려동물 1마리 조회 (보호자 회원번호 포함). 없으면 null (F-07, 다른 팀원 제공용)
    public PetDTO findById(int petId) throws SQLException {
        String sql = SELECT_WITH_MEMBER + "WHERE p.pet_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, petId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<PetDTO> pets = toList(rs);
                return pets.isEmpty() ? null : pets.get(0);
            }
        }
    }

    // F-07 반려동물 수정. 보호자(member_id)는 바꾸지 않는다
    public int update(PetDTO pet) throws SQLException {
        String sql = "UPDATE pet SET name = ?, breed = ?, weight = ?, activity_level = ? WHERE pet_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getBreed());
            pstmt.setBigDecimal(3, pet.getWeight());
            pstmt.setInt(4, pet.getActivityLevel());
            pstmt.setInt(5, pet.getPetId());
            return pstmt.executeUpdate();
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
}
