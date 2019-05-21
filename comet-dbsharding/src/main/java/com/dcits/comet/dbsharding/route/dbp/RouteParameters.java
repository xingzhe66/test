package com.dcits.comet.dbsharding.route.dbp;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @ClassName RouteParameters
 * @Author leijian
 * @Date 2019/5/21 9:52
 * @Description TODO
 * @Version 1.0
 **/
public class RouteParameters {

    private static final String GROUP_ID = "GROUP_ID";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String DB_INDEX = "DB_INDEX";
    private static final String TABLE_ID = "TABLE_ID";
    private static final String ELASTIC_ID = "ELASTIC_ID";
    private static final String SCAN_ALL = "SCAN_ALL()";

    private static final String HINT_PREFIX = "/*+DBP: $ROUTE={";

    private static final String HINT_SUFFIX = "}*/";

    private String groupId;

    private List<String> targetTables;

    private String dbIndex;

    private String tableId;

    private String elasticId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getTargetTables() {
        return targetTables;
    }

    public void setTargetTables(List<String> targetTables) {
        this.targetTables = targetTables;
    }

    public String getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(String dbIndex) {
        this.dbIndex = dbIndex;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getElasticId() {
        return elasticId;
    }

    public void setElasticId(String elasticId) {
        this.elasticId = elasticId;
    }

    private String format(String prefix, String value) {
        return prefix + "(" + value + ")";
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        if (groupId != null) {
            stringJoiner.add(format(GROUP_ID, groupId));
        }
        if (targetTables != null && !targetTables.isEmpty()) {
            stringJoiner.add(format(TABLE_NAME,
                    targetTables.stream().collect(Collectors.joining(","))));
        }
        if (dbIndex != null) {
            stringJoiner.add(format(DB_INDEX, dbIndex));
        }
        if (tableId != null) {
            stringJoiner.add(format(TABLE_ID, tableId));
        }
        if (elasticId != null) {
            stringJoiner.add(format(ELASTIC_ID, elasticId));
        }
        return stringJoiner.toString();
    }

    public String getProcessingSql() {
        return HINT_PREFIX + this.toString() + HINT_SUFFIX;
    }
}
