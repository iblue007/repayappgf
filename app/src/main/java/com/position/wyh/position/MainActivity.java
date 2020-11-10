package com.position.wyh.position;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.position.wyh.position.test.ApiInvoleUtils;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.viewpagerindicator.IconPagerAdapter;
import com.position.wyh.position.viewpagerindicator.IconTabPageIndicator;
import com.position.wyh.position.viewpagerindicator.Md5Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

import okhttp3.Response;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private IconTabPageIndicator mIndicator;
    private static String TAG = "MainActivity";
    private int MSG_RECEIVED_CODE = 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                LogUtils.e("======", "======code:" + code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        OkHttpUtil.initOkHttp();

        initViews();
        //连续启动Service
        Intent intentOne = new Intent(this, AutoClickService.class);
        startService(intentOne);

        SmsObserver mObserver = new SmsObserver(this, mHandler, MSG_RECEIVED_CODE);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
        //申请写的权限
        String[] permissions = {Manifest.permission.READ_SMS};
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(permissions, 200);
        }
    }


    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // taskPost();
            }
        }).start();
    }

    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        BaseFragment userFragment = new knowledgeFragment();
        userFragment.setTitle("测试");
        userFragment.setIconId(R.drawable.tab_knowledge_selector);
        fragments.add(userFragment);

        /*BaseFragment noteFragment = new BaseFragment();
        noteFragment.setTitle("语音");
        noteFragment.setIconId(R.drawable.tab_setting_selector);
        fragments.add(noteFragment);

        BaseFragment contactFragment = new BaseFragment();
        contactFragment.setTitle("统计");
        contactFragment.setIconId(R.drawable.tab_knowledge_selector);
        fragments.add(contactFragment);*/

        BaseFragment recordFragment = new SettingFragment();
        recordFragment.setTitle("设置");
        recordFragment.setIconId(R.drawable.tab_setting_selector);
        fragments.add(recordFragment);

        return fragments;
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }


    public void taskPost() {

        String cPUSerial = SettingFragment.getCPUSerial(this);
        String GetDiskId = SettingFragment.GetDiskId();
        Log.e(TAG, "taskPost: 设备CPU号：" + cPUSerial);
        Log.e(TAG, "taskPost: 设备硬盘号：" + GetDiskId);
        String deviceNo = "d23eab596657293008bd9b9d75f935c62";
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", deviceNo);
        paramMap.put("deviceCpu", cPUSerial);
        paramMap.put("deviceCaliche", GetDiskId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", deviceNo);
        String s = OkHttpUtil.postSubmitFormsynchronization("http://47.242.140.225/api/order/getOrder?", paramMap2);

        Log.e(TAG, "taskPost: " + s);


    }

}
