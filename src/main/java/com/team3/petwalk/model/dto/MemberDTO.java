package com.team3.petwalk.model.dto;

import java.time.LocalDateTime;

public class MemberDTO {
    private final int memberId;
    private final String name;
    private final String phone;
    private final LocalDateTime createdAt;

    public MemberDTO(int memberId, String name, String phone, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // F-17 결제 통계 한 줄: 시터번호(또는 회원번호), 이름, 완료 건수, 금액 합계
    public record StatisticsRow(int id, String name, int count, long total) {
    }
}
