package com.position.wyh.position.test;

import java.io.IOException;
import java.util.HashMap;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ApiInvoleUtils {
	 

    public static void asasdasdasd()   {
    	String deviceNo = "d23eab596657293008bd9b9d75f935c6";
        HashMap<String, Object> paramMap = new HashMap<String,Object>();
        paramMap.put("deviceNo", deviceNo);
        paramMap.put("deviceCpu", "90430e291a087b59guangfabank");
        paramMap.put("deviceCaliche", "d7b523f9e15f53fc040f9e22998bbd42");
        String paramsStr = StringUtils.ascriAsc(paramMap);
        
        HashMap<String, Object> paramMap2 = new HashMap<String,Object>();
        
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", deviceNo);
        JSONObject jsonObj2 = JSONObject.parseObject(JSON.toJSONString(paramMap2));
        System.out.println("请求参数:"+jsonObj2.toJSONString());
        String url="http://127.0.0.1:8080/api/order/getOrder?deviceNo="+deviceNo+"&sign="+sign;
        System.out.println(url);
    }
}
