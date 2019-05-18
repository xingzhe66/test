package com.dcits.comet;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/18 19:22
 **/
public class Main {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new HikariDataSource();
        ((HikariDataSource) dataSource).setJdbcUrl("jdbc:mysql://10.7.25.205:3306/workflow?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        ((HikariDataSource) dataSource).setUsername("rb");
        ((HikariDataSource) dataSource).setPassword("123456");
        ((HikariDataSource) dataSource).setDriverClassName("com.mysql.jdbc.Driver");


        DataSource dataSource2 = new HikariDataSource();
        ((HikariDataSource) dataSource2).setJdbcUrl("jdbc:mysql://127.0.0.1:3306/workflow?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        ((HikariDataSource) dataSource2).setUsername("root");
        ((HikariDataSource) dataSource2).setPassword("root");
        ((HikariDataSource) dataSource2).setDriverClassName("com.mysql.jdbc.Driver");

        Connection connection2 = dataSource2.getConnection();


        String sql = "SELECT * FROM WORKER_NODE";
        String insertSql = " INSERT  INTO WORKER_NODE (HOST_NAME, PORT, TYPE, LAUNCH_DATE,BIZ_TAG, STEP,MIN_SEQ, MIDDLE_ID, MAX_SEQ, CURR_SEQ, COUNT_SEQ,SEQ_CYCLE,SEQ_CACHE,CACHE_COUNT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = connection2.prepareStatement(insertSql);
                preparedStatement1.setString(1, resultSet.getString("HOST_NAME"));
                preparedStatement1.setString(2, resultSet.getString("PORT"));
                preparedStatement1.setString(3, resultSet.getString("TYPE"));
                preparedStatement1.setDate(4, resultSet.getDate("LAUNCH_DATE"));
                preparedStatement1.setString(5, resultSet.getString("BIZ_TAG"));
                //STEP
                preparedStatement1.setInt(6, 1);
                preparedStatement1.setLong(7, resultSet.getLong("MIN_SEQ"));
                preparedStatement1.setString(8, "0.75");
                preparedStatement1.setLong(9, resultSet.getLong("MAX_SEQ"));
                preparedStatement1.setLong(10,resultSet.getLong("CURR_SEQ"));
                preparedStatement1.setLong(11, resultSet.getLong("COUNT_SEQ"));
                preparedStatement1.setString(12, "Y");
                preparedStatement1.setLong(13, resultSet.getLong("SEQ_CACHE"));
                preparedStatement1.setLong(14, resultSet.getLong("CACHE_COUNT"));
                preparedStatement1.executeUpdate();
                //connection2.commit();
            }
            //connection.commit();
        } catch (Exception e) {
           System.out.println(e);
        } finally {

        }
        System.out.println(connection2);
    }
}
