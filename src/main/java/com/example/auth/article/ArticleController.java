package com.example.auth.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("articles")
public class ArticleController {
    private final ArticleService service;
    public ArticleController(ArticleService service) {
        this.service = service;
    }

    // READ ALL
    @GetMapping
    public String readAll(Model model) {
        model.addAttribute("articles", service.readAll());
        return "articles/home";
    }

    // CREATE (View)
    @GetMapping("create")
    public String createView() {
        return "articles/create";
    }

    // CREATE
    @PostMapping
    public String create(
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content
    ) {
        service.create(title, content);
        return "redirect:/articles";
    }
}
