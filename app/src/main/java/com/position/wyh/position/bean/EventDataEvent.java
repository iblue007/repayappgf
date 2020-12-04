package com.position.wyh.position.bean;

/**
 * eventbus 数据传递类型
 * Create by xuqunxing on  2019/4/26
 */
public class EventDataEvent {

    private int mainTabPos;//作用：首页标签切换
    private String message;//json字符串，其中actionType 1：表示切换视图（底部tab），2.表示修改背景图片,3.是否停止高斯模糊图片切换4.刷新当前tab的页面5.修改购物车数量

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMainTabPos() {
        return mainTabPos;
    }

    public void setMainTabPos(int mainTabPos) {
        this.mainTabPos = mainTabPos;
    }
}
