package com.pine.pineapple.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.Markdown;
import com.pine.pineapple.service.MarkdownService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/markdown")
public class MarkdownController {

    @Resource
    MarkdownService markdownService;

    /** 保存 Markdown */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Markdown md) {
        if (md.getContent() == null || md.getContent().isEmpty()) {
            return Result.fail("内容不能为空");
        }
        // 简单：每次保存一条新纪录
        md.setId(null);
        markdownService.save(md);
        return Result.ok("保存成功，id=" + md.getId());
    }

    /** 按 id 获取 Markdown */
    @GetMapping("/load/{name}")
    public Result<?> load(@PathVariable String name) {
        QueryWrapper<Markdown> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Markdown md = markdownService.getOne(queryWrapper);
        if (md == null) {
            return Result.fail("文档不存在");
        }
        return Result.ok(md);
    }
}

