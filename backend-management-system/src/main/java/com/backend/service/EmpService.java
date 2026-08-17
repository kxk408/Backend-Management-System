package com.backend.service;

import com.backend.pojo.Emp;
import com.backend.pojo.EmpQueryParam;
import com.backend.pojo.LoginInfo;
import com.backend.pojo.PageResult;

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
