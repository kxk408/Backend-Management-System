package com.backend.service;

import com.backend.pojo.Clazz;
import com.backend.pojo.ClazzQueryParam;
import com.backend.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    void delete(Integer id);

    void add(Clazz clazz);

    Clazz getById(Integer id);

    void update(Clazz clazz);

    List<Clazz> list();
}
