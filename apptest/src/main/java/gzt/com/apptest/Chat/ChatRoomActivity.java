package gzt.com.apptest.Chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import gzt.com.apptest.Chat.adapter.ChatListAdapter;
import gzt.com.apptest.Chat.bean.ChatItem;
import gzt.com.apptest.Chat.face.FaceConversionUtil;
import gzt.com.apptest.Chat.face.FaceRelativeLayout;
import gzt.com.apptest.Chat.face.PasteEditText;
import gzt.com.apptest.Chat.uitls.DeviceUtils;
import gzt.com.apptest.R;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    // 表情面板空
    private static final int VISIBLE_NONE = 0;
    // 表情面板显示
    private static final int VISIBLE_FACE = 1;
    // 图片面板显示
    private static final int VISIBLE_IMAGE = 2;
    // 声音面板显示
    private static final int VISIBLE_VOICE = 3;

    private XRecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private ArrayList<ChatItem> msgData;

    private LinearLayout inputBroad;
    private LinearLayout chooseBroad;
    private static View faceBroadView;
    private ImageView send;
    private PasteEditText msgEditText;
    private ImageView faceBtn;
    private ImageView voiceBtn;
    private TextView pressSay;
    private LayoutInflater inflater;

    // 选择面板状态
    private int chooseBroadState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        inflater = LayoutInflater.from(this);
        inputBroad = (LinearLayout) findViewById(R.id.input_board);
        chooseBroad = (LinearLayout) findViewById(R.id.choose_board);
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new ChatListAdapter.SpaceItemDecoration(50));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mRecyclerView.loadMoreComplete();
                        for (int i = 0; i < 10; i++) {
                            ChatItem item = new ChatItem();
                            item.setItemId(i);
                            if (i % 2 == 0) {
                                item.setLayoutType(ChatItem.LAYOUT_LEFT_TEXT);
                            } else {
                                item.setLayoutType(ChatItem.LAYOUT_RIGHT_TEXT);
                            }
                            item.setText(new SpannableString("item" + (i + msgData.size())));
                            msgData.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);

            }
        });

        msgData = new ArrayList<ChatItem>();
        for(int i = 0; i < 15 ;i++){
            ChatItem item = new ChatItem();
            item.setItemId(i);
            if (i%2 == 0){
                item.setLayoutType(ChatItem.LAYOUT_LEFT_TEXT);
            }else{
                item.setLayoutType(ChatItem.LAYOUT_RIGHT_TEXT);
            }
            item.setText(new SpannableString("item" + (i + msgData.size())));
            msgData.add(item);
        }
        mAdapter = new ChatListAdapter(this,msgData);
        mRecyclerView.setAdapter(mAdapter);

        initBroadView();
    }

    /**
     * 初始化表情控件
     */
    private void initBroadView(){
        msgEditText = (PasteEditText) findViewById(R.id.et_sendmessage);
        send = (ImageView) findViewById(R.id.btn_send);
        faceBtn = (ImageView) findViewById(R.id.btn_face);
        voiceBtn = (ImageView) findViewById(R.id.btn_voice);
        pressSay = (TextView) findViewById(R.id.btn_press_say);
        voiceBtn.setOnClickListener(this);
        faceBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        pressSay.setOnClickListener(this);
        msgEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DeviceUtils.showSoftKeyBoard(ChatRoomActivity.this,v);
                hideAllBroads();
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            Log.e("qzj", isShouldHideInput(v, ev) + "");
            if (isShouldHideInput(v, ev)) {
                DeviceUtils.hideSoftKeyBoard(this,v);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if ( event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 发送消息
            case R.id.btn_send:
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(this, msgEditText.getText().toString());
                ChatItem item = new ChatItem();
                item.setLayoutType(ChatItem.LAYOUT_LEFT_TEXT);
                item.setText(spannableString);
                msgData.add(0,item);
                mAdapter.notifyDataSetChanged();
                break;

            // 点击表情
            case R.id.btn_face:
                if (chooseBroadState == VISIBLE_FACE){
                    chooseBroad.removeView(faceBroadView);
                    chooseBroadState = VISIBLE_NONE;
                }else{
                    if (faceBroadView == null){
                        faceBroadView = inflater.inflate(R.layout.custom_facerelativelayout,null);
                        FaceRelativeLayout faceLayout = (FaceRelativeLayout) faceBroadView.findViewById(R.id.FaceRelativeLayout);
                        faceLayout.setEditText(msgEditText);
                    }
                    DeviceUtils.hideSoftKeyBoard(this,msgEditText);
                    chooseBroad.addView(faceBroadView);
                    chooseBroadState = VISIBLE_FACE;
                }
                break;

            // 点击语音
            case R.id.btn_voice:
                if(chooseBroadState == VISIBLE_VOICE){
                    chooseBroadState = VISIBLE_NONE;
                    msgEditText.setVisibility(View.VISIBLE);
                    pressSay.setVisibility(View.GONE);
                }else{
                    DeviceUtils.hideSoftKeyBoard(this,msgEditText);
                    msgEditText.setVisibility(View.GONE);
                    pressSay.setVisibility(View.VISIBLE);
                    chooseBroadState = VISIBLE_VOICE;
                }
                break;
        }
    }

    //隐藏所有面板
    private void hideAllBroads(){
        if (chooseBroad.getChildCount() > 0){
            chooseBroad.removeAllViews();
            chooseBroadState = VISIBLE_NONE;
        }
    }
}
