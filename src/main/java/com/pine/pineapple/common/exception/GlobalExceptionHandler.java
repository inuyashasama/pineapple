package com.pine.pineapple.common.exception;

import com.pine.pineapple.common.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        // 记录异常日志
        logException(ex, request);

        // 构造错误响应
        Result<Object> result = Result.fail("系统内部错误: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 处理业务异常（假设存在自定义业务异常）
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("业务异常: {} - URL: {}", ex.getMessage(), request.getRequestURL());

        Result<Object> result = Result.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * 记录异常详细信息
     */
    private void logException(Exception ex, HttpServletRequest request) {
        log.error("""
            ========================================
            🚫 异常发生: {}
            📍 请求地址: {} {}
            📝 请求参数: {}
            📌 请求头 Authorization: {}
            💥 异常消息: {}
            📋 异常堆栈:
            ========================================""",
                ex.getClass().getSimpleName(),
                request.getMethod(),
                request.getRequestURL(),
                request.getQueryString(),
                request.getHeader("Authorization"),
                ex.getMessage(),
                ex);
    }
}
