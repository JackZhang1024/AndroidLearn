package com.lucky.androidlearn.core.util;

/**
 * Created by zfz on 2017/12/7.
 */

public class StringUtils {

    // 判断字符串中的字符长度 1个汉字占两个字符 一个英文字符占一个
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len += 2;
            }
        }
        return Math.round(len);
    }

    // 获取指定长度的中英文字符串 一个汉字算两个英文字符
    private static String calculateLength(CharSequence c, int maxLength) {
        StringBuilder stringBuilder = new StringBuilder();
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (len < maxLength) {
                if (tmp > 0 && tmp < 127) {
                    len += 1;
                } else {
                    len += 2;
                }
                stringBuilder.append(c.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

}
