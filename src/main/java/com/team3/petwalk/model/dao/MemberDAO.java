package com.team3.petwalk.model.dao;

import com.team3.petwalk.common.DBUtil;
import com.team3.petwalk.model.dto.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    // F-01, BR-11: 연락처 중복 확인
    public boolean existsByPhone(String phone) throws SQLException {
        String sql = "SELECT 1 FROM member WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // F-03: 회원번호로 한 명 조회. 없으면 null
    public MemberDTO findById(int memberId) throws SQLException {
        String sql = "SELECT member_id, name, phone, created_at FROM member WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new MemberDTO(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at").toLocalDateTime());
            }
        }
    }

    // F-03, BR-11: 본인(memberId)을 제외하고 같은 연락처가 있는지 확인
    public boolean existsByPhoneExcept(String phone, int memberId) throws SQLException {
        String sql = "SELECT 1 FROM member WHERE phone = ? AND member_id <> ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setInt(2, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // F-03: 이름·연락처 수정
    public void update(int memberId, String name, String phone) throws SQLException {
        String sql = "UPDATE member SET name = ?, phone = ? WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setInt(3, memberId);
            pstmt.executeUpdate();
        }
    }

    // F-02: 이름에 검색어가 포함된 회원 목록. 검색어가 빈 문자열이면 전체 (LIKE '%%')
    public List<MemberDTO> findByName(String keyword) throws SQLException {
        String sql = "SELECT member_id, name, phone, created_at FROM member "
                + "WHERE name LIKE ? ORDER BY member_id";
        List<MemberDTO> members = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(new MemberDTO(
                            rs.getInt("member_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                }
            }
        }
        return members;
    }

    // F-01: 회원 저장 (가입일시는 DB 기본값). 생성된 회원번호를 돌려준다
    public int insert(String name, String phone) throws SQLException {
        String sql = "INSERT INTO member (name, phone) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
