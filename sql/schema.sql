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