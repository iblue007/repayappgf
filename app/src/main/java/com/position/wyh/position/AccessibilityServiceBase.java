package com.position.wyh.position;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.onCallBack;

import java.math.BigDecimal;

public class AccessibilityServiceBase extends AccessibilityService {

    protected static final String TAG = "GK";
    protected String bankAccount = "徐群星";
    protected String bankCardNo = "6230580000259907983";
    protected String transMoney = "0·01";
    protected int zhanShanInputMoneyInt = 0;
    protected int zhanShanPwdInputInt = 0;
    public AutoClickService.State state = AutoClickService.State.Tranfer;
    public String orderScore = "0·01";//BigDecimal.valueOf(0L);
    public static int CARINT_ZHAOSHAN = 0;//0 招商
    public static int CATINT = CARINT_ZHAOSHAN;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    protected void findViewEvent(AccessibilityNodeInfo accessibilityNodeInfo, String str2, int flag, String inputStr, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString() + "--className:" + accessibilityNodeInfo.getClassName());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    if (flag == 1) {
                        performInput(accessibilityNodeInfo, inputStr);//13692255330
                    } else if (flag == 2) {
                        performClick(accessibilityNodeInfo);
                    } else if (flag == 3) {
                        // performInput(accessibilityNodeInfo, "xuqunxing_");//13692255330
                        performClickExt(accessibilityNodeInfo.getParent().getParent(), str2, false);
                    }
                    if (onCallBack != null) {
                        onCallBack.onCallBack(flag);
                    }
                    return;
                } else {

                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                findViewEvent(accessibilityNodeInfo.getChild(i), str2, flag, inputStr, onCallBack);
            }
        }
    }

    protected void findViewClickParentByTag(AccessibilityNodeInfo accessibilityNodeInfo, String tag, String str2, int flag, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getClassName().equals(tag) && accessibilityNodeInfo.getText().toString().equals(str2)) {
                    if (flag == 1) {
                        performInput(accessibilityNodeInfo, "13692255330");//13692255330
                    } else if (flag == 2) {
                        performClick(accessibilityNodeInfo.getParent());
                    } else if (flag == 3) {
                        // performInput(accessibilityNodeInfo, "xuqunxing_");//13692255330
                        performClickExt(accessibilityNodeInfo.getParent(), str2, false);
                    }
                    //   this.state = AutoClickService.State.Login;
                    if (onCallBack != null) {
                        onCallBack.onCallBack(flag);
                    }
                    return;
                } else {

                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                findViewClickParent(accessibilityNodeInfo.getChild(i), str2, flag, onCallBack);
            }
        }
    }

    protected void findViewClickParent(AccessibilityNodeInfo accessibilityNodeInfo, String str2, int flag, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    if (flag == 1) {
                        performInput(accessibilityNodeInfo, "13692255330");//13692255330
                    } else if (flag == 2) {
                        performClick(accessibilityNodeInfo.getParent());
                    } else if (flag == 3) {
                        // performInput(accessibilityNodeInfo, "xuqunxing_");//13692255330
                        performClickExt(accessibilityNodeInfo.getParent(), str2, false);
                    }
                    //  this.state = AutoClickService.State.Login;
                    if (onCallBack != null) {
                        onCallBack.onCallBack(flag);
                    }
                    return;
                } else {

                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                findViewClickParent(accessibilityNodeInfo.getChild(i), str2, flag, onCallBack);
            }
        }
    }

    protected void findParentClickChild(AccessibilityNodeInfo accessibilityNodeInfo, String str2, int flag, int pos, String inputStr, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    LogUtils.e("======", "======flag:" + flag);
                    if (flag == 1) {
                        performInput(accessibilityNodeInfo.getParent().getChild(pos), inputStr);//13692255330
                    } else if (flag == 2) {
                        if (pos < 0) {
                            performClick(accessibilityNodeInfo.getParent());
                        } else {
                            performClick(accessibilityNodeInfo.getParent().getChild(pos));
                        }
                    } else if (flag == 3) {
                        // performInput(accessibilityNodeInfo, "xuqunxing_");//13692255330
                        performClickExt(accessibilityNodeInfo.getParent(), str2, false);
                    }
                    // this.state = AutoClickService.State.Login;
                    if (onCallBack != null) {
                        onCallBack.onCallBack(flag);
                    }
                    return;
                } else {

                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                findParentClickChild(accessibilityNodeInfo.getChild(i), str2, flag, pos, inputStr, onCallBack);
            }
        }
    }

    protected void DFSExtLogin(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2, int flag, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            ztLog("rootInfo = class" + accessibilityNodeInfo.getClassName().toString());
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo = text " + accessibilityNodeInfo.getText().toString());
                ztLog("rootInfo = text " + accessibilityNodeInfo);
            }
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    if (flag == 1) {
                        performInput(accessibilityNodeInfo, "13692255330");//13692255330
                    } else if (flag == 2) {
                        performClick(accessibilityNodeInfo);
                    } else if (flag == 3) {
                        // performInput(accessibilityNodeInfo, "xuqunxing_");//13692255330
                        performClickExt(accessibilityNodeInfo.getParent(), str2, false);
                    }
                    //   this.state = AutoClickService.State.Login;
                    try {
                        Thread.sleep(500);
                        if (onCallBack != null) {
                            onCallBack.onCallBack(flag);
                        }
                    } catch (Exception unused) {
                    }
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                } else {

                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSExtLogin(accessibilityNodeInfo.getChild(i), str, str2, flag, onCallBack);
            }
        }
    }

    protected Point GetPasswordNumPoint(AccessibilityNodeInfo accessibilityNodeInfo, String c) {
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            if (!(accessibilityNodeInfo.getChild(i) == null || accessibilityNodeInfo.getChild(i).getText() == null)) {
                Log.d("GK", "targetInfo.getChild(i) " + ((Object) accessibilityNodeInfo.getChild(i).getText()) + " c: " + c);
                Log.e("====", "=====:" + accessibilityNodeInfo.getChild(i).getText());
                if (accessibilityNodeInfo.getChild(i).getText().toString().equals(c)) {
                    Rect rect = new Rect();
                    accessibilityNodeInfo.getChild(i).getBoundsInScreen(rect);
                    Log.d("GK", "targetInfo.getChild(i)" + rect);
                    return new Point((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
                }
            }
        }
        return new Point(0, 0);
    }

    protected void onGestureLogin(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        Point GetPasswordNumPoint = GetPasswordNumPoint(accessibilityNodeInfo, str);
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

    protected void isExists(AccessibilityNodeInfo accessibilityNodeInfo, String str, onCallBack onCallBack) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfoq=x " + accessibilityNodeInfo.getText().toString() + "--str:" + str);
                if (accessibilityNodeInfo.getText().toString().equals(str)) {
                    ztLog("===state=== found" + this.state + this.orderScore + "-String:" + accessibilityNodeInfo.getText().toString());
                    if (onCallBack != null) {
                        onCallBack.onCallBack(-1);
                    }
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                isExists(accessibilityNodeInfo.getChild(i), str, onCallBack);
            }
        }
    }

    @RequiresApi(api = 24)
    protected void performClickExt(AccessibilityNodeInfo accessibilityNodeInfo, String str, boolean doFor) {
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        Log.d("GK", "printTree: bound:" + rect + " " + rect.height() + " " + rect.width());
        int height = rect.height() / 4;
        int width = rect.width() / 3;
        if (doFor) {
            for (int i = 0; i < str.length(); i++) {
                onGestureLogin(accessibilityNodeInfo, str.charAt(i) + "");
            }
        } else {
            onGestureLogin(accessibilityNodeInfo, str);
        }
    }

    @RequiresApi(api = 24)
    protected void performClickExt(AccessibilityNodeInfo accessibilityNodeInfo) {
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        Log.d("GK", "printTree: bound:" + rect);
        Point point = new Point(rect.left + 10, rect.top + 10);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo((float) point.x, (float) point.y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        dispatchGesture(builder.build(), new GestureResultCallback() {
            /* class com.position.wyh.position.AutoClickService.AnonymousClass3 */

            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d("GK", "onCompleted: 完成..........");
            }

            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("GK", "onCompleted: 取消..........");
            }
        }, null);
    }

    protected void performClick(AccessibilityNodeInfo accessibilityNodeInfo) {
        accessibilityNodeInfo.performAction(16);
    }

    protected void performInput(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", charSequence);
        accessibilityNodeInfo.performAction(1);
        accessibilityNodeInfo.performAction(2097152, bundle);
    }

    /* access modifiers changed from: private */
    protected void ztLog(String str) {
        ztLog(str, false);
    }

    protected void ztLog(String str, boolean z) {
        Log.i("GK", str);
        if (z) {
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void Sleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (Exception unused) {
        }
    }
}
