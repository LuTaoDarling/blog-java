package com.xxxx.blogjava.controller;

import com.xxxx.blogjava.service.TagService;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result hot() {
//        最热门的六个标签，只查询六个
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping
    public Result findAll (){
      return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}
