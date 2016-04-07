package com.qzj.logistics.view.deliver.senderedit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.base.BaseFragment;
import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.AddrMoareData;
import com.qzj.logistics.bean.Bill;
import com.qzj.logistics.bean.MessageEvent;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.ui.DrawableCenterTextView;
import com.qzj.logistics.validation.utils.ValidationUtil;
import com.qzj.logistics.view.common.SelectLocationActivity;
import com.qzj.logistics.view.deliver.adapter.SenderListAdapter;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 发货人编辑
 * Fragment
 */
public class SenderGoodsFragment extends BaseFragment implements View.OnClickListener {

    private boolean isFirstLoading = true;
    private LayoutInflater inflater;
    private View view;
    private View footerView;
    private ExpandableListView eListView;
    private SenderListAdapter adapter;

    private Bill bill;
    private ArrayList<AddrMoare> addrMoareListBack;
    private ArrayList<AddrMoare> addrMoareList;
    private ArrayList<AddrMoare> submitList;
    private MyApplication myApp;
    private User user;

    // 选择的城区id
    private String area_id;
    // 选择的城区父节点id
    private String area_father;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        bill = new Bill();
        addrMoareList = new ArrayList<>();
        submitList = new ArrayList<>();
        myApp = (MyApplication)getContext().getApplicationContext();
        user = myApp.getUser();
        loadSenderAddress();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Activity activity = getActivity();
        if (view == null){
            view = inflater.inflate(R.layout.deliver_goods_fragment_content,null);
            eListView = (ExpandableListView) view.findViewById(R.id.dgfc_list_view);
            fitFooterView();
            initEmptyLayout(view);
            // 设置adapter
            adapter = new SenderListAdapter(SenderGoodsFragment.this,eListView,addrMoareList);
            eListView.setAdapter(adapter);
            eListView.setFocusable(false);
            eListView.expandGroup(0);
            eListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    View focusView = activity.getCurrentFocus();
                    if (focusView != null){
                        focusView.clearFocus();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }

        return view;
    }

    /**
     * 填充footer
     */
    private void fitFooterView() {
        if (footerView == null){
            footerView = inflater.inflate(R.layout.fragment_listview_sender_footer,null);
        }
        DrawableCenterTextView add = (DrawableCenterTextView) footerView.findViewById(R.id.dgfc_add);
        add.setOnClickListener(this);
        eListView.addFooterView(footerView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoading){
            isFirstLoading = false;
        }
    }

    /**
     * 默认初始化一个发货人
     */
    private void initFirstData(){
        int initNum = 1;
        for (int i=0; i<initNum; i++){
            createRecipient(null);
        }
    }

    /**
     * 创建一个收件人
     */
    private void createRecipient(AddrMoare addrMoare){
        if (addrMoare == null){
            addrMoare = new AddrMoare();
        }
        addrMoareList.add(addrMoare);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 添加收件人
            case R.id.dgfc_add:
                createRecipient(null);
                adapter.setaddrMoareList(addrMoareList);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 收件人信息填写验证
     * @param ams
     * @return
     */
    private boolean validateData(ArrayList<AddrMoare> ams){
        String toastMsg = "";
        for (int i=0; i<ams.size(); i++){
            AddrMoare am = ams.get(i);

            // 姓名验证
            if (am!=null && (am.getName()==null || am.getName().equals(""))){
                toastMsg = "发货人"+(i+1)+"姓名未填写";
                Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                return false;
            }

            // 电话号码验证
            if (am!=null && (am.getTelephone()==null || am.getTelephone().equals(""))){
                toastMsg = "发货人"+(i+1)+"电话号码未填写";
                Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if (!new ValidationUtil().isTelOrPhone(am.getTelephone())){
                    toastMsg = "发货人"+(i+1)+"电话号码格式不正确";
                    Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // 区划选择
            if (am!=null && (am.getSelectedArea()==null || am.getSelectedArea().equals(""))){
                toastMsg = "发货人"+(i+1)+"区划未选择";
                Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                return false;
            }

            // 地址验证
            if (am!=null && (am.getAddr()==null || am.getAddr().equals(""))){
                toastMsg = "发货人"+(i+1)+"地址未填写";
                Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                return false;
            }

            // 邮编验证
            if (am!=null && (am.getMail_code()==null || am.getMail_code().equals(""))){
                toastMsg = "发货人"+(i+1)+"邮编未填写";
                Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if (!new ValidationUtil().isPostCode(am.getMail_code())){
                    toastMsg = "发货人"+(i+1)+"邮编格式不正确";
                    Toast.makeText(getContext(),toastMsg,Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 返回区划
        if (resultCode == SelectLocationActivity.RESULT_CODE && data != null){
            addrMoareList.get(requestCode).setSelectedArea(data.getStringExtra("address"));
            addrMoareList.get(requestCode).setRid(data.getStringExtra("area_id"));
            addrMoareList.get(requestCode).setCid(data.getStringExtra("area_father"));

            adapter.setaddrMoareList(addrMoareList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击完成后EventBus通知的方法
     * @param event
     */
    public void onEvent(MessageEvent event){
        if (event.message == MessageEvent.FLAG_SENDER_ADDRESS){
            submitList.clear();
            addrMoareList = adapter.getaddrMoareList();
            compareData();
            Log.e("qzj", submitList.toString());
            saveSenderAddress();
        }
    }

    /**
     * 加载发件人地址列表
     */
    private void loadSenderAddress() {
        if (user == null) return;
        Map<String,Object> params = new HashMap<>();
        params.put("userId",user.getUser_id());
        JsonOkHttpUtils.getInstance()
                .post(Host.LOAD_SENDER_ADDR, params)
                .addEmptyLayout(emptyLayout)
                .executeJson(new JsonCallBack() {

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("qzj", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("qzj", "onResponse:" + response.toString());
                        AddrMoareData addrData = JSON.toJavaObject(response,AddrMoareData.class);
                        if (addrData != null && addrData.getResultCode()==1){
                            ArrayList<AddrMoare> addrList = addrData.getAddrMoareData();
                            if (addrList != null && addrList.size() > 0){
                                addrMoareList = addrList;
                            }else{
                                createRecipient(null);
                            }
                            addrMoareListBack = new ArrayList<AddrMoare>();
                            for (AddrMoare am : addrMoareList){
                                addrMoareListBack.add(am.clone());
                            }
                            adapter.setaddrMoareList(addrMoareList);
                            adapter.notifyDataSetChanged();
                        }
                    }
        });
    }

    /**
     * 保存发件人地址
     */
    private void saveSenderAddress() {
        if (submitList == null || submitList.size()==0) {
            Toast.makeText(getContext(),"地址无任何改动",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validateData(submitList)) return;
        Map<String,Object> params = new HashMap<>();
        params.put("user_id",user.getUser_id());
        params.put("addrMoreList",submitList);
        JsonOkHttpUtils.getInstance()
                .post(Host.SAVE_SENDER_ADDR, params)
                .executeJson(new JsonCallBack() {

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("qzj", "onError:" + e.getMessage());
                        Toast.makeText(getContext(),"保存失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("qzj", "onResponse:" + response.toString());
                        BaseBean baseBean = JSON.toJavaObject(response,BaseBean.class);
                        if (baseBean != null && baseBean.getResultCode()==1){
                            Toast.makeText(getContext(),"已保存",Toast.LENGTH_SHORT).show();
                            // 更新本地发件人地址
                            myApp.setSenders(addrMoareList);
                            myApp.setIsDefaultAddrChanged(true);

                        }else{
                            Toast.makeText(getContext(),baseBean.getResultMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 比对数据是否有更新
     */
    private void compareData(){
        for (AddrMoare am : addrMoareList){
            if (am == null) continue;
            if (am.getAddr_id() > 0){
                AddrMoare amBack = null;
                for (AddrMoare amb : addrMoareListBack){
                    if (am.getAddr_id() == amb.getAddr_id()){
                        amBack = amb;
                        break;
                    }
                }

                if (amBack.getSelectedArea() == null) amBack.findAddress(getContext());
                if (amBack != null && (!am.getGroupMsg().equals(amBack.getGroupMsg()) || amBack.isDefault()!=am.isDefault())) {
                    submitList.add(am);
                }
            }else{
                submitList.add(am);
            }
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
