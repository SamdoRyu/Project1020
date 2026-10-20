package com.team3.petwalk.model.dto;

import java.time.LocalDateTime;

public class SitterDTO {
    private int sitterId;
    private int memberId;
    private int pricePerKm;
    private int pricePerMin;
    private boolean certified;
    private int maxCapacity;
    private LocalDateTime suspendedUntil;
    // member JOIN으로 가져오는 표시용 값 (F-10). sitter 테이블 컬럼 아님
    private String memberName;

    public SitterDTO(int sitterId, int memberId, int pricePerKm, int pricePerMin, boolean certified, int maxCapacity, LocalDateTime suspendedUntil) {
        this.sitterId = sitterId;
        this.memberId = memberId;
        this.pricePerKm = pricePerKm;
        this.pricePerMin = pricePerMin;
        this.certified = certified;
        this.maxCapacity = maxCapacity;
        this.suspendedUntil = suspendedUntil;
    }

    public int getSitterId() {
        return sitterId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getPricePerKm() {
        return pricePerKm;
    }

    public int getPricePerMin() {
        return pricePerMin;
    }

    public boolean isCertified() {
        return certified;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LocalDateTime getSuspendedUntil() {
        return suspendedUntil;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setSitterId(int sitterId) {
        this.sitterId = sitterId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setPricePerKm(int pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public void setPricePerMin(int pricePerMin) {
        this.pricePerMin = pricePerMin;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setSuspendedUntil(LocalDateTime suspendedUntil) {
        this.suspendedUntil = suspendedUntil;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "SitterDTO{" +
                "sitterId=" + sitterId +
                ", memberId=" + memberId +
                ", pricePerKm=" + pricePerKm +
                ", pricePerMin=" + pricePerMin +
                ", certified=" + certified +
                ", maxCapacity=" + maxCapacity +
                ", suspendedUntil=" + suspendedUntil +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}