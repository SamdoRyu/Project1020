package com.team3.petwalk.view.output;

import com.team3.petwalk.model.dto.MemberDTO;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemberOutputView {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void printMenu() {
        System.out.println("""

                [1. 회원 관리]
                1. 등록
                2. 목록·검색
                3. 수정
                4. 삭제
                0. 뒤로
                """);
    }

    public void printInvalidMenu() {
        System.out.println("메뉴 번호를 다시 입력해 주세요.");
    }

    // F-01 등록 결과
    public void printRegistered(int memberId) {
        System.out.println("회원이 등록되었습니다. (회원번호: " + memberId + ")");
    }

    // 업무 규칙 위반 안내 (NFR-07)
    public void printMessage(String message) {
        System.out.println(message);
    }

    // F-03 수정 전 현재 값 출력
    public void printMember(MemberDTO member) {
        System.out.println("현재 값 - 회원번호: " + member.getMemberId()
                + ", 이름: " + member.getName()
                + ", 연락처: " + member.getPhone());
    }

    // F-02 회원 목록 표. 결과가 없으면 안내 문구 (NFR-08)
    // TODO NFR-08: B의 OutputUtil이 나오면 공통 표 출력·날짜 형식으로 교체
    public void printMembers(List<MemberDTO> members) {
        if (members.isEmpty()) {
            System.out.println("조회 결과가 없습니다.");
            return;
        }
        String[] headers = {"회원번호", "이름", "연락처", "가입일"};
        List<String[]> rows = new ArrayList<>();
        for (MemberDTO m : members) {
            rows.add(new String[]{
                    String.valueOf(m.getMemberId()),
                    m.getName(),
                    m.getPhone(),
                    m.getCreatedAt().format(DATE_FORMAT)});
        }
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = displayWidth(headers[i]);
            for (String[] row : rows) {
                widths[i] = Math.max(widths[i], displayWidth(row[i]));
            }
        }
        printRow(headers, widths);
        for (String[] row : rows) {
            printRow(row, widths);
        }
    }

    private void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            sb.append(cells[i]).append(" ".repeat(widths[i] - displayWidth(cells[i]) + 2));
        }
        System.out.println(sb.toString().stripTrailing());
    }

    // 한글 등 전각 문자는 콘솔에서 2칸을 차지하므로 2로 센다
    private int displayWidth(String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            width += (c >= 0x1100) ? 2 : 1;
        }
        return width;
    }

    public void printStatisticsMenu() {
        System.out.println("""

                [5. 결제 통계]
                1. 시터별 수입
                2. 보호자별 지출
                0. 뒤로
                """);
    }
}
