package com.example.auth.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    // 요청이 HandlerMethod(RequestMapping)에 도달하기 전에 실행
    @Override
    public boolean preHandle(
            // 요청을 담고있는 객체
            HttpServletRequest request,
            // 응답을 담고있는 객체
            HttpServletResponse response,
            // 실제로 요청을 처리하는 RequestMapping을 나타내는 메서드 객체
            Object handler
    ) throws Exception {
        // 요청을 처리할 메서드를 가져온다.
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.debug("pre handle {}", handlerMethod.getMethod().getName());

        // 요청에 포함된 헤더들의 이름을 가져온다.
        Enumeration<String> headerNames = request.getHeaderNames();
        // 각 이름마다 실제 헤더의 값을 찾아온다.
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.debug("{}: {}", headerName, request.getHeader(headerName));
        }
        return true;
    }

    // HandlerMethod(RequestMapping)이 처리가 되고 응답이 보내지기 전에 실행
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 요청의 처리가 끝나고 모든 응답이 보내졌을 때
    // 요청 처리 과정에 발생한 예외도 인자로 받는다.
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
