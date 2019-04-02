package com.dcits.comet.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将JSON字符串的key进行大写+下划线格式与驼峰模式互转<br>
 * JSONObject取值二次封装<br>
 * JSONObject取值支持驼峰和大写+下划线格Key取值
 *
 * @author Tim
 */
public class JsonUtils {

    private static final String S1 = ":";
    private static final String S2 = ",";
    private static final String S3 = "_";
    private static final String S4 = "{";
    private static final String S5 = "[";
    private static final String fillStringUnit = "    ";
    private static Map<String, String> uConv = new ConcurrentHashMap<>();
    private static Map<String, String> hConv = new ConcurrentHashMap<>();

    /**
     * @fields UPPER_TYPE
     */
    public static final String UPPER_TYPE = "U";
    /**
     * @fields HUMP_TYPE
     */
    public static final String HUMP_TYPE = "H";
    /**
     * @fields CONVERT 驼峰和大写+下划线格Key转换开关
     */
    public static final boolean CONVERT = true;

    /**
     * 驼峰格式转为大写+下划线格式
     *
     * @param key
     * @return
     */
    public final static String convertUpperCase(String key) {
        if (!CONVERT)
            return key;
        if (uConv.containsKey(key)) {
            return uConv.get(key);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(S3);
                sb.append(c);
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        String tmp = sb.toString();
        uConv.put(key, tmp);
        return tmp;
    }

    /**
     * 大写+下划线格式转化为驼峰格式
     *
     * @param key
     * @return
     */
    public final static String convertHumpCase(String key) {
        if (!CONVERT)
            return key;
        if (hConv.containsKey(key)) {
            return hConv.get(key);
        }
        StringBuilder sb = new StringBuilder();
        String[] fs = key.split(S3);
        int i = 0;
        for (String s : fs) {
            if (i == 0)
                sb.append(s.toLowerCase());
            else {
                sb.append(s.substring(0, 1).toUpperCase());
                sb.append(s.substring(1).toLowerCase());
            }
            i++;
        }
        String tmp = sb.toString();
        hConv.put(key, tmp);
        return tmp;
    }

    /**
     * JSON字符串转换
     *
     * @param msg
     * @param type
     * @return
     */
    public final static String convertMsg(String msg, String type) {
        if (!CONVERT)
            return msg;
        long starttime = 0;

        String[] jsonStrs = msg.split(S1);
        String[] temp = null;
        StringBuilder sb = new StringBuilder(jsonStrs.length);
        int i = 1;
        for (String s1 : jsonStrs) {
            temp = s1.split(S2);
            for (int j = 0; j < temp.length; j++) {
                if (j == temp.length - 1) {
                    if (isHumpKey(temp[j]) && UPPER_TYPE.equals(type))
                        sb.append(convertUpperCase(temp[j]));
                    else if (isUpperKey(temp[j]) && HUMP_TYPE.equals(type))
                        sb.append(convertHumpCase(temp[j]));
                    else
                        sb.append(temp[j]);
                } else {
                    sb.append(temp[j]);
                    sb.append(S2);
                }
            }

            if (i != jsonStrs.length)
                sb.append(S1);
            i++;
        }

        return sb.toString();
    }

    /**
     * 检查是否是Json的Key
     *
     * @param keyStr
     * @return
     */
    protected static boolean isUpperKey(String keyStr) {
        // modify for sonar
        if (null == keyStr) {
            return false;
        }
        Pattern p = Pattern.compile("\\s*|\r|\n");
        Matcher m = p.matcher(keyStr);
        keyStr = m.replaceAll("");
        if (keyStr.startsWith(S4) || keyStr.startsWith(S5))
            return true;
        Pattern pattern = Pattern.compile("^\"[A-Z0-9_]+\"$");
        Matcher matcher = pattern.matcher(keyStr);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否是Json的Key
     *
     * @param keyStr
     * @return
     */
    protected static boolean isHumpKey(String keyStr) {
        // modify for sonar
        if (null == keyStr) {
            return false;
        }
        Pattern p = Pattern.compile("\\s*|\r|\n");
        Matcher m = p.matcher(keyStr);
        keyStr = m.replaceAll("");
        if (keyStr.startsWith(S4) || keyStr.startsWith(S5))
            return true;
        Pattern pattern = Pattern.compile("^\"[a-zA-Z0-9]+\"$");
        Matcher matcher = pattern.matcher(keyStr);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    public final static String formatJson(String json) {
        return formatJson(json, fillStringUnit);
    }

    /**
     * json字符串的格式化
     *
     * @param json
     * @param fillStringUnit
     * @return
     */
    public final static String formatJson(String json, String fillStringUnit) {
        if (json == null || json.trim().length() == 0) {
            return null;
        }

        int fixedLenth = 0;
        ArrayList<String> tokenList = new ArrayList<String>();
        {
            String jsonTemp = json;
            // 预读取
            while (jsonTemp.length() > 0) {
                String token = getToken(jsonTemp);
                jsonTemp = jsonTemp.substring(token.length());
                token = token.trim();
                tokenList.add(token);
            }
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLenth && i < tokenList.size() - 1
                    && tokenList.get(i + 1).equals(":")) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {

            String token = tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }

            buf.append(token);
            // 左对齐
            /**
             * if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":"))
             * { int fillLength = fixedLenth - token.getBytes().length; if
             * (fillLength > 0) { for (int j = 0; j < fillLength; j++) {
             * buf.append(" "); } } }
             */
        }
        return buf.toString();
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao
                    && (token.equals(":") || token.equals("{")
                    || token.equals("}") || token.equals("[")
                    || token.equals("]") || token.equals(","))) {
                if (buf.toString().trim().length() == 0) {
                    buf.append(token);
                }

                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                } else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count,
                               String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    /**
     * 合并JSONObject
     *
     * @param source
     * @param dest
     */
    public static void mergeJSONObject(JSONObject source, JSONObject dest) {
        for (String key : source.keySet()) {
            if (dest.get(key) instanceof JSONObject) {
                if (source.get(key) instanceof JSONObject) {
                    mergeJSONObject((JSONObject) source.get(key),
                            (JSONObject) dest.get(key));
                } else
                    dest.put(key, source.get(key));
            } else if (dest.get(key) instanceof JSONArray) {
                Iterator<Object> it = ((JSONArray) source.get(key)).iterator();
                for (; it.hasNext(); ) {
                    ((JSONArray) dest.get(key)).add(it.next());
                }

            } else
                dest.put(key, source.get(key));
        }
    }


    /**
     * @param key
     * @param in
     * @return
     */
    public static boolean containsKey(String key, JSONObject in) {
        // 不再进行转换
        // if (CONVERT && isUpperKey("\"" + key + "\"")) {
        // key = convertHumpCase(key);
        // }

        return in.containsKey(key);
    }




    /**
     * 支持驼峰和大写字母下划线模式转换
     *
     * @param key
     * @param in
     * @return
     */
    public static Object getObject(String key, JSONObject in) {
        // 不再进行转换
        // if (CONVERT && isUpperKey("\"" + key + "\"")) {
        // key = convertHumpCase(key);
        // }
        return in.get(key);
    }

    /**
     * 支持驼峰和大写字母下划线Key
     *
     * @param key
     * @param in
     * @return
     */
    public static JSONObject getJSONObject(String key, JSONObject in) {
        // 不再进行转换
        // if (CONVERT && isUpperKey("\"" + key + "\"")) {
        // key = convertHumpCase(key);
        // }
        return in.getJSONObject(key);
    }

    /**
     * 支持驼峰和大写字母下划线Key
     *
     * @param key
     * @param in
     * @return
     */
    public static JSONArray getJSONArray(String key, JSONObject in) {
        // 不再进行转换
        // if (CONVERT && isUpperKey("\"" + key + "\"")) {
        // key = convertHumpCase(key);
        // }
        return in.getJSONArray(key);
    }

    /**
     * 支持驼峰和大写字母下划线Key
     *
     * @param key
     * @param in
     * @return
     */
    public static Object putObject(String key, Object value, JSONObject in) {
        // 不再进行转换
        // if (CONVERT && isUpperKey("\"" + key + "\"")) {
        // key = convertHumpCase(key);
        // }
        return in.put(key, value);
    }
}
