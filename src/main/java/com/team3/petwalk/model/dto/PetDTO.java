package com.team3.petwalk.model.dto;

import java.math.BigDecimal;

public class PetDTO {
    private int petId;
    private int memberId;
    private String name;
    private String breed;
    private BigDecimal weight;      // DECIMAL(4,1)
    private int activityLevel;      // 1~5

    // 등록용 (petId는 DB AUTO_INCREMENT)
    public PetDTO(int memberId, String name, String breed, BigDecimal weight, int activityLevel) {
        this.memberId = memberId;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.activityLevel = activityLevel;
    }

    public int getPetId() {
        return petId;
    }

    public int getMemberId() {
        return memberId;
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
