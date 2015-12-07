package gzt.com.apptest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        List<Fragment> fragments = new ArrayList<Fragment>();
        BaseFragment fragment1 = new BaseFragment(R.layout.fragment_layout1);
        BaseFragment fragment2 = new BaseFragment(R.layout.fragment_layout2);
        BaseFragment fragment3 = new BaseFragment(R.layout.fragment_layout3);

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
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
