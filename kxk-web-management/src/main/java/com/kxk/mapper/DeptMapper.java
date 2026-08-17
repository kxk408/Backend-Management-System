package com.kxk.mapper;

import com.kxk.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    @Select("SELECT id, name, create_time, update_time  from dept order by update_time desc ")
    List<Dept> findAll();

    @Delete("DELETE from dept where id = #{id} ")
    void deleteDept(Integer id);

    @Insert("INSERT into dept (name, create_time, update_time) values (#{name}, #{createTime}, #{updateTime})")
    void addDept(Dept dept);

    @Select("SELECT id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    @Update("UPDATE dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
