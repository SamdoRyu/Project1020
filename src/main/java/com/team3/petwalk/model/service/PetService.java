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
        validateRange(pet);
        return petDAO.insert(pet);
    }

    // F-07 수정 전 존재 확인. 없는 동물번호면 복귀 (NFR-07)
    public PetDTO findById(int petId) throws SQLException {
        PetDTO pet = petDAO.findById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("존재하지 않는 동물번호입니다.");
        }
        return pet;
    }

    // F-07 반려동물 수정: 범위 검사 → UPDATE (보호자는 바꾸지 않음)
    public void update(PetDTO pet) throws SQLException {
        validateRange(pet);
        if (petDAO.update(pet) == 0) {
            throw new IllegalArgumentException("존재하지 않는 동물번호입니다.");
        }
    }

    // F-08 반려동물 삭제: 존재 확인 → 주문 이력이 없을 때만 DELETE (BR-10)
    public void delete(int petId) throws SQLException {
        findById(petId);
        if (petDAO.countOrders(petId) > 0) {
            throw new IllegalArgumentException("산책 주문 이력이 있어 삭제할 수 없습니다.");
        }
        petDAO.delete(petId);
    }

    // 무게 DECIMAL(4,1), 활동지수 1~5 (F-05, F-07)
    private void validateRange(PetDTO pet) {
        if (pet.getWeight().signum() <= 0 || pet.getWeight().stripTrailingZeros().scale() > 1) {
            throw new IllegalArgumentException("무게는 0보다 크고 소수 첫째 자리까지만 입력할 수 있습니다.");
        }
        if (pet.getActivityLevel() < 1 || pet.getActivityLevel() > 5) {
            throw new IllegalArgumentException("활동지수는 1~5 사이여야 합니다.");
        }
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

    // 진행 중(대기·수행중) 주문이 있으면 true. 사용처: F-13 주문 등록 (BR-04 반려동물당 진행 중 주문 1건)
    public boolean hasActiveOrder(int petId) throws SQLException {
        return petDAO.countActiveOrders(petId) > 0;
    }

    // 회원의 반려동물 수. 사용처: F-04 회원 삭제 가능 여부 (BR-10)
    public int countByMemberId(int memberId) throws SQLException {
        return petDAO.countByMemberId(memberId);
    }
}
