package com.pine.pineapple.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

        long id = markdownService.modifyArticle(md);
        return Result.ok("保存成功，id=" + id);
    }

    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (null == id) {
            return Result.fail("id不能为空");
        }

        markdownService.deleteArticle(id);
        return Result.ok("文件删除成功");
    }

    /** 按 文件名称 获取 Markdown */
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

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        Page<Markdown> pg = markdownService.pageArticles(page, size);
        return Result.ok(pg);
    }
}

