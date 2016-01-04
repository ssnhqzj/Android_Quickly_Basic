package com.qzj.chat.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qzj.chat.bean.ChatItem;
import com.qzj.chat.controller.VoiceController;
import com.qzj.chat.uitls.popup.PopupList;
import com.qzj.chat.view.ChatImageView;
import com.qzj.chat.viewholder.LeftImageHolder;
import com.qzj.chat.viewholder.LeftTextHolder;
import com.qzj.chat.viewholder.LeftVoiceHolder;
import com.qzj.chat.viewholder.RightImageHolder;
import com.qzj.chat.viewholder.RightTextHolder;
import com.qzj.chat.viewholder.RightVoiceHolder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gzt.com.apptest.R;


/**
 * 聊天室列表Adapter
 * Created by qzj on 15/12/7.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        View.OnLongClickListener {

    // 消息类型为--文本
    public static final int TEXT = 1;
    // 消息类型为--图片
    public static final int IMAGE = 2;
    // 消息类型为--语音
    public static final int VOICE = 3;

    // 语音--开始播放
    private static final int VOICE_PLAY_START = 4;
    // 语音--停止播放
    private static final int VOICE_PLAY_STOP = 5;

    // TAG--消息类别key
    private static final int TAG_TYPE = 6;
    // TAG--position
    private static final int TAG_POSITION = 7;

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
        View view = inflater.inflate(viewType, viewGroup, false);
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
                return new LeftVoiceHolder(view);

            case ChatItem.LAYOUT_RIGHT_VOICE:
                return new RightVoiceHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)){
            // 左边文本
            case ChatItem.LAYOUT_LEFT_TEXT:
                ((LeftTextHolder)viewHolder).textContent.setText(msgData.get(position).getText());
                ((LeftTextHolder)viewHolder).rootView.setTag(R.id.chat_tag_position, position);
                ((LeftTextHolder)viewHolder).rootView.setTag(R.id.chat_tag_msg_type,TEXT);
                ((LeftTextHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;

            // 右边文本
            case ChatItem.LAYOUT_RIGHT_TEXT:
                ((RightTextHolder)viewHolder).textContent.setText(msgData.get(position).getText());
                ((RightTextHolder)viewHolder).rootView.setTag(R.id.chat_tag_position, position);
                ((RightTextHolder)viewHolder).rootView.setTag(R.id.chat_tag_msg_type, TEXT);
                ((RightTextHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;

            // 左边图片
            case ChatItem.LAYOUT_LEFT_IMAGE:
                ((LeftImageHolder)viewHolder).imageView.setTag(R.id.chat_tag_position,position);
                ((LeftImageHolder)viewHolder).imageView.setTag(R.id.chat_tag_msg_type,IMAGE);
                ((LeftImageHolder)viewHolder).imageView.setOnLongClickListener(this);

                break;

            // 右边图片
            case ChatItem.LAYOUT_RIGHT_IMAGE:
                ((RightImageHolder)viewHolder).imageView.setTag(R.id.chat_tag_position,position);
                ((RightImageHolder)viewHolder).imageView.setTag(R.id.chat_tag_msg_type,IMAGE);
                ((RightImageHolder)viewHolder).imageView.setOnLongClickListener(this);
                ((RightImageHolder)viewHolder).imageView.setImageResource(R.mipmap.test_4);
                final ChatImageView iv = ((RightImageHolder) viewHolder).imageView;
                // 发送图片
                if (msgData.get(position).isSend()){
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 3;
                            message.obj = iv;
                            handler.sendMessage(message);
                        }
                    };
                    if (!((RightImageHolder) viewHolder).isUpLoading){
                        timer.schedule(task, 2000, 2000);
                        ((RightImageHolder) viewHolder).isUpLoading = true;
                    }
                }
                // 接收图片
                else{

                }


                break;

            // 左边语音
            case ChatItem.LAYOUT_LEFT_VOICE:
                ((LeftVoiceHolder)viewHolder).rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                VoiceController.playRecord(msgData.get(position).getVoiceUrl(), new VoiceController.OnPlayListener() {
                                    @Override
                                    public void onStart() {
                                        handler.obtainMessage(VOICE_PLAY_START,1,0,((LeftVoiceHolder) viewHolder)).sendToTarget();
                                    }

                                    @Override
                                    public void onFinished() {
                                        handler.obtainMessage(VOICE_PLAY_STOP,1,0,((LeftVoiceHolder) viewHolder)).sendToTarget();
                                    }
                                });
                            }
                        }).start();
                    }
                });
                ((LeftVoiceHolder)viewHolder).second.setText(Html.fromHtml(msgData.get(position).getVoiceSecond() + "\""));

                ((LeftVoiceHolder)viewHolder).rootView.setTag(R.id.chat_tag_position, position);
                ((LeftVoiceHolder)viewHolder).rootView.setTag(R.id.chat_tag_msg_type, VOICE);
                ((LeftVoiceHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;

            // 右边语音
            case ChatItem.LAYOUT_RIGHT_VOICE:
                ((RightVoiceHolder)viewHolder).rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                VoiceController.playRecord(msgData.get(position).getVoiceUrl(), new VoiceController.OnPlayListener() {
                                    @Override
                                    public void onStart() {
                                        handler.obtainMessage(VOICE_PLAY_START, 2, 0, ((RightVoiceHolder) viewHolder)).sendToTarget();
                                    }

                                    @Override
                                    public void onFinished() {
                                        handler.obtainMessage(VOICE_PLAY_STOP, 2, 0, ((RightVoiceHolder) viewHolder)).sendToTarget();
                                    }
                                });
                            }
                        }).start();
                    }
                });
                ((RightVoiceHolder)viewHolder).second.setText(Html.fromHtml(msgData.get(position).getVoiceSecond() + "\""));

                ((RightVoiceHolder)viewHolder).rootView.setTag(R.id.chat_tag_position, position);
                ((RightVoiceHolder)viewHolder).rootView.setTag(R.id.chat_tag_msg_type, VOICE);
                ((RightVoiceHolder)viewHolder).rootView.setOnLongClickListener(this);
                break;
        }
    }

    private final Timer timer = new Timer();
    private TimerTask task;

    @Override
    public boolean onLongClick(View v) {
        ArrayList<String> popupList = new ArrayList<String>();
        switch ((Integer)v.getTag(R.id.chat_tag_msg_type)){
            case TEXT:
                popupList.add("复制");
                popupList.add("删除");
                break;

            case IMAGE:
                popupList.add("删除");
                break;

            case VOICE:
                popupList.add("删除");
                break;
        }
        PopupList popupListWindow = new PopupList();
        popupListWindow.setOnItemClickListener(new PopupListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View contextView, int contextPosition, View view, int position) {
                Toast.makeText(context, contextPosition + "," + position, Toast.LENGTH_SHORT).show();
            }
        });

        PointF pointF = (PointF) v.getTag();
        popupListWindow.showPopupWindow(context, v, (Integer) v.getTag(), popupList, pointF.x, pointF.y);
        return true;
    }

    public void setMsgData(ArrayList<ChatItem> msgData) {
        this.msgData = msgData;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AnimationDrawable drawable = null;
            switch (msg.what){
                // 播放语音
                case VOICE_PLAY_START:
                    if (msg.arg1 == 1){
                        drawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.chat_voice_anim_left);
                        ((RightVoiceHolder)msg.obj).imageView.setImageDrawable(drawable);
                    }else {
                        drawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.chat_voice_anim_right);
                        ((RightVoiceHolder)msg.obj).imageView.setImageDrawable(drawable);
                    }
                    drawable.start();
                    break;

                // 停止语音
                case VOICE_PLAY_STOP:
                    if (msg.arg1 == 1){
                        ((LeftVoiceHolder)msg.obj).imageView.setImageDrawable(drawable.getFrame(drawable.getNumberOfFrames() - 1));
                    }else {
                        drawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.chat_voice_anim_right);
                        ((RightVoiceHolder)msg.obj).imageView.setImageDrawable(drawable.getFrame(drawable.getNumberOfFrames()-1));
                    }
                    break;

                case 3:
                    ChatImageView iv = (ChatImageView) msg.obj;
                    float newProgress = iv.getProgress();
                    iv.setProgress(newProgress + 0.1f);
                    break;
            }
        }
    };

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
