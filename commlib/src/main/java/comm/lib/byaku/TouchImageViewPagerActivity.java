package comm.lib.byaku;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

import comm.lib.utils.FileUtil;
import gzt.com.commlib.R;

public class TouchImageViewPagerActivity extends FragmentActivity {
    public static final String TYPE = "type";
    public static final String TYPE_VALUE_RAW = "type_value_raw";
    public static final String TYPE_VALUE_URL = "type_value_url";

    public static final int TYPE_RAW = 1;
    public static final int TYPE_URL = 2;

    private GalleryViewPager gallery;
    private int[] imagesRaw;
    private String[] imagesUrl;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_image_view_pager);

        gallery = (GalleryViewPager) findViewById(R.id.gallery_view_pager);
        gallery.setAdapter(new GalleryAdapter());
        gallery.setOffscreenPageLimit(1);

        if(getIntent() != null){
            type = getIntent().getIntExtra(TYPE,0);
            switch (type){
                case TYPE_RAW:
                    imagesRaw = getIntent().getIntArrayExtra(TYPE_VALUE_RAW);
                    break;
                case TYPE_URL:
                    imagesUrl = getIntent().getStringArrayExtra(TYPE_VALUE_URL);
                    break;
            }
        }
    }

    private final class GalleryAdapter extends FragmentStatePagerAdapter {

        GalleryAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            switch (type){
                case TYPE_RAW:
                    return imagesRaw.length;
                case TYPE_URL:
                    return imagesUrl.length;
            }
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            switch (type){
                case TYPE_RAW:
                    return GalleryFragment.getInstance(imagesRaw[position], type);
                case TYPE_URL:
                    return GalleryFragment.getUrlInstance(imagesUrl[position], type);
            }
            return null;
        }
    }

    public static final class GalleryFragment extends Fragment {

        public static GalleryFragment getInstance(int imageId,int type) {
            GalleryFragment instance = new GalleryFragment();
            Bundle params = new Bundle();
            params.putInt(TYPE, type);
            params.putInt("imageId", imageId);
            instance.setArguments(params);

            return instance;
        }

        public static GalleryFragment getUrlInstance(String imageUrl,int type) {
            final GalleryFragment instance = new GalleryFragment();
            final Bundle params = new Bundle();
            params.putInt(TYPE, type);
            params.putString("imageUrl", imageUrl);
            instance.setArguments(params);

            return instance;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View v = inflater.inflate(R.layout.gallery_view_pager_item, null);
            final TouchImageView image = (TouchImageView) v.findViewById(R.id.gallery_view_pager_item_image);
            final ProgressBar progress = (ProgressBar) v.findViewById(R.id.gallery_view_pager_item_progress);

            InputStream is = null;
            final int type = getArguments().getInt(TYPE);
            switch (type){
                case TYPE_RAW:
                    int imageRaw = getArguments().getInt("imageId");
                    is = getResources().openRawResource(imageRaw);
                    break;
                case TYPE_URL:
                    String imageUrl = getArguments().getString("imageUrl");
                    try {
                        is = FileUtil.getFileStream(imageUrl);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                // do nothing here
                            }
                        }
                    }
                    break;
            }

            TileBitmapDrawable.attachTileBitmapDrawable(image, is, null, new TileBitmapDrawable.OnInitializeListener() {

                @Override
                public void onStartInitialization() {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEndInitialization() {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception ex) {

                }
            });

            return v;
        }
    }
}
