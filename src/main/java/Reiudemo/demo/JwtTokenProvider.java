package Reiudemo.demo;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    // 1. Chuỗi bí mật cố định
    private final String SECRET_KEY = "YourSecretKeyForJWTAuthMustBeVeryLongAndSecure"; 
    // 2. Chìa khóa tạo từ chuỗi trên
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    // 3. Thời gian hết hạn (Dòng bạn đang thiếu đây)
    private final long expiration = 86400000L; 

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Hết lỗi đỏ tại đây
                .signWith(key)
                .compact();
    }
    // ... các hàm bên dưới giữ nguyên



    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
