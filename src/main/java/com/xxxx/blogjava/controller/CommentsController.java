package com.xxxx.blogjava.controller;

import com.xxxx.blogjava.service.CommentsService;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId) {
        return commentsService.commentsByArticleId(articleId);
    }

    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
