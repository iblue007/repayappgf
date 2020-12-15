package com.position.wyh.position;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.position.wyh.position.utlis.EventBusUtil;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.MessageUtils;
import com.position.wyh.position.utlis.NetApiUtil;
import com.position.wyh.position.utlis.OnClickItemCallBack;
import com.position.wyh.position.utlis.SystemUtil;
import com.position.wyh.position.utlis.ThreadUtil;
import com.position.wyh.position.widget.PermissionTipDialog;
import com.yhao.floatwindow.FloatWindow;

import java.io.File;

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
    private Button mButton_process_exist;
    private Button mButton_sign_type;
    private static String TAG = "MainActivity";
    public static boolean started = false;
    public String orderScore = "";
    protected String bankAccount = "徐群星";
    protected String bankCardNo = "6230580000259907983";
    protected String transMoney = "0·01";
    String tradeNo = "202012030023290131_067aa1e9854a5";
    boolean floatWindowOpen = false;
    int onlyValidAppid = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knowledge_fragment, null, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        mButton_start = view.findViewById(R.id.button_start);
        mButton_assit = view.findViewById(R.id.button_assit);
        mButton_permission = view.findViewById(R.id.button_permission);
        mButton_float_window = view.findViewById(R.id.button_flaot_window);
        mButton_usage = view.findViewById(R.id.button_usage);
        mButton_order_get = view.findViewById(R.id.button_order_get);
        mButton_order_query = view.findViewById(R.id.button_order_query);
        mButton_order_complete = view.findViewById(R.id.button_order_complete);
        mButton_order_clear = view.findViewById(R.id.mButton_order_clear);
        mButton_order_save = view.findViewById(R.id.mButton_order_save);
        mButton_process_exist = view.findViewById(R.id.button_process_exist);
        mButton_sign_type = view.findViewById(R.id.button_sign_type);
        if (started) {
            mButton_start.setText("停止自动化测试");
        } else {
            mButton_start.setText("开始自动化测试");
        }

        mButton_sign_type.setVisibility(View.GONE);
        mButton_order_get.setVisibility(View.GONE);
        mButton_order_query.setVisibility(View.GONE);
        mButton_order_complete.setVisibility(View.GONE);
        mButton_order_clear.setVisibility(View.GONE);
        mButton_order_save.setVisibility(View.GONE);
        mButton_usage.setVisibility(View.GONE);
        mButton_process_exist.setVisibility(View.GONE);

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
//                String sss = "【招商银行】验证码";
//                String yzm = "【招商银行】验证码075597，您正在用一网通登录，如非本人操作，请联系95555。请勿在任何短信或邮件链接的页面中输入验证码！";
//                String YamStr = Commonutil.getYzmFromSms(yzm);
//                LogUtils.e("======", "======YamStr:" + YamStr);
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        100);
            }
        });

        mButton_process_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.getTopAppProcess(getContext());
            }
        });

        mButton_sign_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSignType();
            }
        });

        mButton_order_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeMore(new Runnable() {
                    @Override
                    public void run() {
                        String s = NetApiUtil.taskPost(getContext(), onlyValidAppid);
                        handleGetOrder(s);
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
                        String s = NetApiUtil.taskQuery(getContext(), tradeNo, onlyValidAppid);
                        LogUtils.e(TAG, "taskPost: " + s);
                        JSONObject parseObject = JSONObject.parseObject(s);
                        int intValue = parseObject.getIntValue("code");
                        JSONObject jSONObject = parseObject.getJSONObject("data");
                        if (jSONObject != null && jSONObject.isEmpty()) {
                            int orderStatus = jSONObject.getInteger("orderStatus");
                            LogUtils.e("======", "======orderStatus:" + orderStatus);
                        }
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
                        NetApiUtil.taskComplete(getContext(), tradeNo, onlyValidAppid);
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
        requestSignType();
    }

    private void requestSignType() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "请求获取设备签名方式");
        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                String s = NetApiUtil.getSignType(getContext());
                if (!TextUtils.isEmpty(s)) {
                    JSONObject parseObject = JSONObject.parseObject(s);
                    JSONObject jSONObject = parseObject.getJSONObject("data");
                    onlyValidAppid = jSONObject.getIntValue("onlyValidAppid");
                    getContext().getSharedPreferences("setting", 0).edit().putInt("onlyValidAppid", onlyValidAppid).commit();
                    LogUtils.e("======", "======onlyValidAppid:" + onlyValidAppid);
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
                LogUtils.d("======", "====== tradeNo：" + this.tradeNo);
            } else {

            }
        } else {

        }
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
