# StuEdit 시스템 ERD 및 소스코드 산출물

## 1. 시스템 개요

| 항목 | 내용 |
|------|------|
| 시스템명 | StuEdit (학생 정보 관리 시스템) |
| 프레임워크 | eGovFrame 4.3.1 / Spring MVC 4.x |
| 데이터베이스 | MySQL 8.0 (UTF8MB4) |
| ORM | MyBatis 3.x |
| 뷰 엔진 | Apache Tiles 3.x + JSP |
| 빌드 도구 | Maven |
| 서버 | Apache Tomcat 8.5+ |
| AI 연동 | Google Gemini 2.5 Flash API |

---

## 2. ERD (Entity-Relationship Diagram)

### 2.1 테이블 목록

| 테이블명 | 한글명 | 설명 |
|----------|--------|------|
| TB_STUDENT | 학생 정보 | 학생 기본 정보 관리 |
| TB_USER | 사용자 계정 | 로그인 및 권한 관리 |
| TB_GRADE | 성적 정보 | 과목별 성적 관리 |
| TB_ATTEND | 출결 정보 | 일자별 출결 현황 |
| TB_NOTICE | 공지사항 | 공지 게시물 관리 |
| TB_FILE | 첨부파일 | 파일 업로드 관리 |
| TB_INQUIRY | 1:1 문의 | 학생-관리자 문의 |
| TB_SURVEY | 설문조사 | 설문 정보 관리 |
| TB_SURVEY_QUESTION | 설문 질문 | 설문별 질문 항목 |
| TB_SURVEY_OPTION | 설문 선택지 | 질문별 응답 옵션 |
| TB_SURVEY_RESPONSE | 설문 참여 | 설문 제출 이력 |
| TB_SURVEY_ANSWER | 설문 응답 | 개별 응답 내용 |
| TB_ACADEMIC_SCHEDULE | 학사 일정 | 학사 캘린더 관리 |
| TB_CHAT_LOG | 챗봇 대화 | AI 챗봇 대화 이력 |

---

### 2.2 테이블 상세 정의

#### TB_STUDENT (학생 정보)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| student_id | INT | PK, AUTO_INCREMENT | 학생 고유 ID |
| student_num | VARCHAR(20) | UNIQUE, NOT NULL | 학번 |
| student_name | VARCHAR(50) | NOT NULL | 이름 |
| dept_name | VARCHAR(100) | NOT NULL | 학과명 |
| grade | INT | NOT NULL | 학년 (1~4) |
| phone | VARCHAR(20) | | 연락처 |
| email | VARCHAR(100) | | 이메일 |
| address | VARCHAR(200) | | 주소 |
| gender | CHAR(1) | | 성별 (M/F) |
| birth_date | DATE | | 생년월일 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |
| del_yn | CHAR(1) | DEFAULT 'N' | 삭제여부 |

#### TB_USER (사용자 계정)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| user_id | INT | PK, AUTO_INCREMENT | 사용자 고유 ID |
| login_id | VARCHAR(50) | UNIQUE, NOT NULL | 로그인 아이디 |
| user_pw | VARCHAR(255) | NOT NULL | 비밀번호 (BCrypt 암호화) |
| user_name | VARCHAR(50) | NOT NULL | 사용자명 |
| role | VARCHAR(20) | NOT NULL | 권한 (ROLE_ADMIN / ROLE_STUDENT) |
| phone | VARCHAR(20) | | 연락처 |
| email | VARCHAR(100) | | 이메일 |
| student_id | INT | FK(TB_STUDENT) | 연결 학생 ID (학생 계정만) |
| use_yn | CHAR(1) | DEFAULT 'Y' | 사용여부 |
| reg_date | DATETIME | DEFAULT NOW() | 가입일시 |
| last_login_date | DATETIME | | 최근 로그인 일시 |

#### TB_GRADE (성적 정보)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| grade_id | INT | PK, AUTO_INCREMENT | 성적 고유 ID |
| student_id | INT | FK(TB_STUDENT), NOT NULL | 학생 ID |
| subject_name | VARCHAR(100) | NOT NULL | 과목명 |
| semester | VARCHAR(20) | NOT NULL | 학기 (예: 2024-1) |
| score | DECIMAL(5,2) | | 점수 (0.00 ~ 100.00) |
| grade_point | VARCHAR(5) | | 등급 (A+, A, B+, ...) |
| credit | INT | | 학점 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |
| del_yn | CHAR(1) | DEFAULT 'N' | 삭제여부 |

> **UNIQUE 제약:** (student_id, subject_name, semester) — 동일 학기 동일 과목 중복 입력 방지

#### TB_ATTEND (출결 정보)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| attend_id | INT | PK, AUTO_INCREMENT | 출결 고유 ID |
| student_id | INT | FK(TB_STUDENT), NOT NULL | 학생 ID |
| attend_date | DATE | NOT NULL | 출결 일자 |
| attend_status | VARCHAR(10) | NOT NULL | 상태 (출석/지각/결석/공결) |
| note | VARCHAR(200) | | 비고 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |
| del_yn | CHAR(1) | DEFAULT 'N' | 삭제여부 |

> **UNIQUE 제약:** (student_id, attend_date) — 동일 학생 동일 날짜 중복 입력 방지

#### TB_NOTICE (공지사항)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| notice_id | INT | PK, AUTO_INCREMENT | 공지 고유 ID |
| title | VARCHAR(200) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| writer | VARCHAR(50) | NOT NULL | 작성자 |
| view_count | INT | DEFAULT 0 | 조회수 |
| important_yn | CHAR(1) | DEFAULT 'N' | 중요공지 여부 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |
| mod_date | DATETIME | | 수정일시 |
| del_yn | CHAR(1) | DEFAULT 'N' | 삭제여부 |

#### TB_FILE (첨부파일)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| file_id | INT | PK, AUTO_INCREMENT | 파일 고유 ID |
| ref_id | INT | NOT NULL | 참조 게시물 ID |
| ref_type | VARCHAR(20) | NOT NULL | 참조 유형 (NOTICE 등) |
| original_name | VARCHAR(255) | NOT NULL | 원본 파일명 |
| saved_name | VARCHAR(255) | NOT NULL | 저장 파일명 (UUID) |
| file_path | VARCHAR(500) | NOT NULL | 저장 경로 |
| file_size | BIGINT | | 파일 크기 (bytes) |
| file_ext | VARCHAR(20) | | 확장자 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |

#### TB_INQUIRY (1:1 문의)
| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| inquiry_id | INT | PK, AUTO_INCREMENT | 문의 고유 ID |
| title | VARCHAR(200) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| writer_id | INT | FK(TB_USER) | 작성자 ID |
| writer_name | VARCHAR(50) | | 작성자명 |
| is_secret | CHAR(1) | DEFAULT 'N' | 비밀글 여부 |
| status | VARCHAR(20) | DEFAULT '대기' | 처리 상태 (대기/답변완료) |
| answer | TEXT | | 관리자 답변 내용 |
| answered_by | VARCHAR(50) | | 답변자 |
| answer_date | DATETIME | | 답변일시 |
| reg_date | DATETIME | DEFAULT NOW() | 등록일시 |
| upd_date | DATETIME | | 수정일시 |
| del_yn | CHAR(1) | DEFAULT 'N' | 삭제여부 |

#### TB_SURVEY / TB_SURVEY_QUESTION / TB_SURVEY_OPTION / TB_SURVEY_RESPONSE / TB_SURVEY_ANSWER (설문조사)
| 테이블 | 주요 컬럼 | 설명 |
|--------|----------|------|
| TB_SURVEY | survey_id, title, description, start_date, end_date, status, del_yn | 설문 기본 정보 |
| TB_SURVEY_QUESTION | question_id, survey_id(FK), question_text, question_type(single/multi/text), order_num | 설문 질문 |
| TB_SURVEY_OPTION | option_id, question_id(FK), option_text, order_num | 선택지 |
| TB_SURVEY_RESPONSE | response_id, survey_id(FK), user_id(FK), reg_date | 참여 기록 (UNIQUE: survey_id+user_id) |
| TB_SURVEY_ANSWER | answer_id, response_id(FK), question_id(FK), option_id(FK), answer_text | 개별 응답 |

#### TB_ACADEMIC_SCHEDULE (학사 일정)
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| schedule_id | INT PK | 일정 고유 ID |
| title | VARCHAR(200) | 일정명 |
| content | TEXT | 상세 내용 |
| start_date | DATE | 시작일 |
| end_date | DATE | 종료일 |
| schedule_type | VARCHAR(20) | 유형 (일반/시험/방학/휴일) |
| reg_date | DATETIME | 등록일시 |
| del_yn | CHAR(1) | 삭제여부 |

#### TB_CHAT_LOG (챗봇 대화 이력)
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| chat_id | INT PK | 대화 고유 ID |
| user_id | INT FK(TB_USER) | 사용자 ID |
| user_message | TEXT | 사용자 입력 메시지 |
| bot_response | TEXT | AI 응답 메시지 |
| reg_date | DATETIME | 대화 일시 |

---

### 2.3 테이블 관계도 (텍스트 ERD)

```
TB_USER (1) ──────────── (1) TB_STUDENT
    │                           │
    │ (1:N)                  (1:N)│ (1:N)
    │                           │    │
TB_INQUIRY              TB_GRADE  TB_ATTEND
TB_CHAT_LOG
TB_SURVEY_RESPONSE

TB_NOTICE (1) ─── (1:N) ─── TB_FILE (ref_type='NOTICE')

TB_SURVEY (1)
    └─ (1:N) ─ TB_SURVEY_QUESTION (1)
                    └─ (1:N) ─ TB_SURVEY_OPTION
TB_SURVEY_RESPONSE (1)
    └─ (1:N) ─ TB_SURVEY_ANSWER
```

---

## 3. 소스코드 구조

### 3.1 패키지 구조

```
StuEdit/
└── src/main/
    ├── java/com/stuedit/
    │   ├── web/                    # MVC 컨트롤러 (11개)
    │   │   ├── MainController.java
    │   │   ├── StudentController.java
    │   │   ├── UserController.java
    │   │   ├── GradeController.java
    │   │   ├── AttendController.java
    │   │   ├── NoticeController.java
    │   │   ├── FileController.java
    │   │   ├── InquiryController.java
    │   │   ├── SurveyController.java
    │   │   ├── ScheduleController.java
    │   │   └── ChatController.java
    │   ├── service/                # 서비스 인터페이스 (11개)
    │   │   └── impl/               # 서비스 구현체 (11개)
    │   ├── dao/                    # 데이터 접근 객체 (10개)
    │   │   └── AbstractBaseDAO.java
    │   ├── vo/                     # 값 객체 (14개)
    │   └── common/                 # 공통 컴포넌트
    │       ├── CustomUserDetailsService.java
    │       ├── CustomLoginSuccessHandler.java
    │       ├── GlobalExceptionHandler.java
    │       └── DataInitializer.java
    ├── resources/
    │   ├── egovframework/
    │   │   ├── spring/             # Spring 설정 XML (6개)
    │   │   └── sqlmap/mappers/     # MyBatis SQL 매퍼 (9개)
    │   ├── schema.sql              # DB 스키마 DDL
    │   ├── dummy-data.sql          # 샘플 데이터
    │   └── gemini.properties       # AI API 설정
    └── webapp/
        ├── WEB-INF/
        │   ├── jsp/                # JSP 뷰 (30+개)
        │   └── config/
        │       ├── dispatcher-servlet.xml
        │       └── tiles-definitions.xml
        └── resources/              # 정적 자원 (CSS, JS)
```

### 3.2 컨트롤러 URL 매핑

| 컨트롤러 | URL 패턴 | 주요 기능 |
|----------|----------|----------|
| MainController | `/main.do` | 대시보드 통계 |
| StudentController | `/student/*` | 학생 CRUD, Excel 다운로드 |
| UserController | `/user/*` | 로그인 폼, 마이페이지, 비밀번호 변경 |
| GradeController | `/grade/*` | 성적 CRUD, GPA 계산 |
| AttendController | `/attend/*` | 출결 CRUD, 통계 |
| NoticeController | `/notice/*` | 공지 CRUD, 파일 첨부 |
| FileController | `/file/*` | 파일 업로드/다운로드 |
| InquiryController | `/inquiry/*` | 1:1 문의, 관리자 답변 |
| SurveyController | `/survey/*` | 설문 CRUD, 참여, 결과 |
| ScheduleController | `/schedule/*` | 학사 일정 CRUD |
| ChatController | `/chatbot/*` | AI 챗봇 |

### 3.3 스프링 설정 파일 목록

| 파일명 | 위치 | 역할 |
|--------|------|------|
| context-datasource.xml | egovframework/spring/ | DB 커넥션 풀 설정 |
| context-sqlMap.xml | egovframework/spring/ | MyBatis SqlSession 설정 |
| context-transaction.xml | egovframework/spring/ | 트랜잭션 관리 |
| context-security.xml | egovframework/spring/ | Spring Security 설정 |
| context-mail.xml | egovframework/spring/ | 이메일(SMTP) 설정 |
| context-common.xml | egovframework/spring/ | 공통 빈, RestTemplate, 비동기 |
| dispatcher-servlet.xml | WEB-INF/config/ | MVC 설정, 파일 업로드, 뷰 리졸버 |
| tiles-definitions.xml | WEB-INF/config/ | Tiles 레이아웃 정의 |

### 3.4 MyBatis SQL 매퍼 목록

| 파일명 | 네임스페이스 | 주요 쿼리 수 |
|--------|------------|------------|
| student_SQL.xml | studentSQL | 13개 |
| user_SQL.xml | userSQL | 7개 |
| grade_SQL.xml | gradeSQL | 9개 |
| attend_SQL.xml | attendSQL | 9개 |
| notice_SQL.xml | noticeSQL | 10개 |
| file_SQL.xml | fileSQL | 5개 |
| inquiry_SQL.xml | inquirySQL | 8개 |
| survey_SQL.xml | surveySQL | 14개 |
| schedule_SQL.xml | scheduleSQL | 7개 |
| chatlog_SQL.xml | chatlogSQL | 2개 |

---

## 4. 배포 준비

### 4.1 사전 요구 사항

| 항목 | 버전 | 비고 |
|------|------|------|
| JDK | 1.8 이상 | Java 11 권장 |
| Apache Tomcat | 8.5 이상 | |
| MySQL | 8.0 이상 | |
| Maven | 3.6 이상 | |

### 4.2 빌드 절차

```bash
# 1. 프로젝트 루트 디렉토리로 이동
cd StuEdit

# 2. Maven 빌드 (WAR 파일 생성)
mvn clean package

# 3. 결과물 위치
# target/StuEdit.war
```

### 4.3 DB 초기화 절차

```sql
-- 1. 데이터베이스 생성
CREATE DATABASE student_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 스키마 생성 (전체 테이블 DDL)
SOURCE src/main/resources/schema.sql;

-- 3. 샘플 데이터 적재 (선택)
SOURCE src/main/resources/dummy-data.sql;
```

### 4.4 환경 설정 파일 수정

**① DB 접속 정보** (`context-datasource.xml`)
```xml
<property name="url" value="jdbc:mysql://[DB_HOST]:3306/student_db?useSSL=false&amp;characterEncoding=UTF-8"/>
<property name="username" value="[DB_USER]"/>
<property name="password" value="[DB_PASSWORD]"/>
```

**② 파일 업로드 경로** (`context-common.xml` 또는 properties)
```
upload.path=C:/upload/studit
```
> 업로드 디렉토리는 사전에 생성 필요

**③ Gemini API 키** (`gemini.properties`)
```properties
gemini.api.key=YOUR_GEMINI_API_KEY
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
```

**④ 이메일 설정** (`context-mail.xml`)
```xml
<property name="username" value="[GMAIL_ADDRESS]"/>
<property name="password" value="[APP_PASSWORD]"/>
```

### 4.5 Tomcat 배포

```
1. target/StuEdit.war 를 $TOMCAT_HOME/webapps/ 에 복사
2. Tomcat 시작 → 자동으로 WAR 압축 해제 및 배포
3. DataInitializer가 기본 계정 자동 생성:
   - 관리자: admin / admin1234
   - 학생: student01 / student1234
4. 브라우저에서 http://[서버주소]:[포트]/StuEdit 접속 확인
```

### 4.6 배포 체크리스트

- [ ] JDK 1.8+ 설치 확인
- [ ] Tomcat 8.5+ 설치 확인
- [ ] MySQL 8.0 설치 및 student_db 생성
- [ ] schema.sql 실행 완료
- [ ] context-datasource.xml DB 접속 정보 수정
- [ ] 파일 업로드 디렉토리 생성 및 권한 설정
- [ ] gemini.properties API 키 설정 (챗봇 사용 시)
- [ ] mvn clean package 빌드 성공
- [ ] StuEdit.war Tomcat webapps 배포
- [ ] 브라우저 접속 테스트 (로그인 확인)
- [ ] 기본 계정 비밀번호 변경 (운영 환경)
