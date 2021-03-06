package gzt.com.apptest.Chat.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gzt.com.apptest.Chat.bean.ChatItem;
import gzt.com.apptest.Chat.uitls.PopupWindowUtil;
import gzt.com.apptest.Chat.viewholder.LeftImageHolder;
import gzt.com.apptest.Chat.viewholder.LeftTextHolder;
import gzt.com.apptest.Chat.viewholder.RightImageHolder;
import gzt.com.apptest.Chat.viewholder.RightTextHolder;

/**
 * 聊天室列表Adapter
 * Created by qzj on 15/12/7.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        View.OnLongClickListener {

    private Context context;
    public ArrayList<ChatItem> msgData = null;
    private LayoutInflater inflater = null;

    public ChatListAdapter(Context context, ArrayList<ChatItem> msgData) {
        this.context = context;
        this.msgData = msgData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return msgData==null?0:msgData.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatItem item = msgData.get(position);
        return item == null?0:item.getLayoutType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(viewType,viewGroup,false);
        switch (viewType){
            case ChatItem.LAYOUT_LEFT_TEXT:
                return new LeftTextHolder(view);

            case ChatItem.LAYOUT_RIGHT_TEXT:
                return new RightTextHolder(view);

            case ChatItem.LAYOUT_LEFT_IMAGE:
                return new LeftImageHolder(view);

            case ChatItem.LAYOUT_RIGHT_IMAGE:
                return new RightImageHolder(view);

            case ChatItem.LAYOUT_LEFT_VOICE:

                break;

            case ChatItem.LAYOUT_RIGHT_VOICE:

                break;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case ChatItem.LAYOUT_LEFT_TEXT:
                ((LeftTextHolder)viewHolder).textContent.setText(msgData.get(position).getText());
                ((LeftTextHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;

            case ChatItem.LAYOUT_RIGHT_TEXT:
                ((RightTextHolder)viewHolder).textContent.setText(msgData.get(position).getText());
                ((RightTextHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;

            case ChatItem.LAYOUT_LEFT_IMAGE:
//                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//                ImageLoader.getInstance().displayImage(msgData.get(position).getImageUrl(),
//                        ((LeftImageHolder) viewHolder).imageView, ImageLoaderUtil.getHeadNoCacheOptions());
                break;

            case ChatItem.LAYOUT_RIGHT_IMAGE:
//                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//                ImageLoader.getInstance().displayImage(msgData.get(position).getImageUrl(),
//                        ((RightImageHolder)viewHolder).imageView, ImageLoaderUtil.getHeadNoCacheOptions());
                break;

            case ChatItem.LAYOUT_LEFT_VOICE:

                break;

            case ChatItem.LAYOUT_RIGHT_VOICE:

                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        PopupWindowUtil.openPopupWin(context,v);
        return true;
    }

    public void setMsgData(ArrayList<ChatItem> msgData) {
        this.msgData = msgData;
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildLayoutPosition(view) != 0)
                outRect.top = space;
        }
    }
}
