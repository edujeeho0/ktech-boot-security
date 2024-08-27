package com.example.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    // JWT 암호키를 담기 위한 객체
    private final Key secretKey;
    // JWT를 해석하는 객체
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        log.info("jwtSecret: {}", jwtSecret);
        this.secretKey =
                Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.secretKey)
                .build();
    }

    public String generateToken(UserDetails userDetails) {
        // 현재시각을 기준으로 만료 시각을 정해야 함으로, 현재 시각을 구한다.
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                // 이 JWT를 들고있는 사람이 누구인지
                .setSubject(userDetails.getUsername())
                // 이 JWT가 언제 생성되었는지
                .setIssuedAt(Date.from(now))
                // 이 JWT가 언제 만료되는지
                .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.secretKey)
                .compact();
    }
}
