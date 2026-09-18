package com.stuedit.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * AOP 전역 예외 처리
 * 모든 컨트롤러에서 발생하는 예외를 공통으로 처리
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * RuntimeException 처리 (더 구체적인 타입이므로 Exception보다 우선 매칭)
     * 비즈니스 로직 예외 (예: 존재하지 않는 데이터)
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(HttpServletRequest request, RuntimeException e) {
        logger.warn("Runtime exception at {}: {}", request.getRequestURI(), e.getMessage());
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("requestUri",   request.getRequestURI());
        return mav;
    }

    /**
     * Exception 처리 (최상위 fallback)
     * 예기치 못한 시스템 예외
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception e) {
        logger.error("Unhandled exception at {}: {}", request.getRequestURI(), e.getMessage(), e);
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("requestUri",   request.getRequestURI());
        return mav;
    }
}
