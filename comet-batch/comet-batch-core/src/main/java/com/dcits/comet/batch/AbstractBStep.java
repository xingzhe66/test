package com.dcits.comet.batch;


import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.commons.utils.ClassLoaderUtils;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dbsharding.TableTypeMapContainer;
import com.dcits.comet.dbsharding.helper.ShardingDataSourceHelper;
import io.shardingsphere.api.HintManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description AbstractBStep抽象batch step
 */
public abstract class AbstractBStep<T, O> implements IBStep<T, O> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractBStep.class);

    //TODO 需要实现基于DBP
    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        List<String> list = new LinkedList<>();
        try {
            //shredingdataSource
            LinkedHashSet linkedHashSet = ShardingDataSourceHelper.getDataSourceNames();
            for (Object node : linkedHashSet) {
                list.add(String.valueOf(node));
            }
        } catch (NoSuchBeanDefinitionException e) {
            //DBP通过解析json文件获取
            try {
                LinkedHashSet linkedHashSet = ShardingDataSourceHelper.getDataSourceNames();
                for (Object node : linkedHashSet) {
                    list.add(String.valueOf(node));
                }
            } catch (NoSuchBeanDefinitionException e1) {
                //单库。未进行分库分表操作。主库
                list.add("standalone");
            }
        }


        return list;
    }

    //TODO 需要实现基于DBP
    @Override
    public int getTotalCountNum(BatchContext batchContext, String node) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        TableType tableTypes = null;
        boolean hasAnnotation = false;
        try {
            hasAnnotation = ClassLoaderUtils.loadClass(type.getTypeName()).isAnnotationPresent(TableType.class);
            if (hasAnnotation) {
                tableTypes = ClassLoaderUtils.loadClass(type.getTypeName()).getAnnotation(TableType.class);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (tableTypes != null) {
            if (hasAnnotation && TableTypeMapContainer.getMultiMap().containsValue(tableTypes.name())) {
                //涉及到分区表，强制路由node查询出数据值
                try (HintManager hintManager = HintManager.getInstance()) {
                    hintManager.setDatabaseShardingValue(node);
                    return getCountNum(batchContext, node);
                }
            }
        }
        return getCountNum(batchContext, node);

    }

    @Override
    public int getCountNum(BatchContext batchContext, String node) {
        return -1;
    }


    @Override
    public void preBatchStep(BatchContext batchContext) {
        // logger.debug("preBatchStep");
    }

    @Override
    public List<T> getPageList(BatchContext batchContext, int offset, int pageSize, String node) {
        List<T> list = new ArrayList();
        list.add((T) null);
        return list;
    }

    @Override
    public O process(BatchContext batchContext, T item) {
        return (O) item;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<O> items) {
        for (O item : items) {
            writeOne(batchContext, item);
        }
    }

    public void writeOne(BatchContext batchContext, O item) {

    }

    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }


}
