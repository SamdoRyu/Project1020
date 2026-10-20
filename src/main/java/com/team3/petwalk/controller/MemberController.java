package com.team3.petwalk.controller;

import com.team3.petwalk.model.service.MemberService;
import com.team3.petwalk.view.input.MemberInputView;
import com.team3.petwalk.view.output.MemberOutputView;

import java.sql.SQLException;

// (A) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class MemberController {
    private final MemberInputView inputView = new MemberInputView();
    private final MemberOutputView outputView = new MemberOutputView();
    private final MemberService memberService = new MemberService();

    public void run() throws SQLException {
        while (true) {
            outputView.printMenu();
            switch (inputView.readMenu()) {
                case "1" -> register();           // F-01
                case "2" -> list();               // F-02
                case "3" -> update();             // F-03
                case "4" -> delete();             // F-04
                case "0" -> {
                    return;
                }
                default -> outputView.printInvalidMenu();
            }
        }
    }

    // F-01 회원 등록
    private void register() throws SQLException {
        String name = inputView.readName();
        String phone = inputView.readPhone();
        try {
            int memberId = memberService.register(name, phone);
            outputView.printRegistered(memberId);
        } catch (IllegalArgumentException e) {
            outputView.printMessage(e.getMessage()); // BR-11 연락처 중복: 안내 후 회원 관리 메뉴로 복귀
        }
    }

    // F-02 회원 목록·검색
    private void list() throws SQLException {
        String keyword = inputView.readSearchKeyword();
        outputView.printMembers(memberService.search(keyword));
    }

    // F-03 회원 수정
    private void update() {
        // TODO
    }

    // F-04 회원 삭제
    private void delete() {
        // TODO
    }

    // 메인 메뉴 5. 결제 통계 (A, 선택)
    public void runStatistics() {
        while (true) {
            outputView.printStatisticsMenu();
            switch (inputView.readMenu()) {
                case "1" -> sitterIncome();                 // F-17 시터별 수입
                case "2" -> guardianExpense();              // F-17 보호자별 지출
                case "0" -> {
                    return;
                }
                default -> outputView.printInvalidMenu();
            }
        }
    }

    // F-17 시터별 수입
    private void sitterIncome() {
        // TODO
    }

    // F-17 보호자별 지출
    private void guardianExpense() {
        // TODO
    }
}
