/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package comm.lib.photoview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import comm.lib.downloader.DownloadListener;
import comm.lib.downloader.DownloadManager;
import comm.lib.downloader.DownloadTask;
import comm.lib.downloader.dao.ISql;
import comm.lib.downloader.dao.ISqlImpl;
import gzt.com.commlib.R;

public class PhotoViewActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String CURR_INDEX = "curr_index";
    public static final String URLS = "urls";

    private TextView indexTextView;
    private ImageView back;
    private ViewPager mViewPager;
    private ArrayList<String> sDrawables = null;
    private  int currIndex;

    private RelativeLayout downloadToolBar;
    private ProgressBar downLoadProgress;
    private TextView original;
    private TextView note;

    private DownloadManager downloadManager;
    private ISql iSql;
    private DownloadTask temptask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new HackyViewPager(this);
        setContentView(R.layout.show_image_vp);
        FrameLayout vgViewPager = (FrameLayout) findViewById(R.id.show_image_vg_viewpager);
        indexTextView = (TextView) findViewById(R.id.show_image_index);
        back = (ImageView) findViewById(R.id.show_image_back);
        downloadToolBar = (RelativeLayout) findViewById(R.id.show_image_download);
        downLoadProgress = (ProgressBar) findViewById(R.id.show_image_progressbar);
        original = (TextView) findViewById(R.id.show_image_original);
        original.setOnClickListener(this);
        note = (TextView) findViewById(R.id.show_image_note);
        vgViewPager.addView(mViewPager);
        Intent intent = getIntent();
        if(intent != null){
            currIndex = intent.getIntExtra(CURR_INDEX, 0);
            sDrawables = intent.getStringArrayListExtra(URLS);
        }else{
            this.finish();
        }

        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(currIndex);
        indexTextView.setText(String.valueOf(currIndex + 1));
        mViewPager.setOnPageChangeListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewActivity.this.finish();
            }
        });

        downloadManager = new DownloadManager(this);
        iSql = new ISqlImpl(this);
        initDownLoadTask(currIndex);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currIndex = position;
        indexTextView.setText(String.valueOf(currIndex+1));
        initDownLoadTask(currIndex);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_image_original){
            downLoadProgress.setVisibility(View.VISIBLE);
            DownloadTask task = new DownloadTask(this);
            task.setUrl(sDrawables.get(currIndex));
            task.start(this,listener,false);
        }
    }

    /**
     * 初始化下载任务栏
     * @param position
     */
    private void initDownLoadTask(int position){
        DownloadTask initTask = new DownloadTask(this);
        initTask.setUrl(sDrawables.get(position));
        try {
            temptask = iSql.queryDownloadTask(initTask);
            if(temptask != null && temptask.isComplete()){
                original.setText("重新下载");
                note.setText("查看");
                note.setVisibility(View.VISIBLE);
                note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openImageBySystem(temptask.getPath());
                    }
                });
            }else{
                note.setVisibility(View.GONE);
                downLoadProgress.setVisibility(View.GONE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统相册打开图片
     * @param path
     */
    private void openImageBySystem(String path){
        File file = new File(path);
        if( file != null && file.isFile() == true){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(intent);
        }
    }

    private void toggleDownLoadToolbar(final View view){
        // API 11以上执行动画
        if(Build.VERSION.SDK_INT >= 11){
            if(view.getVisibility() == View.VISIBLE){
                ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(view,"translationY",view.getTranslationY(),view.getHeight());
                hideAnimator.setDuration(400);
                hideAnimator.start();
                hideAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
            }else{
                view.setVisibility(View.VISIBLE);
                ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view,"translationY",view.getTranslationY(),0);
                showAnimator.setDuration(400);
                showAnimator.start();
            }
        }else{
            if(view.getVisibility() == View.VISIBLE){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        }

    }

    class SamplePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return sDrawables.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setBackgroundColor(0xFF000000);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View paramView, float paramFloat1, float paramFloat2) {
                    toggleDownLoadToolbar(downloadToolBar);
                }
            });
            //ImageLoaderUtil.getOtherConfig().displayImage(sDrawables[position], photoView, ImageLoaderUtil.getOtherOptions());
//			ImageLoader.getInstance().displayImage(sDrawables.get(position), photoView, ImageLoaderUtil.getOtherOptions());
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private DownloadListener listener = new DownloadListener<Integer, DownloadTask>() {
        /**
         * The download task has been added to the sqlite.
         * <p/>
         * operation of UI allowed.
         *
         * @param downloadTask the download task which has been added to the sqlite.
         */
        @Override
        public void onAdd(DownloadTask downloadTask) {
            super.onAdd(downloadTask);
        }

        /**
         * The download task has been delete from the sqlite
         * <p/>
         * operation of UI allowed.
         *
         * @param downloadTask the download task which has been deleted to the sqlite.
         */
        @Override
        public void onDelete(DownloadTask downloadTask) {
            super.onDelete(downloadTask);
        }

        /**
         * The download task is stop
         * <p/>
         * operation of UI allowed.
         *
         * @param downloadTask the download task which has been stopped.
         */
        @Override
        public void onStop(DownloadTask downloadTask) {
            super.onStop(downloadTask);
        }

        /**
         * Runs on the UI thread before doInBackground(Params...).
         */
        @Override
        public void onStart() {
            super.onStart();
        }

        /**
         * Runs on the UI thread after publishProgress(Progress...) is invoked. The
         * specified values are the values passed to publishProgress(Progress...).
         *
         * @param values The values indicating progress.
         */
        @Override
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            downLoadProgress.setProgress(values[0]);
        }

        /**
         * Runs on the UI thread after doInBackground(Params...). The specified
         * result is the value returned by doInBackground(Params...). This method
         * won't be invoked if the task was cancelled.
         *
         * @param downloadTask The result of the operation computed by
         *                     doInBackground(Params...).
         */
        @Override
        public void onSuccess(final DownloadTask downloadTask) {
            super.onSuccess(downloadTask);
            original.setText("重新下载");
            note.setText("查看");
            note.setVisibility(View.VISIBLE);
            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageBySystem(downloadTask.getPath());
                }
            });
            downLoadProgress.setVisibility(View.INVISIBLE);
            Toast.makeText(PhotoViewActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
        }

        /**
         * Applications should preferably override onCancelled(Object). This method
         * is invoked by the default implementation of onCancelled(Object). Runs on
         * the UI thread after cancel(boolean) is invoked and
         * doInBackground(Object[]) has finished.
         */
        @Override
        public void onCancelled() {
            super.onCancelled();
        }

        @Override
        public void onError(Throwable thr) {
            super.onError(thr);
        }

        /**
         * Runs on the UI thread after doInBackground(Params...) when the task is
         * finished or cancelled.
         */
        @Override
        public void onFinish() {
            super.onFinish();
        }
    };

}
