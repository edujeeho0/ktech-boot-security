package com.example.auth.jwt;

import com.example.auth.jwt.dto.JwtRequestDto;
import com.example.auth.jwt.dto.JwtResponseDto;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/token")
public class JwtTokenController {
    private final JwtTokenUtils tokenUtils;
    // 1. 사용자 정보를 조회하는 방법
    private final UserDetailsService userService;
    // 2. 비밀번호를 대조하는 방법
    private final PasswordEncoder passwordEncoder;

    public JwtTokenController(
            JwtTokenUtils tokenUtils,
            UserDetailsService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/issue")
    public JwtResponseDto issueJwt(
            @RequestBody
            JwtRequestDto dto
    ) {
        UserDetails userDetails;
        // 사용자 정보를 조회하자.
        try {
             userDetails = userService.loadUserByUsername(dto.getUsername());
        } catch (UsernameNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 비밀번호를 대조하자.
        if (!passwordEncoder.matches(
                dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // JWT 발급
        String jwt = tokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }

//    @GetMapping("/generate-test")
//    public String testIssueJwt() {
//        return tokenUtils.generateToken(User
//                .withUsername("alex")
//                .password("adsf")
//                .build());
//    }
//
    @GetMapping("/validate-test")
    public String validateTest(
            @RequestParam("token")
            String token
    ) {
        if (!tokenUtils.validate(token))
            return "not valid jwt";
        return "valid jwt";
    }

    @GetMapping("/validate")
    public Claims validate(
            @RequestHeader(value = "Authorization", required = false)
            // Bearer <jwt 문자열>
            String authHeader
    ) {
        if (authHeader == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String[] headerSplit = authHeader.split(" ");
        // 잘못된 헤더 값이 입력되었을 때
        if (headerSplit.length != 2 || !headerSplit[0].equals("Bearer"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // headerSplit = { "Bearer", "jwt" };
        // JWT가 잘못되었을 때
        String jwt = headerSplit[1];
        if (!tokenUtils.validate(jwt))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return tokenUtils.parseClaims(jwt);
    }

    @GetMapping("is-authenticated")
    public String isAuthenticated() {
        return "Success";
    }
}
