package com.itheima.service;

//import org.springframework.stereotype.Service;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;

import java.util.List;

//@Service
public interface DeptService {

    List<Dept> findAll();

    void deleteDept(int id);

    void addDept(Dept dept);

    Dept getById(Integer id);

    void changeName(Dept dept);
}
