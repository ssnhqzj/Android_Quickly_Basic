package gzt.com.apptest.Chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    // 表情面板空
    private static final int VISIBLE_NONE = 0;
    // 表情面板显示
    private static final int VISIBLE_FACE = 1;
    // 图片面板显示
    private static final int VISIBLE_IMAGE = 2;
    // 声音面板显示
    private static final int VISIBLE_VOICE = 3;

    // 键盘高度
    private static int keyBroadHeight;

    private static View faceBroadView;
    private static View imageBroadView;

    private XRecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private ArrayList<ChatItem> msgData;

    private LinearLayout inputBroad;
    private LinearLayout chooseBroad;
    private TextView send;
    private ImageView imageBtn;
    private PasteEditText msgEditText;
    private CheckBox faceBtn;
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
        mRecyclerView.setPullRefreshEnabled(false);
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
        send = (TextView) findViewById(R.id.btn_send);
        faceBtn = (CheckBox) findViewById(R.id.btn_face);
        voiceBtn = (ImageView) findViewById(R.id.btn_voice);
        pressSay = (TextView) findViewById(R.id.btn_press_say);
        imageBtn = (ImageView) findViewById(R.id.btn_image);
        voiceBtn.setOnClickListener(this);
        faceBtn.setOnCheckedChangeListener(this);
        send.setOnClickListener(this);
        pressSay.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        msgEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.e("qzj", "et_sendmessage: clicked");
                    DeviceUtils.isAdjustWindow(ChatRoomActivity.this, true);
                    setChooseBroadHeight(0);
                }
                return false;
            }
        });
        msgEditText.setClickable(true);
        keyBroadHeight = DeviceUtils.getKeyBroadHeight(this);
        Log.e("qzj", "init keyBroadHeight = " + keyBroadHeight);
        // 监听界面高度的变化
        msgEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ChatRoomActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ChatRoomActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                Log.e("qzj", "Keyboard Size: " + heightDifference);
                // 软键盘显示
                if (heightDifference > 0) {
                    handler.obtainMessage(1).sendToTarget();
                    if (keyBroadHeight != heightDifference) keyBroadHeight = heightDifference;
                } else { // 软键盘隐藏
//                    setChooseBroadHeight(0);
                }
            }
        });
//        msgEditText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                DeviceUtils.showSoftKeyBoard(ChatRoomActivity.this, v);
//                removeAllBroads();
//                return false;
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            Log.e("qzj", isShouldHideInput(v, ev) + "");
//            if (isShouldHideInput(v, ev)) {
//                DeviceUtils.hideSoftKeyBoard(this,v);
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
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

            // 选择语音
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

            // 选择图片
            case R.id.btn_image:
                /*if(chooseBroadState != VISIBLE_IMAGE){
                    if (imageBroadView == null){
                        imageBroadView = inflater.inflate(R.layout.chat_broad_image,null);
                    }
                    DeviceUtils.hideSoftKeyBoard(this,msgEditText);
                    chooseBroad.addView(imageBroadView);
                    chooseBroadState = VISIBLE_IMAGE;
                }*/
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("qzj", "face isChecked: " + isChecked);
        if (isChecked){
            DeviceUtils.isAdjustWindow(this,false);
            removeAllBroads();
            if (faceBroadView == null){
                faceBroadView = inflater.inflate(R.layout.chat_broad_face,null);
                FaceRelativeLayout faceLayout = (FaceRelativeLayout) faceBroadView.findViewById(R.id.FaceRelativeLayout);
                faceLayout.setEditText(msgEditText);
                faceLayout.setBroadHeight(keyBroadHeight);
            }
            if (faceBroadView.getParent()!=null){
                ((ViewGroup)faceBroadView.getParent()).removeView(faceBroadView);
            }
            chooseBroad.addView(faceBroadView);
            DeviceUtils.hideSoftKeyBoard(this, msgEditText);
            setChooseBroadHeight(keyBroadHeight);
        }else{
            DeviceUtils.showSoftKeyBoard(this, msgEditText);
            setChooseBroadHeight(0);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setChooseBroadHeight(keyBroadHeight);
                    break;
                case 2:
                    setChooseBroadHeight(0);
                    break;
            }
        }
    };

    //隐藏所有面板
    private void removeAllBroads(){
        if (chooseBroad.getChildCount() > 0){
            chooseBroad.removeAllViews();
        }
    }

    /**
     * 设置chooseBroad高度
     * @param height
     */
    private void setChooseBroadHeight(int height){
        Log.e("qzj","setChooseBroadHeight:"+height);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chooseBroad.getLayoutParams();
        params.height = height;
    }

}
