package comm.lib.byaku;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

import comm.lib.utils.FileUtil;
import gzt.com.commlib.R;

public class TouchImageActivity extends Activity {

    public static final String TYPE = "res_type";
    public static final String TYPE_VALUE_RAW = "type_value_raw";
    public static final String TYPE_VALUE_URL = "type_value_url";

    public static final int TYPE_RAW = 1;
    public static final int TYPE_URL = 2;

    private TouchImageView imageView;
    private Intent comeIntent;
    private Drawable placeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_image);

        placeHolder = getResources().getDrawable(R.mipmap.android_placeholder);
        imageView = (TouchImageView) findViewById(R.id.touch_image_view);
        comeIntent = getIntent();
        if(comeIntent != null){
            int type = comeIntent.getIntExtra(TYPE, 0);
            switch (type){
                case TYPE_RAW:
                    int valueRaw = comeIntent.getIntExtra(TYPE_VALUE_RAW,0);
                    openImageByRawId(valueRaw);

                    break;

                case TYPE_URL:
                    String url = comeIntent.getStringExtra(TYPE_VALUE_URL);
                    if(url != null && !url.equals(""))
                        openImageByUrl(url);

                    break;
            }
        }
    }

    /**
     * 打开raw文件
     * @param rawId
     */
    private void openImageByRawId(int rawId){
        InputStream is = getResources().openRawResource(rawId);
        TileBitmapDrawable.attachTileBitmapDrawable(imageView, is, placeHolder, null);
    }

    /**
     * 打开url文件
     * @param url
     */
    private void openImageByUrl(String url){
        InputStream is = FileUtil.getFileStream(url);
        if(is != null)
        TileBitmapDrawable.attachTileBitmapDrawable(imageView, is, placeHolder, null);
    }
}
