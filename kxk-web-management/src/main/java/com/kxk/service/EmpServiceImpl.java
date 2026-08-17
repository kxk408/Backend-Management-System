package com.kxk.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kxk.mapper.EmpExprMapper;
import com.kxk.mapper.EmpMapper;
import com.kxk.pojo.*;
import com.kxk.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class EmpServiceImpl implements EmpService{

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> emp_list = empMapper.list(empQueryParam);
        Page< Emp> p = (Page<Emp>) emp_list;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
}

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void save(Emp emp) {
        // 入库前将明文密码编码为 BCrypt 哈希
        if (StringUtils.hasText(emp.getPassword())) {
            emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        }

        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(Emp emp) {
        // 仅当请求携带新密码时才更新；先 encode 再交给动态 SQL
        if (StringUtils.hasText(emp.getPassword())) {
            emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        } else {
            emp.setPassword(null); // 避免把空串写入动态 update
        }

        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Emp emp) {
        // 1. 只按用户名查库（拿到哈希）
        Emp e = empMapper.selectByUsername(emp.getUsername());
        // 2. 用 PasswordEncoder 比对明文与哈希（不可逆，不能再 WHERE password=明文）
        if (e != null && StringUtils.hasText(emp.getPassword())
                && StringUtils.hasText(e.getPassword())
                && passwordEncoder.matches(emp.getPassword(), e.getPassword())) {
            log.info("登录成功，用户名：{}", e.getUsername());
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateJwt(claims);
            // 使用查库结果 e，避免请求体缺字段导致 LoginInfo 为空
            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }
        return null;
    }
}
