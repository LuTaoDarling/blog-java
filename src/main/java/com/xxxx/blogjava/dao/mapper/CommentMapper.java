package com.xxxx.blogjava.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.blogjava.dao.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
