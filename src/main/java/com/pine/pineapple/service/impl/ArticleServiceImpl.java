package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.Article;
import com.pine.pineapple.service.ArticleService;
import com.pine.pineapple.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author ThinkPad
* @description 针对表【article】的数据库操作Service实现
* @createDate 2025-10-13 11:13:08
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    // 增
    @Override
    public boolean createArticle(Article article) {
        return this.saveOrUpdate(article);
    }

    // 查
    @Override
    public Article getArticleById(Long id) {
        return this.getById(id);
    }

    // 改
    @Override
    public boolean updateArticle(Article article) {
        return this.updateById(article);
    }

    // 删
    @Override
    public boolean deleteArticle(Long id) {
        return this.removeById(id);
    }

    // 分页查询
    @Override
    public IPage<Article> pageArticles(Page<Article> page,Long userId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.page(page,queryWrapper);
    }
}




