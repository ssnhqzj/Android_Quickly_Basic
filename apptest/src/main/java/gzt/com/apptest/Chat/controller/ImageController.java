package gzt.com.apptest.Chat.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import gzt.com.apptest.Chat.uitls.DeviceUtils;
import gzt.com.apptest.R;

/**
 * 图片面板控制器
 * Created by qzj on 2015/12/14.
 */
public class ImageController implements View.OnClickListener{

    private Context context;
    private View view;
    private ImageView camera;
    private ImageView album;

    public ImageController(Context context,View view){
        this.context = context;
        this.view = view;
    }

    public ImageController init(){
        camera = (ImageView) view.findViewById(R.id.chat_broad_image_camera);
        album = (ImageView) view.findViewById(R.id.chat_broad_image_photo);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_broad_image_camera:
                DeviceUtils.openCamera(context);
                break;

            case R.id.chat_broad_image_photo:
                DeviceUtils.openAlbum(context);
                break;
        }
    }
}
