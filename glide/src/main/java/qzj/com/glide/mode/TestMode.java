package qzj.com.glide.mode;

import android.util.Log;

/**
 * 测试
 * Created by Administrator on 2016/1/25.
 */
public class TestMode implements UrlMode {

    String url = "http://img2.imgtn.bdimg.com/it/u=194240101,2532182839&fm=206&gp=0.jpg";
    String url1 = "http://pica.nipic.com/2008-03-19/2008319183523380_2.jpg";

    private int size;

    public TestMode(int size) {
        this.size = size;
    }

    @Override
    public String buildUrl(int width, int height) {
        Log.e("qzj","-------->"+width+",height:"+height);
        if (size == 1){
            return url1;
        }else{
            return url;
        }
    }

}
