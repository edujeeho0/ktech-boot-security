package com.example.auth.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Component
public class LogFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        // 요청이 Spring에 전달되기 전
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        log.info("start request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(httpRequest);
        log.info(new String(
                requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8
        ));
        // 요청을 Spring에 전달
        filterChain.doFilter(requestWrapper, servletResponse);

        log.info(new String(
                requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8
        ));
        // 요청이 Spring에 전달된 후
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        log.info("send response: {}", httpResponse.getStatus());
    }
}
