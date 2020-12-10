package com.ming.dao;

import com.ming.po.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    Employee findEmList(Integer id);

    List<Employee> finEmpByMap(List<Employee> list);

    public Employee getEmpByID(@Param("id") Integer id);

    public void addEmp(Employee employee);

    public void deleteEmpByID(Integer id);

    public void updateEmp(Employee employee);

    public List<Employee> getEmpByIn(Map<String,Object> empMap);
}
