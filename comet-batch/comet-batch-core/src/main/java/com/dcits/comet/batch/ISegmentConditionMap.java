package com.dcits.comet.batch;

import com.dcits.comet.batch.param.BatchContext;

import java.util.HashMap;
import java.util.Map;

public interface ISegmentConditionMap {
   default Map getSegmentConditionMap(BatchContext batchContext){
       return new HashMap();
   }

}
