package com.qzj.logistics.view.deliver.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.qzj.logistics.R;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.ui.PromptDialog;
import com.qzj.logistics.utils.TextViewDrawableUtils;
import com.qzj.logistics.view.books.BookActivity;
import com.qzj.logistics.view.common.SelectLocationActivity;
import com.qzj.logistics.view.deliver.DeliverEditTextWatcher;
import com.qzj.logistics.view.deliver.SpinnerSelectedListener;
import com.qzj.logistics.zxing.activity.CaptureActivity;

import java.util.ArrayList;

/**
 * 发货单中收件人列表adapter
 */
public class RecipientListAdapter extends BaseExpandableListAdapter implements View.OnClickListener,PromptDialog.PromptButtonListener {

    private Fragment fragment;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HisReceiver> hisReceivers;
    private ArrayList<GroupHolder> gHolderList;
    private PromptDialog promptDialog;

    public RecipientListAdapter(Fragment fragment, ArrayList<HisReceiver> hisReceivers){
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.hisReceivers = hisReceivers;
        inflater = LayoutInflater.from(context);
        gHolderList = new ArrayList<>();
        promptDialog = new PromptDialog(context);
        promptDialog.setText("确定要删除此收件人？");
        promptDialog.setListener(this);
    }

    @Override
    public int getGroupCount() {
        return hisReceivers==null?0:hisReceivers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return hisReceivers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return hisReceivers.get(groupPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null){
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.deliver_goods_group,null);
            groupHolder.title = (TextView) convertView.findViewById(R.id.dgg_title);
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

        if (hisReceivers != null && hisReceivers.size()>0){
            HisReceiver hisReceiver = hisReceivers.get(groupPosition);
            if (hisReceiver != null) {
                groupHolder.title.setText("收件人"+(groupPosition+1));
                groupHolder.msg.setText(hisReceiver.getFullAddr());
                groupHolder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.setTag(groupPosition);
                        promptDialog.show();
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null){
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.deliver_goods_child,null);
            childHolder.types = (Spinner) convertView.findViewById(R.id.dgc_types);
            childHolder.name = (EditText) convertView.findViewById(R.id.dgc_name);
            childHolder.phone = (EditText) convertView.findViewById(R.id.dgc_phone);
            childHolder.addr = (EditText) convertView.findViewById(R.id.dgc_addr);
            childHolder.postCode = (EditText) convertView.findViewById(R.id.dgc_postcode);
            childHolder.number = (EditText) convertView.findViewById(R.id.dgc_number);
            childHolder.scan = (TextView) convertView.findViewById(R.id.dgc_scan);
            childHolder.hisConnector = (TextView) convertView.findViewById(R.id.dgc_history);
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
        childHolder.number.removeTextChangedListener((TextWatcher) childHolder.number.getTag());
        childHolder.number.setTag(null);
        // 移除监听之后再清空，避免清空时触发TextWatcher
        clearDataInChildHolder(childHolder);
        // 重新设置监听
        DeliverEditTextWatcher nameWatcher = new DeliverEditTextWatcher(
                hisReceivers.get(groupPosition),DeliverEditTextWatcher.FIELD_NAME);
        childHolder.name.setTag(nameWatcher);
        childHolder.name.addTextChangedListener(nameWatcher);

        DeliverEditTextWatcher phoneWatcher = new DeliverEditTextWatcher(hisReceivers.get(groupPosition)
                ,DeliverEditTextWatcher.FIELD_PHONE);
        childHolder.phone.setTag(phoneWatcher);
        childHolder.phone.addTextChangedListener(phoneWatcher);

        DeliverEditTextWatcher addrWatcher = new DeliverEditTextWatcher(hisReceivers.get(groupPosition)
                ,DeliverEditTextWatcher.FIELD_ADDR);
        childHolder.addr.setTag(addrWatcher);
        childHolder.addr.addTextChangedListener(addrWatcher);

        DeliverEditTextWatcher postCodeWatcher = new DeliverEditTextWatcher(hisReceivers.get(groupPosition)
                ,DeliverEditTextWatcher.FIELD_POSTCODE);
        childHolder.postCode.setTag(postCodeWatcher);
        childHolder.postCode.addTextChangedListener(postCodeWatcher);

        DeliverEditTextWatcher numberWatcher = new DeliverEditTextWatcher(hisReceivers.get(groupPosition)
                ,DeliverEditTextWatcher.FIELD_NUMBER);
        childHolder.number.setTag(numberWatcher);
        childHolder.number.addTextChangedListener(numberWatcher);
        childHolder.selectLocation.setTag(groupPosition);
        childHolder.selectLocation.setOnClickListener(this);

        if (hisReceivers != null && hisReceivers.size()>0){
            HisReceiver hisReceiver = hisReceivers.get(groupPosition);
            if (hisReceiver != null) {
                ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, MyApplication.productTypes);
                companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                childHolder.types.setAdapter(companyAdapter);
                childHolder.types.setOnItemSelectedListener(new SpinnerSelectedListener(hisReceiver, SpinnerSelectedListener.TYPE_THING_TYPES));
                childHolder.types.setSelection(hisReceiver.getType());
                childHolder.name.setText(hisReceiver.getName());
                childHolder.phone.setText(hisReceiver.getTelephone());
                childHolder.addr.setText(hisReceiver.getAddr());
                childHolder.postCode.setText(hisReceiver.getMail_code());
                childHolder.number.setText(hisReceiver.getCourier_number());
                childHolder.selectLocation.setText(hisReceiver.getSelectedArea());

                childHolder.hisConnector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentHis = new Intent();
                        intentHis.setClass(context, BookActivity.class);
                        fragment.startActivityForResult(intentHis, groupPosition);
                    }
                });

                childHolder.scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentScan = new Intent();
                        intentScan.setClass(context, CaptureActivity.class);
                        fragment.startActivityForResult(intentScan,groupPosition);
                    }
                });
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
        childHolder.number.setText("");
    }

    /**
     * 刷新单个GroupView
     * @param groupPosition
     * @param isExpanded
     */
    public void refreshGroupView(int groupPosition, boolean isExpanded) {
        if (gHolderList != null && gHolderList.size() > 0){
            GroupHolder gHolder = gHolderList.get(groupPosition);
            if (gHolder != null){
                changeGroupView(gHolder,groupPosition,isExpanded);
            }
        }
    }

    private void changeGroupView(GroupHolder groupHolder,int groupPosition,boolean isExpanded){
        if (isExpanded){
            groupHolder.msg.setVisibility(View.GONE);
            groupHolder.del.setVisibility(View.VISIBLE);
            Drawable rightDrawable = context.getResources().getDrawable(R.mipmap.arrow_bottom);
            TextViewDrawableUtils.setDrawable(groupHolder.title,null,null,rightDrawable,null);
        }else{
            HisReceiver hisReceiver = hisReceivers.get(groupPosition);
            if (hisReceiver != null) {
                groupHolder.msg.setText(hisReceiver.getGroupMsg());
                groupHolder.msg.setVisibility(View.VISIBLE);
                groupHolder.del.setVisibility(View.GONE);
                Drawable bottomDrawable = context.getResources().getDrawable(R.mipmap.arrow_right);
                TextViewDrawableUtils.setDrawable(groupHolder.title, null, null, bottomDrawable, null);
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

    public void setHisReceivers(ArrayList<HisReceiver> hisReceivers) {
        this.hisReceivers = hisReceivers;
    }

    public ArrayList<HisReceiver> getHisReceivers() {
        return hisReceivers;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 选择城市区划
            case R.id.dgc_location:
                int groupPosition = (int) v.getTag();
                Intent locaIntent = new Intent();
                locaIntent.setClass(context, SelectLocationActivity.class);
                fragment.startActivityForResult(locaIntent,groupPosition);
                break;
        }
    }

    @Override
    public void onSureClick(Object tag) {
        if (promptDialog != null) promptDialog.cancel();
        if (tag != null){
            hisReceivers.remove(Integer.parseInt(tag.toString()));
            notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelClick(Object tag) {
        if (promptDialog != null) promptDialog.cancel();
    }

    class GroupHolder {
        public TextView title;
        public TextView del;
        public TextView msg;
    }

    class ChildHolder {
        public Spinner types;
        public EditText name;
        public EditText phone;
        public EditText addr;
        public EditText postCode;
        public EditText number;
        public EditText selectLocation;
        public TextView scan;
        public TextView hisConnector;
    }
}
