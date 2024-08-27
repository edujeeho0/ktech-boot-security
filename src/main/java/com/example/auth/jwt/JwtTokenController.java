package com.example.auth.jwt;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/validate-test")
    public String validateTest(
            @RequestParam("token")
            String token
    ) {
        if (!tokenUtils.validate(token))
            return "not valid jwt";
        return "valid jwt";
    }

    @GetMapping("/validate-header")
    public Claims validateHeader(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader
    ) {
        log.info(authorizationHeader);
        String token = authorizationHeader.split(" ")[1];
        return tokenUtils.parseClaims(token);
    }
}
