package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "user_table")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    private String email;
    @Setter
    private String phone;

    @Setter
    // 연습이니까, ','로 여러 권한을 나누어서 생각하자.
    // "ROLE_USER"
    // "ROLE_USER,ROLE_ADMIN"
    private String authorities;
}
