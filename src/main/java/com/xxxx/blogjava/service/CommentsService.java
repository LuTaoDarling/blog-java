package com.xxxx.blogjava.service;

import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.CommentParam;

public interface CommentsService {
    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
