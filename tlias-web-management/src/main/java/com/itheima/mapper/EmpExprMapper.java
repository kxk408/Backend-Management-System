package com.itheima.mapper;

import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface EmpExprMapper {

    public void insertBatch(@Param("exprList") List<EmpExpr> exprList);

//    @Delete("delete from emp_expr where emp_id in #{ids}")
    void deleteByEmpIds(@Param("empIds") List<Integer> empIds);
}
