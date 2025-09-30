package com.pine.pineapple.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.Markdown;
import com.pine.pineapple.mapper.MarkdownMapper;
import com.pine.pineapple.service.MarkdownService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MarkdownServiceImpl extends ServiceImpl<MarkdownMapper, Markdown> implements MarkdownService {

    @Resource
    MarkdownMapper markdownMapper;

    @Override
    public Page<Markdown> pageArticles(int page, int size) {
        Page<Markdown> pg = new Page<>(page, size);
        return markdownMapper.selectPage(pg, null);
    }

    @Override
    public void deleteArticle(Long id) {
        markdownMapper.deleteById(id);
    }
}
