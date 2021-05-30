package com.ming.typehandler;

import com.ming.po.EmpStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义类型处理器
 *
 * 实现TypeHandler或者继承BaseTypeHandler
 *
 */
public class MyTypeHandler implements TypeHandler<EmpStatus> {
     // 定义当前数据如何保存到数据库中
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, EmpStatus empStatus, JdbcType jdbcType) throws SQLException {
        System.out.println("数据库要保存的状态码："+empStatus.getCode());
        preparedStatement.setString(i,empStatus.getCode().toString());
    }

    @Override
    public EmpStatus getResult(ResultSet resultSet, String s) throws SQLException {
        //根据从数据库中拿到枚举状态码返回枚举对象
        int code = resultSet.getInt(s);
        System.out.println("从数据库中获取的状态码："+code);
        return EmpStatus.getEmpByCode(code);
    }

    @Override
    public EmpStatus getResult(ResultSet resultSet, int i) throws SQLException {
        //根据从数据库中拿到枚举状态码返回枚举对象
        int code = resultSet.getInt(i);
        System.out.println("从数据库中获取的状态码："+code);
        return EmpStatus.getEmpByCode(code);
    }

    @Override
    public EmpStatus getResult(CallableStatement callableStatement, int i) throws SQLException {
        //根据从数据库中拿到枚举状态码返回枚举对象
        int code = callableStatement.getInt(i);
        System.out.println("从数据库中获取的状态码："+code);
        return EmpStatus.getEmpByCode(code);
    }
}
