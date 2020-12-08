package com.position.wyh.position.utlis;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;

import java.util.HashMap;

public class NetApiUtil {

    public static String getSignType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", 0);
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appid", appId);

        String paramsStr = StringUtils.ascriAsc(paramMap);
        LogUtils.e("====", "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("appid", appId);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/queryAppidSignType?", paramMap2);
        LogUtils.e("====", "====getSignTyp: " + s);
        return s;
    }

    public static String taskPost(Context context, int onlyValidAppid) {//1-只验证appid,0-需要同时验证appid跟deviceNo
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", 0);
        String deviceNo = sharedPreferences.getString("deviceId", "347a9ae9065a8c54b798afde7a08bd73");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        if (onlyValidAppid == 0) {
            paramMap.put("deviceNo", deviceNo);
        }
        paramMap.put("appid", appId);

        String paramsStr = StringUtils.ascriAsc(paramMap);
        LogUtils.e("====", "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        if (onlyValidAppid == 0) {
            paramMap2.put("deviceNo", deviceNo);
        }
        paramMap2.put("appid", appId);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDevGetOrderAppid?", paramMap2);
        LogUtils.e("====", "====taskPost: " + s);
        return s;
    }

    public static String taskQuery(Context context, String tradeNo, int onlyValidAppid) {//1-只验证appid,0-需要同时验证appid跟deviceNo
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", 0);
        String deviceNo = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        if (onlyValidAppid == 0) {
            paramMap.put("deviceNo", deviceNo);
        }
        paramMap.put("tradeNo", tradeNo);
        paramMap.put("appid", appId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);

        HashMap<String, String> paramMap2 = new HashMap<>();

        paramMap2.put("sign", sign);
        if (onlyValidAppid == 0) {
            paramMap2.put("deviceNo", deviceNo);
        }
        paramMap2.put("tradeNo", tradeNo);
        paramMap2.put("appid", appId);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/queryOrderAppid?", paramMap2);
        return s;
    }

    public static String taskComplete(Context context, String tradeNo, int onlyValidAppid) {//1-只验证appid,0-需要同时验证appid跟deviceNo
        String shortMsg = "测试短信";
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", 0);
        String deviceNo = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        if (onlyValidAppid == 0) {
            paramMap.put("deviceNo", deviceNo);
        }
        paramMap.put("tradeNo", tradeNo);
        paramMap.put("orderStatus", "3");
        paramMap.put("shortMsg", shortMsg);
        paramMap.put("appid", appId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);

        LogUtils.e("======", "deviceNoftify: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();

        paramMap2.put("sign", sign);
        if (onlyValidAppid == 0) {
            paramMap2.put("deviceNo", deviceNo);
        }
        paramMap2.put("tradeNo", tradeNo);
        paramMap2.put("shortMsg", shortMsg);
        paramMap2.put("orderStatus", "3");
        paramMap2.put("appid", appId);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDeviceNotifyAppid?", paramMap2);
        // Log.e(TAG, "taskPost: "+s );
        //  String s="{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松11\",\"subbranchName\":\"江苏省-盐城分行\",\"bankCode\":\"BOCOM\",\"orderScore\":10.0,\"tradeNo\":\"202011051935360189_87cf2c55ef7e5\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
        LogUtils.e("======", "======deviceNoftify: " + s);
        return s;
//        JSONObject parseObject = JSONObject.parseObject(s);
//        int intValue = parseObject.getIntValue("code");
//        JSONObject jSONObject = parseObject.getJSONObject("data");
//        if (jSONObject != null && jSONObject.isEmpty()) {
//            int orderStatus = jSONObject.getInteger("orderStatus");
//            LogUtils.e("======", "======orderStatus:" + orderStatus);
//            if (orderStatus == 1) {
//
//            }
//        }
    }

}
