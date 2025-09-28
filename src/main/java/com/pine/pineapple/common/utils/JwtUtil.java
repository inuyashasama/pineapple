package com.pine.pineapple.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    // 🔐 使用至少 32 字符的密钥（HS256 要求 256 位 = 32 字节）
    private static final String SECRET_STRING = "your-32-byte-secret-key-for-jwt!"; // 至少 32 字符
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000 * 24)) // 1天
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // ✅ signWith + SecretKey
                .compact();
    }

    public static Long parseToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder() // ✅ parserBuilder()
                    .setSigningKey(SECRET_KEY)  // ✅ setSigningKey
                    .build()
                    .parseClaimsJws(token);     // ✅ parseClaimsJws
            return Long.parseLong(jws.getBody().getSubject());
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Token has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("JWT format is unsupported", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("JWT is malformed", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("JWT signature is invalid", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}