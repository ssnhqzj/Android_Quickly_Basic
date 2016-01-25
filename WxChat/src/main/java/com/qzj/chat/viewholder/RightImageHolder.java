package com.qzj.chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qzj.chat.view.ChatImageView;

import gzt.com.apptest.R;


/**
 * 右侧图片holder
 * Created by qzj on 2015/12/7.
 */
public class RightImageHolder extends RecyclerView.ViewHolder {

    // 头像
    public ImageView headView;

    // 图片
    public ChatImageView imageView;

    /**
     * 是否正在上传
     */
    public boolean isUpLoading = false;

    public RightImageHolder(View itemView) {
        super(itemView);
        imageView = (ChatImageView) itemView.findViewById(R.id.ciri_content_image);
        headView = (ImageView) itemView.findViewById(R.id.ciri_head);
    }

}
