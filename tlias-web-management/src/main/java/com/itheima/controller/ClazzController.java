package com.itheima.controller;

import com.itheima.pojo.*;
import com.itheima.service.ClazzService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result getByTime(ClazzQueryParam clazzQueryParam) {
        log.info("查询班级统计，参数：{}", clazzQueryParam);
        PageResult<Clazz> pageResult =clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{id}")
    public Result delteById(@PathVariable Integer id) {
        log.info("删除班级，参数：{}", id);
        clazzService.delete(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Clazz clazz) {
        log.info("添加班级，参数：{}", clazz);
        clazzService.add(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("查询班级，参数：{}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级，参数：{}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("查询班级列表");
        List<Clazz> clazzes = clazzService.list();
        return Result.success(clazzes);
    }
}
