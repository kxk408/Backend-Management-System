package com.kxk.controller;

import com.kxk.pojo.Emp;
import com.kxk.pojo.EmpQueryParam;
import com.kxk.pojo.PageResult;
import com.kxk.pojo.Result;
import com.kxk.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import org.springframework.format.annotation.DataTimeFormat;

@RequestMapping("/emps")
@RestController
@Slf4j
public class EmpCrontroller {

    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(EmpQueryParam empQueryParam){
        log.info("分页查询，参数：{}",empQueryParam);
        PageResult<Emp> pageResult =empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result add(@RequestBody Emp emp){
        log.info("新增员工，数据：{}",emp);
        empService.save(emp);
        return Result.success(emp);
    }

    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Integer> ids){
        log.info("删除员工，id为：{}",ids);
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result change(@PathVariable Integer id){
        log.info("查询员工，id为：{}",id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工，数据为：{}",emp);
        empService.update(emp);
        return Result.success();
    }



}
