package com.xxxx.blogjava.handler;

import com.alibaba.fastjson.JSON;
import com.xxxx.blogjava.dao.pojo.SysUser;
import com.xxxx.blogjava.service.LoginService;
import com.xxxx.blogjava.utils.UserThreadLocal;
import com.xxxx.blogjava.vo.ErrorCode;
import com.xxxx.blogjava.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
//            可能是访问静态资源handler
            return true;
        }
        String token = request.getHeader("Authorization");

//        打印日志

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)) {
            Result res = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(res));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            Result res = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(res));
            return false;
        }
//        登录验证成功，直接放行
        UserThreadLocal.put(sysUser);
        return true;
    }


    //    删除方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        删除用完的信息
        UserThreadLocal.remove();
    }
}
