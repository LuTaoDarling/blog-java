package com.xxxx.blogjava.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.blogjava.dao.dto.Archives;
import com.xxxx.blogjava.dao.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();
}
