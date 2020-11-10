package com.position.wyh.position;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hadoop on 2017-08-13.
 */

public class SettingFragment extends BaseFragment {

    private TextView tvCPU;
    private TextView tvDisk;
    private EditText etDeviceID;
    private EditText etPassword;
    private Button mButton_save;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, null, false);
        initView(view);
        return view;
    }

    void initView(View view) {
        tvCPU = (TextView) view.findViewById(R.id.tv_CPU);
        tvDisk = (TextView) view.findViewById(R.id.tv_disk);
        tvCPU.setText("CPU ID: "+getCPUSerial(this.getContext())+"\n\n");
        tvDisk.setText("硬盘 ID: "+GetDiskId()+"\n\n");

        etDeviceID = (EditText) view.findViewById(R.id.editText_deviceID);
        etPassword = (EditText) view.findViewById(R.id.editText_Password);
        //获取id
        mButton_save = view.findViewById(R.id.save);
//获得SharedPreferences的实例
        SharedPreferences sp = this.getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
//通过key值获取到相应的data，如果没取到，则返回后面的默认值
        String data = sp.getString("deviceId", "请输入设备ID");
        etDeviceID.setText(data);
        String pwd = sp.getString("Password", "");
        etPassword.setText(pwd);
        //点击事件
        mButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = etDeviceID.getText().toString();
                //获得SharedPreferences的实例 sp_name是文件名
                SharedPreferences sp = getActivity().getBaseContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
//获得Editor 实例
                SharedPreferences.Editor editor = sp.edit();
//以key-value形式保存数据
                editor.putString("deviceId", data);

                data = etPassword.getText().toString();
                editor.putString("Password", data);
//apply()是异步写入数据
                editor.apply();
//commit()是同步写入数据
                editor.commit();
                Toast.makeText(getActivity(),"保存成功！",Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号(16位)
     * 读取失败为"0000000000000000"
     */
    public static String getCPUSerial(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID)+"guangfabank";
    }

   public static  String  GetDiskId(){

       String hardwareInfo = android.os.Build.BOARD + android.os.Build.BRAND + android.os.Build.DEVICE + android.os.Build.DISPLAY
               + android.os.Build.HOST + android.os.Build.ID + android.os.Build.MANUFACTURER + android.os.Build.MODEL
               + android.os.Build.PRODUCT + android.os.Build.TAGS + android.os.Build.TYPE +android.os. Build.USER;

       return MD5Tool.CalcMD5(hardwareInfo);
   }

}
