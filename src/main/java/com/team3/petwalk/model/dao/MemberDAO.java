package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    // 회원번호로 member 행이 있는지 확인한다 (F-05, F-06, F-09, F-14에서 사용)
    public boolean existsById(int memberId) throws SQLException {
        String sql = "SELECT 1 FROM member WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
