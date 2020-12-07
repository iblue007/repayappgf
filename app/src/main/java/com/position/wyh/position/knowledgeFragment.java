package com.position.wyh.position;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.EventBusUtil;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.MessageUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.utlis.OnClickItemCallBack;
import com.position.wyh.position.utlis.ThreadUtil;
import com.position.wyh.position.widget.PermissionTipDialog;
import com.yhao.floatwindow.FloatWindow;

import java.io.File;
import java.util.HashMap;

/**
 * Created by hadoop on 2017-08-13.
 */

public class knowledgeFragment extends BaseFragment {

    private Button mButton_start;
    private Button mButton_assit;
    private Button mButton_float_window;
    private Button mButton_usage;
    private Button mButton_permission;
    private Button mButton_order_query;
    private Button mButton_order_get;
    private Button mButton_order_complete;
    private Button mButton_order_clear;
    private Button mButton_order_save;
    private static String TAG = "MainActivity";
    public static boolean started = false;
    public String orderScore = "";
    protected String bankAccount = "徐群星";
    protected String bankCardNo = "6230580000259907983";
    protected String transMoney = "0·01";
    String tradeNo = "202012030023290131_067aa1e9854a5";
    boolean floatWindowOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knowledge_fragment, null, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        mButton_start = view.findViewById(R.id.button_start);
        mButton_assit = view.findViewById(R.id.button_assit);
        mButton_float_window = view.findViewById(R.id.button_flaot_window);
        mButton_usage = view.findViewById(R.id.button_usage);
        mButton_permission = view.findViewById(R.id.button_permission);
        mButton_order_get = view.findViewById(R.id.button_order_get);
        mButton_order_query = view.findViewById(R.id.button_order_query);
        mButton_order_complete = view.findViewById(R.id.button_order_complete);
        mButton_order_clear = view.findViewById(R.id.mButton_order_clear);
        mButton_order_save = view.findViewById(R.id.mButton_order_save);
        if (started) {
            mButton_start.setText("停止自动化测试");
        } else {
            mButton_start.setText("开始自动化测试");
        }

        mButton_order_get.setVisibility(View.GONE);
        mButton_order_query.setVisibility(View.GONE);
        mButton_order_complete.setVisibility(View.GONE);
        mButton_order_clear.setVisibility(View.GONE);
        mButton_order_save.setVisibility(View.GONE);

        mButton_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MainActivity.RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        MessageUtils.show(getContext(), "权限已获取。");
                        createDefaultDir();
                    }

                    @Override
                    public void denied() {

                    }
                });
                //  Commonutil.gotoHuaweiPermission(getContext());
//                Commonutil.delFile(MainActivity.MAIN_TEMP + "orderInfo.txt");
//                ThreadUtil.executeMore(new Runnable() {
//                    @Override
//                    public void run() {
//                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
//                        String OrderDetail = "{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松\",\"subbranchName\":\"默认\",\"bankCode\":\"0\",\"orderScore\":7.0000,\"tradeNo\":\"202012030023290075_bd407c7442e2d\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";//sharedPreferences.getString("OrderDetail", "");
//                        Commonutil.saveToSDCard(getActivity(), "orderInfo.txt", OrderDetail);
//                        String readTextFile = Commonutil.readTextFile("orderInfo.txt");
//                        LogUtils.e("======", "======0000:" + readTextFile);
//                    }
//                });
            }
        });
        mButton_float_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(getContext(), new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new MainActivity.RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        MessageUtils.show(getContext(), "权限已获取。");
                        if (FloatWindow.get() != null) {
                            FloatWindow.get().show();
                        }
                    }

                    @Override
                    public void denied() {

                    }
                });

            }
        });
        mButton_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        100);
            }
        });

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

        mButton_order_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().getSharedPreferences("setting", 0).edit().putString("OrderDetail", "").commit();
            }
        });
        //点击事件
        mButton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "点击开始自动化测试");
                EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                requestPermissions(getContext(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MainActivity.RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        LogUtils.e("======", "======22222");
                    }

                    @Override
                    public void denied() {
                        LogUtils.e("======", "======333333");
                    }
                });

                if (!Commonutil.isAccessibilitySettingsOn(getContext())) {
                    Toast.makeText(getContext(), "请先开启辅助功能", Toast.LENGTH_LONG).show();
                    return;
                }
                String string = getContext().getSharedPreferences("setting", 0).getString("Password", "");
                String deviceId = getContext().getSharedPreferences("setting", 0).getString("deviceId", "");
                String loginPassword = getContext().getSharedPreferences("setting", 0).getString("loginPassword", "");
                String loginAccount = getContext().getSharedPreferences("setting", 0).getString("loginAccount", "");
                if (TextUtils.isEmpty(string) || TextUtils.isEmpty(loginAccount) ||
                        TextUtils.isEmpty(loginPassword) || TextUtils.isEmpty(string)) {
                    Toast.makeText(getContext(), "支付密码、登录账号等信息请先完善", Toast.LENGTH_LONG).show();
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
                if (!Commonutil.isAccessibilitySettingsOn(getContext())) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "辅助功能已经开启！", Toast.LENGTH_SHORT).show();
                }


            }
        });
        mButton_order_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String OrderDetail = getContext().getSharedPreferences("setting", 0).getString("OrderDetail", "");
                String OrderDetail = "{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松\",\"subbranchName\":\"默认\",\"bankCode\":\"0\",\"orderScore\":7.0000,\"tradeNo\":\"202012030023290075_bd407c7442e2d\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
                LogUtils.e("======", "======onClick-orderDetail:" + OrderDetail);
                JSONObject parseObject = JSONObject.parseObject(OrderDetail);
                int intValue = parseObject.getIntValue("code");
                if (intValue == 0) {
                    JSONObject jSONObject = parseObject.getJSONObject("data");
                    if (!jSONObject.isEmpty()) {
                        LogUtils.e("======", "======do sp stringBuffer2:" + OrderDetail);
                        parseGetOrderJson(jSONObject);
                    }
                }
            }
        });

    }

    private void parseGetOrderJson(JSONObject jSONObject) {
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
            String orderScoreNormal = "700";//jSONObject.getBigDecimal("orderScore") + "";
            this.orderScore = "0.01";
            String stripZerostr = Commonutil.stripZeros(orderScoreNormal);
            //todo:小数点要修改
            if (stripZerostr.contains(".")) {
                String orderScorechange = Commonutil.getSpecialCharacter(stripZerostr, "·");
                LogUtils.d("GK", "result orderScore = " + orderScorechange);
            }
        } else {
            // ztLog("task code =   " + intValue);
        }
    }

    public void taskPost() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
        String deviceNo = sharedPreferences.getString("deviceId", "347a9ae9065a8c54b798afde7a08bd73");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", deviceNo);
        paramMap.put("appid", appId);

        String paramsStr = StringUtils.ascriAsc(paramMap);
        LogUtils.e(TAG, "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", deviceNo);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDevGetOrder?", paramMap2);
        Log.e(TAG, "taskPost: " + s);
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
        //  String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("tradeNo", tradeNo);
        //  paramMap.put("appId", appId);
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
        LogUtils.e(TAG, "taskPost: " + s);
        JSONObject parseObject = JSONObject.parseObject(s);
        int intValue = parseObject.getIntValue("code");
        JSONObject jSONObject = parseObject.getJSONObject("data");
        if (jSONObject != null && jSONObject.isEmpty()) {
            int orderStatus = jSONObject.getInteger("orderStatus");
            LogUtils.e("======", "======orderStatus:" + orderStatus);
            if (orderStatus == 1) {

            }
        }
    }

    public void deviceNoftify() {
        String shortMsg = "测试短信";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("tradeNo", tradeNo);
        paramMap.put("orderStatus", "3");
        paramMap.put("shortMsg", shortMsg);
        paramMap.put("appid", appId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);

        LogUtils.e(TAG, "taskPostQuery: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();

        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
        paramMap2.put("tradeNo", tradeNo);
        paramMap2.put("shortMsg", shortMsg);
        paramMap2.put("orderStatus", "3");
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDeviceNotifyV2?", paramMap2);
        // Log.e(TAG, "taskPost: "+s );
        //  String s="{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松11\",\"subbranchName\":\"江苏省-盐城分行\",\"bankCode\":\"BOCOM\",\"orderScore\":10.0,\"tradeNo\":\"202011051935360189_87cf2c55ef7e5\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
        LogUtils.e(TAG, "taskPost: " + s);
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


    public void requestPermissions(final Context context, final String[] permissions,
                                   MainActivity.RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    LogUtils.e("======", "======qq2222222222222222222222222222222222");
                    PermissionTipDialog permissionTipDialog = new PermissionTipDialog(getActivity());
                    permissionTipDialog.show();
                    permissionTipDialog.setOnClickItemCallBack(new OnClickItemCallBack() {
                        @Override
                        public void onClickCallBack(String... value) {
                            ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                }
                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    private final int mRequestCode = 1024;
    private MainActivity.RequestPermissionCallBack mRequestPermissionCallBack;

    /**
     * 权限请求结果回调接口
     */
    public interface RequestPermissionCallBack {
        /**
         * 同意授权
         */
        void granted();

        /**
         * 取消授权
         */
        void denied();
    }

    /**
     * 初始化基础目录
     */
    public void createDefaultDir() {
        try {
            final String baseDir = MainActivity.BASE_DIR;
            File dir = new File(baseDir);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            dir = new File(MainActivity.MAIN_TEMP);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            LogUtils.e("======", "======createDefaultDir");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
