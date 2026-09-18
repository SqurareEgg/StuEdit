-- =====================================================================
--  StuEdit 데이터베이스 초기화 스크립트
--  대상 DB : student_db (MySQL 8.0)
--  실행    : source /path/to/schema.sql
-- =====================================================================

-- 한글 인코딩 설정 (반드시 최상단)
SET NAMES utf8mb4;

-- 데이터베이스 생성 (없는 경우)
CREATE DATABASE IF NOT EXISTS student_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE student_db;

-- FK 체크 비활성화 → 순서 무관하게 DROP/CREATE 가능
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================================
--  테이블 DROP (순서 무관)
-- =====================================================================
DROP TABLE IF EXISTS TB_FILE;
DROP TABLE IF EXISTS TB_NOTICE;
DROP TABLE IF EXISTS TB_ATTEND;
DROP TABLE IF EXISTS TB_GRADE;
DROP TABLE IF EXISTS TB_USER;
DROP TABLE IF EXISTS TB_STUDENT;

-- =====================================================================
--  1. 학생 테이블 (TB_STUDENT)
-- =====================================================================
CREATE TABLE TB_STUDENT (
    student_id    INT           NOT NULL AUTO_INCREMENT COMMENT '학생 PK',
    student_num   VARCHAR(20)   NOT NULL                COMMENT '학번',
    student_name  VARCHAR(50)   NOT NULL                COMMENT '이름',
    dept_name     VARCHAR(100)  NOT NULL                COMMENT '학과명',
    grade         INT           NOT NULL DEFAULT 1      COMMENT '학년',
    phone         VARCHAR(20)       NULL                COMMENT '연락처',
    email         VARCHAR(100)      NULL                COMMENT '이메일',
    address       VARCHAR(200)      NULL                COMMENT '주소',
    gender        CHAR(1)           NULL                COMMENT '성별 (M/F)',
    birth_date    DATE              NULL                COMMENT '생년월일',
    reg_date      DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    del_yn        CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (student_id),
    UNIQUE KEY UK_STUDENT_NUM (student_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='학생 정보';

-- =====================================================================
--  2. 사용자 테이블 (TB_USER)
--     user_pw / use_yn  ← user_SQL.xml 컬럼명과 일치
-- =====================================================================
CREATE TABLE TB_USER (
    user_id         INT           NOT NULL AUTO_INCREMENT COMMENT '사용자 PK',
    login_id        VARCHAR(50)   NOT NULL                COMMENT '로그인 아이디',
    user_pw         VARCHAR(100)  NOT NULL                COMMENT 'BCrypt 비밀번호',
    user_name       VARCHAR(50)   NOT NULL                COMMENT '사용자명',
    email           VARCHAR(100)      NULL                COMMENT '이메일',
    phone           VARCHAR(20)       NULL                COMMENT '연락처',
    role            VARCHAR(20)   NOT NULL DEFAULT 'ROLE_STUDENT' COMMENT '권한',
    use_yn          CHAR(1)       NOT NULL DEFAULT 'Y'    COMMENT '사용여부',
    student_id      INT               NULL                COMMENT '연결 학생 ID',
    last_login_date DATETIME          NULL                COMMENT '최종 로그인 일시',
    reg_date        DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    PRIMARY KEY (user_id),
    UNIQUE KEY UK_LOGIN_ID (login_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='사용자 정보';

-- =====================================================================
--  3. 성적 테이블 (TB_GRADE)
-- =====================================================================
CREATE TABLE TB_GRADE (
    grade_id      INT           NOT NULL AUTO_INCREMENT COMMENT '성적 PK',
    student_id    INT           NOT NULL                COMMENT '학생 FK',
    subject_name  VARCHAR(100)  NOT NULL                COMMENT '과목명',
    semester      VARCHAR(20)   NOT NULL                COMMENT '학기 (예: 2024-1)',
    score         DECIMAL(5,2)  NOT NULL DEFAULT 0      COMMENT '점수 (0~100)',
    grade_point   VARCHAR(5)        NULL                COMMENT '학점 (A+, A ...)',
    credit        INT           NOT NULL DEFAULT 3      COMMENT '이수학점',
    reg_date      DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    del_yn        CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (grade_id),
    CONSTRAINT FK_GRADE_STUDENT FOREIGN KEY (student_id)
        REFERENCES TB_STUDENT (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='성적 정보';

-- =====================================================================
--  4. 출결 테이블 (TB_ATTEND)
-- =====================================================================
CREATE TABLE TB_ATTEND (
    attend_id     INT           NOT NULL AUTO_INCREMENT COMMENT '출결 PK',
    student_id    INT           NOT NULL                COMMENT '학생 FK',
    attend_date   DATE          NOT NULL                COMMENT '출결 날짜',
    attend_status VARCHAR(10)   NOT NULL                COMMENT '상태: 출석/지각/결석/공결',
    note          VARCHAR(200)      NULL                COMMENT '비고',
    reg_date      DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    del_yn        CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (attend_id),
    UNIQUE KEY UK_ATTEND_DATE (student_id, attend_date),
    CONSTRAINT FK_ATTEND_STUDENT FOREIGN KEY (student_id)
        REFERENCES TB_STUDENT (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='출결 정보';

-- =====================================================================
--  5. 공지사항 테이블 (TB_NOTICE)
-- =====================================================================
CREATE TABLE TB_NOTICE (
    notice_id    INT           NOT NULL AUTO_INCREMENT COMMENT '공지 PK',
    title        VARCHAR(200)  NOT NULL                COMMENT '제목',
    content      TEXT              NULL                COMMENT '내용',
    writer       VARCHAR(50)   NOT NULL                COMMENT '작성자',
    view_count   INT           NOT NULL DEFAULT 0      COMMENT '조회수',
    important_yn CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '중요공지여부',
    reg_date     DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    mod_date     DATETIME          NULL                COMMENT '수정일시',
    del_yn       CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (notice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='공지사항';

-- =====================================================================
--  6. 파일 테이블 (TB_FILE)
-- =====================================================================
CREATE TABLE TB_FILE (
    file_id       INT           NOT NULL AUTO_INCREMENT COMMENT '파일 PK',
    ref_id        INT           NOT NULL                COMMENT '참조 ID',
    ref_type      VARCHAR(20)   NOT NULL                COMMENT '참조 유형 (notice 등)',
    original_name VARCHAR(200)  NOT NULL                COMMENT '원본 파일명',
    saved_name    VARCHAR(200)  NOT NULL                COMMENT 'UUID 저장명',
    file_path     VARCHAR(300)  NOT NULL                COMMENT '저장 경로',
    file_size     BIGINT            NULL                COMMENT '파일 크기(bytes)',
    file_ext      VARCHAR(10)       NULL                COMMENT '확장자',
    reg_date      DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    PRIMARY KEY (file_id),
    INDEX IDX_FILE_REF (ref_id, ref_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='첨부파일';

-- =====================================================================
--  7. 1:1 문의 테이블 (TB_INQUIRY)
-- =====================================================================
CREATE TABLE TB_INQUIRY (
    inquiry_id   INT           NOT NULL AUTO_INCREMENT COMMENT '문의 PK',
    title        VARCHAR(200)  NOT NULL                COMMENT '제목',
    content      TEXT          NOT NULL                COMMENT '내용',
    writer_id    VARCHAR(50)   NOT NULL                COMMENT '작성자 login_id',
    writer_name  VARCHAR(50)   NOT NULL                COMMENT '작성자명',
    is_secret    CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '비밀글 여부',
    status       VARCHAR(10)   NOT NULL DEFAULT '대기'  COMMENT '상태(대기/답변완료)',
    answer       TEXT              NULL                COMMENT '답변 내용',
    answered_by  VARCHAR(50)       NULL                COMMENT '답변자 login_id',
    answer_date  DATETIME          NULL                COMMENT '답변 일시',
    reg_date     DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    upd_date     DATETIME          NULL                COMMENT '수정일시',
    del_yn       CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (inquiry_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='1:1 문의';

-- =====================================================================
--  8. 설문조사 테이블 (TB_SURVEY)
-- =====================================================================
CREATE TABLE TB_SURVEY (
    survey_id    INT           NOT NULL AUTO_INCREMENT COMMENT '설문 PK',
    title        VARCHAR(200)  NOT NULL                COMMENT '설문 제목',
    description  TEXT              NULL                COMMENT '설문 설명',
    start_date   DATE          NOT NULL                COMMENT '시작일',
    end_date     DATE          NOT NULL                COMMENT '종료일',
    status       VARCHAR(10)   NOT NULL DEFAULT '진행중' COMMENT '상태(진행중/종료)',
    reg_date     DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    del_yn       CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (survey_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='설문조사';

CREATE TABLE TB_SURVEY_QUESTION (
    question_id   INT           NOT NULL AUTO_INCREMENT COMMENT '문항 PK',
    survey_id     INT           NOT NULL                COMMENT '설문 FK',
    question_text VARCHAR(500)  NOT NULL                COMMENT '문항 내용',
    question_type VARCHAR(10)   NOT NULL DEFAULT 'single' COMMENT '유형(single/multi/text)',
    order_num     INT           NOT NULL DEFAULT 1      COMMENT '순서',
    PRIMARY KEY (question_id),
    CONSTRAINT FK_QUESTION_SURVEY FOREIGN KEY (survey_id) REFERENCES TB_SURVEY(survey_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='설문 문항';

CREATE TABLE TB_SURVEY_OPTION (
    option_id     INT           NOT NULL AUTO_INCREMENT COMMENT '선택지 PK',
    question_id   INT           NOT NULL                COMMENT '문항 FK',
    option_text   VARCHAR(200)  NOT NULL                COMMENT '선택지 내용',
    order_num     INT           NOT NULL DEFAULT 1      COMMENT '순서',
    PRIMARY KEY (option_id),
    CONSTRAINT FK_OPTION_QUESTION FOREIGN KEY (question_id) REFERENCES TB_SURVEY_QUESTION(question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='설문 선택지';

CREATE TABLE TB_SURVEY_RESPONSE (
    response_id  INT           NOT NULL AUTO_INCREMENT COMMENT '참여 PK',
    survey_id    INT           NOT NULL                COMMENT '설문 FK',
    user_id      INT           NOT NULL                COMMENT '참여 사용자 PK',
    reg_date     DATETIME      NOT NULL DEFAULT NOW()  COMMENT '참여 일시',
    PRIMARY KEY (response_id),
    UNIQUE KEY UK_SURVEY_USER (survey_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='설문 참여 기록';

CREATE TABLE TB_SURVEY_ANSWER (
    answer_id    INT           NOT NULL AUTO_INCREMENT COMMENT '답변 PK',
    response_id  INT           NOT NULL                COMMENT '참여 FK',
    question_id  INT           NOT NULL                COMMENT '문항 FK',
    option_id    INT               NULL                COMMENT '선택지 FK (선택형)',
    answer_text  TEXT              NULL                COMMENT '서술 답변 (텍스트형)',
    PRIMARY KEY (answer_id),
    CONSTRAINT FK_ANSWER_RESPONSE FOREIGN KEY (response_id) REFERENCES TB_SURVEY_RESPONSE(response_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='설문 답변';

-- =====================================================================
--  9. 학사 일정 테이블 (TB_ACADEMIC_SCHEDULE)
-- =====================================================================
CREATE TABLE TB_ACADEMIC_SCHEDULE (
    schedule_id   INT           NOT NULL AUTO_INCREMENT COMMENT '일정 PK',
    title         VARCHAR(200)  NOT NULL                COMMENT '일정 제목',
    content       TEXT              NULL                COMMENT '일정 내용',
    start_date    DATE          NOT NULL                COMMENT '시작일',
    end_date      DATE          NOT NULL                COMMENT '종료일',
    schedule_type VARCHAR(20)   NOT NULL DEFAULT '일반'  COMMENT '유형(일반/시험/방학/휴일)',
    reg_date      DATETIME      NOT NULL DEFAULT NOW()  COMMENT '등록일시',
    del_yn        CHAR(1)       NOT NULL DEFAULT 'N'    COMMENT '삭제여부',
    PRIMARY KEY (schedule_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='학사 일정';

-- =====================================================================
--  10. 챗봇 대화 기록 (TB_CHAT_LOG)
-- =====================================================================
CREATE TABLE TB_CHAT_LOG (
    chat_id      INT           NOT NULL AUTO_INCREMENT COMMENT '대화 PK',
    user_id      INT           NOT NULL                COMMENT '사용자 PK',
    user_message TEXT          NOT NULL                COMMENT '사용자 메시지',
    bot_response TEXT          NOT NULL                COMMENT '봇 응답',
    reg_date     DATETIME      NOT NULL DEFAULT NOW()  COMMENT '대화 일시',
    PRIMARY KEY (chat_id),
    INDEX IDX_CHAT_USER (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='챗봇 대화 기록';

-- FK 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
--  샘플 데이터
-- =====================================================================

-- ── 학생 데이터 ─────────────────────────────────────────────────────
INSERT INTO TB_STUDENT (student_num, student_name, dept_name, grade, phone, email, gender, birth_date) VALUES
('20210001', '홍길동',   '컴퓨터공학과',   3, '010-1234-5678', 'hong@example.com',  'M', '2002-03-15'),
('20210002', '김민지',   '정보통신공학과', 3, '010-2345-6789', 'kim@example.com',   'F', '2002-07-22'),
('20220001', '이준서',   '소프트웨어학과', 2, '010-3456-7890', 'lee@example.com',   'M', '2003-11-05'),
('20220002', '박소연',   '컴퓨터공학과',   2, '010-4567-8901', 'park@example.com',  'F', '2003-04-18'),
('20230001', '최우진',   '정보통신공학과', 1, '010-5678-9012', 'choi@example.com',  'M', '2004-09-30'),
('20230002', '정하린',   '소프트웨어학과', 1, '010-6789-0123', 'jung@example.com',  'F', '2004-01-12'),
('20210003', '강동현',   '컴퓨터공학과',   3, '010-7890-1234', 'kang@example.com',  'M', '2002-06-08'),
('20240001', '윤서아',   '정보통신공학과', 1, '010-8901-2345', 'yoon@example.com',  'F', '2005-02-25');

-- ※ 사용자(TB_USER) 계정은 Tomcat 기동 시 DataInitializer.java 가 자동 생성
--   admin / admin1234  (ROLE_ADMIN)
--   student01 / student1234  (ROLE_STUDENT)

-- ── 성적 데이터 ──────────────────────────────────────────────────────
INSERT INTO TB_GRADE (student_id, subject_name, semester, score, grade_point, credit) VALUES
(1, '자료구조',       '2024-1', 92.5, 'A',  3),
(1, '알고리즘',       '2024-1', 87.0, 'B+', 3),
(1, '운영체제',       '2024-2', 95.0, 'A+', 3),
(2, '데이터베이스',   '2024-1', 88.0, 'B+', 3),
(2, '네트워크',       '2024-2', 91.0, 'A',  3),
(3, '프로그래밍언어', '2024-1', 78.0, 'C+', 3);

-- ── 출결 데이터 ──────────────────────────────────────────────────────
INSERT INTO TB_ATTEND (student_id, attend_date, attend_status) VALUES
(1, CURDATE(),                         '출석'),
(2, CURDATE(),                         '출석'),
(3, CURDATE(),                         '지각'),
(4, CURDATE(),                         '결석'),
(5, CURDATE(),                         '출석'),
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '출석'),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '출석'),
(3, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '출석');

-- ── 공지사항 데이터 ───────────────────────────────────────────────────
INSERT INTO TB_NOTICE (title, content, writer, important_yn) VALUES
('[중요] 2025학년도 1학기 수강신청 안내',
 '2025학년도 1학기 수강신청이 시작됩니다.\n\n수강신청 기간: 2025년 2월 10일 ~ 2월 14일\n\n수강신청 전 반드시 이수 구분 및 학점 기준을 확인하시기 바랍니다.',
 '시스템관리자', 'Y'),
('도서관 이용시간 변경 안내',
 '도서관 운영 시간이 아래와 같이 변경됩니다.\n\n변경 전: 09:00 ~ 21:00\n변경 후: 08:00 ~ 22:00',
 '시스템관리자', 'N'),
('교내 Wi-Fi 점검 안내',
 '교내 Wi-Fi 시스템 정기 점검이 있을 예정입니다.\n\n점검 일시: 2025년 1월 25일(토) 02:00 ~ 06:00',
 '시스템관리자', 'N');

-- ── 학사 일정 데이터 ──────────────────────────────────────────────────
INSERT INTO TB_ACADEMIC_SCHEDULE (title, content, start_date, end_date, schedule_type) VALUES
('2025학년도 1학기 중간고사', '중간고사 기간입니다. 시험 일정을 확인하세요.', '2025-10-13', '2025-10-17', '시험'),
('2025학년도 1학기 기말고사', '기말고사 기간입니다. 성적 입력 기한도 확인하세요.', '2025-12-08', '2025-12-12', '시험'),
('여름방학', '2025년 여름방학 기간입니다.', '2025-07-21', '2025-08-22', '방학'),
('2025학년도 2학기 개강', '2025학년도 2학기 개강일입니다.', '2025-09-01', '2025-09-01', '일반'),
('광복절', '국가 공휴일 - 수업 없음', '2025-08-15', '2025-08-15', '휴일');

-- ── 결과 확인 ────────────────────────────────────────────────────────
SELECT '=== 테이블 생성 완료 ===' AS result;
SELECT TABLE_NAME, TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'student_db'
ORDER BY TABLE_NAME;
