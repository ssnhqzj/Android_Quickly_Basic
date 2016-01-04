package com.qzj.chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import gzt.com.apptest.R;


/**
 * 左侧图片holder
 * Created by qzj on 2015/12/7.
 */
public class LeftImageHolder extends RecyclerView.ViewHolder {

    // 头像
    public ImageView headView;

    // 图片
    public ImageView imageView;

    public LeftImageHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.cili_content_image);
        headView = (ImageView) itemView.findViewById(R.id.cili_head);
    }

}
