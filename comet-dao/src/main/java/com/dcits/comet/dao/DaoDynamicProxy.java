package com.dcits.comet.dao;

import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Dao方法代理，统一Dao方法
 * 参数使用ParamSupport接口，封装缓存
 * 其他使用daoSupport接口
 *
 * @author ChengLiang
 */
public class DaoDynamicProxy implements InvocationHandler {

    /**
     * 代理的真实对象
     * 数据库访问对象
     */
    private DaoSupport daoSupport;


    /**
     * 代理的真实对象
     * 业务参数访问对象
     * 会使用cache
     */
    private ParamSupport paramSupport;

    /**
     * 返回代理对象
     *
     * @param daoSupport
     * @param paramSupport
     * @return
     */
    public Object getProxyObject(DaoSupport daoSupport, ParamSupport paramSupport) {
        this.daoSupport = daoSupport;
        this.paramSupport = paramSupport;

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象
         * 第一个参数 daoSupport.getClass().getClassLoader() ，使用daoSupport类的ClassLoader对象来加载我们的代理对象
         * 第二个参数 daoSupport.getClass().getInterfaces()，为代理对象提供的接口是真实对象所实行的接口
         * 第三个参数 代理自身， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        return Proxy.newProxyInstance(daoSupport.getClass().getClassLoader(), daoSupport.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object result;
        boolean isParamTable = false;
        for (Object arg : args) {
            if (arg instanceof BasePo) {
                isParamTable = isParamTableType((BasePo) arg);
            }
        }

        //业务参数表
        if (isParamTable) {
            //当代理对象调用真实对象的方法时
            // 其会自动的跳转到代理对象关联的paramSupport对象的invoke方法来进行调用
            result = method.invoke(paramSupport, args);
        } else {
            //当代理对象调用真实对象的方法时
            // 其会自动的跳转到代理对象关联的daoSupport对象的invoke方法来进行调用
            result = method.invoke(daoSupport, args);
        }

        return result;
    }

    /**
     * 判断是否为业务参数表
     * <p>
     * 示例：@TableType(TableTypeEnum.PARAM)
     *
     * @param basePo
     * @return
     */
    private boolean isParamTableType(BasePo basePo) {
        boolean ret = false;
        boolean hasAnnotation = basePo.getClass().isAnnotationPresent(TableType.class);
        if (hasAnnotation) {
            TableType[] tableTypes = basePo.getClass().getAnnotationsByType(TableType.class);
            if (tableTypes != null) {
                for (TableType tableType : tableTypes) {
                    if (tableType.value().equals(TableTypeEnum.PARAM)) {
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }
}
