package com.qzj.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import gzt.com.apptest.R;
import com.qzj.chat.view.ChatImageView;
import com.qzj.chat.view.ChatItemLayout;

public class ChatActivity extends AppCompatActivity {

    private ChatItemLayout chatItemLayout;
    private ChatImageView chatImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatItemLayout = (ChatItemLayout) findViewById(R.id.chat_item_layout);
        chatItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("qzj", "onLongClick: ");
                return true;
            }
        });
        chatItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qzj", "onClick: ");
            }
        });

        chatImageView = (ChatImageView) findViewById(R.id.chat_item_image_view);
        chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qzj", "imageview onClick: ");
            }
        });

        chatImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("qzj", "imageview onLongClick: ");
                return true;
            }
        });
    }

}
