package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.blogjava.dao.pojo.Article;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.ArticleParam;
import com.xxxx.blogjava.vo.params.PageParams;
import org.springframework.stereotype.Service;


public interface ArticleService extends IService<Article> {
    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticles(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
