package com.pine.pineapple.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pine.pineapple.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ThinkPad
* @description 针对表【article】的数据库操作Service
* @createDate 2025-10-13 11:13:08
*/
public interface ArticleService extends IService<Article> {

    boolean createArticle(Article article);
    Article getArticleById(Long id);
    boolean updateArticle(Article article);
    boolean deleteArticle(Long id);

    IPage<Article> pageArticles(Page<Article> page,Long userId);
}
