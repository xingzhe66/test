package com.dcits.comet.dao.interceptor;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/5/23 18:22
 **/
public class SelectSegmentHelper {

    private static ThreadLocal<Boolean> selectSegmentHelper = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public SelectSegmentHelper() {
    }

    public static boolean isSelectSegment() {
        return ((Boolean) selectSegmentHelper.get()).booleanValue();
    }

    public static void setSelectSegment() {
        selectSegmentHelper.set(true);
    }

    public static void cancelSelectSegment() {
        selectSegmentHelper.set(false);
    }

    public static String getSelectSegment(String dbType, String tmpSql, String pageSize) {
        switch (dbType.toLowerCase()) {
            case "oracle":
                return "select MIN(KEY_FIELD) START_KEY, MAX(KEY_FIELD) END_KEY from(" +
                        tmpSql + ") group by trunc((rownum - 1)/" + pageSize + ") order by START_KEY";

            case "mysql":
                return "SELECT\r\n" +
                        "	MIN(KEY_FIELD) START_KEY,\r\n" +
                        "	MAX(KEY_FIELD) END_KEY,\r\n" +
                        "	count(1) ROW_COUNT\r\n" +
                        "FROM\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			KEY_FIELD ,@rownum:=@rownum + 1 AS rownum\r\n" +
                        "	\r\n" +
                        "FROM\r\n" +
                        "	(" + tmpSql + ") t1,\r\n" +
                        "	(SELECT @rownum := - 1) t\r\n" +
                        "ORDER BY\r\n" +
                        "	t1.key_field\r\n" +
                        ") tt\r\n" +
                        "GROUP BY\r\n" +
                        "	floor(tt.rownum / " + pageSize + ")";
            default:
                return tmpSql;
        }
    }

}
