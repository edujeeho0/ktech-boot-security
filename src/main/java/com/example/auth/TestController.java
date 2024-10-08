package com.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    // /no-auth로 들어오는 요청은
    // 사용자가 로그인을 안해도 접근이 가능하게 하자.
    @GetMapping("/no-auth")
    public String noAuth() {
        return "no auth success!";
    }

    // ROLE에 따라 접근 가능한지 확인
    @GetMapping("/user-role")
    public String userRole() {
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        return "userRole";
    }

    @GetMapping("/admin-role")
    public String adminRole() {
        return "adminRole";
    }

    // AUTHORITY에 따라 접근 가능한지 확인
    @GetMapping("/read-authority")
    public String readAuthority() {
        return "readAuthority";
    }

    @GetMapping("/write-authority")
    public String writeAuthority() {
        return "writeAuthority";
    }
}
