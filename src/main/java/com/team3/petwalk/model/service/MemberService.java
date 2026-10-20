package com.team3.petwalk.model.service;

import com.team3.petwalk.common.OrderConstants;
import com.team3.petwalk.model.dao.MemberDAO;
import com.team3.petwalk.model.dto.MemberDTO;

import java.sql.SQLException;
import java.util.List;

public class MemberService {
    private final MemberDAO memberDAO = new MemberDAO();
    private final SitterService sitterService = new SitterService();

    // 회원번호가 존재하면 true. SQLException은 각 Controller가 받아 NFR-10 안내를 출력한다
    // (F-05, F-06, F-09, F-14에서 사용)
    public boolean existsById(int memberId) throws SQLException {
        return memberDAO.existsById(memberId);
    }

    // F-03: 회원번호로 조회. 없으면 IllegalArgumentException(안내 문구 포함)
    public MemberDTO findById(int memberId) throws SQLException {
        MemberDTO member = memberDAO.findById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원번호입니다.");
        }
        return member;
    }

    // F-03, BR-11: 본인을 제외하고 연락처가 중복이면 IllegalArgumentException
    public void update(int memberId, String name, String phone) throws SQLException {
        if (memberDAO.existsByPhoneExcept(phone, memberId)) {
            throw new IllegalArgumentException("다른 회원이 이미 사용 중인 연락처입니다.");
        }
        memberDAO.update(memberId, name, phone);
    }

    // F-17: 완료 주문의 시터별 수입 (합계 큰 순)
    public List<MemberDTO.StatisticsRow> sitterIncome() throws SQLException {
        return memberDAO.sumBySitter(OrderConstants.STATUS_COMPLETED);
    }

    // F-17: 완료 주문의 보호자별 지출 (합계 큰 순)
    public List<MemberDTO.StatisticsRow> guardianExpense() throws SQLException {
        return memberDAO.sumByGuardian(OrderConstants.STATUS_COMPLETED);
    }

    // F-04, BR-10: 반려동물이나 시터 정보가 있는 회원은 삭제 불가 (IllegalArgumentException)
    public void delete(int memberId) throws SQLException {
        findById(memberId);
        // PetService가 MemberService를 필드로 가지므로, 순환 생성을 피하려고 여기서만 만든다
        boolean hasPet = !new PetService().findByMemberId(memberId).isEmpty();
        if (hasPet || sitterService.isSitter(memberId)) {
            throw new IllegalArgumentException("반려동물 또는 시터 정보가 있어 삭제할 수 없습니다.");
        }
        memberDAO.delete(memberId);
    }

    // F-02: 이름 검색어로 회원 목록 조회. 빈 검색어는 전체
    public List<MemberDTO> search(String keyword) throws SQLException {
        return memberDAO.findByName(keyword);
    }

    // F-01, BR-11: 연락처가 중복이면 IllegalArgumentException(안내 문구 포함), 아니면 새 회원번호
    public int register(String name, String phone) throws SQLException {
        if (memberDAO.existsByPhone(phone)) {
            throw new IllegalArgumentException("이미 등록된 연락처입니다.");
        }
        return memberDAO.insert(name, phone);
    }
}
