package com.ming.dao;

import com.ming.po.Employee;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 没有sql映射文件，所有的sql都是利用注解写在接口上
 */
public interface EmployeeMapperAnnotation {

    @Select("select id,last_name AS lastName,gender,email from tbl_employee where id=#{id}")
    public Employee getEmpByID(Integer id);

}
