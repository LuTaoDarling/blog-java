package com.xxxx.blogjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxxx.blogjava.dao.mapper.ArticleMapper;
import com.xxxx.blogjava.dao.mapper.CommentMapper;
import com.xxxx.blogjava.dao.pojo.Article;
import com.xxxx.blogjava.dao.pojo.Comment;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.service.CommentsService;
import com.xxxx.blogjava.service.SysUserService;
import com.xxxx.blogjava.utils.UserThreadLocal;
import com.xxxx.blogjava.vo.CommentVo;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.UserVo;
import com.xxxx.blogjava.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 根据文章id查询评论列表
     *
     * @param articleId 文章id
     * @return {@link Result}
     */
    @Override
    public Result commentsByArticleId(Long articleId) {
//        根据文章id查询评论列表
//        根据作者id查询作者的信息
//        查看评论等级看看有没有子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
//        子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
//        给谁评论的 如果大于1就不是本篇文章的评论 等于1就是此文章的评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }

        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }

    /**
     * 评论功能
     *
     * @param commentParam 评论参数
     * @return {@link Result}
     */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentMapper.insert(comment);

//        更新评论数

        Article article = articleMapper.selectById(commentParam.getArticleId());
//        从Article表中查出的评论数
        Integer viewCounts = article.getCommentCounts();

//        从评论表中查出的对应文章的总评论数
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, article.getId());
        Integer commentCount = commentMapper.selectCount(queryWrapper);

        if (commentCount != viewCounts) {
            viewCounts = commentCount;
        }

        Article articleUpdate = new Article();
        articleUpdate.setCommentCounts(viewCounts);
        articleUpdate.setId(article.getId());
        articleMapper.updateById(articleUpdate);


        return Result.success(null);
    }
}
