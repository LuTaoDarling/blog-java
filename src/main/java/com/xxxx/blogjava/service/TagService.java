package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.blogjava.dao.pojo.Tag;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<TagVo> findTagsByArticleId(Long id);

    Result hots(int limit);

    /**
     * 查询所有文章标签
     *
     * @return {@link Result}
     */
    Result findAll();

    Result findAllDetail();
}
