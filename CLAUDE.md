# 반려동물 산책 매칭 서비스 관리자 콘솔 — 팀 프로젝트 지침

요구사항 원문(기능 명세·업무 규칙·메뉴 구조 전부): @docs/요구사항_정의서_v0.4.md

## 이 프로젝트의 성격

- Java 부트캠프 4인 팀 과제. 하루 진행.
- 실제 서비스 개발이 아니다. 콘솔 메뉴에서 받은 입력이 View → Controller → Service → DAO를 거쳐 JDBC로 MySQL에 들어가고 나오는 과정을 연습하는 것이 목적이다.
- 반려동물 산책 매칭 서비스는 데이터 소재일 뿐이다. 프로그램 사용자는 관리자 1명이고 로그인 없이 바로 메인 메뉴가 뜬다.

## Claude가 지킬 작업 원칙

- 요구사항 문서에 없는 기능, 메뉴, 컬럼을 추가하지 않는다. 애매하면 구현 전에 물어본다.
- 제외 범위는 만들지 않는다: 회원가입·로그인(관리자 포함), 결제 연동, 파일 업로드, 이메일·문자 인증, GPS, 할증·할인 요금, 페이징.
- 하루 일정이므로 단순한 구현을 우선한다. 과한 추상화, 디자인 패턴, 라이브러리 추가를 하지 않는다.
- 커밋 메시지, PR, 주석에 기능 ID(F-xx)와 규칙 ID(BR-xx, NFR-xx)를 적는다.
- 담당 경계를 지킨다. 다른 팀원 테이블의 SQL을 직접 쓰지 않고, 그 담당자의 DAO·Service 메서드를 호출한다. 필요한 메서드가 없으면 만들지 말고 알려 준다.
- 작업 시작 시 사용자가 A~D 중 누구인지 모르면 먼저 물어본다.

## 기술 스택

- JDK 21, MySQL 8.0, MySQL Connector/J, 콘솔 입출력(Scanner)
- 문자셋 utf8mb4 (DB, 테이블, JDBC 접속 URL 모두)

## 계층 규칙 (NFR-02~05)

| 계층 | 하는 일 | 하지 않는 일 |
| --- | --- | --- |
| InputView / OutputView | 입력 받기, 출력하기 | 검증 로직, SQL |
| Controller | 메뉴 흐름, Service 호출, 결과를 View로 전달 | 업무 규칙 판단 |
| Service | 업무 규칙(BR) 검사, 계산, 트랜잭션 묶기 | System.out 출력, Scanner 입력 |
| DAO | SQL 실행만 | 업무 규칙 판단 |
| MainController | 메인 메뉴 분기만 | 그 외 전부 |

- 모든 쿼리는 PreparedStatement. 문자열 이어붙이기 금지.
- Connection·PreparedStatement·ResultSet은 try-with-resources.
- 트랜잭션은 F-16 시터 측 취소 한 곳: 주문 UPDATE + 시터 제재 UPDATE를 하나의 Connection에서 setAutoCommit(false) → commit(), 실패 시 rollback(). 그래서 C의 제재종료일 변경 메서드는 Connection을 매개변수로 받는다.

## 입력·출력 공통 규칙 (NFR-06~10)

- 형식 오류(숫자 자리에 문자, 날짜·연락처 형식, 범위 밖, 빈 값): 안내 후 그 항목만 다시 입력. 공통 InputUtil 사용.
- 업무 규칙 위반(없는 번호, 제재 중, 상태 순서 위반, 중복, 삭제 불가): 어떤 조건에 걸렸는지 안내 후 해당 관리 메뉴로 복귀.
- DB 오류: "데이터 처리 중 오류가 발생했습니다." 출력 후 메인 메뉴로 복귀. 프로그램을 종료하지 않는다.
- 출력은 공통 OutputUtil 사용: 금액 `15,000원`, 날짜 `yyyy-MM-dd HH:mm`, 목록은 열을 맞춘 표, 결과 없으면 "조회 결과가 없습니다."
- 회원번호·시터번호·동물번호·주문번호는 입력 안내와 표 열 이름에서 항상 구분해 쓴다. 회원번호와 시터번호는 다른 값이다.

## 데이터 핵심 (7장 요약)

- 테이블 4개: `member`, `pet`, `sitter`, `walk_order` (결제 정보는 walk_order에 포함)
- 회원에 역할 컬럼 없음. 반려동물이 있으면 보호자, sitter 행이 있으면 시터. 둘 다 가능.
- `sitter.sitter_id`는 별도 AUTO_INCREMENT, `sitter.member_id`는 UNIQUE.
- `walk_order`에 보호자 번호를 두지 않는다. 보호자는 pet을 통해 찾는다.
- FK는 모두 RESTRICT(기본값).
- 한글 값 ENUM. Java는 enum 매핑 없이 같은 문자열을 상수 클래스에 둔다.
  - status: '대기', '수행중', '완료', '취소' (기본 '대기')
  - fee_type: '거리', '시간'
  - canceled_by: '보호자', '시터' (취소 상태에서만 값 있음)
- 소수 자리: weight DECIMAL(4,1), distance_km DECIMAL(5,2).

## 자주 틀릴 업무 규칙

- 주문 상태는 대기 → 수행중 → 완료만. 취소는 대기에서만 (BR-05).
- 주문 등록 시 요금방식에 맞는 단가 하나를 applied_price에 복사 (BR-02).
- 요금: 거리 = distance_km × applied_price 원 미만 버림, 시간 = duration_min × applied_price (BR-01).
- 제재 검사는 주문 등록 때만. 이미 접수된 대기 주문은 제재 후에도 시작 가능 (BR-03).
- 시터 측 취소: 제재종료일 = 현재 + 3일, 기존 값이 더 늦으면 유지 (BR-06).
- 시작 시 수용량 검사: 수행중 주문 수 < max_capacity (BR-07). 산책일시와는 비교하지 않음 (BR-05).
- 시터가 그 반려동물의 보호자 본인이면 주문 불가 (BR-03).

## 담당 (8장 요약)

| 담당 | 테이블 | 기능 | 공통·지원 작업 |
| --- | --- | --- | --- |
| A | member | F-01~04, F-17 | Main, MainController, DBUtil, db.properties.example |
| B | pet | F-05~08 | InputUtil, OutputUtil, sample-data.sql |
| C | sitter | F-09~12 | schema.sql, 상수 클래스, FeeCalculator |
| D | walk_order | F-13~16 | 없음 (B·C가 제공하는 메서드 사용) |

- 다른 팀원에게 제공할 메서드 목록은 요구사항 정의서 8장 표를 따른다.
- 공통 작업(schema.sql, DBUtil)이 먼저 develop 브랜치에 병합되어야 나머지가 시작할 수 있다.

## 파일 위치 (정해진 것)

- `sql/schema.sql`, `sql/sample-data.sql`
- `common/DBUtil.java`, `db.properties.example` (실제 `db.properties`는 .gitignore)
- DTO: `MemberDTO`, `PetDTO`, `SitterDTO`, `WalkOrderDTO`
- 확정된 기획서는 `docs/기획서.md` 양식으로 옮길 예정

## 아직 정하지 않은 것 (임의로 정하지 말고 물어볼 것)

- 패키지명과 패키지 구조
- 팀원 간 제공 메서드의 이름과 매개변수 (공통 작업 때 결정)
- 팀원 A~D 실명 배정
- develop 외 브랜치 이름, PR 리뷰 규칙
