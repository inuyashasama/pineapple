package com.pine.pineapple.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.Markdown;
import com.pine.pineapple.mapper.MarkdownMapper;
import com.pine.pineapple.service.MarkdownService;
import org.springframework.stereotype.Service;

@Service
public class MarkdownServiceImpl extends ServiceImpl<MarkdownMapper, Markdown> implements MarkdownService {
}
