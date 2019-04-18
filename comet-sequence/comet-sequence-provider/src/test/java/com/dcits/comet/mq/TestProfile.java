package com.dcits.comet.mq;

import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Profile("test")
@Configuration
public class TestProfile {

    List<Map<String, Object>> datas = new ArrayList<>();

    {
        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put("id", 1);
        row1.put("name", "gary1");
        row1.put("age", 31);
        datas.add(row1);

        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put("id", 1);
        row2.put("name", "gary");
        row2.put("age", 25);
        datas.add(row2);
    }


    boolean started = false;
    Iterator<Map<String, Object>> iterator;
    Map<String, Object> currentMap;


    @Bean("datasource")
    public DataSource getDataSource() {
        DataSource dataSource = Mockito.mock(DataSource.class);
        Connection conn = Mockito.mock(Connection.class);
        Statement stmt = Mockito.mock(Statement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        try {
            dataSource.getConnection();
            Mockito.when(dataSource.getConnection()).then(t -> {
                System.out.println("get conn");
                return conn;
            });

            Mockito.doAnswer(t -> {
                Object[] arguments = t.getArguments();
                System.out.println("set autocommit " + arguments[0]);
                return DoesNothing.doesNothing();
            }).when(conn).setAutoCommit(Mockito.anyBoolean());

            Mockito.doAnswer(t -> {
                System.out.println("commit");
                return DoesNothing.doesNothing();
            }).when(conn).commit();
            Mockito.doAnswer(t -> {
                System.out.println("rollback");
                return DoesNothing.doesNothing();
            }).when(conn).rollback();
            Mockito.doAnswer(t -> {
                System.out.println("close");
                return DoesNothing.doesNothing();
            }).when(conn).close();
            Mockito.when(conn.createStatement()).thenReturn(stmt);

            Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(rs);

            Mockito.doAnswer(t -> {
                if (!started) {
                    started = true;
                    iterator = datas.iterator();
                }
                boolean flag = iterator.hasNext();
                if (!flag) {
                    started = false;
                    iterator = null;
                }
                if (started) {
                    currentMap = iterator.next();
                }
                return flag;
            }).when(rs).next();


            Mockito.doAnswer(t -> {
                Object[] params = t.getArguments();
                return currentMap.get(params[0]);
            }).when(rs).getObject(Mockito.anyString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
