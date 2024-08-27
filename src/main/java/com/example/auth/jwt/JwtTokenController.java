package com.example.auth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/token")
public class JwtTokenController {
    private final JwtTokenUtils tokenUtils;
    public JwtTokenController(
            JwtTokenUtils tokenUtils
    ) {
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/test")
    public String testIssueJwt() {
        return tokenUtils.generateToken(User
                .withUsername("alex")
                .password("adsf")
                .build());
    }
}
