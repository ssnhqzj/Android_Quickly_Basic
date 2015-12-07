package gzt.com.apptest.Chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import gzt.com.apptest.Chat.adapter.ChatListAdapter;
import gzt.com.apptest.Chat.bean.ChatItem;
import gzt.com.apptest.Chat.face.FaceConversionUtil;
import gzt.com.apptest.Chat.face.FaceRelativeLayout;
import gzt.com.apptest.Chat.face.PasteEditText;
import gzt.com.apptest.R;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private XRecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private ArrayList<ChatItem> msgData;

    private FaceRelativeLayout faceRelativeLayout;
    private ImageView send;
    private PasteEditText msgEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
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
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mRecyclerView.loadMoreComplete();
                            for(int i = 0; i < 10 ;i++){
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
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.refreshComplete();
                        }
                    }, 1000);

            }
        });

        msgData = new  ArrayList<ChatItem>();
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

        initFaceView();
    }

    /**
     * 初始化表情控件
     */
    private void initFaceView(){
        faceRelativeLayout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
        msgEditText = (PasteEditText) findViewById(R.id.et_sendmessage);
        send = (ImageView) findViewById(R.id.btn_send);
        send.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(ChatRoomActivity.this,faceRelativeLayout);
            }
        }).start();
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
}
