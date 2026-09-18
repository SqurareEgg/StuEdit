# StuEdit — 학생정보 관리시스템

전자정부 표준프레임워크(eGovFrame) 4.x 기반의 학생 정보 통합 관리 웹 애플리케이션입니다.  
학생 CRUD, 성적·출결 관리, 공지사항 게시판, 파일 업로드, 엑셀 다운로드, 이메일 발송 기능을 제공합니다.

---

## 목차

1. [기술 스택](#기술-스택)
2. [프로젝트 구조](#프로젝트-구조)
3. [DB 테이블 구조](#db-테이블-구조)
4. [설치 및 실행](#설치-및-실행)
5. [초기 계정](#초기-계정)
6. [URL 맵핑 전체 목록](#url-맵핑-전체-목록)
7. [기능 상세](#기능-상세)
8. [보안 설계](#보안-설계)
9. [설정 파일 위치](#설정-파일-위치)
10. [주요 클래스 목록](#주요-클래스-목록)
11. [알려진 제약사항](#알려진-제약사항)

---

## 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| 프레임워크 | eGovFrame (전자정부 표준프레임워크) | 4.3.0 |
| 웹 프레임워크 | Spring MVC | 5.3.37 |
| 보안 | Spring Security | 5.8.13 |
| ORM | MyBatis (via eGovFrame psl.dataaccess) | — |
| 데이터베이스 | MySQL | 8.0 |
| 커넥션 풀 | Apache Commons DBCP2 | 2.9.0 |
| 뷰 레이아웃 | Apache Tiles | 3.0.8 |
| 엑셀 | Apache POI | 5.2.5 |
| 파일 업로드 | Commons FileUpload | 1.5 |
| 이메일 | Spring Mail + JavaMail | — |
| 로깅 | Log4j2 | 2.23.1 |
| 빌드 | Maven | — |
| 서버 | Apache Tomcat | 9.0.117 |
| Java | JDK 17 (Eclipse Adoptium) | 17.0.11 |
| 프론트엔드 | Bootstrap 5.3 + Bootstrap Icons 1.11 | CDN |

---

## 프로젝트 구조

```
StuEdit/
├── src/main/
│   ├── java/com/stuedit/
│   │   ├── web/                          # Controller 계층
│   │   │   ├── MainController.java       # 대시보드 /main.do
│   │   │   ├── StudentController.java    # 학생 CRUD /student/*.do
│   │   │   ├── UserController.java       # 로그인·마이페이지 /user/*.do
│   │   │   ├── GradeController.java      # 성적 관리 /grade/*.do
│   │   │   ├── AttendController.java     # 출결 관리 /attend/*.do
│   │   │   ├── NoticeController.java     # 공지사항 /notice/*.do
│   │   │   └── FileController.java       # 파일 /file/*.do
│   │   ├── service/                      # Service 인터페이스
│   │   │   ├── impl/                     # Service 구현체
│   │   │   │   ├── StudentServiceImpl.java
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── GradeServiceImpl.java
│   │   │   │   ├── AttendServiceImpl.java
│   │   │   │   ├── NoticeServiceImpl.java
│   │   │   │   ├── FileServiceImpl.java
│   │   │   │   └── MailServiceImpl.java
│   │   ├── dao/                          # DAO 계층 (MyBatis SqlSessionDaoSupport)
│   │   │   ├── AbstractBaseDAO.java      # 공통 부모 DAO
│   │   │   ├── StudentDAO.java
│   │   │   ├── UserDAO.java
│   │   │   ├── GradeDAO.java
│   │   │   ├── AttendDAO.java
│   │   │   ├── NoticeDAO.java
│   │   │   └── FileDAO.java
│   │   ├── vo/                           # Value Object
│   │   │   ├── StudentVO.java
│   │   │   ├── UserVO.java
│   │   │   ├── GradeVO.java
│   │   │   ├── AttendVO.java
│   │   │   ├── NoticeVO.java
│   │   │   └── FileVO.java
│   │   └── common/                       # 공통 유틸
│   │       ├── CustomUserDetailsService.java  # Spring Security 인증
│   │       ├── CustomLoginSuccessHandler.java # 로그인 성공 처리
│   │       └── DataInitializer.java          # 초기 계정 자동 생성
│   │
│   ├── resources/
│   │   └── egovframework/
│   │       ├── spring/                   # Spring 설정 파일
│   │       │   ├── context-datasource.xml
│   │       │   ├── context-common.xml
│   │       │   ├── context-sqlMap.xml
│   │       │   ├── context-transaction.xml
│   │       │   ├── context-security.xml
│   │       │   └── context-mail.xml
│   │       └── sqlmap/
│   │           ├── config/
│   │           │   └── sql-mapper-config.xml  # typeAlias 등록
│   │           └── mappers/              # SQL Mapper XML
│   │               ├── student_SQL.xml
│   │               ├── user_SQL.xml
│   │               ├── grade_SQL.xml
│   │               ├── attend_SQL.xml
│   │               ├── notice_SQL.xml
│   │               └── file_SQL.xml
│   │
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── web.xml                   # 서블릿 설정
│       │   ├── config/
│       │   │   ├── dispatcher-servlet.xml
│       │   │   └── tiles/
│       │   │       └── tiles-definitions.xml
│       │   └── jsp/
│       │       ├── common/               # 공통 JSP (레이아웃·헤더·사이드바·에러)
│       │       ├── student/              # 학생 JSP
│       │       ├── user/                 # 로그인·마이페이지 JSP
│       │       ├── grade/                # 성적 JSP
│       │       ├── attend/               # 출결 JSP
│       │       └── notice/               # 공지사항 JSP
│       └── resources/
│           ├── css/style.css             # 공통 CSS
│           └── js/common.js              # 공통 JS
└── pom.xml
```

---

## DB 테이블 구조

> 데이터베이스명: `student_db`

### TB_STUDENT (학생 정보)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| student_id | INT AUTO_INCREMENT PK | 학생 ID |
| student_num | VARCHAR(20) UNIQUE | 학번 |
| student_name | VARCHAR(50) | 이름 |
| dept_name | VARCHAR(100) | 학과명 |
| grade | TINYINT | 학년 (1~4) |
| phone | VARCHAR(20) | 연락처 |
| email | VARCHAR(100) | 이메일 |
| address | VARCHAR(200) | 주소 |
| gender | CHAR(1) | 성별 (M/F) |
| birth_date | DATE | 생년월일 |
| reg_date | DATETIME | 등록일 |
| del_yn | CHAR(1) DEFAULT 'N' | 소프트삭제 여부 |

### TB_USER (사용자 계정)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| user_id | INT AUTO_INCREMENT PK | 사용자 ID |
| login_id | VARCHAR(50) UNIQUE | 로그인 아이디 |
| user_pw | VARCHAR(100) | BCrypt 해시 비밀번호 |
| user_name | VARCHAR(50) | 사용자명 |
| role | VARCHAR(20) | 권한 (ROLE_ADMIN / ROLE_STUDENT) |
| phone | VARCHAR(20) | 연락처 |
| email | VARCHAR(100) | 이메일 |
| use_yn | CHAR(1) DEFAULT 'Y' | 사용여부 |
| student_id | INT | 연결된 학생 ID (FK, nullable) |
| reg_date | DATETIME | 등록일 |
| last_login_date | DATETIME | 마지막 로그인 일시 |

### TB_GRADE (성적)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| grade_id | INT AUTO_INCREMENT PK | 성적 ID |
| student_id | INT FK | 학생 ID |
| subject_name | VARCHAR(100) | 과목명 |
| semester | VARCHAR(10) | 학기 (예: 2024-1) |
| score | DECIMAL(5,2) | 점수 (0.00~100.00) |
| grade_point | VARCHAR(3) | 학점 (A+/A/B+/B/C+/C/D+/D/F) |
| credit | TINYINT | 학점수 (1~3) |
| reg_date | DATETIME | 등록일 |

### TB_ATTEND (출결)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| attend_id | INT AUTO_INCREMENT PK | 출결 ID |
| student_id | INT FK | 학생 ID |
| attend_date | DATE | 출결 날짜 |
| attend_status | VARCHAR(10) | 출결 상태 (출석/지각/결석/공결) |
| note | VARCHAR(200) | 비고 |
| reg_date | DATETIME | 등록일 |

### TB_NOTICE (공지사항)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| notice_id | INT AUTO_INCREMENT PK | 공지 ID |
| title | VARCHAR(200) | 제목 |
| content | TEXT | 내용 |
| writer | VARCHAR(50) | 작성자 (login_id) |
| view_count | INT DEFAULT 0 | 조회수 |
| important_yn | CHAR(1) DEFAULT 'N' | 중요공지 여부 |
| del_yn | CHAR(1) DEFAULT 'N' | 소프트삭제 여부 |
| reg_date | DATETIME | 등록일 |
| mod_date | DATETIME | 수정일 |

### TB_FILE (첨부파일)

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| file_id | INT AUTO_INCREMENT PK | 파일 ID |
| ref_id | INT | 참조 ID (공지 ID 등) |
| ref_type | VARCHAR(20) | 참조 타입 (NOTICE 등) |
| original_name | VARCHAR(255) | 원본 파일명 |
| saved_name | VARCHAR(255) | UUID 저장명 |
| file_path | VARCHAR(500) | 저장 경로 |
| file_size | BIGINT | 파일 크기 (bytes) |
| file_ext | VARCHAR(10) | 확장자 |
| reg_date | DATETIME | 등록일 |

---

## 설치 및 실행

### 사전 요건

- Java 17 (Eclipse Adoptium JDK 17)
- Apache Tomcat 9.0.x
- MySQL 8.0
- Maven 3.x
- Eclipse eGovFrame IDE 4.3.1 (또는 STS)

### 1. DB 설정

```sql
CREATE DATABASE student_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 위 6개 테이블 DDL 실행
-- (TB_STUDENT, TB_USER, TB_GRADE, TB_ATTEND, TB_NOTICE, TB_FILE)
```

### 2. DataSource 설정 변경

`src/main/resources/egovframework/spring/context-datasource.xml`

```xml
<property name="url"      value="jdbc:mysql://localhost:3306/student_db?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Seoul"/>
<property name="username" value="root"/>
<property name="password" value="YOUR_PASSWORD"/>  <!-- 실제 비밀번호로 변경 -->
```

### 3. 파일 업로드 경로 생성

```
C:\upload\studit\    (Windows)
/upload/studit/      (Linux)
```

`context-common.xml`의 `upload.path` 프로퍼티를 실제 경로로 수정합니다.

### 4. Maven 의존성 다운로드

```
Eclipse: 프로젝트 우클릭 → Maven → Update Project (Alt+F5)
```

### 5. Tomcat 서버 등록 후 실행

Eclipse Servers 뷰에서 Tomcat 9 추가 → StuEdit 프로젝트 Deploy → Start

### 6. 접속

```
http://localhost:8080/StuEdit/
```

---

## 초기 계정

애플리케이션 최초 기동 시 `DataInitializer`가 아래 계정을 자동 생성합니다.

| 구분 | 아이디 | 비밀번호 | 권한 |
|------|--------|----------|------|
| 관리자 | `admin` | `admin1234` | ROLE_ADMIN |
| 학생 | `student01` | `student1234` | ROLE_STUDENT |

> **주의**: 운영 환경에서는 반드시 비밀번호를 변경하세요.

---

## URL 맵핑 전체 목록

### 공통

| Method | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/main.do` | 대시보드 | 인증 필요 |
| GET | `/user/login.do` | 로그인 폼 | 전체 허용 |
| POST | `/user/loginProc.do` | 로그인 처리 (Security) | 전체 허용 |
| POST | `/user/logout.do` | 로그아웃 | 전체 허용 |
| GET | `/user/mypage.do` | 마이페이지 조회 | 인증 필요 |
| POST | `/user/mypageUpdate.do` | 연락처·이메일 수정 | 인증 필요 |
| GET | `/user/pwChange.do` | 비밀번호 변경 폼 | 인증 필요 |
| POST | `/user/pwChange.do` | 비밀번호 변경 처리 | 인증 필요 |

### 학생 관리 (ROLE_ADMIN 전용)

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/student/list.do` | 목록 (페이징+검색) |
| GET | `/student/detail.do?studentId={id}` | 상세 조회 |
| GET | `/student/insertForm.do` | 등록 폼 |
| POST | `/student/insert.do` | 등록 처리 |
| GET | `/student/updateForm.do?studentId={id}` | 수정 폼 |
| POST | `/student/update.do` | 수정 처리 |
| GET | `/student/delete.do?studentId={id}` | 소프트삭제 |
| GET | `/student/excelDownload.do` | 엑셀 다운로드 (.xlsx) |

### 성적 관리 (인증 필요 / 등록·수정·삭제는 ROLE_ADMIN)

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/grade/list.do` | 목록 (학생별 필터·학기 검색) |
| GET | `/grade/insertForm.do` | 등록 폼 |
| POST | `/grade/insert.do` | 등록 처리 |
| GET | `/grade/updateForm.do?gradeId={id}` | 수정 폼 |
| POST | `/grade/update.do` | 수정 처리 |
| GET | `/grade/delete.do?gradeId={id}` | 삭제 |

### 출결 관리 (인증 필요 / 등록·수정·삭제는 ROLE_ADMIN)

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/attend/list.do` | 목록 (기간·상태·학생 검색) |
| GET | `/attend/insertForm.do` | 등록 폼 |
| POST | `/attend/insert.do` | 등록 처리 |
| GET | `/attend/updateForm.do?attendId={id}` | 수정 폼 |
| POST | `/attend/update.do` | 수정 처리 |
| GET | `/attend/delete.do?attendId={id}` | 삭제 |

### 공지사항 (조회: 인증 필요 / 쓰기: ROLE_ADMIN)

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/notice/list.do` | 목록 (제목·내용·작성자 검색) |
| GET | `/notice/detail.do?noticeId={id}` | 상세 (이전글·다음글, 조회수 증가) |
| GET | `/notice/insertForm.do` | 등록 폼 |
| POST | `/notice/insert.do` | 등록 처리 |
| GET | `/notice/updateForm.do?noticeId={id}` | 수정 폼 |
| POST | `/notice/update.do` | 수정 처리 |
| GET | `/notice/delete.do?noticeId={id}` | 소프트삭제 |

### 파일

| Method | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | `/file/upload.do` | 파일 업로드 (Ajax) | ROLE_ADMIN |
| GET | `/file/download.do?fileId={id}` | 파일 다운로드 | 인증 필요 |
| GET | `/file/delete.do?fileId={id}` | 파일 삭제 | ROLE_ADMIN |

---

## 기능 상세

### 1. 학생 관리
- 이름·학번·학과명 기준 검색
- 페이지당 10/20/50건 선택 가능
- **소프트삭제**: `del_yn = 'Y'` 처리, 물리 삭제 없음
- **학번 중복 검사**: 등록 시 DB에서 실시간 확인
- **엑셀 다운로드**: 현재 검색 조건 반영, Apache POI .xlsx 형식

### 2. 성적 관리
- 학생별 필터 또는 전체 목록 조회
- **동일 과목+학기 중복 등록 방지** (비즈니스 예외 처리)
- 점수 입력 시 학점(A+~F) **자동 계산** (JavaScript)
- 학생별 **평균 점수·GPA·총 이수 학점** 카드 표시

### 3. 출결 관리
- 상태: 출석 / 지각 / 결석 / 공결
- 기간(시작일~종료일) + 상태 + 학생명/학번 복합 검색
- **동일 날짜 중복 등록 방지**
- 학생별 **출석률 통계** (총·출석·지각·결석·공결·출석률%)
- 대시보드에 **오늘 전체 출석률** 실시간 표시

### 4. 공지사항 게시판
- **중요 공지** 상단 고정 (황색 강조)
- 이전 글 / 다음 글 네비게이션
- **조회수 자동 증가** (상세 진입 시)
- XSS 방지: 내용 출력 시 `fn:escapeXml()` 적용
- 첨부파일 수 목록에 아이콘으로 표시

### 5. 파일 업로드/다운로드
- 저장명: UUID 기반 (원본명 별도 저장)
- 허용 확장자: `pdf`, `jpg`, `jpeg`, `png`, `gif`, `doc`, `docx`, `xls`, `xlsx`
- 최대 파일 크기: 10MB (MultipartResolver 설정)
- 다운로드 시 원본 파일명 복원, Content-Disposition 헤더 처리

### 6. 이메일 발송 (비동기)
- 학생 등록 완료 시 등록 학생 이메일로 HTML 메일 자동 발송
- `@Async` 비동기 처리 (메일 실패가 등록 트랜잭션에 영향 없음)
- Gmail SMTP (포트 587, STARTTLS) 기본 설정

### 7. 대시보드 (ROLE_ADMIN)
- 전체 학생 수 · 이번 달 신규 등록 수
- 공지사항 총 건수 · 오늘 전체 출석률
- 학과별 인원 및 비율 막대그래프
- 최근 등록 학생 5명 목록

---

## 보안 설계

### Spring Security 설정 (`context-security.xml`)

| 항목 | 설정값 |
|------|--------|
| 비밀번호 인코더 | BCryptPasswordEncoder |
| 로그인 URL | `/user/login.do` (GET) |
| 로그인 처리 URL | `/user/loginProc.do` (POST) |
| 로그아웃 URL | `/user/logout.do` (POST) |
| 로그아웃 성공 URL | `/user/login.do` |
| 세션 만료 URL | `/user/login.do?expired=true` |
| 동시 로그인 제한 | 1개 (max-sessions=1) |
| CSRF | 활성화 (`<sec:csrfInput/>` 폼 사용) |
| 인증 실패 URL | `/user/login.do?error=true` |

### 역할(Role) 권한 구조

```
ROLE_ADMIN  ─── 전체 기능 접근 가능
                └── 학생 관리 (CRUD + 엑셀)
                └── 성적/출결 쓰기 (등록·수정·삭제)
                └── 공지사항 쓰기 (등록·수정·삭제)
                └── 파일 업로드·삭제
                └── 관리자 대시보드 통계

ROLE_STUDENT ─── 로그인 후 조회 기능
                └── 성적 조회 (본인 studentId 기준)
                └── 출결 조회 (본인 studentId 기준)
                └── 공지사항 목록·상세 조회
                └── 파일 다운로드
                └── 마이페이지 (연락처·이메일 수정, 비밀번호 변경)
```

### XSS / SQL Injection 방지

- **XSS**: eGovFrame `HTMLTagFilter` 적용 (`*.do` 전체 요청)
- **SQL Injection**: MyBatis `#{}` 파라미터 바인딩만 사용 (`${}` 미사용)
- **공지 내용 출력**: `fn:escapeXml()` 적용

---

## 설정 파일 위치

| 파일 | 위치 | 역할 |
|------|------|------|
| `context-datasource.xml` | `resources/egovframework/spring/` | MySQL 연결, DBCP2 풀 |
| `context-common.xml` | 동일 | 컴포넌트 스캔, 비동기 실행기, 공통 프로퍼티 |
| `context-sqlMap.xml` | 동일 | MyBatis SqlSessionFactory/Template |
| `context-transaction.xml` | 동일 | AOP 선언적 트랜잭션 |
| `context-security.xml` | 동일 | Spring Security 전체 설정 |
| `context-mail.xml` | 동일 | JavaMailSenderImpl (Gmail SMTP) |
| `sql-mapper-config.xml` | `resources/egovframework/sqlmap/config/` | MyBatis typeAlias 6개 |
| `dispatcher-servlet.xml` | `webapp/WEB-INF/config/` | MVC 설정, Tiles, ViewResolver |
| `tiles-definitions.xml` | `webapp/WEB-INF/config/tiles/` | Tiles 뷰 정의 |
| `web.xml` | `webapp/WEB-INF/` | 서블릿 설정, 필터 체인 |

### 이메일 발송 설정 변경 (`context-common.xml`)

```xml
<value>
    mail.host=smtp.gmail.com
    mail.port=587
    mail.username=실제발신계정@gmail.com      <!-- 변경 필요 -->
    mail.password=앱비밀번호16자리             <!-- 변경 필요 -->
    mail.fromName=StuEdit 학생관리시스템
</value>
```

> Gmail 앱 비밀번호: [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords) 에서 생성

---

## 주요 클래스 목록

### Controller

| 클래스 | URL 패턴 | 주요 메서드 |
|--------|----------|-------------|
| `MainController` | `/main.do` | 대시보드 통계 데이터 조회 |
| `StudentController` | `/student/*.do` | CRUD + 엑셀 다운로드 |
| `UserController` | `/user/*.do` | 로그인폼·마이페이지·비번변경 |
| `GradeController` | `/grade/*.do` | CRUD + 학생별 GPA 계산 |
| `AttendController` | `/attend/*.do` | CRUD + 출석률 통계 |
| `NoticeController` | `/notice/*.do` | CRUD + 조회수 증가 |
| `FileController` | `/file/*.do` | 업로드·다운로드·삭제 |

### Service (핵심 비즈니스 로직)

| 클래스 | 주요 비즈니스 로직 |
|--------|-------------------|
| `StudentServiceImpl` | 학번 중복 검사, 등록 후 이메일 발송(@Async) |
| `UserServiceImpl` | BCrypt 암호화·검증, 비번 변경 시 현재 비번 확인 |
| `GradeServiceImpl` | 동일 과목+학기 중복 방지 |
| `AttendServiceImpl` | 동일 날짜 중복 방지 |
| `MailServiceImpl` | @Async 비동기 HTML 메일 발송 |

### DAO (MyBatis)

| 클래스 | SQL Namespace | SQL 종류 |
|--------|---------------|---------|
| `StudentDAO` | `studentSQL` | 9종 (목록/건수/상세/등록/수정/삭제/중복확인/대시보드/엑셀) |
| `UserDAO` | `userSQL` | 7종 (loginId조회/PK조회/등록/정보수정/비번변경/로그인시간/중복확인) |
| `GradeDAO` | `gradeSQL` | 8종 (목록/건수/상세/등록/수정/삭제/중복확인/평균학점/학기목록) |
| `AttendDAO` | `attendSQL` | 8종 (목록/건수/상세/등록/수정/삭제/중복확인/출석률/오늘출석률) |
| `NoticeDAO` | `noticeSQL` | 9종 (목록/건수/상세/조회수/등록/수정/삭제/이전글/다음글) |
| `FileDAO` | `fileSQL` | 5종 (등록/목록/단건/파일삭제/참조전체삭제) |

---

## 알려진 제약사항

| 항목 | 내용 |
|------|------|
| Eclipse IDE 경고 | EL 3.0 표현식(`?.`, 람다 등)은 Eclipse JSP 검증기 미지원 — 런타임에는 정상 동작 |
| 이메일 발송 | Gmail 앱 비밀번호 미설정 시 발송 실패 (등록 자체는 정상 처리됨) |
| 파일 업로드 경로 | `C:/upload/studit` 폴더 사전 생성 필요 |
| 동시 로그인 | 동일 계정으로 두 곳 동시 로그인 불가 (Security concurrency-control) |
| DB 비밀번호 | `context-datasource.xml`의 `1234`는 개발용 기본값 — 운영 시 반드시 변경 |

---

## 트러블슈팅 이력

| 오류 | 원인 | 해결 |
|------|------|------|
| `ClassNotFoundException: egovframework.rte.ptl.mvc.filter.HTMLTagFilter` | eGovFrame 4.x에서 패키지 변경 | `web.xml`에서 `org.egovframe.rte.ptl.mvc.filter.HTMLTagFilter`로 수정 |
| `ClassNotFoundException: egovframework.rte.ptl.mvc.handler.SimpleAnnotationExceptionHandlerResolver` | eGovFrame 4.x에서 클래스 제거 | `dispatcher-servlet.xml`에서 해당 Bean 선언 제거 |
| `UserController` 컴파일 오류 (RequestParam) | `@RequestParam` 앞 `@` 누락 | `@` 추가 및 import 보완 |
| `StudentServiceImpl.egovBizException()` 미정의 | `EgovAbstractServiceImpl` 미상속 | `throw new RuntimeException()`으로 교체 |
| `sidebar.jsp` EL Syntax Error (getContains) | Eclipse IDE가 EL 3.0 메서드 호출 미지원 | `fn:contains()` JSTL 함수로 교체 |
| `attendInsertForm.jsp` EL Syntax Error `[` | EL 3.0 배열 리터럴 `${[...]}` 미지원 | `<c:forTokens>` 으로 교체 |
| `gradeUpdateForm.jsp` EL Syntax Error `[` | 동일 | 동일 |
| `attendInsertForm.jsp` `&&`, `\|\|` 오류 | HTML 속성 내 EL `&&`/`\|\|`를 Eclipse가 HTML 속성명으로 오인 | `<c:set>`으로 사전 계산 후 `and`/`or` 사용 |
| `pwChange.jsp` `minlength` 미정의 | 구형 Eclipse JSP 검증기가 HTML5 속성 미인식 | 속성 제거 (JS로 검증) |

---

*최종 수정일: 2026-04-29*  
*eGovFrame 4.3.0 · Spring 5.3.37 · Spring Security 5.8.13 · MyBatis · MySQL 8.0*
