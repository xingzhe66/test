package com.dcits;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @Author wangyun
 * @Date 2019/4/29
 **/
public class ClazzTest {

    @Test
    public void test(){
        String classname = "com.dcits.yunyun.entity.SysLog";

        try {
            Class c = Class.forName(classname);
            Object obj = c.newInstance();
            Method mt = c.getMethod("setId",java.lang.Long.class);
            mt.invoke(obj,1L);
//            Method mt2 = c.getMethod("setName");
//            mt2.invoke(obj, "测试1");
//            // Object obj2 = c.newInstance();
//            Method mt3 = c.getMethod("setVal");
//            mt3.invoke(obj, (long) 100);
//            this.dao.insert((BasePo) obj);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue(true);

    }

}
