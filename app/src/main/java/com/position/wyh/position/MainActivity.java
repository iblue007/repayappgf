package com.position.wyh.position;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.position.wyh.position.adapter.HomeTitleBarAdapter;
import com.position.wyh.position.bean.EventDataEvent;
import com.position.wyh.position.test.ApiInvoleUtils;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.EventBusUtil;
import com.position.wyh.position.utlis.Global;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.MessageUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.utlis.OnClickItemCallBack;
import com.position.wyh.position.viewpagerindicator.IconPagerAdapter;
import com.position.wyh.position.viewpagerindicator.IconTabPageIndicator;
import com.position.wyh.position.viewpagerindicator.Md5Utils;
import com.position.wyh.position.widget.PermissionTipDialog;
import com.position.wyh.position.widget.WrapWrongLinearLayoutManger;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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
    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhuanzhangAssist/";
    public static final String MAIN_TEMP = BASE_DIR + "temp/";
    private ViewPager mViewPager;
    private IconTabPageIndicator mIndicator;
    private static String TAG = "MainActivity";
    private boolean showFloatWindow = false;
    private HomeTitleBarAdapter homeTitleBarAdapter = null;
    private RecyclerView recyclerViewMessgeList;
    private List<String> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        OkHttpUtil.initOkHttp();
        initViews();
        //连续启动Service
        Intent intentOne = new Intent(this, AutoClickService.class);
        startService(intentOne);
        SmsObserver mObserver = new SmsObserver(this, new Handler());
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
        requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE}, null);
        Global.setContext(this);
        Global.setHandler(new Handler());
        EventBus.getDefault().register(this);
        initFloatWindow();
    }

    private void initFloatWindow() {
        View view = View.inflate(this, R.layout.dialog_float_window, null);
        //LinearLayout LogMessageLL = view.findViewById(R.id.log_message_ll);
        recyclerViewMessgeList = view.findViewById(R.id.log_message_rv);
        recyclerViewMessgeList.setLayoutManager(new WrapWrongLinearLayoutManger(this));
        homeTitleBarAdapter = new HomeTitleBarAdapter(null);
        recyclerViewMessgeList.setAdapter(homeTitleBarAdapter);
        view.findViewById(R.id.window_manager_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FloatWindow.get() != null) {
                    FloatWindow.get().hide();
                }
//                messageList.add("456456561231");
//                homeTitleBarAdapter.setNewData(messageList);
//                homeTitleBarAdapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.log_message_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeTitleBarAdapter != null && homeTitleBarAdapter.getData().size() > 0) {
                    Commonutil.delFile(MainActivity.MAIN_TEMP + "logInfo.txt");
                    Commonutil.saveToSDCard(getApplicationContext(), "logInfo.txt", homeTitleBarAdapter.getData().toString());
                    MessageUtils.show(MainActivity.this, "保存成功！");
                }else {
                    MessageUtils.show(MainActivity.this, "没有日志可以保存！！！");
                }
            }
        });
        FloatWindow
                .with(getApplicationContext())
                .setView(view)
                .setWidth(Screen.width, 0.4f)   //设置控件宽高
                .setHeight(Screen.width, 0.4f)
                .setX(0)     //设置控件初始位置
                .setY(Screen.height, 0.3f)
                .setDesktopShow(true)    //桌面显示
                .setViewStateListener(null)    //监听悬浮控件状态改变
                .setPermissionListener(null)  //监听权限申请结果
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Commonutil.isAccessibilitySettingsOn(this) && !showFloatWindow) {
            showFloatWindow = true;
            FloatWindow.get().show();
        }
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
    }

    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        BaseFragment userFragment = new knowledgeFragment();
        userFragment.setTitle("测试");
        userFragment.setIconId(R.drawable.tab_knowledge_selector);
        fragments.add(userFragment);
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

    public void requestPermissions(final Context context, final String[] permissions,
                                   RequestPermissionCallBack callback) {
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
                    PermissionTipDialog permissionTipDialog = new PermissionTipDialog(this);
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
        if (isAllGranted && mRequestPermissionCallBack != null) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    private final int mRequestCode = 1024;
    private RequestPermissionCallBack mRequestPermissionCallBack;

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


    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            if (FloatWindow.get() != null) {
                FloatWindow.destroy();
            }
            messageList.clear();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventDataEvent eventDataEvent) {
        String message = eventDataEvent.getMessage();
        if (!TextUtils.isEmpty(message)) {
            try {
                JSONObject jsonObject = new JSONObject(message);
                int actionType = jsonObject.optInt("actionType");
                if (actionType == EventBusUtil.REQUEST_FLOAT_WINDOW) {
                    String messageStr = jsonObject.optString("message");
                    if (!TextUtils.isEmpty(messageStr)) {
                        if (homeTitleBarAdapter != null) {
                            messageList.add(messageStr);
                            homeTitleBarAdapter.setNewData(messageList);
                            homeTitleBarAdapter.notifyItemChanged(homeTitleBarAdapter.getData().size() - 1);
                            recyclerViewMessgeList.scrollToPosition(homeTitleBarAdapter.getData().size() - 1);
                            if (messageList.size() >= 1000) {
                                messageList.clear();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
