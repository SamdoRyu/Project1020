# 반려동물 산책 매칭 서비스 (콘솔)

보호자와 산책 시터를 연결하고, 산책 주문과 정산을 관리하는 Java 콘솔 프로그램입니다.

## 팀원

| 이름 | GitHub | 역할 | 담당 기능 |
|---|---|---|---|
| (팀장 이름) | @SamdoRyu | 팀장 | (기획 후 작성) |
| (이름) | @ | 팀원 | |
| (이름) | @ | 팀원 | |
| (이름) | @ | 팀원 | |

## 개발 환경

- JDK 21
- MySQL 8.x
- MySQL Connector/J
- Git / GitHub

## 실행 방법

1. MySQL에서 `sql/schema.sql` → `sql/sample-data.sql` 순서로 실행합니다.
2. MySQL Connector/J를 클래스패스에 추가한 뒤 메인 클래스를 실행합니다.

## 폴더 구조

```
docs/   기획서, ERD
sql/    schema.sql(CREATE), sample-data.sql(INSERT)
src/    Java 소스 (MVC)
```

## 브랜치 규칙

- `main` : 발표용 완성본 (직접 push 금지)
- `develop` : 통합본 (PR로만 병합)
- `feature/기능-이름` : 개인 작업 브랜치 (예: `feature/member-ryu`)

작업이 끝나면 `feature → develop`으로 PR을 올리고, 팀원 1명 이상 승인 후 병합합니다.

## 커밋 메시지 규칙

`타입: 한글 설명` 형식으로 작성합니다.

| 타입 | 언제 | 예시 |
|---|---|---|
| feat | 기능 추가 | feat: 산책 주문 등록 기능 추가 |
| fix | 버그 수정 | fix: 정산 금액 null 오류 수정 |
| docs | 문서 수정 | docs: README 실행 방법 추가 |
| refactor | 동작 변화 없는 코드 정리 | refactor: OrderDAO 중복 코드 정리 |
| sql | SQL 파일 변경 | sql: order_pet 테이블 FK 추가 |
