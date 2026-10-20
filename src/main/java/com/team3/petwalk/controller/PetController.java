package com.team3.petwalk.controller;

import com.team3.petwalk.model.dto.PetDTO;
import com.team3.petwalk.model.service.PetService;
import com.team3.petwalk.view.input.PetInputView;
import com.team3.petwalk.view.output.PetOutputView;

import java.sql.SQLException;
import java.util.List;

// (B) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class PetController {
    private final PetInputView inputView = new PetInputView();
    private final PetOutputView outputView = new PetOutputView();
    private final PetService petService = new PetService();

    public void run() {
        while (true) {
            outputView.printMenu();
            try {
                switch (inputView.readMenu()) {
                    case "1" -> register();           // F-05
                    case "2" -> list();               // F-06
                    case "3" -> update();             // F-07
                    case "4" -> delete();             // F-08
                    case "0" -> {
                        return;
                    }
                    default -> outputView.printInvalidMenu();
                }
            } catch (SQLException e) {
                // NFR-10: DB 오류는 안내 후 메인 메뉴로 복귀
                outputView.printDbError();
                return;
            }
        }
    }

    // F-05 반려동물 등록
    private void register() throws SQLException {
        PetDTO pet = inputView.readRegisterInfo();
        try {
            int petId = petService.register(pet);
            outputView.printRegistered(petId);
        } catch (IllegalArgumentException e) {
            // NFR-07: 걸린 조건 안내 후 관리 메뉴로 복귀
            outputView.printError(e.getMessage());
        }
    }

    // F-06 반려동물 목록 (1.전체 2.보호자별)
    private void list() throws SQLException {
        int type = inputView.readListType();
        try {
            List<PetDTO> pets = (type == 1)
                    ? petService.findAll()
                    : petService.findByMemberId(inputView.readMemberId());
            outputView.printList(pets);
        } catch (IllegalArgumentException e) {
            // NFR-07: 없는 회원번호 안내 후 관리 메뉴로 복귀
            outputView.printError(e.getMessage());
        }
    }

    // F-07 반려동물 수정
    private void update() throws SQLException {
        try {
            PetDTO current = petService.findById(inputView.readPetId());
            outputView.printCurrent(current);
            PetDTO changed = inputView.readUpdateInfo(current);
            petService.update(changed);
            outputView.printUpdated();
        } catch (IllegalArgumentException e) {
            // NFR-07: 없는 동물번호 안내 후 관리 메뉴로 복귀
            outputView.printError(e.getMessage());
        }
    }

    // F-08 반려동물 삭제
    private void delete() {
        // TODO
    }
}
