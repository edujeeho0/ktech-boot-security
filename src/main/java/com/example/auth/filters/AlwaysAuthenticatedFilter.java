package com.example.auth.filters;

import com.example.auth.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


// 이 필터가 등록되면 모든 요청은 인증된 요청이라고 판단한다.
@Slf4j
public class AlwaysAuthenticatedFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("try all auth filter");
        // Spring Security 내부적으로 인증 정보를 담고있는 객체
        SecurityContext context = SecurityContextHolder
                .createEmptyContext();
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        new CustomUserDetails(
                                1L,
                                "alex",
                                "password",
                                "a@a.a",
                                "010-abcd-ef01"
                        ),
                        "password",
                        new ArrayList<>()
                );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}








