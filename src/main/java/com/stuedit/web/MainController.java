package com.stuedit.web;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuedit.service.AttendService;
import com.stuedit.service.NoticeService;
import com.stuedit.service.StudentService;

/**
 * 메인(대시보드) 컨트롤러
 */
@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private AttendService attendService;

    @Autowired
    private NoticeService noticeService;

    /** 대시보드 메인 페이지 */
    @RequestMapping("/main.do")
    public String main(ModelMap model) throws Exception {
        logger.debug("대시보드 진입");
        try {
            // 관리자 대시보드 통계 데이터 조회
            Map<String, Object> dashData = studentService.selectDashboardData();
            model.addAllAttributes(dashData);
            // 오늘 출석률 추가
            double todayAttendRate = attendService.selectTodayAttendRate();
            model.addAttribute("todayAttendRate", todayAttendRate);
            int totalNoticeCount = noticeService.selectTotalNoticeCount();
            model.addAttribute("totalNoticeCount", totalNoticeCount);
        } catch (Exception e) {
            logger.error("대시보드 데이터 조회 오류", e);
            // 오류 발생 시 빈 값으로 페이지 표시 (서비스 중단 방지)
            model.addAttribute("totalStudentCount", 0);
            model.addAttribute("monthlyNewCount",   0);
            model.addAttribute("todayAttendRate",   "0.0");
            model.addAttribute("totalNoticeCount",  0);
        }
        return "main";
    }
}
