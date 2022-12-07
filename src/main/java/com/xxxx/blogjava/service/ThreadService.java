package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxxx.blogjava.dao.mapper.ArticleMapper;
import com.xxxx.blogjava.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    //    操作在线程池里面进行，执行 不会影响原有的主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
//        阅读数
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
//        线程安全
        updateWrapper.eq(Article::getViewCounts, viewCounts);

//        执行阅读量更新操作
        articleMapper.update(articleUpdate, updateWrapper);

//        try {
//            Thread.sleep(5000);
//            System.out.println("更新完成了...");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
