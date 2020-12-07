package com.position.wyh.position;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.position.wyh.position.test.Md5Util;
import com.position.wyh.position.test.StringUtils;
import com.position.wyh.position.utlis.EventBusUtil;
import com.position.wyh.position.utlis.LogUtils;
import com.position.wyh.position.utlis.OkHttpUtil;
import com.position.wyh.position.utlis.SystemUtil;
import com.position.wyh.position.utlis.ThreadUtil;
import com.position.wyh.position.utlis.onCallBack;
import com.position.wyh.position.utlis.testUtil;

import java.util.HashMap;
import java.util.TimerTask;

public class AutoClickService extends AccessibilityServiceZhanShan {

    TimerTask task = new TimerTask() {
        /* class com.position.wyh.position.AutoClickService.AnonymousClass1 */
        public void run() {
            try {
                String topApp = SystemUtil.getTopApp(getApplicationContext());
                LogUtils.e("======", "======##################当前运行的app:"+topApp);
                if (state == State.WAITING || state == State.ShortMessage) {
                    if (!TextUtils.isEmpty(SmsObserver.mReceivedSmsStr) && SmsObserver.mReceivedState == 1) {
                        LogUtils.e("======", "======##################上传短信信息");
                        deviceNoftify();
                        SmsObserver.mReceivedSmsStr = "";
                        SmsObserver.mReceivedState = -1;
                        shortMessageCount = 0;
                        state = State.WAITING;
                        return;
                    }
                    AutoClickService.this.ztLog("===TimerTask=== " + AutoClickService.this.orderScore + " " + knowledgeFragment.started + " tradeNo " + AutoClickService.this.tradeNo + " lastTradeNo " + AutoClickService.this.lastTradeNo + " durings " + AutoClickService.this.durings);
                    if (!getOrderData && knowledgeFragment.started) {//还没有请求getOrder  AutoClickService.this.orderScore == ""
                        AutoClickService.this.ztLog("===TimerTask===11 " + AutoClickService.this.orderScore + " " + knowledgeFragment.started);
                        if (state == State.ShortMessage) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("message", "等待短信信息:" + state);
                            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                            shortMessageCount = shortMessageCount + 1;
                            LogUtils.e("=======", "======shortMessageCount:" + shortMessageCount);
                            if (shortMessageCount >= 6) {//6次等于50秒
                                SmsObserver.mReceivedSmsStr = "没收到短信";
                                deviceNoftify();
                                SmsObserver.mReceivedSmsStr = "";
                                SmsObserver.mReceivedState = -1;
                                shortMessageCount = 0;
                                state = State.WAITING;
                            }
                        } else {
                            AutoClickService.this.taskPost();
                        }
                        AutoClickService.this.performTaskClick();
                        AutoClickService.this.Sleep(200);
                        AutoClickService.this.performTaskClick();
                        AutoClickService.this.lastTradeNo = AutoClickService.this.tradeNo;
                        AutoClickService.this.durings = 0;
                    } else if (getOrderData && knowledgeFragment.started) {////请求getOrder后 AutoClickService.this.tradeNo.equals(AutoClickService.this.lastTradeNo)
                        AutoClickService.this.durings++;
                    }
                    if (AutoClickService.this.durings >= 1) {////请求getOrder3个循环后
                        AutoClickService.this.performTaskClick();
                        AutoClickService.this.Sleep(200);
                        AutoClickService.this.performTaskClick();
                        AutoClickService.this.durings = 0;
                        if (!TextUtils.isEmpty(bankAccount) && !TextUtils.isEmpty(bankCardNo)) {
                            state = State.Tranfer;
                            durings = 0;
                            lastTradeNo = "";
                            changeCount = 0;
                        }
                    }
                    //SystemUtil.isBackground(getApplicationContext());
//                    if (!knowledgeFragment.started) {
//                        AutoClickService.this.orderScore = "";//BigDecimal.valueOf(0L);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public enum State {
        Main,
        Tranfer,
        Bank,
        Accout,
        Conform,
        SMB,
        WAITING,
        Password,
        Login,
        ShortMessage;
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

    @RequiresApi(api = 18)
    public void onAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        String charSequence = accessibilityEvent.getPackageName().toString();
        String charSequence2 = accessibilityEvent.getClassName().toString();
        final AccessibilityNodeInfo rootInActiveWindow22 = getRootInActiveWindow();
        testUtil.test(eventType);
        //LogUtils.e("======", "=======onAccessibilityEvent--state:" + state);
        LogUtils.e("######", "######--state:" + state);
        if (eventType == 2048 || eventType == 32 || eventType == 4096) {
            if (AccessibilityServiceBase.BANKINT == AccessibilityServiceBase.BANK_ZHAOSHAN) {
                if (state == State.Main) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "进入首页:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
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
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "执行登录脚本:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                    isExists(rootInActiveWindow22, "银行卡", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            Sleep(2000);
                            findParentClickChild(rootInActiveWindow22, "首页", 2, -1, "", new onCallBack() {
                                @Override
                                public void onCallBack(Object object) {
                                    state = State.WAITING;
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("message", "已经登录，回到首页，进入等待状态:" + state);
                                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                                    LogUtils.e("======", "======已经登录~~~");
                                }
                            });
                        }
                    });
                    findViewEvent2(rootInActiveWindow22, "请输入密码", 3, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "======请输入密码click");
                        }
                    });
                    isExists(rootInActiveWindow22, "请输入密码", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("message", "开始输入密码:" + state);
                            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
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
                                                    JsonObject jsonObject = new JsonObject();
                                                    jsonObject.addProperty("message", "点击登录:" + state);
                                                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                                                    findParentClickChild(rootInActiveWindow22, "登录", 2, -1, "", new onCallBack() {
                                                        @Override
                                                        public void onCallBack(Object object) {
                                                            Sleep(2000);
                                                            isExists(rootInActiveWindow22, "银行卡", new onCallBack() {
                                                                @Override
                                                                public void onCallBack(Object object) {
                                                                    Sleep(2000);
                                                                    findParentClickChild(rootInActiveWindow22, "首页", 2, -1, "", new onCallBack() {
                                                                        @Override
                                                                        public void onCallBack(Object object) {
                                                                            state = State.WAITING;
                                                                            LogUtils.e("======", "======已经登录~~~");
                                                                            findParentClickChild(rootInActiveWindow22, "首页", 2, -1, "", new onCallBack() {
                                                                                @Override
                                                                                public void onCallBack(Object object) {
                                                                                    JsonObject jsonObject = new JsonObject();
                                                                                    jsonObject.addProperty("message", "已经登录，回到首页，进入等待状态:" + state);
                                                                                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                                                                                    LogUtils.e("======", "======已经登录~~~");
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
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (state == State.Tranfer) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "执行转账脚本:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                    pfczBoolean = false;
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
//
                    findParentClickChild(rootInActiveWindow22, "转账", 2, -1, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======转账--state:" + state);
                            Sleep(1000);
                            findViewEvent(rootInActiveWindow22, "银行账号转账", 2, "", new onCallBack() {
                                @Override
                                public void onCallBack(Object object) {
                                    LogUtils.e("======", "=======银行账号转账--state:" + state);
                                }
                            });
                        }
                    });
                    JsonObject jsonObject2 = new JsonObject();
                    jsonObject2.addProperty("message", "转账-输入账号等信息:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject2);
                    findViewEvent(rootInActiveWindow22, "银行账号转账", 2, "", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======银行账号转账--state:" + state);
                        }
                    });

                    findParentClickChild(rootInActiveWindow22, "账号", 1, 2, bankCardNo, new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            if (!pfczBoolean) {
                                state = State.Accout;
                            }
                            LogUtils.e("======", "=======账号--state:" + state);
                        }
                    });

                    Sleep(1000);
                    findParentClickChild(rootInActiveWindow22, "户名", 1, 2, bankAccount, new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            LogUtils.e("======", "=======户名--state:" + state);
                        }
                    });
                } else if (state == State.Accout) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "转账-输入金额:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                    Sleep(2000);
                    SmsObserver.mReceivedSmsStr = "";
                    SmsObserver.mReceivedState = 1;
//                     orderStatus = 1;
                    if (orderStatus == 1) {
                        findViewEvent(rootInActiveWindow22, "0手续费", 2, "0.1", new onCallBack() {
                            @Override
                            public void onCallBack(Object object) {
                                DFSPasswordZhaoshan(rootInActiveWindow22, "1", "android.view.View", orderScore, new onCallBack() {
                                    @Override
                                    public void onCallBack(Object object) {
                                        // String strings = (String) object;
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("message", "输入转账金额:" + state);
                                        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);

                                        zhanShanInputMoneyInt = zhanShanInputMoneyInt + 1;
                                        LogUtils.e("======", "======str:" + zhanShanInputMoneyInt);
                                        if (zhanShanInputMoneyInt >= orderScore.length()) {
                                            //   state = State.Conform;
                                            Sleep(2000);
                                            findViewClickParent(rootInActiveWindow22, "完成", 3, new onCallBack() {
                                                @Override
                                                public void onCallBack(Object object) {
                                                    Sleep(1000);
                                                    DFSExtLogin(rootInActiveWindow22, "android.widget.Button", "下一步", 2, new onCallBack() {
                                                        @Override
                                                        public void onCallBack(Object object) {
                                                            state = State.Password;
                                                            orderStatus = -1;
                                                            zhanShanInputMoneyInt = 0;
                                                            JsonObject jsonObject = new JsonObject();
                                                            jsonObject.addProperty("message", "金额输入完毕，准备输入密码:" + state);
                                                            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                        if (zhanShanInputMoneyInt >= orderScore.length()) {
                            findViewClickParent(rootInActiveWindow22, "完成", 3, new onCallBack() {
                                @Override
                                public void onCallBack(Object object) {
                                    Sleep(1000);
                                    DFSExtLogin(rootInActiveWindow22, "android.widget.Button", "下一步", 2, new onCallBack() {
                                        @Override
                                        public void onCallBack(Object object) {
                                            state = State.Password;
                                            orderStatus = -1;
                                            zhanShanInputMoneyInt = 0;
                                        }
                                    });
                                }
                            });
                        }
                    } else if (orderStatus == -1) {
                        ThreadUtil.executeMore(new Runnable() {
                            @Override
                            public void run() {
                                AutoClickService.this.taskPostQuery(new onCallBack() {
                                    @Override
                                    public void onCallBack(Object object) {
                                        orderStatus = 1;
                                    }
                                });
                            }
                        });
                    } else {
                        JsonObject jsonObject2 = new JsonObject();
                        jsonObject2.addProperty("message", "订单状态不对，进入等待状态:" + state);
                        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject2);
                        state = State.WAITING;
                        performBackClick();
                        Sleep(500);
                        performBackClick();
                    }
                } else if (state == State.Password) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "执行输入密码脚本:" + state);
                    EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
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
                                zhanShanPwdInputInt = 0;
                                DFSExtLogin(rootInActiveWindow22, "android.widget.Button", "确认转账", 2, new onCallBack() {
                                    @Override
                                    public void onCallBack(Object object) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("message", "确认转账:" + state);
                                        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                                        LogUtils.e("======", "======okkkkkk");
                                    }
                                });
                            }
                        }
                    });
                    isExists(rootInActiveWindow22, "转账成功", new onCallBack() {
                        @Override
                        public void onCallBack(Object object) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("message", "转账成功，退回首页:" + state);
                            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                            state = State.ShortMessage;
                            getOrderData = false;
                            performBackClick();
                            Sleep(1000);
                            performBackClick();
                        }
                    });
                }
            } else if (state == State.WAITING) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "等待状态:" + state);
                EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
                LogUtils.e("======", "=======等待--state:" + state);
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
//        String OrderDetail = sharedPreferences.getString("OrderDetail", "");
        // String OrderDetail = Commonutil.readTextFile("orderInfo.txt");
        // if (TextUtils.isEmpty(OrderDetail)) {
        String deviceNo = sharedPreferences.getString("deviceId", "347a9ae9065a8c54b798afde7a08bd73");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", deviceNo);
        paramMap.put("appid", appId);

        String paramsStr = StringUtils.ascriAsc(paramMap);
        LogUtils.e(TAG, "taskPost: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", deviceNo);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDevGetOrder?", paramMap2);
        Log.e(TAG, "taskPost: " + s);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("message", "获取本地订单信息:" + s);
        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject2);
        handleGetOrder(s);
//        } else {
//            JsonObject jsonObject2 = new JsonObject();
//            jsonObject2.addProperty("message", "获取本地订单信息:" + OrderDetail);
//            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject2);
//            JSONObject parseObject = JSONObject.parseObject(OrderDetail);
//            int intValue = parseObject.getIntValue("code");
//            if (intValue == 0) {
//                JSONObject jSONObject = parseObject.getJSONObject("data");
//                if (!jSONObject.isEmpty()) {
//                    LogUtils.e("======", "======do sp stringBuffer2:" + OrderDetail);
//                    parseGetOrderJson(jSONObject);
//                }
//            }
//        }
    }

    public void taskPostQuery(onCallBack onCallBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "查看订单状态:" + state);
        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        //  String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("tradeNo", tradeNo);
        //  paramMap.put("appId", appId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);
        LogUtils.e(TAG, "taskPostQuery: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
        paramMap2.put("tradeNo", tradeNo);
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/queryOrder?", paramMap2);
        LogUtils.e(TAG, "taskPost: " + s);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("message", "查询订单状态接口:" + state);
        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject2);
        JSONObject parseObject = JSONObject.parseObject(s);
        int intValue = parseObject.getIntValue("code");
        JSONObject jSONObject = parseObject.getJSONObject("data");
        if (jSONObject != null && !jSONObject.isEmpty()) {
            orderStatus = jSONObject.getInteger("orderStatus");
            LogUtils.e("======", "======orderStatus:" + orderStatus);
            JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("message", "订单状态:" + orderStatus);
            EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject3);
            if (orderStatus == 1) {
                if (onCallBack != null) {
                    onCallBack.onCallBack(orderStatus);
                }
            } else if (orderStatus == 3) {
                state = State.WAITING;
                JsonObject jsonObject4 = new JsonObject();
                jsonObject4.addProperty("message", "订单已经转账过了，不要重复转账:" + state);
                EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject4);
                resetData(false);
            }
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
                //  getSharedPreferences("setting", 0).edit().putString("OrderDetail", stringBuffer2).commit();
//                Commonutil.delFile(MainActivity.MAIN_TEMP + "orderInfo.txt");
//                Commonutil.saveToSDCard(getApplicationContext(), "orderInfo.txt", stringBuffer2);
                LogUtils.e("======", "======stringBuffer2:" + stringBuffer2);
                parseGetOrderJson(jSONObject);
            }
        }
    }

    private void parseGetOrderJson(JSONObject jSONObject) {
        getOrderData = true;
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
            String orderScoreNormal = jSONObject.getBigDecimal("orderScore") + "";
            // this.orderScore = "0.01";
            String stripZerostr = Commonutil.stripZeros(orderScoreNormal);
            //todo:小数点要修改
            if (stripZerostr.contains(".")) {
                orderScore = Commonutil.getSpecialCharacter(stripZerostr, "·");
            } else {
                orderScore = stripZerostr;
            }

            LogUtils.d("GK", "result orderScore = " + orderScore);
            LogUtils.d("GK", "result orderScore = " + this.orderScore);
//                if (!TextUtils.isEmpty(bankAccount) && !TextUtils.isEmpty(bankCardNo)) {
//                    if (changeCount % 2 == 0) {
//                        LogUtils.e("======", "======qqqq111:" + changeCount);
//                    } else {
//                        LogUtils.e("======", "======qqqq111前台:" + changeCount);
//                        state = State.Tranfer;
//                    }
//                }
        } else {
            // ztLog("task code =   " + intValue);
        }
    }

    public void deviceNoftify() {
        if (TextUtils.isEmpty(SmsObserver.mReceivedSmsStr)) {
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "确认订单完成:" + state);
        EventBusUtil.sendMessage(EventBusUtil.REQUEST_FLOAT_WINDOW, jsonObject);
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String string = sharedPreferences.getString("deviceId", "d23eab596657293008bd9b9d75f935c6");
        String appId = sharedPreferences.getString("appId", "aa12bda5ddc10ee8e547043a532485c6");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceNo", string);
        paramMap.put("tradeNo", tradeNo);
        paramMap.put("orderStatus", "3");
        paramMap.put("shortMsg", SmsObserver.mReceivedSmsStr);
        paramMap.put("appid", appId);
        String paramsStr = StringUtils.ascriAsc(paramMap);
        String sign = Md5Util.MD5Encode(paramsStr);
        paramMap.put("sign", sign);
        LogUtils.e("======", "taskPostQuery: " + paramsStr);
        HashMap<String, String> paramMap2 = new HashMap<>();
        paramMap2.put("sign", sign);
        paramMap2.put("deviceNo", string);
        paramMap2.put("tradeNo", tradeNo);
        paramMap2.put("shortMsg", SmsObserver.mReceivedSmsStr);
        paramMap2.put("orderStatus", "3");
        String ip = sharedPreferences.getString("IP", "47.242.229.28");
        String s = OkHttpUtil.postSubmitFormsynchronization("http://" + ip + "/api/order/appDeviceNotifyV2?", paramMap2);
        LogUtils.e("======", "======taskPost: " + s);
        resetData(true);
    }

}
