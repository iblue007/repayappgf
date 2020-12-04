package com.position.wyh.position.adapter;


import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.position.wyh.position.R;

import java.util.List;

/**
 * Create by xuqunxing on  2019/3/22
 */
public class HomeTitleBarAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HomeTitleBarAdapter(List<String> mDatas) {
        super(R.layout.corelib_home_titlebar_item, mDatas);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String title, final int pos) {
        TextView itemTitleTv = baseViewHolder.getView(R.id.log_message_item_tv);
        itemTitleTv.setText(title);
    }


//    private OnClickItemCallBack onClickItemCallBack;
//
//    public void setOnClickItemCallBack(OnClickItemCallBack onClickItemCallBack) {
//        this.onClickItemCallBack = onClickItemCallBack;
//    }
}
