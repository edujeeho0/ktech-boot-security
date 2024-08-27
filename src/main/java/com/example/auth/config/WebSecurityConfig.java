package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        // URL에 따라 접근 권한을 조정하는 방법
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/no-auth", "/")
                            .permitAll();
                    // 인증이 된 사용자만 허용하는 URL
                    auth.requestMatchers("/users/my-profile")
                            .authenticated();
                    auth.requestMatchers("/users/register")
                            .anonymous();
//                    auth.requestMatchers(HttpMethod.POST, "/articles")
//                            .authenticated();
//                    auth.requestMatchers(HttpMethod.GET, "/articles")
//                            .permitAll();
                    // /articles, /articles/1, /articles/1/update
                    auth.requestMatchers("/articles/**")
                            .authenticated();
//                    auth.requestMatchers("/")
//                            .permitAll();
//                    auth.requestMatchers("/nobody")
//                            .denyAll();
                })
                // 로그인은 어떤 URL에서 어떤 방식으로 이뤄지는지
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/users/my-profile")
                        .failureUrl("/users/login?fail")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/login")
                )
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
