package com.pine.pineapple.common.filter;

import com.pine.pineapple.common.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;

/**
 * 简单的 JWT 过滤器：解析 token 并将 userId 放入 request attribute "userId"
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = path.startsWith("/user") ||
                path.startsWith("/swagger") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/api/upload") ||
                path.startsWith("/uploads");

        log.debug("Request URI: {}, Should skip JWT filter: {}", path, shouldSkip);
        return shouldSkip;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                if (!token.isEmpty()) {
                    // 👉 在这里加日志和 try-catch
                    log.info("🔐 正在验证 JWT Token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                    Long userId = JwtUtil.parseToken(token);
                    request.setAttribute("userId", userId);
                    // ✅ 关键：必须设置 Spring Security 认证
                    // 否则后续请求仍被视为未登录，返回 401
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }



            } catch (ExpiredJwtException e) {
                log.error("⏰ JWT Token 已过期: {}", e.getMessage(), e);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token 已过期");
            } catch (MalformedJwtException e) {
                log.error("📛 JWT 格式错误: {}", e.getMessage(), e);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "无效的 Token");
            } catch (IllegalArgumentException e) {
                log.error("🟡 JWT Token 参数异常: {}", e.getMessage(), e);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token 参数错误");
            } catch (Exception e) {
                log.error("💥 未知异常发生在 JwtAuthenticationFilter: {}", e.getMessage(), e);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "认证过程发生未知错误");
            }
        }
        // 继续执行下一个过滤器
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpStatus status,
                                   String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // 使用标准的成功/失败标识
        boolean isSuccess = status.is2xxSuccessful();
        writer.write("{\"success\":" + isSuccess +
                ",\"data\":null" +
                ",\"message\":\"" + message + "\"}");
        writer.flush();
        writer.close();
    }





}

