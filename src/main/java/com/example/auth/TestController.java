package com.example.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    // /no-auth로 들어오는 요청은
    // 사용자가 로그인을 안해도 접근이 가능하게 하자.
    @GetMapping("/no-auth")
    public String noAuth() {
        return "no auth success!";
    }
}
