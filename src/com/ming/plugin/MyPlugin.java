package com.ming.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Statement;
import java.util.Properties;

/**
 *
 *
 * 使用@Intercepts注解完成插件签名：告诉mybatis当前插件用来拦截哪个对象的哪个方法
 */
//type:拦截的四大对象之一StatementHandler（处理SQL语句预编译设置参数），method:对象的方法（parameterize设置参数） args:对象参数的列表
@Intercepts(
        {
             @Signature(type = StatementHandler.class,method ="parameterize",args = Statement.class)
        })
public class MyPlugin implements Interceptor{

    /**
     * 拦截目标对象的目标方法的执行
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyPlugin....intercept:自定义插件拦截的目标方法"+invocation.getMethod());
        //intercept()可以在目标方法放行前后做一些操作、动态修改mybatis运行流程
        //操作：动态改变sql运行的参数、比如以前查询id为1的员工、实际从数据库查询3号员工
        //拿到StatementHandler-->ParameterHandler->parameterObject的值
        //拦截到的目标对象
        Object target = invocation.getTarget();
        //拿到target元数据
        System.out.println("拦截到的目标对象："+invocation.getTarget());
        //1、分离代理对象。由于会形成多次代理，所以需要通过一个 while 循环分离出最终被代理对象，从而方便提取信息
        MetaObject metaObject = SystemMetaObject.forObject(target);
        //2、获取到代理对象中包含的被代理的真实对象
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println("sql语句用的参数是："+value);
        //修改完sql语句要用的参数
        metaObject.setValue("parameterHandler.parameterObject","5");


        //放行、执行目标方法
        Object proceed = invocation.proceed();
        //返回执行后的返回值
        return proceed;
    }

    /**
     * plugin:包装目标对象的，所谓的包装就是为目标对象创建代理对象
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        System.out.println("MyPlugin...四大对象创建的时候被调用....plugin:Mybatis将要包装的对象"+target);
        //可以借助 Plugin.wrap()方法使用当前的Intercept包装我们目标对象
        Object wrap = Plugin.wrap(target,this);

        //返回为当前target创建的动态代理
        return wrap;
    }

    /**
     * 将插件注册时的Property属性设置进来
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的信息properties："+properties);
    }


}
