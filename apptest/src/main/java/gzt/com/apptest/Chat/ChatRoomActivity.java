package gzt.com.apptest.Chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import gzt.com.apptest.Chat.adapter.ChatListAdapter;
import gzt.com.apptest.Chat.bean.ChatItem;
import gzt.com.apptest.Chat.face.FaceConversionUtil;
import gzt.com.apptest.Chat.face.PasteEditText;
import gzt.com.apptest.Chat.uitls.DeviceUtils;
import gzt.com.apptest.R;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    // 键盘高度
    private static int keyBroadHeight;

    private static View faceBroadView;
    private static View imageBroadView;
    private boolean faceBtnChecked;

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
    // 界面是否被压缩
    private boolean isSoftBroadShown;
    // 选择面板是否展开
    private boolean isChooseShown;

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
        send.setOnClickListener(this);
        pressSay.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        msgEditText.setClickable(true);
        faceBtn.setOnCheckedChangeListener(this);
        keyBroadHeight = DeviceUtils.getKeyBroadHeight(this);
        Log.e("qzj", "init keyBroadHeight = " + keyBroadHeight);
        msgEditText.getViewTreeObserver().addOnGlobalLayoutListener(new SoftBroadChangeListener());
        msgEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e("qzj", ">>>>>>>>EditText onTouch>>>>>>>>>>");
                        // 只要触摸了EditText都要设置成可调整
                        faceBtn.setChecked(false);
                        isSoftBroadShown = true;
                        DeviceUtils.isAdjustWindow(ChatRoomActivity.this, true);
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            // 触摸EditText
//            if (DeviceUtils.isTouchThisView(ev,msgEditText)){
//                DeviceUtils.isAdjustWindow(ChatRoomActivity.this, true);
//                DeviceUtils.showSoftKeyBoard(this, msgEditText);
//                setChooseBroadHeight(0);
//                faceBtn.setChecked(false);
//            } // 触摸face button
//            else if(DeviceUtils.isTouchThisView(ev,faceBtn)){
//                Log.e("qzj", ">>>>>>>>>>>>>face btn touch...>>>>>>>>>>>>");
//                DeviceUtils.isAdjustWindow(ChatRoomActivity.this, false);
//                setChooseBroadHeight(keyBroadHeight);
//                chooseBroad.requestLayout();
//            }
//        }
        return super.dispatchTouchEvent(ev);
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

        }
    }


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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("qzj", "isSoftBroadShown:" + isSoftBroadShown+",isChooseShown:" + isChooseShown);
        if (isChecked){
            if (!isChooseShown){
                setChooseBroadHeight(keyBroadHeight);
                DeviceUtils.isAdjustWindow(ChatRoomActivity.this, true);
                if (!isSoftBroadShown){
                    chooseBroad.requestLayout();
                }
            }
            DeviceUtils.hideSoftKeyBoard(this, getCurrentFocus());
            isSoftBroadShown = false;
            isChooseShown = true;
        }else{
            DeviceUtils.isAdjustWindow(ChatRoomActivity.this, false);
            DeviceUtils.showSoftKeyBoard(ChatRoomActivity.this,getCurrentFocus());
            isSoftBroadShown = true;
        }
    }

    /**
     * 监听软键盘高度变化
     */
    class SoftBroadChangeListener implements ViewTreeObserver.OnGlobalLayoutListener{
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            ChatRoomActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int screenHeight = ChatRoomActivity.this.getWindow().getDecorView().getRootView().getHeight();
            int heightDifference = screenHeight - r.bottom;
            Log.e("qzj", "----------获取到键盘高度------------: " + heightDifference);
            if (heightDifference > 0) {
                if (keyBroadHeight != heightDifference) keyBroadHeight = heightDifference;
            } else {

            }
        }
    }
}
