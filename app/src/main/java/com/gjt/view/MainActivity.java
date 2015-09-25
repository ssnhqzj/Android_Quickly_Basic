package com.gjt.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjt.R;
import com.gjt.common.utils.DisplayUtils;
import com.gjt.common.utils.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import comm.lib.downloader.DownloadListener;
import comm.lib.uiutils.AdvViewPager;


public class MainActivity extends ActionBarActivity {
    @Bind(R.id.goodman)
    TextView goodman;
    private ImageView test;
    private String testUrl = "http://c.hiphotos.baidu.com/image/pic/item/b812c8fcc3cec3fd56df8f40d288d43f87942725.jpg";
    private String[] urls = new String[]{"http://img2.3lian.com/2014/f7/5/d/22.jpg",
            "http://61.144.56.195/forum/201302/19/144727b4b4zbfbyhbmfv4a.jpg",
            "http://img6.faloo.com/Picture/0x0/0/747/747488.jpg"};
    TextView title;

    //注解框架中API==http://jakewharton.github.io/butterknife/         Studio插件：Zelezny
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        goodman.setText("我是个好人哟");
//        test = (ImageView) findViewById(R.id.main_test);
//        VolleyUtil.getInstance(this).initImageLoader().loadImage(test,testUr,400,400,true);
//        FileLoadUtils.getInstance(this).initDownLoader().downLoad(testUrl,listener,null);

       /* SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.refresh_root);
        layout.setSize(SwipeRefreshLayout.LARGE);
        layout.setColorSchemeColors(0xFFFF0000, 0xFF00ff00, 0xFF0000ff);
        layout.setProgressBackgroundColorSchemeColor(0xFFFFFF00);*/

        /*View targetView = findViewById(R.id.text1);
        BadgeView badgeView = new BadgeView(this,targetView);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        badgeView.setBadgeBackgroundColor(0xFFFF0000);
        badgeView.setText("1");
        badgeView.setGravity(Gravity.CENTER);
        badgeView.show();*/

        /*ImageView[] views = new ImageView[urls.length];
        for(int i=0; i<views.length; i++){
            ImageView imageView = new ImageView(this);
            VolleyUtil.getInstance(this).initImageLoader().loadImage(imageView,urls[i],false);
            views[i] = imageView;
        }*/

        AdvViewPager adv = (AdvViewPager) findViewById(R.id.adv);
        adv.setAdvWidthAndHeight(DisplayUtils.getDisplayWidth(this), DisplayUtils.getDisplayWidth(this) * 9 / 16);
        adv.setTipGravity(RelativeLayout.ALIGN_PARENT_RIGHT);
        adv.setTipsMargin(0, 0, 0, 5);
        adv.setUrls(urls).setIsAutoScroll(true).build();
        adv.setAdvClickListener(new AdvViewPager.OnAdvClickListener() {
            @Override
            public void onAdvClick(int position) {
                Logger.e("qzj", ">>>>>>>>>>>>>>position>>>>>>>>>>>" + position);
            }
        });
    }

    DownloadListener listener = new DownloadListener() {
        @Override
        public void onSuccess(Object o) {
            Logger.e("qzj", ">>>>>>>>>>>>>>success>>>>>>>>>>>");
        }

        @Override
        public void onError(Throwable thr) {
            Logger.e("qzj", ">>>>>>>>>>>>>>failed>>>>>>>>>>>");
        }

        @Override
        public void onProgressUpdate(Object[] values) {
            Logger.e("qzj", ">>>>>>>>>>>>>>>>>" + values.toString());
        }
    };
}
