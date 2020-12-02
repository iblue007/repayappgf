package com.position.wyh.position;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.position.wyh.position.utlis.LogUtils;

public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;
    private int mReceivedCode = 1;
    private String mReceivedSmsID = "";
    public static String mReceivedSmsStr = "";
    public static int mReceivedState = -1; //-1 不接受，1 接收

    public SmsObserver(Context context, Handler handler, int received_code) {
        super(handler);
        mContext = context;
        mHandler = handler;
        mReceivedCode = received_code;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (mReceivedState == -1) {
            return;
        }
        if (uri.toString().contains("content://sms/raw")) {
            return;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                mReceivedSmsStr = c.getString(c.getColumnIndex("body"));
                String _id = c.getString(c.getColumnIndex("_id")) + "";
                //  LogUtils.e("发件人为：" + address + "》》》》短信内容为：" + mReceivedSmsStr + "-_id:" + _id + "-url:" + uri);
                if (!mReceivedSmsID.equals(_id)) {
                    if (!TextUtils.isEmpty(mReceivedSmsStr) && mReceivedSmsStr.contains("招商银行")) {
                        LogUtils.e("发件人为：" + address + "》》》》短信内容为：" + mReceivedSmsStr + "-_id:" + _id);
                    }
                } else {
                    // LogUtils.e("发件人为2222：" + address + "》》》》短信内容为：" + body + "-_id:" + _id);
                }
                mReceivedSmsID = _id;
//                Pattern pattern = Pattern.compile("(\\d{4,6})");
//                Matcher matcher = pattern.matcher(body);
//                if (matcher.find()) {
//                    code = matcher.group(0);
//                    LogUtils.e("验证码》》》" + code);
//                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
//                    cmb.setText(code);
//
//                    mHandler.obtainMessage(mReceivedCode, code).sendToTarget();
//                }
            }
            c.close();
        }
    }
}
