package com.ming.test;

import com.ming.dao.OrderMapper;
import com.ming.po.Order;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


public class OrderTest {

    public static  SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    /**
     * 一对一查询
     * @throws IOException
     */
    @Test
    public void findOrderOneToOrder() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper =  sqlSession.getMapper(OrderMapper.class);
        Order order = orderMapper.findOneOrderById(1);
        System.out.println(order);
    }
    /**
     * 一个订单对应多个订单详情
     * @throws IOException
     */
    @Test
    public void findOrderOneToMany() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper =  sqlSession.getMapper(OrderMapper.class);
        Order order = orderMapper.findOrderListById(1);
        System.out.println(order);
    }
}
