package com.xxxx.blogjava.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.blogjava.vo.params.LoginParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper extends BaseMapper<LoginParam> {
}
