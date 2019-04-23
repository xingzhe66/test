/**
 * <p>Title: Strings.java</p>
 * <p>Description: 字符串工具类
 * 目前实现了左右填充字符和Exception堆栈转为字符串</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: dcits</p>
 *
 * @author Tim
 * @update 2014年9月15日 下午2:02:19
 * @version V1.0
 */
package com.dcits.comet.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Tim
 * @version V1.0
 * @description 异常工具类
 * @update 2014年9月15日 下午2:08:12
 */

public class ExceptionUtils {

    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return "NULL";
        }
        StringBuffer b = new StringBuffer();
        b.append(t.getMessage());
        b.append("\n");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        t.printStackTrace(ps);
        b.append(baos.toString());
        return b.toString();
    }
}
