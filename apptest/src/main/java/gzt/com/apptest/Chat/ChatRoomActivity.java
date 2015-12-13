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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
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

    // 键盘高度
    private static int keyBroadHeight;

    private static View faceBroadView;
    private static View imageBroadView;
    private FaceRelativeLayout faceLayout;
    private ArrayList<View> eventViews;

    private XRecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;

    private ArrayList<ChatItem> msgData;
    private LinearLayout chooseBroad;
    private TextView send;
    private CheckBox imageBtn;
    private PasteEditText msgEditText;
    private CheckBox faceBtn;
    private CheckBox voiceBtn;
    private TextView pressSay;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        inflater = LayoutInflater.from(this);
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
        msgEditText.requestFocus();
        send = (TextView) findViewById(R.id.btn_send);
        faceBtn = (CheckBox) findViewById(R.id.btn_face);
        voiceBtn = (CheckBox) findViewById(R.id.btn_voice);
        pressSay = (TextView) findViewById(R.id.btn_press_say);
        imageBtn = (CheckBox) findViewById(R.id.btn_image);
        voiceBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        pressSay.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        msgEditText.setClickable(true);
        faceBtn.setOnClickListener(this);
        keyBroadHeight = DeviceUtils.getKeyBroadHeight(this);
        Log.e("qzj", "init keyBroadHeight = " + keyBroadHeight);
        msgEditText.getViewTreeObserver().addOnGlobalLayoutListener(new SoftBroadChangeListener());
        msgEditText.setOnTouchListener(new MsgEditTouchListener());

        eventViews = new ArrayList<View>();
        eventViews.add(msgEditText);
        eventViews.add(faceBtn);
        eventViews.add(imageBtn);
        eventViews.add(voiceBtn);
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

            // 点击切换表情按钮
            case R.id.btn_face:
                // 选中表情按钮
                if (faceBtn.isChecked()){
                    checkedChooseBroad(true);
                    fillFaceView();
                    cancelOtherState(faceBtn);
                }else{
                    checkedChooseBroad(false);
                }
                break;

            // 点击切换图片按钮
            case R.id.btn_image:
                // 选中图片按钮
                if (imageBtn.isChecked()){
                    checkedChooseBroad(true);
                    fillImageView();
                    cancelOtherState(imageBtn);
                }else{
                    checkedChooseBroad(false);
                }
                break;

            // 点击切换语音按钮
            case R.id.btn_voice:
                // 选中语音按钮
                if (voiceBtn.isChecked()){
                    fillVoiceView(true);
                    cancelOtherState(voiceBtn);
                }else{
                    fillVoiceView(false);
                }
                break;

        }
    }

    /**
     * 移除选择面板中的内容
     */
    private void removeAllBroads(){
        if (chooseBroad.getChildCount() > 0){
            chooseBroad.removeAllViews();
        }
    }

    /**
     * 填充表情布局
     */
    private void fillFaceView(){
        removeAllBroads();
        if (faceBroadView == null || (faceLayout != null && keyBroadHeight != faceLayout.getBroadHeight())){
            faceBroadView = inflater.inflate(R.layout.chat_broad_face,null);
            faceLayout = (FaceRelativeLayout) faceBroadView.findViewById(R.id.FaceRelativeLayout);
            faceLayout.setEditText(msgEditText);
            faceLayout.setBroadHeight(keyBroadHeight);
        }
        if (faceBroadView.getParent()!=null){
            ((ViewGroup)faceBroadView.getParent()).removeView(faceBroadView);
        }
        chooseBroad.addView(faceBroadView);
    }

    /**
     * 填充语音布局
     * isFill true:填充,false:取消
     */
    private void fillVoiceView(boolean isFill){
        if (isFill){
            DeviceUtils.hideSoftKeyBoard(ChatRoomActivity.this);
            setChooseBroadHeight(0);
            msgEditText.setVisibility(View.GONE);
            pressSay.setVisibility(View.VISIBLE);
        }else{
            checkedChooseBroad(false);
            msgEditText.setVisibility(View.VISIBLE);
            pressSay.setVisibility(View.GONE);
            msgEditText.requestFocus();
        }
    }

    /**
     * 填充图片布局
     */
    private void fillImageView(){
        removeAllBroads();
        if (imageBroadView == null){
            imageBroadView = inflater.inflate(R.layout.chat_broad_image,null);
        }
        if (imageBroadView.getParent()!=null){
            ((ViewGroup)imageBroadView.getParent()).removeView(imageBroadView);
        }
        chooseBroad.addView(imageBroadView);
    }

    /**
     * 设置chooseBroad高度
     * @param height
     */
    private void setChooseBroadHeight(int height){
        Log.e("qzj", "setChooseBroadHeight:" + height);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chooseBroad.getLayoutParams();
        params.height = height;
    }

    /**
     * 是否选中表情或者图片选中面板
     * @param isChecked
     */
    private void checkedChooseBroad(boolean isChecked){
        if (isChecked){
            setChooseBroadHeight(keyBroadHeight);
            DeviceUtils.hideSoftKeyBoard(ChatRoomActivity.this);
            DeviceUtils.isAdjustWindow(ChatRoomActivity.this, false);
            chooseBroad.requestLayout();
        }else{
            setChooseBroadHeight(0);
            DeviceUtils.isAdjustWindow(ChatRoomActivity.this, true);
            DeviceUtils.showSoftKeyBoard(ChatRoomActivity.this);
        }
    }

    /**
     * 取消除EditText和当前CheckBox以外的其他CheckBo选中状态
     * @param cView
     */
    private void cancelOtherState(View cView){
        for (View v : eventViews){
            if (v == cView) continue;
            if (v instanceof EditText) continue;
            if (v instanceof  CheckBox) ((CheckBox)v).setChecked(false);
            if (v == voiceBtn) {
                msgEditText.setVisibility(View.VISIBLE);
                msgEditText.requestFocus();
                pressSay.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 消息EditText触摸监听
     */
    class MsgEditTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 只要触摸了EditText都要设置成可调整
                    checkedChooseBroad(false);
                    cancelOtherState(msgEditText);
                    break;
            }
            return false;
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
                if (keyBroadHeight != heightDifference) {
                    keyBroadHeight = heightDifference;
                    DeviceUtils.saveKeyBroadHeight(ChatRoomActivity.this,keyBroadHeight);
                }
            }
        }
    }
}
