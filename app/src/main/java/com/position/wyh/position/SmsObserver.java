package com.position.wyh.position;

import android.content.ClipboardManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.position.wyh.position.utlis.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;
    private int mReceivedCode = 1;

    public SmsObserver(Context context, Handler handler, int received_code) {
        super(handler);
        mContext = context;
        mHandler = handler;
        mReceivedCode = received_code;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String code = "";
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));

                LogUtils.e("发件人为：" + address + "》》》》短信内容为：" + body);
                Pattern pattern = Pattern.compile("(\\d{4,6})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                    LogUtils.e("验证码》》》" + code);
                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(code);

                    mHandler.obtainMessage(mReceivedCode, code).sendToTarget();
                }

            }
            c.close();
        }

    }
}
