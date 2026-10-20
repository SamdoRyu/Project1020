package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.model.dto.PetDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
