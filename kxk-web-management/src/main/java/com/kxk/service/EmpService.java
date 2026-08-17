package com.kxk.service;

import com.kxk.pojo.Emp;
import com.kxk.pojo.EmpQueryParam;
import com.kxk.pojo.LoginInfo;
import com.kxk.pojo.PageResult;

import java.util.ArrayList;
import java.util.List;

public interface EmpService {
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> id);

    Emp getById(Integer id);

    void update(Emp emp);

    LoginInfo login(Emp emp);
}
