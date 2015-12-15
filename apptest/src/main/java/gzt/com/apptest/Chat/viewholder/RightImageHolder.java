package gzt.com.apptest.Chat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import gzt.com.apptest.Chat.ChatItemLayout;
import gzt.com.apptest.R;

/**
 * 右侧图片holder
 * Created by qzj on 2015/12/7.
 */
public class RightImageHolder extends RecyclerView.ViewHolder {

    // root view
    public ChatItemLayout rootView;

    // 头像
    public ImageView headView;

    // 图片
    public ImageView imageView;

    public RightImageHolder(View itemView) {
        super(itemView);
        rootView = (ChatItemLayout) itemView.findViewById(R.id.ciri_content);
        imageView = (ImageView) itemView.findViewById(R.id.ciri_content_image);
        headView = (ImageView) itemView.findViewById(R.id.ciri_head);
    }

}
