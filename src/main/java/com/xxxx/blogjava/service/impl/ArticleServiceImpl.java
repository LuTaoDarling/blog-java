package com.xxxx.blogjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.blogjava.dao.dto.Archives;
import com.xxxx.blogjava.dao.mapper.ArticleBodyMapper;
import com.xxxx.blogjava.dao.mapper.ArticleMapper;
import com.xxxx.blogjava.dao.mapper.ArticleTagMapper;
import com.xxxx.blogjava.dao.pojo.Article;
import com.xxxx.blogjava.dao.pojo.ArticleBody;
import com.xxxx.blogjava.dao.pojo.ArticleTag;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.service.*;
import com.xxxx.blogjava.utils.UserThreadLocal;
import com.xxxx.blogjava.vo.*;
import com.xxxx.blogjava.vo.params.ArticleParam;
import com.xxxx.blogjava.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    //  复制方法
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        if (isTag) {
            Long id = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(id));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            Long bodyId = article.getId();
            articleVo.setBody(findArticleByBodyId(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }


    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();

        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }

    //    方法重载
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.last("limit " + limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    /**
     * 最新文章
     *
     * @param limit 限制
     * @return {@link Result}
     */
    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archives = articleMapper.listArchives();
        return Result.success(archives);
    }

    /**
     * 查看文章详情
     *
     * @param articleId 文章id
     * @return {@link Result}
     */
    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
//        根据id查询文章信息
//        根据bodyId和categoryId去做关联查询
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
//        查看完文章，新增阅读数，
//        查看完文章之后 把更新操作放入线程池中进行
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }


    /**
     * 根据文章bodyId查找文章
     *
     * @param articleId 文章id
     * @return {@link ArticleBodyVo}
     */
    private ArticleBodyVo findArticleByBodyId(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    /**
     * 文章发布服务
     *
     * @param articleParam 文章参数
     * @return {@link Result}
     */
    @Override
    public Result publish(ArticleParam articleParam) {
//        获取作者id
        SysUser sysUser = UserThreadLocal.get();

//        发布文章 目的 构建Article对象
//        作者id  当前的登录用户
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        // 获取标签
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
//            设置标签
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
            }
        }

//        设置body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        HashMap<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());

        return Result.success(map);
    }
}

