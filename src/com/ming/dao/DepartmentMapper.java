package com.ming.dao;

import com.ming.po.Department;
import org.apache.ibatis.annotations.Param;

public interface DepartmentMapper {

    public Department getDepartmentListById(@Param("id") Integer id);
}
