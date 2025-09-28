package com.pine.pineapple.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class GlobalExceptionLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionLoggingAspect.class);

    // 定义切入点：拦截所有 controller 的方法
    @Pointcut("execution(* com.pine.pineapple.controller..*.*(..))")
    public void controllerPointcut() {}

    // 拦截所有在 controller 中抛出的异常
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        log.error("========================================");
        log.error("🚫 异常发生: {}", ex.getClass().getSimpleName());
        log.error("📍 请求地址: {} {}", request.getMethod(), request.getRequestURL());
        log.error("📝 请求参数: {}", request.getQueryString());
        log.error("📌 请求头 Authorization: {}", request.getHeader("Authorization"));
        log.error("💥 异常消息: {}", ex.getMessage());
        log.error("📋 异常堆栈: ", ex); // 打印完整堆栈
        log.error("========================================");
    }
}