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
}
