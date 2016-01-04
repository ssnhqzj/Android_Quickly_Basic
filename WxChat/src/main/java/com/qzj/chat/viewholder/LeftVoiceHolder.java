package com.qzj.chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gzt.com.apptest.R;
import com.qzj.chat.view.ChatItemLayout;


/**
 * 左侧语音holder
 * Created by qzj on 2015/12/7.
 */
public class LeftVoiceHolder extends RecyclerView.ViewHolder {

    // root view
    public ChatItemLayout rootView;

    // 头像
    public ImageView headView;

    // 语音
    public ImageView imageView;

    // 时长
    public TextView second;

    // 小红点
    public TextView hint;

    public LeftVoiceHolder(View itemView) {
        super(itemView);
        rootView = (ChatItemLayout) itemView.findViewById(R.id.cili_content);
        imageView = (ImageView) itemView.findViewById(R.id.cili_content_image);
        headView = (ImageView) itemView.findViewById(R.id.cili_head);
        second = (TextView) itemView.findViewById(R.id.cili_content_second);
        hint = (TextView) itemView.findViewById(R.id.cili_hint);
    }

}
