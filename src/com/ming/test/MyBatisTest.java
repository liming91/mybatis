package com.ming.test;

import com.ming.dao.DepartmentMapper;
import com.ming.dao.EmployeeMapper;
import com.ming.po.Department;
import com.ming.po.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试mybatis-config.xml
 */
public class MyBatisTest {
    Logger log = Logger.getLogger(MyBatisTest.class);
    public static SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    /**
     * 1、根据xml配置文件（全局配置文件mybatis-config.xml）创建一个SqlsessionFactory对象，mybatis-config.xml有数据源一些环境信息
     * 2、sql映射文件EmployeeMapper.xml配置了每一个sql，以及sql的封装规则等。
     * 3、将sql映射文件注册在全局配置文件中
     * 4、写代码：
     * 4.1.根据全局配置文件得到sqlsessionFactory
     * 4.2.使用SqlSession工程进行crud,sqlseesion就代表和数据库进行会话，用完close
     * 4.3.使用sql标识告知mybatis来执行哪个sql,sql都是保存在sql映射文件中
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //2、获取SqlSession实例，能直接执行已经映射了的sql语句，selectOne：sql唯一标识，执行sql要用到的参数
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            Employee employee = openSession.selectOne("com.ming.dao.EmployeeMapper.getEmpByID", 1);
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testInterface() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.getEmpByID(1);
            System.out.println(employee);
            Employee employee2 = employeeMapper.getEmpByID(5);
            System.out.println(employee2);
            System.out.println(employee==employee2);

        }finally {
            sqlSession.close();
        }
    }
        @Test
    public  void addEmp() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee();
            employee.setLastName("张三");
            employee.setEmail("524933@qq.com");
            employee.setGender("男");
            employeeMapper.addEmp(employee);
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

/*    @Test
    public void findEmpByMap() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee();
            employee.setLastName("tom");
            employee.setGender("女");
            Employee employee1 = new Employee();
            employee1.setLastName("李四");
            employee1.setGender("男");
            Employee employee2 = new Employee();
            employee2.setLastName("张三");
            employee2.setGender("男");
            List<Employee> list = new ArrayList<Employee>();
            list.add(employee);
            list.add(employee1);
            list.add(employee2);
            List<Employee> employees = employeeMapper.finEmpByMap(list);
            System.out.println(employees);
        }finally {
            sqlSession.close();
        }
    }*/

    @Test
    public void findEmpList() throws Throwable {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.findEmList(6);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void findDeptList() throws Throwable {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            Department department = departmentMapper.getDepartmentListById(2);
            System.out.println(department);
            System.out.println(department.getEmployeeList());
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void findDeptIn() throws Throwable {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Integer> list = new ArrayList<>();
            list.add(1);
            list.add(5);
            list.add(6);
            Map<String,Object> map = new HashMap<>();
            map.put("ids",list);
          List<Employee>  employeeList =  employeeMapper.getEmpByIn(map);
            System.out.println(employeeList);
        }finally {
            sqlSession.close();
        }
    }
}
