package burnCalories.diet.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 안전한 비밀키 자동 생성

    public JwtToken generateToken(Authentication authentication) {
        String username = authentication.getName();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //현재 시간
        long now = System.currentTimeMillis();
        long access_expirationTime = 1000 * 60 * 60;  // 1시간 유효 토큰
        long refresh_expirationtime = 7*24*60*60*1000;  //7일

        //액세스 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("auth", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + access_expirationTime))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        //리프레쉬 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refresh_expirationtime))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        return new JwtToken(accessToken, refreshToken, access_expirationTime);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported token", e);
        } catch (IllegalArgumentException e) {
            log.info("Token is empty", e);
        } catch (Exception e) {
            log.info("error" , e);
        }
        return false;
    }

 /*   public Authentication getAuthentication(String accessToken) {

    }*/

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        Claims claims = getClaims(token);
        if (claims.get("auth").equals("")) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        String roles = claims.get("auth", String.class);

        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
