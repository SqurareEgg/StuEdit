SET NAMES utf8mb4;
USE student_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE TB_ATTEND;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
-- TB_ATTEND 출결 더미 데이터
-- 기간: 2025-03-03 ~ 2025-05-30 (평일만, 약 65일)
--
-- 출결 패턴 (student_id % 10 기준):
--   9 → [위험]   결석 30% + 지각 20%  → 환산결석 ~24회 > 허용치(21.7) → F/수강취소 위험
--   8 → [주의]   결석 10% + 지각 20%  → 출석률 ~70% < 75%
--   7 → [공결多] 공결 10%             → 출석률 정상, 공결 다수
--   기타 → [양호] 결석/지각 각 3.3%   → 출석률 ~93%
-- =====================================================================
INSERT INTO TB_ATTEND (student_id, attend_date, attend_status)
WITH RECURSIVE cal AS (
    SELECT DATE('2025-03-03') AS d
    UNION ALL
    SELECT DATE_ADD(d, INTERVAL 1 DAY) FROM cal WHERE d < DATE('2025-05-30')
)
SELECT
    s.student_id,
    cal.d,
    CASE
        /* ── 위험 그룹 (% 10 = 9): 결석 30%, 지각 20%, 출석 50% ── */
        WHEN s.student_id % 10 = 9
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 10 < 3 THEN '결석'
        WHEN s.student_id % 10 = 9
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 10 < 5 THEN '지각'

        /* ── 주의 그룹 (% 10 = 8): 결석 10%, 지각 20%, 출석 70% ── */
        WHEN s.student_id % 10 = 8
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 10 < 1 THEN '결석'
        WHEN s.student_id % 10 = 8
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 10 < 3 THEN '지각'

        /* ── 공결多 그룹 (% 10 = 7): 공결 10%, 출석 90% ── */
        WHEN s.student_id % 10 = 7
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 10 < 1 THEN '공결'

        /* ── 양호 그룹 (그 외): 결석 3.3%, 지각 3.3%, 공결 3.3%, 출석 90% ── */
        WHEN s.student_id % 10 NOT IN (7, 8, 9)
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 30 < 1 THEN '결석'
        WHEN s.student_id % 10 NOT IN (7, 8, 9)
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 30 < 2 THEN '지각'
        WHEN s.student_id % 10 NOT IN (7, 8, 9)
             AND CRC32(CONCAT(s.student_id, '_', cal.d)) % 30 < 3 THEN '공결'

        ELSE '출석'
    END AS attend_status
FROM TB_STUDENT s
CROSS JOIN cal
WHERE s.del_yn = 'N'
  AND DAYOFWEEK(cal.d) NOT IN (1, 7);   -- 일요일(1), 토요일(7) 제외

-- ── 결과 확인 ──
SELECT CONCAT('출결 레코드 총 ', COUNT(*), '건') AS 결과 FROM TB_ATTEND;

-- ── 패턴별 샘플 통계 (학생 9, 18, 27 대표 확인) ──
SELECT
    s.student_num,
    s.student_name,
    s.dept_name,
    a.total_count,
    a.present_count AS 출석,
    a.late_count    AS 지각,
    a.absent_count  AS 결석,
    a.official_count AS 공결,
    a.attend_rate   AS 출석률,
    CASE
        WHEN (a.absent_count + a.late_count / 3.0) >= a.total_count / 3.0 THEN '⚠️ 위험'
        WHEN a.attend_rate < 75 THEN '⚠️ 주의'
        ELSE '✅ 양호'
    END AS 상태
FROM TB_STUDENT s
JOIN (
    SELECT
        student_id,
        COUNT(*) AS total_count,
        SUM(attend_status = '출석') AS present_count,
        SUM(attend_status = '지각') AS late_count,
        SUM(attend_status = '결석') AS absent_count,
        SUM(attend_status = '공결') AS official_count,
        ROUND((SUM(attend_status IN ('출석','공결')) / COUNT(*)) * 100, 1) AS attend_rate
    FROM TB_ATTEND
    GROUP BY student_id
) a ON s.student_id = a.student_id
WHERE s.student_id IN (9, 18, 19, 27, 28, 29, 30)
ORDER BY s.student_id;
