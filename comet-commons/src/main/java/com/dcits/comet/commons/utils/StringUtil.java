package com.dcits.comet.commons.utils;

/**
 * @descriptions: TODO
 */
public class StringUtil {

    public static boolean isWhitespace(String s) {
        if(isEmpty(s)) {
            return true;
        } else {
            for(int i = 0; i < s.length(); ++i) {
                char c = s.charAt(i);
                if(" \t\n\r".indexOf(c) == -1) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}
