package com.xxxx.blogjava.controller;

import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.utils.UserThreadLocal;
import com.xxxx.blogjava.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
