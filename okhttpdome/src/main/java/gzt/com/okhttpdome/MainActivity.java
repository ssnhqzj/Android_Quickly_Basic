package gzt.com.okhttpdome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://img2.imgtn.bdimg.com/it/u=194240101,2532182839&fm=21&gp=0.jpg";
    private static final String gifUrl = "http://img1.imgtn.bdimg.com/it/u=2595464369,4006409358&fm=21&gp=0.jpg";

    private static final String jsonUrl = "http://www.weather.com.cn/data/sk/101010100.html";

    private ImageView glideView;
    private ImageView glideGifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glideView = (ImageView) findViewById(R.id.glide_image);
        Glide.with(this).load(url).into(glideView);

        glideGifView = (ImageView) findViewById(R.id.glide_gif_image);
        Glide.with(this).load(gifUrl).asGif().into(glideGifView);
        try {
            loadJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJson() throws IOException {
        final OkHttpClient client = new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormEncodingBuilder()
//                        .add("search", "Jurassic Park")
                        .build();
                Request request = new Request.Builder()
                        .url(jsonUrl)
                        .post(formBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        OkHttpUtils
                .post()
                .url(jsonUrl)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
//                .addHeader("Content-type","text/html")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("qzj", response);
                    }
                });
    }

}
