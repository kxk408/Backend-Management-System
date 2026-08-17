package com.kxk.service;

import com.kxk.pojo.Clazz;
import com.kxk.pojo.ClazzQueryParam;
import com.kxk.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    void delete(Integer id);

    void add(Clazz clazz);

    Clazz getById(Integer id);

    void update(Clazz clazz);

    List<Clazz> list();
}
