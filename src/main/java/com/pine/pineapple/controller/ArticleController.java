package com.pine.pineapple.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.Article;
import com.pine.pineapple.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        Page<Article> pg = articleService.pageArticles(page, size);
        return Result.ok(pg);
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        Article a = articleService.getById(id);
        if (a == null) return Result.fail("文章不存在");
        return Result.ok(a);
    }

    @PostMapping
    public Result<?> create(@RequestBody Article article,
                            @RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.fail("未登录");
        article.setUserId(userId);
        articleService.createArticle(article);
        return Result.ok("创建成功", null);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id,
                            @RequestBody Article article,
                            @RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.fail("未登录");
        Article exist = articleService.getById(id);
        if (exist == null) return Result.fail("文章不存在");
        if (!exist.getUserId().equals(userId)) return Result.fail("没有权限");
        article.setId(id);
        article.setUserId(userId);
        articleService.updateArticle(article);
        return Result.ok("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id,
                            @RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.fail("未登录");
        Article exist = articleService.getById(id);
        if (exist == null) return Result.fail("文章不存在");
        if (!exist.getUserId().equals(userId)) return Result.fail("没有权限");
        articleService.deleteArticle(id);
        return Result.ok("删除成功", null);
    }
}

