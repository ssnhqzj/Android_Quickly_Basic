package com.qzj.logistics.view.usercenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qzj.logistics.R;
import com.qzj.logistics.app.AppManager;
import com.qzj.logistics.bean.SettingItem;
import com.qzj.logistics.utils.DensityUtils;
import com.qzj.logistics.utils.TextViewDrawableUtils;
import com.qzj.logistics.utils.VersionUtils;
import com.qzj.logistics.view.deliver.senderedit.SenderGoodsActivity;
import com.qzj.logistics.view.usercenter.LoginActivity;
import com.qzj.logistics.view.usercenter.SelfMsgUpdateActivity;
import com.qzj.logistics.view.usercenter.SettingActivity;
import com.qzj.logistics.view.usercenter.WifiSettingActivity;

import java.util.ArrayList;

/**
 * setting adapter
 */
public class SettingAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<SettingItem> dataList;
    private LayoutInflater inflater;

    public SettingAdapter(Context context){
        this(context,null);
    }

    public SettingAdapter(Context context, ArrayList<SettingItem> list){
        this.context = context;
        this.dataList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList == null?0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(dataList != null && dataList.size() > 0){
            convertView = inflater.inflate(R.layout.setting_item,null);
            TextView nameView = (TextView) convertView.findViewById(R.id.user_center_item_name);
            TextView dataView = (TextView) convertView.findViewById(R.id.user_center_item_data);
            View hint = (View) convertView.findViewById(R.id.user_center_item_hint);
            RelativeLayout root = (RelativeLayout) convertView.findViewById(R.id.user_center_item_root);
            root.setOnClickListener(this);

            SettingItem item = dataList.get(position);
            if(item != null){
                if (item.isShowTopMargin()){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) root.getLayoutParams();
                    params.topMargin = DensityUtils.dip2px(context,10);
                }
                nameView.setText(item.getItemName()==null?"":item.getItemName());
                dataView.setText(item.getItemData()==null?"":item.getItemData());
                TextViewDrawableUtils.setDrawable(dataView, null, null, item.getItemDataIcon(), null);
                if (item.isShowRedTip()){
                    hint.setVisibility(View.VISIBLE);
                }else{
                    hint.setVisibility(View.GONE);
                }

                root.setTag(item.getItemId());
                convertView.setTag(item.getItemId());
            }
        }

        return convertView;
    }

    public ArrayList<SettingItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<SettingItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag()==null?"0":v.getTag().toString());
        switch (position) {
            // 个人信息修改
            case SettingActivity.SETTING_SELF_MSG:
                Intent intent_self = new Intent();
                intent_self.setClass(context, SelfMsgUpdateActivity.class);
                context.startActivity(intent_self);
                break;

            // 发货人编辑
            case SettingActivity.SETTING_SEND_PERSON:
                Intent intent_sender = new Intent();
                intent_sender.setClass(context, SenderGoodsActivity.class);
                context.startActivity(intent_sender);
                break;

            // 清除缓存
            case SettingActivity.SETTING_CLEAR_CACHE:

                break;

            // 检查更新
            case SettingActivity.SETTING_CHECK_UPDATE:
                VersionUtils version = new VersionUtils(context);
                version.initVersion();
                version.checkAppVersion();
                break;

            // 退出当前账号
            case SettingActivity.SETTING_EXIT:
                Intent loginIntent = new Intent();
                loginIntent.setClass(context, LoginActivity.class);
                context.startActivity(loginIntent);

                AppManager.getAppManager().finishNotSpecifiedActivity(LoginActivity.class);
                break;

            // 连接设置
            case SettingActivity.SETTING_CONN_SETTING:
                Intent connIntent = new Intent();
                connIntent.setClass(context, WifiSettingActivity.class);
                context.startActivity(connIntent);

                break;
        }
    }
}
