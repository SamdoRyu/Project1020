package com.team3.petwalk.model.service;

import com.team3.petwalk.model.dao.MemberDAO;

import java.sql.SQLException;

public class MemberService {
    private final MemberDAO memberDAO = new MemberDAO();

    // 회원번호가 존재하면 true. SQLException은 각 Controller가 받아 NFR-10 안내를 출력한다
    // (F-05, F-06, F-09, F-14에서 사용)
    public boolean existsById(int memberId) throws SQLException {
        return memberDAO.existsById(memberId);
    }

    // F-01, BR-11: 연락처가 중복이면 IllegalArgumentException(안내 문구 포함), 아니면 새 회원번호
    public int register(String name, String phone) throws SQLException {
        if (memberDAO.existsByPhone(phone)) {
            throw new IllegalArgumentException("이미 등록된 연락처입니다.");
        }
        return memberDAO.insert(name, phone);
    }
}
