//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcits.comet.dao.interceptor;

public class SelectForUpdateHelper {
    private static String updateSql = " FOR UPDATE ";
    private static ThreadLocal<Boolean> selectForUpdateholder = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public SelectForUpdateHelper() {
    }

    public static boolean isSelectForUpdate() {
        return ((Boolean)selectForUpdateholder.get()).booleanValue();
    }

    public static void setSelectForUpdate() {
        selectForUpdateholder.set(true);
    }

    public static void cancelSelectForUpdate() {
        selectForUpdateholder.set(false);
    }

    public static String getUpdateSql() {
        return updateSql;
    }

    public void setWaitTime(String waitTime) {
        String updateSql = waitTime.toUpperCase();
        if (updateSql.length() > 0 && updateSql.indexOf("WAIT") == -1) {
            updateSql = "WAIT " + waitTime;
        }

        updateSql = " FOR UPDATE " + updateSql;
    }
}
