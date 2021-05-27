package com.ming.test;

import com.github.pagehelper.PageHelper;
import com.ming.dao.DepartmentMapper;
import com.ming.dao.EmployeeMapper;
import com.ming.po.Department;
import com.ming.po.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    /**
     * mybatis运行原理以查询为例
     * @throws IOException
     */
    @Test
    public void testInterface() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.getEmpByID(1);
            System.out.println(employee);

        }finally {
            sqlSession.close();
        }
    }

    /**
     *  插件原理
     *
     *   protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {

     *     this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
     *     this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, parameterHandler, resultHandler, boundSql);
     *   }
     *    configuration.newParameterHandler创建四大对象时调用parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
     *
     *     public Object pluginAll(Object target) {
     *     for (Interceptor interceptor : interceptors) {
     *       target = interceptor.plugin(target);
     *     }
     *     return target;
     *   }
     *   在四大对象创建的时候
     *   1、每个创建出来的对象不是直接返回的，而是调用pluginAll()返回
     *   2、获取所有的interceptor(拦截器)（插件需要实现的接口）调用interceptor.plugin(target);返回target包装后的对象
     *   3、插件机制、我们可以使用插件为目标对象创建一个代理对象 AOP机制
     *   我们的插件可以为四大对象创建出代理对象
     *   代理对象可以拦截到四大对象的每一个执行方法
     */
    //步骤
    //1、编写Interceptor接口的实现类
    // 2、使用@Intercepts完成插件签名
    //3、将写好的插件注册到全局配置文件中
    @Test
    public void testPlugin() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        PageHelper.startPage(1,10);
        List<Employee> empList = employeeMapper.getEmpList();
        empList.stream().forEach(System.out::println);
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


    @Test
    public  void addBatchEmp() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //可以执行批量操作的sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            for (int i = 0; i < 1000; i++) {
                Employee employee = new Employee();
                employee.setLastName(UUID.randomUUID().toString().substring(0,5));
                employee.setEmail("524933@qq.com");
                employee.setGender("男");
                employeeMapper.addEmps(employee);
            }
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

    @Test
    public void procedureTest() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> emps = employeeMapper.getEmps(1);
            System.out.println(emps);
        }finally {
            sqlSession.close();
        }
    }
}
