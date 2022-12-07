package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.UserVo;

public interface SysUserService extends IService<SysUser> {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    UserVo findUserVoById(Long id);
}
