package com.qzj.chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gzt.com.apptest.R;
import com.qzj.chat.view.ChatItemLayout;

/**
 * 左侧文本holder
 * Created by qzj on 2015/12/7.
 */
public class LeftTextHolder extends RecyclerView.ViewHolder {

    // root view
    public ChatItemLayout rootView;

    // 头像
    public ImageView headView;

    // 文字内容
    public TextView textContent;

    public LeftTextHolder(View itemView) {
        super(itemView);
        rootView = (ChatItemLayout) itemView.findViewById(R.id.cilt_content);
        textContent = (TextView) itemView.findViewById(R.id.cilt_content_text);
        headView = (ImageView) itemView.findViewById(R.id.cilt_head);
    }

}
