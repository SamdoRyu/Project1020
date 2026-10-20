-- =========================================================
-- 반려동물 산책 매칭 서비스 관리자 콘솔 - 스키마
-- MySQL 8.0 / utf8mb4
-- 처음부터 다시 실행해도 오류가 나지 않도록 자식 테이블부터 지우고 다시 만든다.
-- =========================================================

CREATE DATABASE IF NOT EXISTS pet_walk DEFAULT CHARACTER SET utf8mb4;
USE pet_walk;

-- FK가 걸려 있으므로 자식 → 부모 순서로 삭제
DROP TABLE IF EXISTS walk_order;
DROP TABLE IF EXISTS sitter;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS member;

-- ---------------------------------------------------------
-- 산책 주문 + 결제 (D)  pet 1 : N walk_order, sitter 1 : N walk_order
--   - 보호자 번호는 두지 않는다 (pet을 통해 알 수 있음, 3NF)
-- ---------------------------------------------------------
CREATE TABLE walk_order (
                            order_id       INT                                 AUTO_INCREMENT PRIMARY KEY COMMENT '주문번호',
                            pet_id         INT                                 NOT NULL COMMENT '동물번호',
                            sitter_id      INT                                 NOT NULL COMMENT '시터번호',
                            walk_at        DATETIME                            NOT NULL COMMENT '산책일시',
                            fee_type       ENUM('거리','시간')                 NOT NULL COMMENT '요금방식',
                            applied_price  INT                                 NOT NULL COMMENT '주문 시 복사한 단가(원)',
                            status         ENUM('대기','수행중','완료','취소') NOT NULL DEFAULT '대기' COMMENT '주문상태',
                            canceled_by    ENUM('보호자','시터')               NULL     COMMENT '취소 주체',
                            distance_km    DECIMAL(5,2)                        NULL     COMMENT '실측거리(km)',
                            duration_min   INT                                 NULL     COMMENT '실측시간(분)',
                            amount         INT                                 NULL     COMMENT '결제금액(원)',
                            paid_at        DATETIME                            NULL     COMMENT '결제일시',
                            created_at     DATETIME                            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문일시',
                            CONSTRAINT fk_order_pet    FOREIGN KEY (pet_id)    REFERENCES pet (pet_id)       ON DELETE RESTRICT,
                            CONSTRAINT fk_order_sitter FOREIGN KEY (sitter_id) REFERENCES sitter (sitter_id) ON DELETE RESTRICT,
                            CONSTRAINT chk_order_price    CHECK (applied_price > 0),
                            CONSTRAINT chk_order_distance CHECK (distance_km > 0),
                            CONSTRAINT chk_order_duration CHECK (duration_min > 0),
                            CONSTRAINT chk_order_amount   CHECK (amount >= 0),
    -- BR-08: 결제 정보는 완료 상태에서만, 취소 주체는 취소 상태에서만 값이 있다
                            CONSTRAINT chk_order_paid CHECK (
                                (status =  '완료' AND distance_km IS NOT NULL AND duration_min IS NOT NULL
                                    AND amount IS NOT NULL AND paid_at IS NOT NULL)
                                    OR (status <> '완료' AND amount IS NULL AND paid_at IS NULL)
                                ),
                            CONSTRAINT chk_order_cancel CHECK (
                                (status =  '취소' AND canceled_by IS NOT NULL)
                                    OR (status <> '취소' AND canceled_by IS NULL)
                                )
) COMMENT '산책 주문(결제 포함)';
