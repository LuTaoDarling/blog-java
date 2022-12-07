package com.xxxx.blogjava.controller;

import com.xxxx.blogjava.service.LoginService;
import com.xxxx.blogjava.vo.Result;
import com.xxxx.blogjava.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login (@RequestBody LoginParam loginParam){
//      登录，验证用户 访问用户表，对比数据库
        return loginService.login(loginParam);
    }
}
