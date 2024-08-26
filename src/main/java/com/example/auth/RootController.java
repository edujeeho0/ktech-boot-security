package com.example.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// "/"로 왔을 때 어떻게 행동할지 정의하는 Controller
@Controller
public class RootController {
    @GetMapping
    public String root() {
        return "index";
    }
}
