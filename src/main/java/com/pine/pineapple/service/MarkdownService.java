package com.pine.pineapple.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pine.pineapple.entity.Markdown;

public interface MarkdownService extends IService<Markdown> {

    Page<Markdown> pageArticles(int page, int size);
    void deleteArticle(Long id);
    long modifyArticle(Markdown md);
}

