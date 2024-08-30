package com.example.auth.oauth;

import com.example.auth.UserService;
import com.example.auth.jwt.JwtTokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
    public OAuth2SuccessHandler(
            UserService userService,
            JwtTokenUtils tokenUtils
    ) {
        this.userService = userService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User =
                (OAuth2User) authentication.getPrincipal();

        String provider = oAuth2User.getAttribute("provider");
        String email = oAuth2User.getAttribute("email");
        // {naver}edujeeho@naver.com
        String username = String.format("{%s}%s", provider, email);
        String providerId = oAuth2User.getAttribute("id").toString();

        if (!userService.userExists(username)) {
            userService.createUser(username, providerId, providerId);
        }
        UserDetails userDetails =
                userService.loadUserByUsername(username);

        String jwt = tokenUtils.generateToken(userDetails);
        // 프런트엔드에서 jwt를 회수할 수 있도록 query에 jwt를 포함해서 리다이렉트한다.
        // URL을 만든다.
        String targetUrl = String.format(
                "http://localhost:8080/token/validate-test?token=%s", jwt
        );
        // redirect 한다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
