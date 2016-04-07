package com.qzj.logistics.view.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.empty.EmptyLayout;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.AddrMoareData;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.db.dao.HatAreaDao;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectSenderAddrActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    // 返回码
    public static final int RESULT_CODE_SENDER_ADDR = 5;
    private User user;
    private ListView listView;
    private SenderAddrAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sender_addr);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText("选择发件地址");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        initView();
        loadSenderAddress();
    }

    private void initView(){
        user = ((MyApplication)getApplication()).getUser();
        listView = (ListView) findViewById(R.id.sender_addr_listview);
        adapter = new SenderAddrAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        initEmptyLayout();
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
     * 加载发件人地址列表
     */
    private void loadSenderAddress() {
        if (user == null) return;
        Map<String,Object> params = new HashMap<>();
        params.put("userId",user.getUser_id());
        JsonOkHttpUtils.getInstance()
                .post(Host.LOAD_SENDER_ADDR, params)
                .executeJson(new JsonCallBack() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        if (emptyLayout != null){
                            emptyLayout.setEmptyType(EmptyLayout.LOADING);
                        }
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("qzj", "onError:" + e.getMessage());
                        if (emptyLayout != null){
                            emptyLayout.setEmptyType(EmptyLayout.LOADING_FAILED);
                        }
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("qzj", "onResponse:" + response.toString());
                        AddrMoareData addrData = JSON.toJavaObject(response, AddrMoareData.class);
                        if (addrData != null && addrData.getResultCode()==1){
                            ArrayList<AddrMoare> addrList = addrData.getAddrMoareData();
                            adapter.setAddrList(addrList);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        if (emptyLayout != null){
                            emptyLayout.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AddrMoare am = (AddrMoare) parent.getAdapter().getItem(position);
        Intent data = new Intent();
        if (am != null){
            data.putExtra("addr_moare", am);
            setResult(RESULT_CODE_SENDER_ADDR,data);
        }else{
            setResult(-1,data);
        }
        finish();
    }

    class SenderAddrAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<AddrMoare> addrList;

        public SenderAddrAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return addrList==null?0:addrList.size();
        }

        @Override
        public Object getItem(int position) {
            return addrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holderView = null;
            if (convertView == null){
                holderView = new HolderView();
                convertView = View.inflate(context,R.layout.sender_addr_item,null);
                holderView.addrView = (TextView) convertView.findViewById(R.id.sender_addr_text);
            }else{
                holderView = (HolderView) convertView.getTag();
            }

            if (addrList != null && addrList.size() > 0){
                AddrMoare addrMoare = addrList.get(position);
                if (addrMoare != null && addrMoare.getRid() != null && !addrMoare.getRid().equals("")){
                    if (addrMoare.getSelectedArea() == null || addrMoare.getSelectedArea().equals("")){
                        String[] pcr = new HatAreaDao(context).queryPcr(addrMoare.getRid());
                        String joinAddr = "";
                        if (pcr.length > 0) {
                            addrMoare.setpName(pcr[0]);
                            joinAddr += pcr[0];
                        }
                        if (pcr.length > 1) {
                            addrMoare.setcName(pcr[1]);
                            joinAddr += pcr[1];
                        }
                        if (pcr.length > 2) {
                            addrMoare.setrName(pcr[2]);
                            joinAddr += pcr[2];
                        }
                        addrMoare.setSelectedArea(joinAddr);
                    }
                    holderView.addrView.setText(addrMoare.getFullAddr());
                }
            }

            return convertView;
        }

        public void setAddrList(ArrayList<AddrMoare> addrList) {
            this.addrList = addrList;
        }

        class HolderView {
            public TextView addrView;
        }
    }
}
