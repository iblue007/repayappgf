package com.position.wyh.position.test;

import java.util.Arrays;
import java.util.HashMap;

public class StringUtils {
	/**
	 * 参数按照ASCII asc 排序
	 * @param params2
	 * @return
	 */
	public static String ascriAsc(HashMap<String, Object> params2) {
        String[] sortedKeys = params2.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);// 排序请求参数
        StringBuilder s2 = new StringBuilder();
        for (String key : sortedKeys) {
            s2.append(key).append("=").append(params2.get(key)).append("&");
        }
        s2.deleteCharAt(s2.length() - 1);
        return s2.toString();
    }
}
