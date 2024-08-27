package com.example.auth.article.entity;

import com.example.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;
    @Setter
    private String content;

    @Setter
    @ManyToOne
    private UserEntity writer;
}
