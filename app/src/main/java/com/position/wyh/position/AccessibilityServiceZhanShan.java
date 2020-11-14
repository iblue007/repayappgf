package com.position.wyh.position;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.onCallBack;

public class AccessibilityServiceZhanShan extends AccessibilityServiceBase {

    @RequiresApi(api = 24)
    protected void DFSPasswordZhaoshan(AccessibilityNodeInfo accessibilityNodeInfo, String flag, String str, String str2, onCallBack onCallBack) {//flag "1","q"
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfoq=x " + accessibilityNodeInfo.getText().toString() + "-str2:" + str2);

                if (accessibilityNodeInfo.getText().toString().equals(flag)) {
                    LogUtils.e("======", "======1111111111");
                    performClickZhanshan(accessibilityNodeInfo.getParent().getParent(), str2, true, onCallBack);
//                    this.state = State.Login;
                    ztLog("===state=== found" + this.state + this.orderScore + "-String:" + accessibilityNodeInfo.getText().toString());
                    return;
                }
            }
            LogUtils.e("======", "======22222222222");
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSPasswordZhaoshan(accessibilityNodeInfo.getChild(i), flag, str, str2, onCallBack);
            }
        }
    }

    @RequiresApi(api = 24)
    protected void performClickZhanshan(AccessibilityNodeInfo accessibilityNodeInfo, String str, boolean doFor, onCallBack onCallBack) {
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        Log.d("GK", "printTree: bound:" + rect + " " + rect.height() + " " + rect.width());
        int height = rect.height() / 4;
        int width = rect.width() / 3;
        if (doFor) {
            for (int i = 0; i < str.length(); i++) {
                onGestureZhanshan(accessibilityNodeInfo, str.charAt(i) + "", onCallBack);
            }
        } else {
            onGestureZhanshan(accessibilityNodeInfo, str, onCallBack);
        }
    }

    protected void onGestureZhanshan(AccessibilityNodeInfo accessibilityNodeInfo, String str, onCallBack onCallBack) {
        Point GetPasswordNumPoint = GetPasswordNumPointZhanshan(accessibilityNodeInfo, str);
        StringBuilder sb = new StringBuilder();
        sb.append("printTree: num:");
        sb.append(str + "-0");
        sb.append("position");
        sb.append(GetPasswordNumPoint);
        Log.d("======GK", "====" + sb.toString());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo((float) GetPasswordNumPoint.x, (float) GetPasswordNumPoint.y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        if (onCallBack != null) {
            onCallBack.onCallBack(str);
        }
        dispatchGesture(builder.build(), new GestureResultCallback() {
            /* class com.position.wyh.position.AutoClickService.AnonymousClass4 */

            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d("GK", "onCompleted: 完成..........");
            }

            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("GK", "onCompleted: 取消..........");
            }
        }, null);
        try {
            Thread.sleep(300);
        } catch (Exception unused) {
        }
    }

    protected Point GetPasswordNumPointZhanshan(AccessibilityNodeInfo accessibilityNodeInfo, String c) {
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            if (!(accessibilityNodeInfo.getChild(i) == null || accessibilityNodeInfo.getChild(i).getChild(0).getText() == null)) {
                Log.d("GK", "targetInfo.getChild(i) " + ((Object) accessibilityNodeInfo.getChild(i).getChild(0).getText()) + " c: " + c);
                Log.e("====", "=====:" + accessibilityNodeInfo.getChild(i).getChild(0).getText());
                if (accessibilityNodeInfo.getChild(i).getChild(0).getText().toString().equals(c)) {
                    Rect rect = new Rect();
                    accessibilityNodeInfo.getChild(i).getBoundsInScreen(rect);
                    Log.d("GK", "targetInfo.getChild(i)" + rect);
                    return new Point((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
                }
            }
        }
        return new Point(0, 0);
    }
}
