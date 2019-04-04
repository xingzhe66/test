package com.dcits.comet.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/3 15:15
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestProfile.class)
@ActiveProfiles("test")
public class SimpleTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test01() throws Exception {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from t1");
        while (rs.next()) {
            System.out.println(rs.getObject("id") + "--" + rs.getObject("name"));
        }
        connection.close();
    }


    @Test
    public void test() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void simpleTest() {
        //创建mock对象，参数可以是类，也可以是接口
        List<String> list = Mockito.mock(List.class);
        //设置方法的预期返回值 （如果list.get(0) 被调用 ，调用之后返回 helloworld）
        //当然前提是要创建了Mock对象，如这里就是创建了跟 List相关的 Mock对象
        //这里还看不出什么作用，因为Mock 还看不出来，List很容易就能创建
        //假如是一个很复杂的对象，构造这样一个对象很有难度，使用Mock就很方便了，我们不用去一步一步填充它的属性去构造，
        //只需要Mock 一下，就可以拿到这个对象，去测试它的方法，（当然，如果方法有参数我们是需要传递的，像get(0)）
        Mockito.when(list.get(0)).thenReturn("helloworld");
        //list.get(0)的调用就会返回 helloworld
        String result = list.get(0);
        System.out.println(result);
        //验证方法调用(是否调用了get(0))
        Mockito.verify(list).get(0);
        Assert.assertEquals("helloworld", result);
    }

    @Test
    public void WorkerNodePo() throws SQLException {
        //创建mock对象，参数可以是类，也可以是接口
        Mockito.when(dataSource.getConnection().getSchema()).thenReturn("123");

    }
}
