package com.xxxx.blogjava.handler;

import com.xxxx.blogjava.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

//对加了Controller注解的方法进行拦截处理AOP的实现
@ControllerAdvice
public class AllExceptionHandler {
    //    进行异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999,"系统出现异常!-_-");
    }
}
