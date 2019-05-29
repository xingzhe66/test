package com.dcits.comet.batch;

import com.dcits.comet.batch.helper.HintManagerHelper;
import com.dcits.comet.batch.holder.SpringContextHolder;
import com.dcits.comet.batch.param.BatchContext;
import com.dcits.comet.commons.utils.BeanUtil;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dbsharding.route.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wangyun
 * @Date 2019/5/23
 **/
@Component
@Slf4j
public class AbstractSegmentStep<T, O> implements ISegmentStep<T, O> {

    public static final String STEP_NAME = "stepName";
    public static final String COMET_KEY_FIELD = "cometKeyField";
    public static final String COMET_START = "cometStart";
    public static final String COMET_END = "cometEnd";
    public static final String COMET_BATCH_CONDITION_MAP = "Comet_Batch_Condition_Map";

    @Autowired
    private DaoSupport dao;

//    @Override
//    public List<Segment> getThreadSegmentList(BatchContext batchContext, Comparable allStart,Comparable allEnd, String node){
//        List<Segment> list=new ArrayList<>();
//        Segment segment=new Segment();
//        segment.setStartKey(String.valueOf(allStart));
//        segment.setEndKey(String.valueOf(allEnd));
//        segment.setRowCount(0);
//        list.add(segment);
//        return list;
//    }

    @Override
    public List<String> getNodeList(BatchContext batchContext) {
        return HintManagerHelper.getNodeList(getTClass());
    }

    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    @Override
    public List<Segment> getSegmentList(BatchContext batchContext, String node, Integer segmentSize, String keyField, String stepName) {
        return querySegmentList(batchContext, node, segmentSize, keyField, stepName);
    }

    @Override
    public List<Segment> getThreadSegmentList(BatchContext batchContext, Comparable allStart, Comparable allEnd, String node, Integer pageSize, String keyField, String stepName) {
        return querySegmentList(batchContext, node, pageSize, keyField, stepName);
    }

    private List<Segment> querySegmentList(BatchContext batchContext, String node, Integer pageSize, String keyField, String stepName) {
        Route route = null;
        try {
            route = HintManagerHelper.getInstance(getTClass(), node);
            Map<String, Object> map = new HashMap();
            map.put(COMET_KEY_FIELD, keyField);
            try {
                ApplicationContext ap = SpringContextHolder.getApplicationContext();
                ISegmentConditionMap segmentConditionMap = ap.getBean(ISegmentConditionMap.class);
                if (segmentConditionMap != null) {
                    map.putAll(segmentConditionMap.getSegmentConditionMap(batchContext));
                }
            } catch (NoSuchBeanDefinitionException e) {
                log.warn("No bean named '{}' available", ISegmentConditionMap.class.getName());
            } finally {
            }
            log.debug("querySegmentList查询传入参数[{}]", map.toString());
            return dao.selectSegmentList(getTClass().getName() + "." + stepName, map, pageSize);
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        } finally {
            route.close();
        }
    }

    @Override
    public List<T> getPageList(BatchContext batchContext, Comparable start, Comparable end, String node, String stepName) {
        log.info("StepName: [{}] ,DataBaseNode: [{}] SplitKey: [{} -- {}]", stepName, node,start, end);

        Route route = null;
        try {
            route = HintManagerHelper.getInstance(getTClass(), node);
            Map<String, Object> map = new HashMap();
            map.put(COMET_START, start);
            map.put(COMET_END, end);
            ApplicationContext ap = SpringContextHolder.getApplicationContext();
            ISegmentConditionMap segmentConditionMap = ap.getBean(ISegmentConditionMap.class);
            if (segmentConditionMap != null) {
                map.putAll(segmentConditionMap.getSegmentConditionMap(batchContext));
            }
            log.debug("getPageList查询传入参数 [{}]", map.toString());
            return BeanUtil.mapToBean((List<Map<String, Object>>) dao.selectList(getTClass().getName() + "." + stepName, map), getTClass());
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        } finally {
            route.close();
        }
    }

    @Override
    public void preBatchStep(BatchContext batchContext) {

    }

    @Override
    public O process(BatchContext batchContext, T item) {
        return (O) item;
    }

    @Override
    public void writeChunk(BatchContext batchContext, List<O> item) {

    }


    @Override
    public void afterBatchStep(BatchContext batchContext) {

    }
}
