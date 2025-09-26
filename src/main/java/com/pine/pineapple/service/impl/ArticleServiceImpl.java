package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.Article;
import com.pine.pineapple.mapper.ArticleMapper;
import com.pine.pineapple.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    ArticleMapper articleMapper;

    @Override
    public Page<Article> pageArticles(int page, int size) {
        Page<Article> pg = new Page<>(page, size);
        return articleMapper.selectPage(pg, null);
    }

    @Override
    public Article getById(Long id) {
        Article a = articleMapper.selectById(id);
        if (a != null) {
            // 简单增加阅读量
            a.setViews((a.getViews() == null ? 0 : a.getViews()) + 1);
            articleMapper.updateById(a);
        }
        return a;
    }

    @Override
    public void createArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.insert(article);
    }

    @Override
    public void updateArticle(Article article) {
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleMapper.deleteById(id);
    }
}

