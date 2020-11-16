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

/**
 * Created by hadoop on 2017-08-13.
 */

public class knowledgeFragment extends BaseFragment {

    private Button mButton_start;
    private Button mButton_assit;
    private static String TAG = "MainActivity";
    public static boolean started = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knowledge_fragment, null, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        mButton_start = view.findViewById(R.id.button_start);
        mButton_assit = view.findViewById(R.id.button_assit);
        if (started) {

            mButton_start.setText("停止自动化测试");
        } else {
            mButton_start.setText("开始自动化测试");
        }
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


}
