package com.team3.petwalk.model.service;

import com.team3.petwalk.model.dao.PetDAO;
import com.team3.petwalk.model.dto.PetDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

// (B) 반려동물 업무 규칙 판단 (NFR-02)
// 규칙 위반은 IllegalArgumentException(안내 문구)으로 알리고, Controller가 출력 후 관리 메뉴로 복귀한다 (NFR-07)
public class PetService {
    private final PetDAO petDAO = new PetDAO();
    private final MemberService memberService = new MemberService();

    // F-05 반려동물 등록: 회원 존재 확인 → 범위 검사 → 저장. 동물번호를 돌려준다
    public int register(PetDTO pet) throws SQLException {
        // TODO #7: A의 회원 존재 확인 메서드 이름이 확정되면 맞춘다
        if (!memberService.existsById(pet.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 회원번호입니다.");
        }
        if (pet.getWeight().signum() <= 0 || pet.getWeight().stripTrailingZeros().scale() > 1) {
            throw new IllegalArgumentException("무게는 0보다 크고 소수 첫째 자리까지만 입력할 수 있습니다.");
        }
        if (pet.getActivityLevel() < 1 || pet.getActivityLevel() > 5) {
            throw new IllegalArgumentException("활동지수는 1~5 사이여야 합니다.");
        }
        return petDAO.insert(pet);
    }

    // F-06 전체 목록
    public List<PetDTO> findAll() throws SQLException {
        return petDAO.findAll();
    }

    // F-06 보호자별 목록: 없는 회원이면 복귀 (NFR-07). 반려동물 없음은 빈 목록으로 돌려준다
    public List<PetDTO> findByMemberId(int memberId) throws SQLException {
        // TODO #7: A의 회원 존재 확인 메서드 이름이 확정되면 맞춘다
        if (!memberService.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원번호입니다.");
        }
        return petDAO.findByMemberId(memberId);
    }
}
