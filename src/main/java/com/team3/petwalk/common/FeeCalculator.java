package com.team3.petwalk.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 결제금액 계산 (BR-01). 요금방식 쪽 실측값만 사용한다
public final class FeeCalculator {

    private FeeCalculator() {
    }

    // 거리 = 실측거리(km) × km당 단가, 원 미만 버림 / 시간 = 실측시간(분) × 분당 단가
    // distance_km는 DECIMAL(5,2)이므로 BigDecimal로 받는다 (rs.getBigDecimal, double이면 BigDecimal.valueOf)
    // appliedPrice는 주문에 복사해 둔 단가 (BR-02)
    public static int calculate(String feeType, BigDecimal distanceKm, int durationMin, int appliedPrice) {
        if (OrderConstants.FEE_TYPE_DISTANCE.equals(feeType)) {
            return distanceKm.multiply(BigDecimal.valueOf(appliedPrice))
                    .setScale(0, RoundingMode.DOWN)
                    .intValueExact();
        }
        if (OrderConstants.FEE_TYPE_TIME.equals(feeType)) {
            return durationMin * appliedPrice;
        }
        throw new IllegalArgumentException("알 수 없는 요금방식입니다: " + feeType);
    }
}
