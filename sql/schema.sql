-- 반려동물 산책 매칭 서비스 스키마
-- STEP 3(DB 모델링) 완료 후 CREATE TABLE 문을 작성합니다.
-- 처음부터 다시 실행해도 오류가 없도록 작성합니다.
CREATE DATABASE IF NOT EXISTS pet_walk DEFAULT CHARACTER SET utf8mb4;
USE pet_walk;

-- FK가 걸려 있으므로 자식 → 부모 순서로 삭제
DROP TABLE IF EXISTS walk_order;
DROP TABLE IF EXISTS sitter;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS member;

-- ---------------------------------------------------------
-- 회원 (A)
-- ---------------------------------------------------------
CREATE TABLE member (
                        member_id   INT          AUTO_INCREMENT PRIMARY KEY COMMENT '회원번호',
                        name        VARCHAR(20)  NOT NULL                   COMMENT '이름',
                        phone       VARCHAR(13)  NOT NULL                   COMMENT '연락처 010-0000-0000',
                        created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
                        CONSTRAINT uq_member_phone UNIQUE (phone),
                        CONSTRAINT chk_member_phone CHECK (phone REGEXP '^010-[0-9]{4}-[0-9]{4}$')
    ) COMMENT '회원';

-- ---------------------------------------------------------
-- 반려동물 (B)  member 1 : N pet
-- ---------------------------------------------------------
CREATE TABLE pet (
                     pet_id          INT          AUTO_INCREMENT PRIMARY KEY COMMENT '동물번호',
                     member_id       INT          NOT NULL                   COMMENT '보호자 회원번호',
                     name            VARCHAR(30)  NOT NULL                   COMMENT '이름',
                     breed           VARCHAR(30)  NOT NULL                   COMMENT '견종',
                     weight          DECIMAL(4,1) NOT NULL                   COMMENT '무게(kg)',
                     activity_level  TINYINT      NOT NULL                   COMMENT '활동지수 1~5',
                     CONSTRAINT fk_pet_member FOREIGN KEY (member_id) REFERENCES member (member_id)
                         ON DELETE RESTRICT,
                     CONSTRAINT chk_pet_weight   CHECK (weight > 0),
                     CONSTRAINT chk_pet_activity CHECK (activity_level BETWEEN 1 AND 5)
) COMMENT '반려동물';
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
