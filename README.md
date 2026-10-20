# 반려동물 산책 매칭 서비스 (콘솔)

보호자와 산책 시터를 연결하고, 산책 주문과 요금 정산을 관리하는 Java 콘솔 프로그램입니다.

## 팀원과 담당 기능

| 이름 | GitHub | 담당 테이블 | 담당 기능 |
|---|---|---|---|
| (팀장 이름) | @SamdoRyu | | |
| (이름) | @ | | |
| (이름) | @ | | |
| (이름) | @ | | |

## 기능 목록

기획 확정 후 `docs/기획서.md`의 기능 목록을 요약해 적습니다.

## 개발 환경

- JDK 21
- MySQL 8.x
- MySQL Connector/J
- Git / GitHub

## 실행 방법

1. MySQL Workbench에서 `sql/schema.sql` → `sql/sample-data.sql` 순서로 실행합니다.
2. `db.properties.example`을 복사해 **프로젝트 루트**에 `db.properties`를 만들고 `user`, `password`를 채웁니다.
3. MySQL Connector/J jar를 프로젝트 라이브러리에 등록합니다.
4. `src/Main.java`를 실행합니다.

## 폴더 구조

```
docs/   기획서(기능 목록·명세·메뉴 구조·테이블 정의서), ERD
sql/    schema.sql(CREATE), sample-data.sql(INSERT)
src/    Java 소스 (controller / model(dto·service·dao) / view(input·output) / common)
```

## 브랜치 규칙

- `main` : 발표용 완성본 (develop에서만 병합, v1.0 태그)
- `develop` : 통합본 (PR로만 병합)
- `feature/기능-이름` : 개인 작업 브랜치 (예: `feature/member-ryu`)

기능 1개가 완성될 때마다 `feature → develop`으로 PR을 올리고, 팀원 1명 이상 승인 후 병합합니다.

## 커밋 메시지 규칙

`타입: 한글 설명` 형식으로 작성합니다.

| 타입 | 언제 | 예시 |
|---|---|---|
| feat | 기능 추가 | feat: 산책 주문 등록 기능 추가 |
| fix | 버그 수정 | fix: 요금 계산 null 오류 수정 |
| docs | 문서 수정 | docs: README 실행 방법 추가 |
| refactor | 동작 변화 없는 코드 정리 | refactor: PetDAO 중복 코드 정리 |
| sql | SQL 파일 변경 | sql: order_pet 테이블 FK 추가 |
