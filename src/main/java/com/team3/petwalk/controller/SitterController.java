package com.team3.petwalk.controller;

import com.team3.petwalk.view.input.SitterInputView;
import com.team3.petwalk.view.output.SitterOutputView;

// (C) 관리 메뉴 흐름만 담당한다. 업무 규칙 판단은 Service에서 (NFR-02)
public class SitterController {
    private final SitterInputView inputView = new SitterInputView();
    private final SitterOutputView outputView = new SitterOutputView();

    public void run() {
        while (true) {
            outputView.printMenu();
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
        }
    }

    // F-09 시터 등록
    private void register() {
        // TODO
    }

    // F-10 시터 목록 (1.전체 2.매칭 가능 시터만)
    private void list() {
        // TODO
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
