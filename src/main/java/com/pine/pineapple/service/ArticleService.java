package com.pine.pineapple.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pine.pineapple.entity.Article;

public interface ArticleService {
    Page<Article> pageArticles(int page, int size);
    Article getById(Long id);
    void createArticle(Article article);
    void updateArticle(Article article);
    void deleteArticle(Long id);
}

