package gzt.com.apptest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

@SuppressLint("ShowToast")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            this(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
