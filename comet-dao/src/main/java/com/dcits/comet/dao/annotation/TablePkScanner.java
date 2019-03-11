package com.dcits.comet.dao.annotation;

import com.dcits.comet.dao.model.BasePo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TablePk注解扫描器
 *
 * @author Tim
 * @version V1.0
 * @description
 * @update 2015年2月10日 下午1:17:40
 */

public class TablePkScanner {

    private final static Map<String, String[]> PKS = new ConcurrentHashMap<>();

    public static <T extends BasePo> String[] pkColsScanner(T mapper) {
        String key = mapper.getClass().getName();
        if (PKS.containsKey(key)) {
            return PKS.get(key);
        }
        List<String> colsName = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        Class<? extends BasePo> currentClass = mapper.getClass();
        Field[] fields = currentClass.getDeclaredFields();
        for (Field elem : fields) {
            if (elem.isAnnotationPresent(TablePk.class)) {
                TablePk annotation = elem.getAnnotation(TablePk.class);
                colsName.add(elem.getName());
                index.add(annotation.index());
            }
        }
        Integer[] indexs = new Integer[index.size()];
        indexs = index.toArray(indexs);
        String[] cols = new String[index.size()];
        cols = colsName.toArray(cols);
        int temp = 0;
        String col = null;
        // 冒泡排序
        for (int j = 0; j < indexs.length; j++) {
            for (int i = j; i < indexs.length - 1; i++) {
                if (indexs[j] > indexs[i + 1]) {
                    temp = indexs[j];
                    indexs[j] = indexs[i + 1];
                    indexs[i + 1] = temp;
                    col = cols[j];
                    cols[j] = cols[i + 1];
                    cols[i + 1] = col;
                }
            }
        }
        PKS.put(key, cols);
        return cols;
    }
}
