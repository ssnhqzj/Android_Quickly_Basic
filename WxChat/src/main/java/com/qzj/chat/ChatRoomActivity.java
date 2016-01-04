package com.qzj.chat;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qzj.chat.adapter.ChatListAdapter;
import com.qzj.chat.bean.ChatItem;
import com.qzj.chat.controller.ImageController;
import com.qzj.chat.controller.VoiceController;
import com.qzj.chat.face.FaceConversionUtil;
import com.qzj.chat.face.FaceRelativeLayout;
import com.qzj.chat.face.PasteEditText;
import com.qzj.chat.uitls.DeviceUtils;
import com.qzj.chat.uitls.popup.PopupWindowUtil;
import com.qzj.chat.uitls.watcher.Observer;

import java.util.ArrayList;

import gzt.com.apptest.R;


public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener,Observer {

    // toolbar
    private TextView toolbarMore;

    // 键盘高度
    private static int keyBroadHeight;

    private static View faceBroadView;
    private static View imageBroadView;
    private FaceRelativeLayout faceLayout;
    private ArrayList<View> eventViews;

    private XRecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private ChatListAdapter mAdapter;

    private ArrayList<ChatItem> msgData;
    private LinearLayout chooseBroad;
    private LinearLayout inputBroad;
    private TextView send;
    private CheckBox imageBtn;
    private PasteEditText msgEditText;
    private CheckBox faceBtn;
    private CheckBox voiceBtn;
    private Button record;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        inflater = LayoutInflater.from(this);
        chooseBroad = (LinearLayout) findViewById(R.id.choose_board);
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);

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
//        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        initToolbar();
        initBroadView();
    }

    private void initToolbar(){
        toolbarMore = (TextView) findViewById(R.id.chat_room_toolbar_more);
        toolbarMore.setOnClickListener(this);
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
        record = (Button) findViewById(R.id.btn_press_say);
        imageBtn = (CheckBox) findViewById(R.id.btn_image);
        inputBroad = (LinearLayout) findViewById(R.id.input_board);
        voiceBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        msgEditText.setClickable(true);
        faceBtn.setOnClickListener(this);
        keyBroadHeight = DeviceUtils.getKeyBroadHeight(this);
        Log.e("qzj", "init keyBroadHeight = " + keyBroadHeight);
        msgEditText.getViewTreeObserver().addOnGlobalLayoutListener(new SoftBroadChangeListener());
        msgEditText.setOnTouchListener(new MsgEditTouchListener());
        msgEditText.addTextChangedListener(new MsgEditWatcher());

        eventViews = new ArrayList<View>();
        eventViews.add(msgEditText);
        eventViews.add(faceBtn);
        eventViews.add(imageBtn);
        eventViews.add(voiceBtn);

        new VoiceController(this,record).attach(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if(DeviceUtils.isTouchSpaceArea(ev,inputBroad)){
                DeviceUtils.hideSoftKeyBoard(ChatRoomActivity.this);
                setChooseBroadHeight(0);
                cancelOtherState(msgEditText);
                chooseBroad.requestLayout();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 发送消息
            case R.id.btn_send:
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(this, msgEditText.getText().toString());
                ChatItem item = new ChatItem();
                item.setLayoutType(ChatItem.LAYOUT_RIGHT_TEXT);
                item.setText(spannableString);
                msgData.add(0,item);
                mAdapter.notifyDataSetChanged();
                layoutManager.scrollToPosition(0);
                msgEditText.setText("");
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

            // 点击toolbar上的更多
            case R.id.chat_room_toolbar_more:
                PopupWindowUtil.showPwMore(this, toolbarMore);
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
            record.setVisibility(View.VISIBLE);
        }else{
            checkedChooseBroad(false);
            msgEditText.setVisibility(View.VISIBLE);
            record.setVisibility(View.GONE);
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
            new ImageController(this,imageBroadView).init();
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
                record.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == DeviceUtils.REQUEST_CODE_ALBUM
                || requestCode == DeviceUtils.REQUEST_CODE_CAMERA)) {
            String path = DeviceUtils.getReturnImagePath(requestCode, resultCode, data, this);
            Log.e("qzj", "--------------------image path------------------ " + path);
            ChatItem item = new ChatItem();
            item.setLayoutType(ChatItem.LAYOUT_RIGHT_IMAGE);
            item.setImageUrl("file://" + path);
            msgData.add(0, item);
            mAdapter.setMsgData(msgData);
            mAdapter.notifyItemInserted(0);
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void update(int what,Object obj) {
        if (what == VoiceController.WATCHER_WHAT_VOICE){
            if (obj != null) {
                Bundle bundle = (Bundle) obj;
                ChatItem item = new ChatItem();
                item.setLayoutType(ChatItem.LAYOUT_RIGHT_VOICE);
                item.setVoiceUrl("file://" + bundle.get("voicePath"));
                item.setVoiceSecond(bundle.getInt("recodeTime"));
                msgData.add(0, item);
                mAdapter.setMsgData(msgData);
                mAdapter.notifyDataSetChanged();
                layoutManager.scrollToPosition(0);
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
     * 消息EditText文本改变监听
     */
    class MsgEditWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().equals("")){
                send.setVisibility(View.VISIBLE);
                imageBtn.setVisibility(View.GONE);
            }else{
                send.setVisibility(View.GONE);
                imageBtn.setVisibility(View.VISIBLE);
            }
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
