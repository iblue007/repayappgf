package com.position.wyh.position;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commonutil {

    /*取出没用的小数点后面的0 例如2.00 -> 2*/
    public static String stripZeros(String number) {
        try {
            BigDecimal result = new BigDecimal(number).stripTrailingZeros();
            if (result.compareTo(BigDecimal.ZERO) == 0) {
                return 0 + "";
            } else {
                return result.toPlainString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return number;
        }
    }

    public static String getSpecialCharacter(String character, String newChar) {
        // 使用正则表达式, 匹配特殊字符
        Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
        Matcher m = pattern.matcher(character);
        return m.replaceAll(newChar).trim();
    }

}
