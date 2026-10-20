package com.team3.petwalk.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// walk_order 한 행 + 목록 출력용 JOIN 값 (D)
public class WalkOrderDTO {
    // walk_order 컬럼
    private int orderId;
    private int petId;
    private int sitterId;
    private LocalDateTime walkAt;
    private String feeType;             // '거리' / '시간'
    private int appliedPrice;           // 주문 시 복사한 단가 (BR-02)
    private String status;              // '대기' / '수행중' / '완료' / '취소'
    private String canceledBy;          // 취소 상태에서만 값 있음 (BR-08)
    private BigDecimal distanceKm;      // 완료 상태에서만 값 있음
    private Integer durationMin;        // 완료 상태에서만 값 있음
    private Integer amount;             // 완료 상태에서만 값 있음 (BR-08)
    private LocalDateTime paidAt;       // 완료 상태에서만 값 있음 (BR-08)
    private LocalDateTime createdAt;

    // F-14 목록 출력용 (pet, member, sitter JOIN)
    private String petName;
    private int guardianId;             // 보호자 회원번호
    private String guardianName;
    private String sitterName;

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public int getSitterId() { return sitterId; }
    public void setSitterId(int sitterId) { this.sitterId = sitterId; }

    public LocalDateTime getWalkAt() { return walkAt; }
    public void setWalkAt(LocalDateTime walkAt) { this.walkAt = walkAt; }

    public String getFeeType() { return feeType; }
    public void setFeeType(String feeType) { this.feeType = feeType; }

    public int getAppliedPrice() { return appliedPrice; }
    public void setAppliedPrice(int appliedPrice) { this.appliedPrice = appliedPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCanceledBy() { return canceledBy; }
    public void setCanceledBy(String canceledBy) { this.canceledBy = canceledBy; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public Integer getDurationMin() { return durationMin; }
    public void setDurationMin(Integer durationMin) { this.durationMin = durationMin; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public int getGuardianId() { return guardianId; }
    public void setGuardianId(int guardianId) { this.guardianId = guardianId; }

    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }

    public String getSitterName() { return sitterName; }
    public void setSitterName(String sitterName) { this.sitterName = sitterName; }
}
