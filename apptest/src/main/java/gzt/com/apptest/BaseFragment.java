package gzt.com.apptest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/11/12.
 */
public class BaseFragment extends Fragment {

    private int resViewId = 0;
    private View rootView;

    public BaseFragment(){}

    public BaseFragment(int resViewId){
        this.resViewId = resViewId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(resViewId,container,false);
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }
}
