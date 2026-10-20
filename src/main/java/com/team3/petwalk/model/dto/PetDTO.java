package com.team3.petwalk.model.dto;

import java.math.BigDecimal;

public class PetDTO {
    private int petId;
    private int memberId;
    private String name;
    private String breed;
    private BigDecimal weight;      // DECIMAL(4,1)
    private int activityLevel;      // 1~5
    private String memberName;      // 조회용, pet JOIN member (F-06)

    // 등록용 (petId는 DB AUTO_INCREMENT)
    public PetDTO(int memberId, String name, String breed, BigDecimal weight, int activityLevel) {
        this.memberId = memberId;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.activityLevel = activityLevel;
    }

    // 조회용 (보호자 이름 포함)
    public PetDTO(int petId, int memberId, String name, String breed, BigDecimal weight, int activityLevel,
                  String memberName) {
        this(memberId, name, breed, weight, activityLevel);
        this.petId = petId;
        this.memberName = memberName;
    }

    public int getPetId() {
        return petId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public int getActivityLevel() {
        return activityLevel;
    }
}
