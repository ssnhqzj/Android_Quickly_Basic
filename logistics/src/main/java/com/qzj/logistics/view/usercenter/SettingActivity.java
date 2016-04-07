package com.qzj.logistics.view.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.qzj.logistics.R;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.SettingItem;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.view.usercenter.adapter.SettingAdapter;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {

    // 个人信息修改
    public static final int SETTING_SELF_MSG = 1;
    // 发货人编辑
    public static final int SETTING_SEND_PERSON = 2;
    // 清除缓存
    public static final int SETTING_CLEAR_CACHE = 3;
    // 检查更新
    public static final int SETTING_CHECK_UPDATE = 4;
    // 退出当前账号
    public static final int SETTING_EXIT = 5;
    // 连接设置
    public static final int SETTING_CONN_SETTING = 6;

    private ListView listView;
    private SettingAdapter adapter;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText("设置");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        myApp = (MyApplication) getApplication();
        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.setting_listview);
        adapter = new SettingAdapter(this,getDataList());
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                this.finish();
                break;
        }
    }

    /**
     * 设置--列表数据
     * @return
     */
    private ArrayList<SettingItem> getDataList(){
        ArrayList<SettingItem> items = new ArrayList<SettingItem>();
        SettingItem item1 = new SettingItem();
        item1.setItemId(SETTING_SELF_MSG);
        item1.setItemName("个人信息修改");
        item1.setItemDataIcon(getResources().getDrawable(R.mipmap.arrows_right));
        items.add(item1);

        SettingItem item2 = new SettingItem();
        item2.setItemId(SETTING_SEND_PERSON);
        item2.setItemName("发货人编辑");
        item2.setItemDataIcon(getResources().getDrawable(R.mipmap.arrows_right));
        items.add(item2);

        SettingItem item6 = new SettingItem();
        item6.setItemId(SETTING_CONN_SETTING);
        item6.setItemName("连接设置");
        item6.setItemDataIcon(getResources().getDrawable(R.mipmap.arrows_right));
        items.add(item6);

        SettingItem item4 = new SettingItem();
        item4.setItemId(SETTING_CHECK_UPDATE);
        item4.setItemName("检查更新");
        item4.setIsShowTopMargin(true);
        if (myApp != null) item4.setIsShowRedTip(myApp.isHasVersionChange());
        items.add(item4);

        SettingItem item5 = new SettingItem();
        item5.setItemId(SETTING_EXIT);
        item5.setItemName("退出当前账号");
        item5.setIsShowTopMargin(true);
        items.add(item5);

        return items;
    }

}
