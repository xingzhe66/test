package com.dcits.comet.dao;

/**
 * @ClassName Route
 * @Author leijian
 * @Date 2019/5/21 9:52
 * @Description TODO
 * @Version 1.0
 **/
public interface Route {

    void buildDbIndex(String dbIndex, String tableId);

    void close();
}
