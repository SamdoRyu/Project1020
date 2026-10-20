package com.team3.petwalk.common;

// walk_order의 한글 ENUM 값. Java enum 매핑 없이 DB와 같은 문자열을 그대로 쓴다
public final class OrderConstants {

    // status (기본 '대기'). 대기 → 수행중 → 완료 순서로만, 취소는 대기에서만 (BR-05)
    public static final String STATUS_WAITING = "대기";
    public static final String STATUS_IN_PROGRESS = "수행중";
    public static final String STATUS_COMPLETED = "완료";
    public static final String STATUS_CANCELED = "취소";

    // fee_type
    public static final String FEE_TYPE_DISTANCE = "거리";
    public static final String FEE_TYPE_TIME = "시간";

    // canceled_by (취소 상태에서만 값 있음)
    public static final String CANCELED_BY_OWNER = "보호자";
    public static final String CANCELED_BY_SITTER = "시터";

    private OrderConstants() {
    }
}
