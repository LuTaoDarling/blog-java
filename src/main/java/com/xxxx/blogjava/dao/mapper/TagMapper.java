package com.xxxx.blogjava.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.blogjava.dao.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 通过文章id找到标签
     *
     * @param articleId 文章id
     * @return {@link List}<{@link Tag}>
     */
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagsId(@Param("tagIds") List<Long> tagIds);
}
