package com.position.wyh.position.utlis;

import android.widget.BaseAdapter;

import com.google.gson.JsonObject;
import com.position.wyh.position.bean.EventDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Create by xuqunxing on  2019/6/12
 */
public class EventBusUtil {

    public static int REQUEST_FLOAT_WINDOW = 1000;

    public static void sendMessage(int actionType) {
        EventDataEvent dataSynEvent = new EventDataEvent();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("actionType", actionType);
        dataSynEvent.setMessage(jsonObject.toString());
        EventBus.getDefault().post(dataSynEvent);
    }

    public static void sendMessage(int actionType, JsonObject jsonObject) {
        EventDataEvent dataSynEvent = new EventDataEvent();
        jsonObject.addProperty("actionType", actionType);
        dataSynEvent.setMessage(jsonObject.toString());
        EventBus.getDefault().post(dataSynEvent);
    }

    public static void sendEventH5RefreshMessage(int actionType) {
        EventDataEvent dataSynEvent = new EventDataEvent();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("actionType", actionType);
        dataSynEvent.setMessage(jsonObject.toString());
        EventBus.getDefault().post(dataSynEvent);
    }

    //跳转到对应活动的fragment
    public static void jumpToActivityFragment(int actionType, int mainTabPos, int activityId) {
        EventDataEvent dataSynEvent = new EventDataEvent();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("actionType", actionType);
        jsonObject.addProperty("mainTabPos", mainTabPos);
        if (activityId > 0) {
            jsonObject.addProperty("activityId", activityId);
        }
        dataSynEvent.setMessage(jsonObject.toString());
        EventBus.getDefault().post(dataSynEvent);
    }


}
