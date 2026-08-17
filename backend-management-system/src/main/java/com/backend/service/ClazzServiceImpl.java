package com.backend.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.backend.mapper.ClazzMapper;
import com.backend.pojo.Clazz;
import com.backend.pojo.ClazzQueryParam;
import com.backend.pojo.Emp;
import com.backend.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService{
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        //        1.设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        //        2.执行查询
        List<Clazz> clazzes = clazzMapper.list(clazzQueryParam);
        //        3.封装为PageResult并返回
        Page<Clazz> p = (Page<Clazz>) clazzes;
        return new PageResult<Clazz>(p.getTotal(), p.getResult());

    }

    @Override
    public void delete(Integer id) {
        clazzMapper.delete(id);
    }

    @Override
    public void add(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.add(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
//        System.out.println("id:"+clazzMapper.getById(id));
        return clazzMapper.getById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> list() {
        List<Clazz> clazzes = clazzMapper.findAll();
        return clazzes;
    }

}
