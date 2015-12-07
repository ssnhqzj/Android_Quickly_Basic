package gzt.com.apptest.Chat;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gzt.com.apptest.Chat.bean.ChatItem;
import gzt.com.apptest.Chat.viewholder.LeftTextHolder;
import gzt.com.apptest.Chat.viewholder.RightTextHolder;

/**
 * 聊天室列表Adapter
 * Created by qzj on 15/12/7.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<ChatItem> msgData = null;
    private LayoutInflater inflater = null;

    public ChatListAdapter(Context context, ArrayList<ChatItem> msgData) {
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

                break;

            case ChatItem.LAYOUT_RIGHT_IMAGE:

                break;

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
                break;

            case ChatItem.LAYOUT_RIGHT_TEXT:
                ((RightTextHolder)viewHolder).textContent.setText(msgData.get(position).getText());
                break;

            case ChatItem.LAYOUT_LEFT_IMAGE:

                break;

            case ChatItem.LAYOUT_RIGHT_IMAGE:

                break;

            case ChatItem.LAYOUT_LEFT_VOICE:

                break;

            case ChatItem.LAYOUT_RIGHT_VOICE:

                break;
        }
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
