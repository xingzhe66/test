package com.dcits.comet.batch.helper;

import com.dcits.comet.batch.entity.TablePO;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dbsharding.helper.ShardingDataSourceHelper;
import com.dcits.comet.dbsharding.route.Route;
import com.dcits.comet.dbsharding.route.RouteProxy;
import com.dcits.comet.dbsharding.route.dbSharding.DbShardingHintManager;
import com.dcits.comet.dbsharding.route.dbp.DbpHintManager;
import io.shardingsphere.core.rule.ShardingDataSourceNames;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.shardingjdbc.jdbc.core.ShardingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/21 14:41
 **/
@Slf4j
public class HintManagerHelper {


    /**
     * @param entity 实体类
     * @return java.util.List<java.lang.String>
     * @author leijian
     * @Description //TODO
     * @date 2019/5/21 15:36
     **/
    public static List<String> getNodeList(Class entity) {
        List<String> list = new LinkedList<>();
        boolean hasAnnotation = false;
        TableType tableTypes = null;
        hasAnnotation = entity.isAnnotationPresent(TableType.class);
        if (hasAnnotation) {
            tableTypes = (TableType) entity.getAnnotation(TableType.class);
        }
        try {
            ShardingContext shardingContext = ShardingDataSourceHelper.getShardingContext();
            ShardingRule shardingRule = shardingContext.getShardingRule();
            ShardingDataSourceNames shardingDataSourceNames = shardingRule.getShardingDataSourceNames();
            String defaultDataSourceName = shardingDataSourceNames.getDefaultDataSourceName();
            //3.涉及到level表,返所有库的节点编号
            if (tableTypes != null && TableTypeEnum.LEVEL.name().equalsIgnoreCase(tableTypes.name())) {
                return (List<String>) shardingRule.getShardingDataSourceNames().getDataSourceNames();
            }
            //1.不涉及到分库分表直接返回默认库
            //2.涉及到up只返回默认库名
            list.add(defaultDataSourceName);
            return list;
        } catch (NoSuchBeanDefinitionException e) {
            ////TODO 需要实现基于DBP
            try {
                LinkedHashSet linkedHashSet = ShardingDataSourceHelper.getDataSourceNames();
                for (Object node : linkedHashSet) {
                    list.add(String.valueOf(node));
                }
            } catch (NoSuchBeanDefinitionException e1) {
                //两者的javaBean均不在的情况，默认是单库的JDBC的DataSource
                list.add("standalone");
            }
        }
        return list;
    }

    /**
     * @param [entity, node, daoSupport]
     * @return int
     * @author leijian
     * @Description 根据表明查询总条数
     * @date 2019/5/21 16:12
     **/
    public static int getCountNum(Class entity, String node, DaoSupport daoSupport) {
        Route route = null;
        boolean hasAnnotation = false;
        TableType tableTypes = null;
        hasAnnotation = entity.isAnnotationPresent(TableType.class);
        if (hasAnnotation) {
            tableTypes = (TableType) entity.getAnnotation(TableType.class);
        }
        try {
            route = DbpHintManager.getInstance();
            RouteProxy routeProxy = new RouteProxy(route);
            routeProxy.getProxy().buildDbIndex(node, "");
            HashMap map = new HashMap();
            map.put("table", tableTypes.name());
            return daoSupport.count(TablePO.class.getName()+".SelectCount", map);
        } catch (Exception e) {
            log.error("{}", e);
        } finally {
            route.close();
        }
        return 0;
    }
}
