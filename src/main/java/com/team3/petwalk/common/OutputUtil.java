package com.team3.petwalk.common;

import java.util.List;

// (B) 공통 출력 (NFR-08). 금액·날짜 포맷은 #9에서 추가
public class OutputUtil {
    public static final String DB_ERROR = "데이터 처리 중 오류가 발생했습니다.";
    public static final String NO_RESULT = "조회 결과가 없습니다.";

    private OutputUtil() {
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    // 열을 맞춘 표 출력. 한글은 콘솔에서 2칸을 차지하므로 표시 너비로 맞춘다
    public static void printTable(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = displayWidth(headers[i]);
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], displayWidth(row[i]));
            }
        }

        printRow(headers, widths);
        StringBuilder line = new StringBuilder();
        for (int width : widths) {
            line.append("-".repeat(width)).append("  ");
        }
        System.out.println(line.toString().stripTrailing());
        for (String[] row : rows) {
            printRow(row, widths);
        }
    }

    private static void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            sb.append(cells[i]).append(" ".repeat(widths[i] - displayWidth(cells[i]))).append("  ");
        }
        System.out.println(sb.toString().stripTrailing());
    }

    // 한글(가~힣)은 2칸, 나머지는 1칸으로 센다
    private static int displayWidth(String text) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += (c >= '가' && c <= '힣') ? 2 : 1;
        }
        return width;
    }
}
