package com.pine.pineapple.common.aspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pine.pineapple.common.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class GlobalExceptionLoggingAspect {

    // 定义切入点：拦截所有 controller 的方法
    @Pointcut("execution(* com.pine.pineapple.controller..*.*(..))")
    public void controllerPointcut() {}

    // 拦截所有在 controller 中抛出的异常
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        // 记录异常日志
        logException(ex, request);

        // 返回统一错误响应
        sendErrorResponse(response, ex);
    }

    private void logException(Exception ex, HttpServletRequest request) {
        log.error("========================================");
        log.error("🚫 异常发生: {}", ex.getClass().getSimpleName());
        log.error("📍 请求地址: {} {}", request.getMethod(), request.getRequestURL());
        log.error("📝 请求参数: {}", request.getQueryString());
        log.error("📌 请求头 Authorization: {}", request.getHeader("Authorization"));
        log.error("💥 异常消息: {}", ex.getMessage());
        log.error("📋 异常堆栈: ", ex); // 打印完整堆栈
        log.error("========================================");
    }

    private void sendErrorResponse(HttpServletResponse response, Exception ex) {
        try {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");

            // 构造统一返回结果
            Result<Object> result = Result.fail("系统内部错误: " + ex.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(result);

            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            log.error("发送错误响应失败: ", e);
        }
    }
}
