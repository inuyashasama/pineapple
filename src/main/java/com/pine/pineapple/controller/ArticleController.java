package com.pine.pineapple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.Article;
import com.pine.pineapple.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    // 创建文章
    @PostMapping("/createArticle")
    public Result<Boolean> create(@RequestBody Article article) {
        boolean success = articleService.createArticle(article);
        return success ? Result.ok("创建成功", true) : Result.fail("创建失败");
    }

    // 查询文章
    @GetMapping("getById/{id}")
    public Result<Article> get(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        return article != null ? Result.ok(article) : Result.fail("文章不存在");
    }

    // 更新文章
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Article article) {
        boolean success = articleService.updateArticle(article);
        return success ? Result.ok("更新成功", true) : Result.fail("更新失败");
    }

    // 删除文章
    @PostMapping("deleteById/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = articleService.deleteArticle(id);
        return success ? Result.ok("删除成功", true) : Result.fail("删除失败");
    }

    // 分页查询文章
    @GetMapping("/page")
    public Result<IPage<Article>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Article> pageInfo = new Page<>(page, size);
        IPage<Article> result = articleService.pageArticles(pageInfo);
        return Result.ok(result);
    }

}
