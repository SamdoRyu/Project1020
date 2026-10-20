package com.team3.petwalk.controller;

import com.team3.petwalk.model.dto.SitterDTO;
import com.team3.petwalk.model.service.SitterService;
import com.team3.petwalk.view.input.SitterInputView;
import com.team3.petwalk.view.output.SitterOutputView;

import java.sql.SQLException;
import java.util.List;

// (C) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class SitterController {
    private final SitterInputView inputView = new SitterInputView();
    private final SitterOutputView outputView = new SitterOutputView();
    private final SitterService sitterService = new SitterService();

    public void run() {
        while (true) {
            outputView.printMenu();
            try {
                switch (inputView.readMenu()) {
                    case "1" -> register();           // F-09
                    case "2" -> list();               // F-10
                    case "3" -> update();             // F-11
                    case "4" -> delete();             // F-12
                    case "0" -> {
                        return;
                    }
                    default -> outputView.printInvalidMenu();
                }
            } catch (IllegalArgumentException e) {
                // NFR-07: 걸린 조건을 안내하고 시터 관리 메뉴로 복귀
                outputView.printError(e.getMessage());
            } catch (SQLException e) {
                // NFR-10: 안내 후 메인 메뉴로 복귀
                outputView.printDbError();
                return;
            }
        }
    }

    // F-09 시터 등록. 회원번호를 먼저 확인한 뒤 나머지 값을 받는다
    private void register() throws SQLException {
        int memberId = inputView.readMemberId();
        sitterService.checkRegistrable(memberId);

        int pricePerKm = inputView.readPricePerKm();
        int pricePerMin = inputView.readPricePerMin();
        boolean certified = inputView.readCertified();
        int maxCapacity = inputView.readMaxCapacity();

        // 시터번호는 DB가 만들어 주므로 0, 신규 시터는 제재 없음(null)
        SitterDTO sitter = new SitterDTO(0, memberId, pricePerKm, pricePerMin, certified, maxCapacity, null);
        int sitterId = sitterService.register(sitter);
        outputView.printRegistered(sitterId);
    }

    // F-10 시터 목록 (1.전체 2.매칭 가능 시터만)
    private void list() throws SQLException {
        List<SitterDTO> sitters;
        if (inputView.readListOption() == 1) {
            sitters = sitterService.findAll();
        } else {
            sitters = sitterService.findAvailable();
        }
        outputView.printSitterList(sitters);
    }

    // F-11 시터 수정·제재 해제 (1.정보 수정 2.제재 해제)
    private void update() {
        // TODO
    }

    // F-12 시터 삭제
    private void delete() {
        // TODO
    }
}
