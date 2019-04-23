package com.dcits.comet.commons.utils;

import java.math.BigDecimal;

public final class BigDecimalUtil {
    private BigDecimalUtil() {
    }

    /**
     * 将空的BigDecimal类型转换为0
     *
     * @param big
     * @return BigDecimal
     */
    public static BigDecimal nullToZero(BigDecimal big) {
        if (big == null) {
            return BigDecimal.ZERO;
        } else {
            return big;
        }
    }

    /**
     * 转换为BigDecimal类型(空转换为0)
     *
     * @param obj
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str)) {
                return BigDecimal.ZERO;
            }
        }

        return new BigDecimal(String.valueOf(obj));
    }

    /**
     * 转换为BigDecimal类型(空转换为0)
     *
     * @param obj
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimalNull(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str)) {
                return null;
            }
        }

        return new BigDecimal(String.valueOf(obj));
    }

    /**
     * 转换为字符串
     *
     * @param big
     * @return String
     */
    public static String toString(BigDecimal big) {
        return big == null ? "" : big.toString();
    }

    /**
     * 非NUll利率转换为字符串，null不转换
     */
    public static String rateToString(BigDecimal big) {
        if (big != null) {
            String str = big.toString();
            if ("".equals(str)) {
                return null;
            }
            return str;
        } else {
            return null;
        }
    }


    //利率类除法
    public static final BigDecimal rateDiv(BigDecimal b1, BigDecimal b2) {
        if (b1 != null && b2 != null && b2.compareTo(BigDecimal.ZERO) != 0) {
            return b1.divide(b2, 8, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    //金额类除法
    public static final BigDecimal amountDiv(BigDecimal b1, BigDecimal b2, int scale) {
        if (b1 != null && b2 != null && b2.compareTo(BigDecimal.ZERO) != 0) {
            return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

}
