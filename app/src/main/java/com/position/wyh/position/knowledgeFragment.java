package com.position.wyh.position;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.utlis.ThreadUtil;
import com.position.wyh.position.viewpagerindicator.Md5Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by hadoop on 2017-08-13.
 */

public class knowledgeFragment extends BaseFragment {

    private Button mButton_start;
    private Button mButton_assit;
    private Button mButton_order_query;
    private Button mButton_order_get;
    private Button mButton_order_complete;
    private static String TAG = "MainActivity";
    public static boolean started = false;
    public String orderScore = "";
    protected String bankAccount = "徐群星";
    protected String bankCardNo = "6230580000259907983";
    protected String transMoney = "0·01";
    String tradeNo = "202011290003520177_8e108e3b7ce1e";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knowledge_fragment, null, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        mButton_start = view.findViewById(R.id.button_start);
        mButton_assit = view.findViewById(R.id.button_assit);
        mButton_order_get = view.findViewById(R.id.button_order_get);
        mButton_order_query = view.findViewById(R.id.button_order_query);
        mButton_order_complete = view.findViewById(R.id.button_order_complete);
        if (started) {

            mButton_start.setText("停止自动化测试");
        } else {
            mButton_start.setText("开始自动化测试");
        }
        mButton_order_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        taskPost();
                    }
                });
            }
        });
        mButton_order_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        takePostQuery();
                    }
                });
            }
        });
        mButton_order_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        deviceNoftify();
                    }
                });
            }
        });
        //点击事件
        mButton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = getContext().getSharedPreferences("setting", 0).getString("Password", "");
                String deviceId = getContext().getSharedPreferences("setting", 0).getString("deviceId", "");
                String loginPassword = getContext().getSharedPreferences("setting", 0).getString("loginPassword", "");
                String loginAccount = getContext().getSharedPreferences("setting", 0).getString("loginAccount", "");
                if (TextUtils.isEmpty(string) || TextUtils.isEmpty(loginAccount) ||
                        TextUtils.isEmpty(loginPassword) || TextUtils.isEmpty(string)) {
                    Toast.makeText(getContext(), "支付密码、登录账号等信息请先完善", 1).show();
                    return;
                }
                started = !started;
                if (started) {
                    //Toast.makeText(getActivity(),"启动成功！",Toast.LENGTH_SHORT).show();
                    mButton_start.setText("停止自动化测试");
                    try {
                        PackageManager packageManager = getContext().getPackageManager();
                        Intent intent = new Intent();
                        intent = packageManager.getLaunchIntentForPackage("cmb.pb");
                        if (intent == null) {
                            Toast.makeText(getContext(), "未安装", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mButton_start.setText("开始自动化测试");
                }
            }
        });

        //点击事件
        mButton_assit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //如果没开启，就提醒开启辅助功能
                if (!isAccessibilitySettingsOn(getContext())) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "辅助功能已经开启！", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //判断是否开启辅助功能
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getActivity().getPackageName() + "/" + AutoClickService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    public void taskPost() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "347a9ae9065a8c54b798afde7a08bd73");
        String cPUSerial = SettingFragment.getCPUSerial(getContext());
        String GetDiskId = SettingFragment.GetDiskId();
        String appid = GetDiskId + "&deviceNo=" + cPUSerial;
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);

        // TODO: 2020/11/28 写成可配置的
        paramMap.put("appid", "aa12bda5ddc10ee8e547043a532485c6");

        //String paramsStr = StringUtils.ascriAsc(paramMap);
        String paramsStr = StringUtils.ascriAsc(paramMap);

        LogUtils.e(TAG, "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
//        paramMap2.put("deviceCpu", cPUSerial);
//        paramMap2.put("deviceCaliche", GetDiskId);
        /// String ip = sharedPreferences.getString("IP", "47.242.140.225");
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDevGetOrder?", paramMap2);
        Log.e(TAG, "taskPost: " + s);
        //  String s="{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松11\",\"subbranchName\":\"江苏省-盐城分行\",\"bankCode\":\"BOCOM\",\"orderScore\":10.0,\"tradeNo\":\"202011051935360189_87cf2c55ef7e5\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
//         LogUtils.e(TAG, "taskPost: " + s);
        handleGetOrder(s);
    }

    private void handleGetOrder(String stringBuffer2) {
        if (TextUtils.isEmpty(stringBuffer2)) {
            return;
        }
        JSONObject parseObject = JSONObject.parseObject(stringBuffer2);
        int intValue = parseObject.getIntValue("code");
        if (intValue == 0) {
            JSONObject jSONObject = parseObject.getJSONObject("data");
            if (!jSONObject.isEmpty()) {
                this.tradeNo = jSONObject.getString("tradeNo");
                LogUtils.d("GK", "result tradeNo = " + this.tradeNo);
                jSONObject.getString("bankName");
                jSONObject.getString("bankCode");
                this.bankAccount = jSONObject.getString("bankAccount");
                LogUtils.d("GK", "result bankAccount = " + this.bankAccount);
                this.bankCardNo = jSONObject.getString("bankCardNo");
                LogUtils.d("GK", "result bankCardNo = " + this.bankCardNo);
                jSONObject.getString("subbranchName");
                jSONObject.getString("subbranchProvince");
                jSONObject.getString("subbranchCity");
                this.orderScore = "0·01";//jSONObject.getBigDecimal("orderScore") + "";
                LogUtils.d("GK", "result orderScore = " + this.orderScore);

            } else {

            }
        } else {

        }
    }

    private void takePostQuery() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("tradeNo", tradeNo);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);

        LogUtils.e(TAG, "taskPostQuery: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();

        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
        paramMap2.put("tradeNo", tradeNo);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/queryOrder?", paramMap2);
        // Log.e(TAG, "taskPost: "+s );
        //  String s="{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松11\",\"subbranchName\":\"江苏省-盐城分行\",\"bankCode\":\"BOCOM\",\"orderScore\":10.0,\"tradeNo\":\"202011051935360189_87cf2c55ef7e5\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
        LogUtils.e(TAG, "taskPost: " + s);
        JSONObject parseObject = JSONObject.parseObject(s);
        int intValue = parseObject.getIntValue("code");
        JSONObject jSONObject = parseObject.getJSONObject("data");
        if (jSONObject != null && jSONObject.isEmpty()) {
            int orderStatus = jSONObject.getInteger("orderStatus");
            LogUtils.e("======", "======orderStatus:" + orderStatus);

        }
    }
    public String deviceNoftify() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "112233445566");
        String cPUSerial = SettingFragment.getCPUSerial(getContext());
        String GetDiskId = SettingFragment.GetDiskId();
        Log.d("GK", "deviceNoftify = ");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        String format = new SimpleDateFormat("yyyy-M-d hh:mm:ss").format(new Date());
        HashMap hashMap = new HashMap();
        hashMap.put("deviceNo", string);
        hashMap.put("deviceCpu", cPUSerial);
        hashMap.put("deviceCaliche", GetDiskId);
        hashMap.put("tradeNo", this.tradeNo);
        hashMap.put("transactionalNo", format);
        hashMap.put("orderStatus", "3");
        hashMap.put("transferTime", format);
        hashMap.put("fee", valueOf);
        String hash = Md5Utils.hash(StringsUtils.sortMapByKeyAsc(hashMap));
        try {
           // HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://" + sharedPreferences.getString("IP", "") + "//api//order//deviceNotify").openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://47.242.229.28/api//order//deviceNotify").openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            StringBuffer stringBuffer = new StringBuffer();
            String str = "deviceNo=" + string + "&sign=" + hash + "&remark=" + ("已经成功转账 " + this.bankAccount + " " + this.bankCardNo + " " + this.orderScore + "元") + "&tradeNo=" + this.tradeNo + "&transactionalNo=" + format + "&orderStatus=" + "3" + "&transferTime=" + format + "&fee=" + valueOf + "&transferImg=" + "no Image";
            Log.d("GK", "data = " + str);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(str.getBytes());
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        stringBuffer.append(readLine);
                        stringBuffer.append("\r\n");
                    } else {
                        bufferedReader.close();
                        Log.d("GK", "result = " + stringBuffer.toString());
                        return null;
                    }
                }
            } else {
                Log.d("GK", "responseCode = " + responseCode);
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

}
