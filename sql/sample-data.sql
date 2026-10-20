-- 시연용 샘플 데이터 (테이블마다 5행 이상)
-- schema.sql 실행 후 이 파일을 실행합니다. 부모 테이블 데이터부터 넣습니다.
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
USE pet_walk;
