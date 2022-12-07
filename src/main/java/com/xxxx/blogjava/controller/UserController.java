package com.xxxx.blogjava.controller;

import com.xxxx.blogjava.service.SysUserService;
import com.xxxx.blogjava.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }
}
