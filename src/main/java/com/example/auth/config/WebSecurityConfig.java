package com.example.auth.config;

import com.example.auth.filters.AlwaysAuthenticatedFilter;
import com.example.auth.jwt.JwtTokenFilter;
import com.example.auth.jwt.JwtTokenUtils;
import com.example.auth.oauth.OAuth2UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService detailsService;
    private final OAuth2UserServiceImpl oAuth2UserService;

    public WebSecurityConfig(
            JwtTokenUtils tokenUtils,
            UserDetailsService detailsService,
            OAuth2UserServiceImpl oAuth2UserService
    ) {
        this.tokenUtils = tokenUtils;
        this.detailsService = detailsService;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        // URL에 따라 접근 권한을 조정하는 방법
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/no-auth", "/", "/token/issue", "/error")
                                .permitAll();
                        // 인증이 된 사용자만 허용하는 URL
                        auth.requestMatchers("/users/my-profile")
                                .authenticated();
                        auth.requestMatchers("/users/register")
                                .anonymous();
                        // ROLE에 따른 접근 권한
                        auth.requestMatchers("/user-role")
                                .hasRole("USER");
                        auth.requestMatchers("/admin-role")
                                .hasRole("ADMIN");
                        // AUTHORITY에 따른 접근 권한
                        auth.requestMatchers("/read-authority")
                                .hasAuthority("READ");
                        auth.requestMatchers("/write-authority")
                                .hasAuthority("WRITE");
                        // /articles, /articles/1, /articles/1/update
                        auth.requestMatchers("/articles/**", "/token/is-authenticated")
                                .authenticated();
                })
//                // 로그인은 어떤 URL에서 어떤 방식으로 이뤄지는지
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/users/login")
//                        .defaultSuccessUrl("/users/my-profile")
//                        .failureUrl("/users/login?fail")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/users/logout")
//                        .logoutSuccessUrl("/users/login")
//                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/users/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .permitAll()
                )
                .addFilterBefore(
//                        new AlwaysAuthenticatedFilter(),
                        new JwtTokenFilter(
                                tokenUtils,
                                detailsService
                        ),
                        AuthorizationFilter.class
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        // before 6.1
        /*
        http.authorizeHttpRequests()
                .requestMatchers("/no-auth")
                .permitAll()
                .and();
         */

        return http.build();
    }

//    @Bean
//    public UserDetailsManager userDetailsManager(
//            PasswordEncoder passwordEncoder
//    ) {
//        UserDetails user1 = User.withUsername("user1")
//                .password(passwordEncoder.encode("password"))
//                .build();
//        return new InMemoryUserDetailsManager(user1);
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
