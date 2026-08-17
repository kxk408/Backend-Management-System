package com.backend.mapper;

import com.backend.pojo.Clazz;
import com.backend.pojo.ClazzQueryParam;
import com.backend.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClazzMapper {


    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    void delete(Integer id);

    void add(Clazz clazz);

    @Select("select * from clazz where id = #{id}")
    Clazz getById(Integer id);

    void update(Clazz clazz);

    List<Clazz> findAll();
}
