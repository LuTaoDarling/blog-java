package com.xxxx.blogjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.blogjava.dao.mapper.SysUserMapper;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.service.LoginService;
import com.xxxx.blogjava.service.SysUserService;
import com.xxxx.blogjava.vo.ErrorCode;
import com.xxxx.blogjava.vo.LoginUserVo;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("佚名");
        }
        return sysUser;
    }

    /**
     * 登录逻辑
     *
     * @param account  账户
     * @param password 密码
     * @return {@link SysUser}
     */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 解析token获取用户信息
     *
     * @param token 令牌
     * @return {@link Result}
     */
    @Override
    public Result findUserByToken(String token) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            Result.fail(ErrorCode.TOKEN_EOOOR.getCode(), ErrorCode.TOKEN_EOOOR.getMsg());
        }

//        如果获取到了用户信息就赋值
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("test");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }
}
