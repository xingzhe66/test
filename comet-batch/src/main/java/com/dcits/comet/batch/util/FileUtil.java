package com.dcits.comet.batch.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String CHAR = "char";

    public FileUtil() {
    }

    public static boolean exists(String fileName) {
        File f = new File(fileName);
        logger.debug(f.getAbsolutePath());
        return f.exists();
    }

    public static <T> long getFileRowCount(String fileName, Class<T> clazz) {
        if (!exists(fileName)) {
            throw new RuntimeException(fileName.concat("文件不存在"));
        } else {
            Data data = FileTemplateContext.getInstance().getConfig(TemplateType.READ, clazz.getName());
            int headLength = data.getHeadLength();
            int perRecordLength = data.getPerRecordLength();
            int endingLength = data.getEndingLength();
            long detailLen;
            if (!data.hasLineSeparator()) {
                long len = (new File(fileName)).length();
                detailLen = len - (long)headLength - (long)endingLength;
                if (detailLen % (long)perRecordLength != 0L) {
                    throw new RuntimeException("文件不完整");
                } else {
                    return detailLen / (long)perRecordLength;
                }
            } else {
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), data.getCharSet()));

                    int lineIndex;
                    for(lineIndex = 0; br.readLine() != null; ++lineIndex) {
                        ;
                    }

                    detailLen = (long)(lineIndex - headLength - endingLength);
                    return detailLen;
                } catch (Exception var18) {
                    throw new RuntimeException(var18);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException var17) {
                            ;
                        }
                    }

                }
            }
        }
    }

    public static <T> List<T> readFileToList(String fileName, Class<T> clazz, int startLineIndex, int maxLine) {
        if (!exists(fileName)) {
            throw new RuntimeException(fileName.concat("文件不存在"));
        } else {
            Data data = FileTemplateContext.getInstance().getConfig(TemplateType.READ, clazz.getName());
            return data.hasLineSeparator() ? readHasLineSeparatorFileToList(fileName, data, clazz, startLineIndex, maxLine) : readSequenceFileToList(fileName, data, clazz, startLineIndex, maxLine);
        }
    }

    private static <T> List<T> readSequenceFileToList(String fileName, Data data, Class<T> clazz, int startLineIndex, int maxLine) {
        int headLength = data.getHeadLength();
        int perRecordLength = data.getPerRecordLength();
        BufferedInputStream fis = null;

        try {
            fis = new BufferedInputStream(new FileInputStream(fileName));
            long startLineIndexL = (long)startLineIndex;
            long skipByte = startLineIndexL * (long)perRecordLength + (long)headLength;
            long actSkipByte = fis.skip(skipByte);
            if (skipByte != actSkipByte) {
                logger.error("startLineIndex：{},lineByteSize：{},headSize：{}", new Object[]{startLineIndexL, perRecordLength, headLength});
                logger.error("预计跳过的字节数为：{},实际跳过的字节数为：{}", skipByte, actSkipByte);
                throw new RuntimeException("读取数据错误");
            } else {
                List<T> list = new ArrayList();
                int lineNum = 0;

                for(byte[] buf = new byte[perRecordLength]; lineNum < maxLine && fis.read(buf) == perRecordLength; ++lineNum) {
                    String lineContent = new String(buf);
                    T instance = convert(data, clazz, lineContent, lineNum + startLineIndex);
                    list.add(instance);
                }

                return list;
            }
        } catch (Exception var26) {
            throw new RuntimeException("读取文件失败", var26);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var25) {
                    ;
                }
            }

        }
    }

    private static <T> List<T> readHasLineSeparatorFileToList(String fileName, Data data, Class<T> clazz, int startLineIndex, int maxLine) {
        List<T> list = new ArrayList();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), data.getCharSet()));
            int lineIndex = 0;

            for(startLineIndex += data.getHeadLength(); lineIndex < startLineIndex; ++lineIndex) {
                br.readLine();
            }

            String lineContent = null;
            lineIndex = 0;

            while(lineIndex < maxLine && (lineContent = br.readLine()) != null) {
                if (lineContent.length() != 0) {
                    T instance = convert(data, clazz, lineContent, lineIndex + startLineIndex);
                    list.add(instance);
                    ++lineIndex;
                }
            }

            return list;
        } catch (RuntimeException var18) {
            throw var18;
        } catch (Exception var19) {
            throw new RuntimeException("读取文件数据失败", var19);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var17) {
                    ;
                }
            }

        }
    }

    private static <T> T convert(Data data, Class<T> clazz, String lineContent, int index) throws Exception {
        List<Column> colList = data.getColumnList();
        String split = data.getSplit();
        int size = colList.size();
        T instance = clazz.newInstance();
        int a;
        if (data.hasSplit()) {
            String[] values = null;
            if (null != data.getSplitType() && CHAR.equals(data.getSplitType())) {
                values = charToString(lineContent + " ").split(split);
            } else {
                values = (lineContent + " ").split(split);
            }

            if (values.length != size) {
                throw new FileUtil.IllegalFileException("文件第" + index + "行字段数不符合");
            }

            values[values.length - 1] = values[values.length - 1].substring(0, values[values.length - 1].length() - 1);

            for(a = 0; a < values.length; ++a) {
                Column col = (Column)colList.get(a);
                parseValue(values[a], col, instance);
            }
        } else {
            byte[] lineData = lineContent.getBytes(data.getCharSet());
            a = 0;

            for(a = 0; a < lineData.length; ++a) {
                Column col = (Column)colList.get(a);
                String valueStr = LengthType.BYTE == data.getLengthType() ? new String(lineData, a, col.getLength(), data.getCharSet()) : lineContent.substring(a, a + col.getLength());
                parseValue(valueStr, col, instance);
                a += ((Column)colList.get(a)).getLength();
            }
        }

        return instance;
    }

    private static <T> void parseValue(String valueStr, Column column, T t) throws Exception {
        Method method = column.getWriteValueMethod();
        if (method != null) {
            Class<?> parameterType = method.getParameterTypes()[0];
            Pattern pattern = column.getPattern();
            if (column.getLength() > 0 && valueStr.length() > column.getLength()) {
                throw new FileUtil.IllegalFileException("属性[" + column.getName() + "]值[" + valueStr + "]超出定义的长度");
            } else if (pattern != null && !pattern.matcher(valueStr).matches()) {
                throw new FileUtil.IllegalFileException(valueStr + " no matches " + pattern.pattern());
            } else {
                if (valueStr != null && !"".equals(valueStr.trim())) {
                    Object args = null;
                    if (Date.class.isAssignableFrom(parameterType)) {
                        if (!"0".equals(valueStr)) {
                            args = DateUtil.parse(valueStr, column.getDateFormat());
                        }
                    } else {
                        valueStr = valueStr.trim();
                        if (column.getNumMultiple() != null) {
                            valueStr = (new BigDecimal(valueStr)).multiply(new BigDecimal(column.getNumMultiple())).toString();
                        }

                        if (column.isNeedTrans()) {
                            valueStr = column.trans(valueStr);
                        }

                        if (!parameterType.isPrimitive()) {
                            args = parameterType.getConstructor(String.class).newInstance(valueStr);
                        } else if (Character.TYPE == parameterType) {
                            args = valueStr.toCharArray()[0];
                        } else if (Integer.TYPE == parameterType) {
                            args = Integer.parseInt(valueStr);
                        } else if (Double.TYPE == parameterType) {
                            args = Double.parseDouble(valueStr);
                        } else if (Float.TYPE == parameterType) {
                            args = Float.parseFloat(valueStr);
                        } else if (Long.TYPE == parameterType) {
                            args = Long.parseLong(valueStr);
                        } else if (Short.TYPE == parameterType) {
                            args = Short.parseShort(valueStr);
                        } else if (Boolean.TYPE == parameterType) {
                            args = Boolean.parseBoolean(valueStr);
                        } else {
                            if (Byte.TYPE != parameterType) {
                                throw new RuntimeException("类型转换错误" + parameterType);
                            }

                            args = Byte.parseByte(valueStr);
                        }
                    }

                    method.invoke(t, args);
                }

            }
        }
    }

    public static <T> void write(String fileName, List<T> list, boolean append) {
        StringBuilder content = new StringBuilder();
        BufferedWriter bw = null;
        String charSet = "UTF-8";

        try {
            if (list != null && list.size() > 0) {
                Data data = FileTemplateContext.getInstance().getConfig(TemplateType.WRITE, list.get(0).getClass().getName());
                List<Column> cols = data.getColumnList();
                charSet = data.getCharSet();
                Iterator i$ = list.iterator();

                while(i$.hasNext()) {
                    T t = (T) i$.next();
                    int a = 0;

                    for(int size = cols.size(); a < size; ++a) {
                        Column col = (Column)cols.get(a);
                        Method m = col.getReadValueMethod();
                        Class<?> returnType = m.getReturnType();
                        String valueStr = null;
                        Object value = m.invoke(t);
                        if (Date.class.isAssignableFrom(returnType)) {
                            valueStr = value != null ? DateUtil.format((Date)value, col.getDateFormat()) : "";
                        } else if (!Number.class.isAssignableFrom(returnType) && !returnType.isPrimitive()) {
                            valueStr = value == null ? "" : value.toString();
                            if (col.isNeedTrans()) {
                                valueStr = col.trans(valueStr);
                            }

                            if (col.getLength() > 0) {
                                int maxLen = col.getLength();
                                String errMsg = valueStr + "超过[" + col.getName() + "]的最大长度" + maxLen;
                                if (LengthType.CHAR == data.getLengthType()) {
                                    if (valueStr.length() > maxLen) {
                                        throw new FileUtil.IllegalFileException(errMsg);
                                    }

                                    valueStr = String.format("%-" + maxLen + "s", valueStr);
                                } else {
                                    byte[] colData = valueStr.getBytes(charSet);
                                    if (colData.length > maxLen) {
                                        throw new FileUtil.IllegalFileException(errMsg);
                                    }

                                    if (colData.length != maxLen) {
                                        byte blank = 32;
                                        byte[] target = Arrays.copyOf(colData, maxLen);
                                        Arrays.fill(target, colData.length, maxLen, blank);
                                        valueStr = new String(target, charSet);
                                    }
                                }
                            }
                        } else {
                            if (!returnType.isPrimitive()) {
                                if (value == null) {
                                    value = Integer.valueOf(0);
                                    valueStr = "";
                                } else {
                                    valueStr = value.toString();
                                }
                            }

                            if (returnType.isPrimitive()) {
                                valueStr = String.valueOf(value);
                            }

                            if (col.isNeedTrans()) {
                                valueStr = col.trans(valueStr);
                            }

                            if (col.getNumMultiple() != null) {
                                BigDecimal bd = (new BigDecimal(value.toString())).multiply(new BigDecimal(col.getNumMultiple()));
                                valueStr = bd.toString();
                                if (col.getLength() > 0) {
                                    valueStr = String.format("%0" + col.getLength() + "d", bd.intValue());
                                }
                            }
                        }

                        content.append(valueStr);
                        if (data.hasSplit() && a < size - 1) {
                            if (null != data.getSplitType() && "char".equals(data.getSplitType())) {
                                content.append(stringToChar(data.getSplit()));
                            } else {
                                content.append(data.getSplit());
                            }
                        }
                    }

                    if (data.hasLineSeparator()) {
                        content.append("\r\n");
                    }
                }
            }

            File f = new File(fileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, append), charSet));
            bw.write(content.toString());
        } catch (Exception var29) {
            throw new RuntimeException(var29);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException var28) {
                    ;
                }
            }

        }

    }

    public static void touch(String fileName) {
        File f = new File(fileName);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        FileOutputStream bw = null;

        try {
            bw = new FileOutputStream(fileName, false);
            bw.write("ok".getBytes());
        } catch (Exception var11) {
            throw new RuntimeException(var11);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException var10) {
                    ;
                }
            }

        }

    }

    public static String checkStr(String str) {
        if (str == null) {
            return "";
        } else {
            StringBuilder buff = new StringBuilder();
            char[] array = str.toCharArray();
            int k = 0;

            for(int len = array.length; k < len; ++k) {
                char cc = array[k];
                if (cc >= ' ') {
                    switch(cc) {
                        case '"':
                            buff.append("&quot;");
                            break;
                        case '&':
                            buff.append("&amp;");
                            break;
                        case '\'':
                            buff.append("&apos;");
                            break;
                        case '<':
                            buff.append("&lt;");
                            break;
                        case '>':
                            buff.append("&gt;");
                            break;
                        default:
                            buff.append(cc);
                    }
                } else if (cc >= 0 && cc <= '\b' || cc >= 11 && cc <= '\f' || cc >= 14 && cc < ' ') {
                    if (cc < 16) {
                        buff.append("\\\\0x0" + Integer.toHexString(cc) + ";");
                    } else {
                        buff.append("\\\\0x" + Integer.toHexString(cc) + ";");
                    }
                } else {
                    buff.append(cc);
                }
            }

            return buff.toString();
        }
    }

    public static String charToString(String str) {
        if (str == null) {
            return "";
        } else {
            StringBuilder buff = new StringBuilder();
            char[] array = str.toCharArray();
            int k = 0;

            for(int len = array.length; k < len; ++k) {
                char cc = array[k];
                if (cc >= ' ') {
                    switch(cc) {
                        case '"':
                            buff.append("&quot;");
                            break;
                        case '&':
                            buff.append("&amp;");
                            break;
                        case '\'':
                            buff.append("&apos;");
                            break;
                        case '<':
                            buff.append("&lt;");
                            break;
                        case '>':
                            buff.append("&gt;");
                            break;
                        default:
                            buff.append(cc);
                    }
                } else if (cc >= 0 && cc <= '\b' || cc >= 11 && cc <= '\f' || cc >= 14 && cc < ' ') {
                    if (cc < 16) {
                        buff.append("\\\\0x0" + Integer.toHexString(cc) + ";");
                    } else {
                        buff.append("\\\\0x" + Integer.toHexString(cc) + ";");
                    }
                } else {
                    buff.append(cc);
                }
            }

            return buff.toString();
        }
    }

    public static String stringToChar(String value) {
        if (value.indexOf(92) < 0) {
            return value;
        } else {
            StringBuffer sb = new StringBuffer();
            char[] array = value.toCharArray();
            int i = 0;

            for(int len = array.length; i < len; ++i) {
                char c = array[i];
                if (c == '\\' && i + 6 <= len) {
                    if (array[i + 1] == '0' && array[i + 2] == 'x' && array[i + 5] == ';') {
                        String temp = array[i + 3] + String.valueOf(array[i + 4]);
                        char v = (char)Integer.parseInt(temp, 16);
                        sb.append(v);
                        i += 5;
                    } else {
                        sb.append(c);
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static class IllegalFileException extends RuntimeException {
        private static final long serialVersionUID = -4040593750097815090L;

        public IllegalFileException(String message) {
            super(message);
        }
    }
}
