package com.kosign.push.commons;

import java.math.BigDecimal;

public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    public static BigDecimal normalizeAmount(String str) {
        return NumberUtils.createBigDecimal(StringUtils.replace(str, ",", ""));
    }

    public static Number zeroToNull(Number num) {
        if(num != null) {
            return NumberUtils.createNumber(zeroToNull(NumberUtils.createBigDecimal(num.toString())).toString());
        }

        return num;
    }

    public static BigDecimal zeroToNull(BigDecimal num) {
        if(num != null) {
            if (equals(num, BigDecimal.ZERO)) return null;
        }

        return num;
    }

//    public static String formatKoreanCurrency(String str) {
//        return formatKoreanCurrency(createBigDecimal(str));
//    }
//
//    public static String formatKoreanCurrency(BigDecimal d) {
//        double amount = d.doubleValue();
//
//        DecimalFormat formatter = new DecimalFormat("#,###");
//
//        String ss = formatter.format(amount);
//        return ss;
//    }

    public static boolean equals(Number a, Number b) {
        if (a == null) {
            return b == null;
        }
        else if (b == null) {
            return false;
        }
        else {
            return NumberUtils.createBigDecimal(a.toString()).compareTo(NumberUtils.createBigDecimal(b.toString())) == 0;
        }
    }

    public static int compareTo(Number n1, Number n2) {
        BigDecimal b1 = new BigDecimal(n1.doubleValue());
        BigDecimal b2 = new BigDecimal(n2.doubleValue());
        return b1.compareTo(b2);
    }

    public static boolean notEqual(Number a, Number b) {
        return !equals(a, b);
    }

    public static Number parseNumber(String str) {
        if(isCreatable(str)) {
            return createNumber(str);
        }

        return null;
    }

    public static BigDecimal abs(BigDecimal num) {
        BigDecimal absNum = num;

        if(num.compareTo(BigDecimal.ZERO) < 0) {
            absNum = num.negate();
        }

        return absNum;
    }

    public static boolean isPositive(Number num) {
        return compareTo(num, BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(Number num) {
        return compareTo(num, BigDecimal.ZERO) < 0;
    }

    public static boolean isZero(Number num) {
        return compareTo(num, BigDecimal.ZERO) == 0;
    }

    public static boolean isZero(String str) {
        return isZero(parseNumber(str));
    }

    public static boolean isNullOrZero(String str) {
        if(StringUtils.isBlank(str)) return false;
        return isZero(parseNumber(str));
    }

    public static BigDecimal toNegative(BigDecimal num) {
        return abs(num).negate();
    }
}
