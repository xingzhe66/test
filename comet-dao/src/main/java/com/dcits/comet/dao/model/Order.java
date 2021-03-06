
package com.dcits.comet.dao.model;

import java.io.Serializable;
/**
 * @author wangyun
 * @date 2019/3/21
 * @description 排序
 * 可以将排序写在sql中，也可以使用此类，此类会在拦截器中转换成sql
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -9052322409478952879L;
    private String propertyName;
    private String columnName;
    private String sort;

    public static Order asc(String propertyName) {
        return new Order(propertyName, " ASC");
    }

    public static Order desc(String propertyName) {
        return new Order(propertyName, " DESC");
    }

    private Order(String propertyName, String sort) {
        this.propertyName = propertyName;
        this.columnName = propertyName;
        this.sort = sort;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getSort() {
        return this.sort;
    }
}
