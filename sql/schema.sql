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
-- 시터 (C)  member 1 : 0..1 sitter
--   - 이름·연락처는 member에만 두고 JOIN으로 가져온다 (3NF)
--   - member_id UNIQUE → 회원 1명당 시터 프로필 1개
--   - 제재 중 여부, 수행중 주문 수는 저장하지 않고 조회 시 계산한다
-- ---------------------------------------------------------
CREATE TABLE sitter (
                        sitter_id        INT       AUTO_INCREMENT PRIMARY KEY COMMENT '시터번호',
                        member_id        INT       NOT NULL                   COMMENT '회원번호',
                        price_per_km     INT       NOT NULL                   COMMENT 'km당 단가(원)',
                        price_per_min    INT       NOT NULL                   COMMENT '분당 단가(원)',
                        certified        BOOLEAN   NOT NULL DEFAULT FALSE     COMMENT '자격증 보유 여부',
                        max_capacity     TINYINT   NOT NULL DEFAULT 1         COMMENT '최대수용량 1~3',
                        suspended_until  DATETIME  NULL                       COMMENT '제재종료일시, NULL이면 제재 없음',
                        CONSTRAINT uq_sitter_member UNIQUE (member_id),
                        CONSTRAINT fk_sitter_member FOREIGN KEY (member_id) REFERENCES member (member_id)
                            ON DELETE RESTRICT,
                        CONSTRAINT chk_sitter_price_km  CHECK (price_per_km > 0),
                        CONSTRAINT chk_sitter_price_min CHECK (price_per_min > 0),
                        CONSTRAINT chk_sitter_capacity  CHECK (max_capacity BETWEEN 1 AND 3)
) COMMENT '시터';