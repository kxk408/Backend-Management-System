package com.kxk.controller;

import com.kxk.pojo.Dept;
import com.kxk.pojo.Result;
import com.kxk.service.DeptService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("depts")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;
    @Autowired
    private HikariDataSource dataSource;
//    private static final Logger log = LoggerFactory.getLogger(DeptController.class);

    //    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    @GetMapping
    public Result list() {
        log.info("查询全部部门数据");
        List<Dept> deptlist = deptService.findAll();
        return Result.success(deptlist);
    }

//    @RequestMapping(value="/depts", method= RequestMethod.DELETE)
    @DeleteMapping
    public Result delete(Integer id) {
        log.info("删除部门id为：{}", id);
        deptService.deleteDept(id);
        return Result.success();
    }
//    public Result list(@RequestParam("id") Integer deptId) {
//        System.out.println("删除部门id为："+deptId);
//        deptService.deleteDept(deptId);
//        return Result.success();
//    }
//    public Result list(HttpServletRequest request) {
//        String idstr = request.getParameter("id");
//        int id = Integer.parseInt(idstr);
//        System.out.println("删除部门id为："+id);
//        deptService.deleteDept(id);
//        return Result.success();
//    }
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门：{}", dept);
        deptService.addDept(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("查询部门id为：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门：{}", dept);
        deptService.changeName(dept);
        return Result.success();
    }
}
