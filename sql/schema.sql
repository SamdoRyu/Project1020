-- 반려동물 산책 매칭 서비스 스키마
-- STEP 3(DB 모델링) 완료 후 CREATE TABLE 문을 작성합니다.
-- 처음부터 다시 실행해도 오류가 없도록 작성합니다.
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
CREATE DATABASE IF NOT EXISTS pet_walk DEFAULT CHARACTER SET utf8mb4;
USE pet_walk;
