package com.qzj.logistics.view.deliver.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.db.dao.HatAreaDao;
import com.qzj.logistics.ui.PromptDialog;
import com.qzj.logistics.utils.TextViewDrawableUtils;
import com.qzj.logistics.view.common.SelectLocationActivity;
import com.qzj.logistics.view.deliver.senderedit.SenderEditTextWatcher;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 发货单中收件人列表adapter
 */
public class SenderListAdapter extends BaseExpandableListAdapter implements View.OnClickListener, PromptDialog.PromptButtonListener {

    private Fragment fragment;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AddrMoare> addrMoareList;
    private ArrayList<GroupHolder> gHolderList;
    private ExpandableListView listView;
    private PromptDialog promptDialog;

    public SenderListAdapter(Fragment fragment,ExpandableListView listView, ArrayList<AddrMoare> addrMoareList){
        this.fragment = fragment;
        this.listView = listView;
        this.context = fragment.getContext();
        this.addrMoareList = addrMoareList;
        inflater = LayoutInflater.from(context);
        gHolderList = new ArrayList<>();
        promptDialog = new PromptDialog(context);
        promptDialog.setText("确定要删除此地址？");
        promptDialog.setListener(this);
    }

    @Override
    public int getGroupCount() {
        return addrMoareList==null?0:addrMoareList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return addrMoareList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return addrMoareList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null){
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.deliver_sender_group,null);
            groupHolder.groupRoot = (RelativeLayout) convertView.findViewById(R.id.dgg_root);
            groupHolder.title = (TextView) convertView.findViewById(R.id.dgg_title);
            groupHolder.setDefault = (CheckBox) convertView.findViewById(R.id.dgg_setting_default);
            groupHolder.del = (TextView) convertView.findViewById(R.id.dgg_del);
            groupHolder.msg = (TextView) convertView.findViewById(R.id.dgg_msg);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (gHolderList.size()>groupPosition) {
            gHolderList.set(groupPosition, groupHolder);
        }else{
            gHolderList.add(groupPosition,groupHolder);
        }
        changeGroupView(groupHolder, groupPosition, isExpanded);
        groupHolder.groupRoot.setTag(groupPosition);
        groupHolder.groupRoot.setOnClickListener(this);
        groupHolder.setDefault.setTag(groupPosition);
        groupHolder.setDefault.setOnClickListener(this);
        groupHolder.del.setTag(groupPosition);
        groupHolder.del.setOnClickListener(this);

        if (addrMoareList != null && addrMoareList.size()>0){
            AddrMoare addrMoare = addrMoareList.get(groupPosition);
            if (addrMoare != null) {
                groupHolder.title.setText("发件人"+(groupPosition+1));
                groupHolder.setDefault.setChecked(addrMoare.isDefault());
                if (addrMoare.getRid() != null && !addrMoare.getRid().equals("")){
                    addrMoare.setSelectedArea(new HatAreaDao(context).queryJoinAddr(addrMoare.getRid()));
                }
                groupHolder.msg.setText(addrMoare.getGroupMsg());
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null){
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.deliver_sender_child,null);
            childHolder.name = (EditText) convertView.findViewById(R.id.dgc_name);
            childHolder.phone = (EditText) convertView.findViewById(R.id.dgc_phone);
            childHolder.addr = (EditText) convertView.findViewById(R.id.dgc_addr);
            childHolder.postCode = (EditText) convertView.findViewById(R.id.dgc_postcode);
            childHolder.selectLocation = (EditText) convertView.findViewById(R.id.dgc_location);
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        // 先移除监听
        childHolder.name.removeTextChangedListener((TextWatcher) childHolder.name.getTag());
        childHolder.name.setTag(null);
        childHolder.phone.removeTextChangedListener((TextWatcher) childHolder.phone.getTag());
        childHolder.phone.setTag(null);
        childHolder.addr.removeTextChangedListener((TextWatcher) childHolder.addr.getTag());
        childHolder.addr.setTag(null);
        childHolder.postCode.removeTextChangedListener((TextWatcher) childHolder.postCode.getTag());
        childHolder.postCode.setTag(null);
        // 移除监听之后再清空，避免清空时触发TextWatcher
        clearDataInChildHolder(childHolder);
        // 重新设置监听
        SenderEditTextWatcher nameWatcher = new SenderEditTextWatcher(
                addrMoareList.get(groupPosition),SenderEditTextWatcher.FIELD_NAME);
        childHolder.name.setTag(nameWatcher);
        childHolder.name.addTextChangedListener(nameWatcher);

        SenderEditTextWatcher phoneWatcher = new SenderEditTextWatcher(addrMoareList.get(groupPosition)
                ,SenderEditTextWatcher.FIELD_PHONE);
        childHolder.phone.setTag(phoneWatcher);
        childHolder.phone.addTextChangedListener(phoneWatcher);

        SenderEditTextWatcher addrWatcher = new SenderEditTextWatcher(addrMoareList.get(groupPosition)
                ,SenderEditTextWatcher.FIELD_ADDR);
        childHolder.addr.setTag(addrWatcher);
        childHolder.addr.addTextChangedListener(addrWatcher);

        SenderEditTextWatcher postCodeWatcher = new SenderEditTextWatcher(addrMoareList.get(groupPosition)
                ,SenderEditTextWatcher.FIELD_POSTCODE);
        childHolder.postCode.setTag(postCodeWatcher);
        childHolder.postCode.addTextChangedListener(postCodeWatcher);

        childHolder.selectLocation.setTag(groupPosition);
        childHolder.selectLocation.setOnClickListener(this);

        if (addrMoareList != null && addrMoareList.size()>0){
            AddrMoare addrMoare = addrMoareList.get(groupPosition);
            if (addrMoare != null) {
                childHolder.name.setText(addrMoare.getName());
                childHolder.phone.setText(addrMoare.getTelephone());
                childHolder.addr.setText(addrMoare.getAddr());
                childHolder.postCode.setText(addrMoare.getMail_code());
                childHolder.selectLocation.setText(addrMoare.getSelectedArea());
            }
        }
        return convertView;
    }

    /**
     * 清空ChildHolder中的数据
     * @param childHolder
     */
    private void clearDataInChildHolder(ChildHolder childHolder){
        childHolder.name.setText("");
        childHolder.phone.setText("");
        childHolder.addr.setText("");
        childHolder.postCode.setText("");
        childHolder.selectLocation.setText("");
    }

    /**
     * 刷新单个GroupView
     * @param groupPosition
     * @param isExpanded
     */
    public void refreshGroupView(int groupPosition, boolean isExpanded) {
        if (gHolderList != null && gHolderList.size()>0){
            GroupHolder gHolder = gHolderList.get(groupPosition);
            if (gHolder != null){
                changeGroupView(gHolder,groupPosition,isExpanded);
            }
        }
    }

    private void changeGroupView(GroupHolder groupHolder,int groupPosition,boolean isExpanded){
        if (isExpanded){
            groupHolder.msg.setVisibility(View.GONE);
            Drawable rightDrawable = context.getResources().getDrawable(R.mipmap.arrow_bottom);
            TextViewDrawableUtils.setDrawable(groupHolder.title,null,null,rightDrawable,null);
        }else{
            if (addrMoareList != null && addrMoareList.size()>0){
                AddrMoare AddrMoare = addrMoareList.get(groupPosition);
                if (AddrMoare != null) {
                    groupHolder.msg.setText(AddrMoare.getGroupMsg());
                    groupHolder.msg.setVisibility(View.VISIBLE);
                    Drawable bottomDrawable = context.getResources().getDrawable(R.mipmap.arrow_right);
                    TextViewDrawableUtils.setDrawable(groupHolder.title, null, null, bottomDrawable, null);
                }
            }
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        refreshGroupView(groupPosition,false);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        refreshGroupView(groupPosition, true);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<AddrMoare> getaddrMoareList() {
        return addrMoareList;
    }

    public void setaddrMoareList(ArrayList<AddrMoare> addrMoareList) {
        this.addrMoareList = addrMoareList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dgg_root:
                int gPosition = (int) v.getTag();
                if (listView.isGroupExpanded(gPosition)){
                    listView.collapseGroup(gPosition);
                }else{
                    listView.expandGroup(gPosition);
                }
                break;

            // 选择城市区划
            case R.id.dgc_location:
                int groupPosition = (int) v.getTag();
                Intent locaIntent = new Intent();
                locaIntent.setClass(context, SelectLocationActivity.class);
                fragment.startActivityForResult(locaIntent,groupPosition);
                break;

            // 更改默认地址
            case R.id.dgg_setting_default:
                CheckBox ck = (CheckBox) v;
                onCheckedChanged(ck, ck.isChecked());
                break;

            // 删除地址
            case R.id.dgg_del:
                promptDialog.setTag(v.getTag());
                promptDialog.show();
                break;
        }
    }

    private void onCheckedChanged(CheckBox buttonView, boolean isChecked) {
        int groupPosition = (int) buttonView.getTag();
        if (isChecked) {
            for (int i = 0; i < addrMoareList.size(); i++) {
                if (groupPosition == i) {
                    addrMoareList.get(i).reSetState(true);
                } else {
                    addrMoareList.get(i).reSetState(false);
                }
            }
            this.notifyDataSetChanged();
        }else{
            buttonView.setChecked(true);
        }
    }

    /**
     * 删除地址
     * @param groupPosition
     */
    private void delAddress(int groupPosition){
        AddrMoare am = addrMoareList.get(groupPosition);
        // addr_id>0需要删除服务器上地址
        if (am.getAddr_id() > 0){
            if (am.isDefault()){
                Toast.makeText(context, "默认地址不能删除", Toast.LENGTH_SHORT).show();
            }else{
                delSenderAddress(am.getAddr_id(),groupPosition);
            }
        }else{
            addrMoareList.remove(groupPosition);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除服务器发件人地址
     */
    private void delSenderAddress(int addrId,final int groupPosition) {
        if (addrId <=0 ) return;
        Map<String,Object> params = new HashMap<>();
        params.put("addrId",addrId);
        JsonOkHttpUtils.getInstance().post(Host.DELETE_SENDER_ADDR, params)
            .executeJson(new JsonCallBack() {

                @Override
                public void onError(Request request, Exception e) {
                    Log.e("qzj", "onError:" + e.getMessage());
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("qzj", "onResponse:" + response.toString());
                    BaseBean baseBean = JSON.toJavaObject(response, BaseBean.class);
                    if (baseBean != null && baseBean.getResultCode() == 1) {
                        Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                        addrMoareList.remove(groupPosition);
                        SenderListAdapter.this.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, baseBean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onSureClick(Object tag) {
        if (promptDialog != null) promptDialog.cancel();
        delAddress((Integer) tag);
    }

    @Override
    public void onCancelClick(Object tag) {
        if (promptDialog != null) promptDialog.cancel();
    }

    class GroupHolder {
        public RelativeLayout groupRoot;
        public TextView title;
        public CheckBox setDefault;
        public TextView del;
        public TextView msg;
    }

    class ChildHolder {
        public EditText name;
        public EditText phone;
        public EditText addr;
        public EditText postCode;
        public EditText selectLocation;
    }
}
