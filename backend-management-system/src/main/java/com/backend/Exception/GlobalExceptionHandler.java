package com.backend.Exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.backend.pojo.Result;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler
    public Result handleException(Exception e){
        log.error("服务器异常",e);
        return Result.error("服务器异常");
    }

    @ExceptionHandler
    public Result handleDuplicateException(DuplicateKeyException e){
        log.error("服务器异常",e);
        String msg = e.getMessage();
        int i = msg.indexOf("Duplicate entry");
        String errorMsg = msg.substring(i);
        String[] arr= errorMsg.split(" ");
        return Result.error(arr[2] + "已存在");
    }
}
