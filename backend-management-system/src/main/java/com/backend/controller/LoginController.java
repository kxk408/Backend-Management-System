package com.backend.controller;

import com.backend.pojo.Emp;
import com.backend.pojo.Result;
import com.backend.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.backend.pojo.LoginInfo;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;


    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        // 不打印完整 emp，避免明文密码进日志
        log.info("登录请求，用户名：{}", emp.getUsername());
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误");
    }

}
