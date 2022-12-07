package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.LoginParam;

public interface LoginService extends IService<LoginParam> {
    /**
     * 登录功能
     *
     * @param loginParam 登录参数
     * @return {@link Result}
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
