package com.dcits.comet.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 文件工具类<br/>
 * 
 * @author WUDUFENG
 * 
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 校验文件是否存在，若不存在则抛出异常
     * 
     * @param fileName
     */
    public static boolean exists(String fileName) {
        File f = new File(fileName);
        logger.debug(f.getAbsolutePath());
        return f.exists();
    }


    /**
     * 获取文件内容的总记录数，除去文件头和文件尾
     * 
     * @param fileName
     *            文件名,包含文件路径
     * @param clazz
     *            文件内容对应的类
     * @return
     */
    public static <T> long getFileRowCount(String fileName, Class<T> clazz) {
        if (!exists(fileName))
            throw new RuntimeException(fileName.concat("文件不存在"));

        Data data = FileTemplateContext.getInstance().getConfig(TemplateType.READ, clazz.getName());

        int headLength = data.getHeadLength();
        int perRecordLength = data.getPerRecordLength();
        int endingLength = data.getEndingLength();

        // 定长的顺序文件
        if (!data.hasLineSeparator()) {
            long len = new File(fileName).length();
            long detailLen = len - headLength - endingLength;// 明细长度

            if (detailLen % perRecordLength != 0) {
                throw new RuntimeException("文件不完整");
            }
            return detailLen / perRecordLength;
        }

        // 非顺序文件一行行地去统计了
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), data.getCharSet()));
            int lineIndex = 0;
            while (br.readLine() != null) {
                lineIndex++;
            }
            return lineIndex - headLength - endingLength;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                }
        }
    }


    /**
     * 读取带换行的txt文件
     * 
     * @param fileName
     *            文件名
     * @param clazz
     *            文件内容对应的class类
     * @param startLineIndex
     *            读文件到起始行从0开始 ，忽略了文件头的情况
     * @param maxLine
     *            需要读取的行数
     * @return
     */
    public static <T> List<T> readFileToList(String fileName, Class<T> clazz, int startLineIndex, int maxLine) {
        if (!exists(fileName))
            throw new RuntimeException(fileName.concat("文件不存在"));

        Data data = FileTemplateContext.getInstance().getConfig(TemplateType.READ, clazz.getName());
        if (data.hasLineSeparator()) {
            return readHasLineSeparatorFileToList(fileName, data, clazz, startLineIndex, maxLine);
        } else {
            return readSequenceFileToList(fileName, data, clazz, startLineIndex, maxLine);
        }
    }


    private static <T> List<T> readSequenceFileToList(String fileName, Data data, Class<T> clazz,
            int startLineIndex, int maxLine) {
        int headLength = data.getHeadLength();
        int perRecordLength = data.getPerRecordLength();
        BufferedInputStream fis = null;

        try {
            fis = new BufferedInputStream(new FileInputStream(fileName));
            long startLineIndexL = startLineIndex;// 防止相乘后超过Integer最大值
            long skipByte = startLineIndexL * perRecordLength + headLength;
            long actSkipByte = fis.skip(skipByte);
            if (skipByte != actSkipByte) {
                logger.error("startLineIndex：{},lineByteSize：{},headSize：{}", startLineIndexL,
                    perRecordLength, headLength);
                logger.error("预计跳过的字节数为：{},实际跳过的字节数为：{}", skipByte, actSkipByte);
                throw new RuntimeException("读取数据错误");
            }

            List<T> list = new ArrayList<T>();

            int lineNum = 0;
            byte[] buf = new byte[perRecordLength];

            while (lineNum < maxLine && (fis.read(buf) == perRecordLength)) {
                String lineContent = new String(buf);
                T instance = convert(data, clazz, lineContent, lineNum + startLineIndex);
                list.add(instance);
                lineNum += 1;
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败", e);
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                }
        }

    }


    private static <T> List<T> readHasLineSeparatorFileToList(String fileName, Data data, Class<T> clazz,
            int startLineIndex, int maxLine) {
        List<T> list = new ArrayList<T>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), data.getCharSet()));
            int lineIndex = 0;
            startLineIndex += data.getHeadLength();
            while (lineIndex < startLineIndex) {
                br.readLine();
                lineIndex++;
            }//读文件
            String lineContent = null;
            lineIndex = 0;
            while ((lineIndex < maxLine) && (lineContent = br.readLine()) != null) {
                if (lineContent.length() == 0)
                    continue;
//                int lineLen = 0;
//                if (data.getLengthType() == LengthType.BYTE) {
//                    lineLen = lineContent.getBytes(data.getCharSet()).length;
//               } else {
//                    lineLen = lineContent.length();
//               }
//                if (!data.hasSplit() && lineLen != data.getPerRecordLength())
//                    throw new RuntimeException(String.format("文件错误,行记录长度应为%d,实际为%d",
//                        data.getPerRecordLength(), lineLen));
                T instance = convert(data, clazz, lineContent, lineIndex + startLineIndex);
                list.add(instance);
                lineIndex++;
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("读取文件数据失败", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return list;
    }


    private static <T> T convert(Data data, Class<T> clazz, String lineContent, int index) throws Exception {
        List<Column> colList = data.getColumnList();
        String split = data.getSplit();
        int size = colList.size();// 配置文件里一条记录的字段数

        T instance = clazz.newInstance();

        // 带分隔符
        if (data.hasSplit()) {
        	String[] values = null;
        	//add by huacongnan 20170508
        	//添加不可见字符为分隔符的情况，配置如：split="\\0x07;"
        	if (null != data.getSplitType() && data.getSplitType().equals("char")) {
        		values = charToString(new StringBuilder(lineContent).append(" ").toString()).split(split);
			} else {
				values = new StringBuilder(lineContent).append(" ").toString().split(split);
			}
            if (values.length != size) {
                throw new IllegalFileException("文件第" + index + "行字段数不符合");
            }
            // 最后一个把加的空格去掉
            values[values.length - 1] =
                    values[values.length - 1].substring(0, values[values.length - 1].length() - 1);

            for (int a = 0; a < values.length; a++) {
                Column col = colList.get(a);
                parseValue(values[a], col, instance);
            }
        } else {
            // 定长 ， 需要区分自己还是字符
//            int a = 0;
            byte[] lineData = lineContent.getBytes(data.getCharSet());
            for (int c = 0,a=0; c < lineData.length; c=colList.get(a).getLength()+c,a++) {  
                Column col = colList.get(a);
                String valueStr =
                            LengthType.BYTE == data.getLengthType() ? new String(lineData, c, col.getLength(),
                                data.getCharSet()) : // 字节长度
                                    lineContent.substring(c, c + col.getLength()); // 字符长度
//                c = c + col.getLength();
                parseValue(valueStr, col, instance);
                
                
            }
        }
        return instance;
    }


    /**
     * 将字符串设置成对象属性值 ， 读文件
     * 
     * @param valueStr
     * @param column
     * @param t
     * @throws Exception
     */
    private static <T> void parseValue(String valueStr, Column column, T t) throws Exception {
        Method method = column.getWriteValueMethod();
        if (method == null)
            return;
        Class<?> parameterType = method.getParameterTypes()[0];
        Pattern pattern = column.getPattern();
        if (column.getLength() > 0 && valueStr.length() > column.getLength()) {
            throw new IllegalFileException("属性[" + column.getName() + "]值[" + valueStr + "]超出定义的长度");
        }
        if (pattern != null && !pattern.matcher(valueStr).matches())
            throw new IllegalFileException(valueStr + " no matches " + pattern.pattern());
        if (valueStr != null && !valueStr.trim().equals("")) {
            Object args = null;
            if (Date.class.isAssignableFrom(parameterType)) {
                if (!"0".equals(valueStr))
                    args = DateUtil.parse(valueStr, column.getDateFormat());
            } else {
                valueStr = valueStr.trim();
                // 数字类型，判断是否需要转换
                // Number.class.isAssignableFrom(parameterType)
                if (column.getNumMultiple() != null) {
                    valueStr =
                            new BigDecimal(valueStr).multiply(new BigDecimal(column.getNumMultiple()))
                                .toString();
                }
                if (column.isNeedTrans()) {
                    valueStr = column.trans(valueStr);
                }
                if (!parameterType.isPrimitive()) {
                    args = parameterType.getConstructor(String.class).newInstance(valueStr);
                } else {
                    if (char.class == parameterType)
                        args = valueStr.toCharArray()[0];
                    else if (int.class == parameterType)
                        args = Integer.parseInt(valueStr);
                    else if (double.class == parameterType)
                        args = Double.parseDouble(valueStr);
                    else if (float.class == parameterType)
                        args = Float.parseFloat(valueStr);
                    else if (long.class == parameterType)
                        args = Long.parseLong(valueStr);
                    else if (short.class == parameterType)
                        args = Short.parseShort(valueStr);
                    else if (boolean.class == parameterType)
                        args = Boolean.parseBoolean(valueStr);
                    else if (byte.class == parameterType)
                        args = Byte.parseByte(valueStr);
                    else
                        throw new RuntimeException("类型转换错误" + parameterType);

                }
            }
            method.invoke(t, args);
        }
    }


    /**
     * 写文件
     * 
     * @param fileName
     * @param list
     * @param append
     *            是否追加文件
     */
    public static <T> void write(String fileName, List<T> list, boolean append) {
        StringBuilder content = new StringBuilder();
        BufferedWriter bw = null;
        String charSet = "UTF-8";

        try {
            if (list != null && list.size() > 0) {
                Data data =
                        FileTemplateContext.getInstance().getConfig(TemplateType.WRITE,
                            list.get(0).getClass().getName());
                List<Column> cols = data.getColumnList();
                charSet = data.getCharSet();

                for (T t : list) {
                    for (int a = 0, size = cols.size(); a < size; a++) {
                        Column col = cols.get(a);
                        Method m = col.getReadValueMethod();
                        Class<?> returnType = m.getReturnType();
                        String valueStr = null;
                        Object value = m.invoke(t);
                        if (Date.class.isAssignableFrom(returnType)) {
                            valueStr =
                                    value != null ? DateUtil.format((Date) value, col.getDateFormat()) : "";
                        } else if (Number.class.isAssignableFrom(returnType) || returnType.isPrimitive()) {
                            if (!returnType.isPrimitive()) {
                                if (value == null) {
                                    value = 0;
                                    valueStr = "";
                                } else
                                    valueStr = value.toString();
                            }
                            if (returnType.isPrimitive()) {
                                valueStr = String.valueOf(value);
                            }
                            if (col.isNeedTrans()) {
                                valueStr = col.trans(valueStr);
                            }
                            if (col.getNumMultiple() != null) {
                                BigDecimal bd =
                                        new BigDecimal(value.toString()).multiply(new BigDecimal(col
                                            .getNumMultiple()));
                                valueStr = bd.toString();
                                if (col.getLength() > 0) {
                                    // 数字左补零
                                    valueStr = String.format("%0" + col.getLength() + "d", bd.intValue());
                                }
                            }
                        } else {
                            valueStr = value == null ? "" : value.toString();
                            if (col.isNeedTrans()) {
                                valueStr = col.trans(valueStr);
                            }
                            if (col.getLength() > 0) {
                                // 字符串右补空格
                                int maxLen = col.getLength();
                                String errMsg = valueStr + "超过[" + col.getName() + "]的最大长度" + maxLen;
                                if (LengthType.CHAR == data.getLengthType()) {
                                    if (valueStr.length() > maxLen)
                                        throw new IllegalFileException(errMsg);
                                    valueStr = String.format("%-" + maxLen + "s", valueStr);
                                } else {
                                    byte[] colData = valueStr.getBytes(charSet);
                                    if (colData.length > maxLen)
                                        throw new IllegalFileException(errMsg);
                                    if (colData.length != maxLen) {
                                        byte blank = 32;
                                        byte[] target = Arrays.copyOf(colData, maxLen);
                                        Arrays.fill(target, colData.length, maxLen, blank);
                                        valueStr = new String(target, charSet);
                                    }
                                }
                            }
                        }
                        content.append(valueStr);
                        if (data.hasSplit() && a < size - 1) {
                        	//add by huacongnan 20170508
                        	//添加不可见字符为分隔符的情况
                        	if (null != data.getSplitType() && data.getSplitType().equals("char")) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * 创建.ok文件
     * 
     * @param fileName
     */
    public static void touch(String fileName) {
        File f = new File(fileName);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        FileOutputStream bw = null;
        try {
            bw = new FileOutputStream(fileName, false);
            bw.write("ok".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }

    }

    public static class IllegalFileException extends RuntimeException {
        private static final long serialVersionUID = -4040593750097815090L;


        public IllegalFileException(String message) {
            super(message);
        }
    }
    
	public static String checkStr(String str)
	{	
		if(str==null) {
			return "";
		}
		StringBuilder buff = new StringBuilder();
		char[] array = str.toCharArray();
		for (int k = 0, len = array.length; k < len; k++)
		{
			char cc = array[k];
			int ss = (int) cc;
			if (ss >= 32)
			{
				switch (cc)
				{
				case '&':
					buff.append("&amp;");
					break;
				case '<':
					buff.append("&lt;");
					break;
				case '>':
					buff.append("&gt;");
					break;
				case '\'':
					buff.append("&apos;");
					break;
				case '\"':
					buff.append("&quot;");
					break;
				default:
					buff.append(cc);
				}
			} else if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss < 32))) {
				if(ss < 16 ){
					buff.append("\\\\0x0" + Integer.toHexString(ss) + ";");
				} else {
					buff.append("\\\\0x" + Integer.toHexString(ss) + ";");
				}
			} else {
				buff.append(cc);
			}	
		}
		return buff.toString();
	}
	
	
	public static String charToString(String str)
	{	
		if(str==null) {
			return "";
		}
		StringBuilder buff = new StringBuilder();
		char[] array = str.toCharArray();
		for (int k = 0, len = array.length; k < len; k++)
		{
			char cc = array[k];
			int ss = (int) cc;
			if (ss >= 32)
			{
				switch (cc)
				{
				case '&':
					buff.append("&amp;");
					break;
				case '<':
					buff.append("&lt;");
					break;
				case '>':
					buff.append("&gt;");
					break;
				case '\'':
					buff.append("&apos;");
					break;
				case '\"':
					buff.append("&quot;");
					break;
				default:
					buff.append(cc);
				}
			} else if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss < 32))) {
				if(ss < 16 ){
					buff.append("\\\\0x0" + Integer.toHexString(ss) + ";");
				} else {
					buff.append("\\\\0x" + Integer.toHexString(ss) + ";");
				}
			} else {
				buff.append(cc);
			}	
		}
		return buff.toString();
	}
	
	public static String stringToChar(String value)
	{
		if (value.indexOf('\\') < 0)
		{
			return value;
		} else
		{
			StringBuffer sb = new StringBuffer();
			char[] array = value.toCharArray();
			for (int i = 0, len = array.length; i < len; i++)
			{
				char c = array[i];
				if (c == '\\' && i + 6 <= len)
				{
					if (array[i + 1] == '0' && array[i + 2] == 'x' && array[i + 5] == ';')
					{
						String temp = String.valueOf(array[i + 3]) + String.valueOf(array[i + 4]);
						char v = (char) Integer.parseInt(temp, 16);
						sb.append(v);
						i = i + 5;
					} else {
						sb.append(c);
					}
				} else
					sb.append(c);
			}
			return sb.toString();
		}
	}

}
