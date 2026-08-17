package com.kxk.mapper;

import com.kxk.pojo.Emp;
import com.kxk.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
//    @Select("select count(*) from emp left join dept on emp.dept_id = dept.id")
//    public Long count();
//
//    @Select("select emp.*, dept.name deptName from emp left join dept on emp.dept_id = dept.id " +
//            "order by emp.update_time desc limit #{start}, #{pageSize}")
//    public List<Emp> list(@Param("start")Integer start, @Param("pageSize")Integer pageSize);

//        @Select("select emp.*, dept.name deptName from emp left join dept on emp.dept_id = dept.id " +
//            "where name = %#{name}% and gender = #{gender} " +
//                "and emp.create_time between #{begin} and #{end}" +
//                "order by emp.update_time desc")
//        public List<Emp> list(@Param("name")String name, @Param("gender")Integer gender,
//                              @Param("begin")String begin, @Param("end")String end);
        public List<Emp> list(EmpQueryParam empQueryParam);

        @Options(useGeneratedKeys = true, keyProperty = "id")
        @Insert("insert into emp(username, password, name, gender, phone, job, salary, image," +
                " entry_date, dept_id, create_time, update_time) " +
                "values (#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}," +
                " #{entryDate}, #{deptId}, #{createTime}, #{updateTime}) ")
        void insert(Emp emp);

//        @Delete("delete from emp where id in #{ids}")
        void deleteByIds(@Param("ids") List<Integer> ids);

//        @Select("select * from emp where id = #{id}")
        Emp getById(Integer id);

//        @Update("update emp set username = #{username}, name = #{name}, gender = #{gender}, " +
//                "phone = #{phone}, job = #{job}, salary = #{salary}, image = #{image}, " +
//                "entry_date = #{entryDate}, dept_id = #{deptId}, update_time = #{updateTime} " +
//                "where id = #{id}")
        void updateById(Emp emp);


        @MapKey("pos")
        List<Map<String, Object>> countEmpJobData();

        @MapKey("name")
        List<Map<String, Object>> countEmpGenderData();

        /**
         * 仅按用户名查询（含 password 哈希），密码比对在 Service 层用 PasswordEncoder 完成。
         */
        @Select("select id, username, name, password from emp where username = #{username}")
        Emp selectByUsername(String username);

        @Select("select id, username, password from emp")
        List<Emp> listIdUsernamePassword();

        @Update("update emp set password = #{password} where id = #{id}")
        void updatePasswordById(@Param("id") Integer id, @Param("password") String password);
}
