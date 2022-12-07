package com.xxxx.blogjava.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.xxxx.blogjava.common.aop.LogAnnotation;
import com.xxxx.blogjava.service.ArticleService;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.ArticleParam;
import com.xxxx.blogjava.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     *
     * @param pageParams 页面参数
     * @return {@link Result}
     */
    @PostMapping
    @LogAnnotation(module="文章",operator="获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams) {

        return articleService.listArticle(pageParams);
    }

    @PostMapping("/hot")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    @PostMapping("/new")
    public Result newArticle() {
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 文章归档
     *
     * @return {@link Result}
     */
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    /**
     * 找到文章id
     *
     * @param  articleId
     * @return {@link Result}
     */
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     *
     * @param articleParam 文章参数
     * @return {@link Result}
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
