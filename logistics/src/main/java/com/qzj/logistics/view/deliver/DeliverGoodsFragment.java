package com.qzj.logistics.view.deliver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseFragment;
import com.qzj.logistics.bean.AddrMoare;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.bean.PrintWifi;
import com.qzj.logistics.bean.RecordAndReceiver;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;
import com.qzj.logistics.templete.BaseBill;
import com.qzj.logistics.ui.DrawableCenterTextView;
import com.qzj.logistics.utils.print.PrintConnector;
import com.qzj.logistics.utils.wifi.WifiController;
import com.qzj.logistics.validation.utils.ValidationUtil;
import com.qzj.logistics.view.common.SelectLocationActivity;
import com.qzj.logistics.view.deliver.adapter.RecipientListAdapter;
import com.qzj.logistics.view.deliver.adapter.SpinnerAdapter;
import com.qzj.logistics.view.deliver.senderedit.SenderGoodsActivity;
import com.qzj.logistics.view.usercenter.WifiSettingActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多人发货，多人发货
 * Fragment
 */
public class DeliverGoodsFragment extends BaseFragment implements View.OnClickListener {
    // 单人发货
    public static final int MODE_ONE = 1;
    // 多人发货
    public static final int MODE_MORE = 2;
    // 历史联系人
    public static final int RESULT_CODE_HISTORY = 3;
    // 扫描订单号
    public static final int RESULT_CODE_NUMBER = 4;
    // 显示dialog
    public static final int DIALOG_SHOW = 5;
    // 隐藏dialog
    public static final int DIALOG_HIDEEN = 6;
    // 重置wifi
    public static final int RESET_WIFI = 7;
    private static final String TAG = "DeliverGoodsFragment";

    private LayoutInflater inflater;
    private View view;
    private View headerView;
    private View footerView;
    private Spinner spannerPerson;
    private Spinner spannerCompany;
    private TextView senderAddress;
    private ExpandableListView eListView;
    private RecipientListAdapter adapter;

    private ArrayList<HisReceiver> hisReceivers;
    private RecordAndReceiver rar;
    private BaseBill bill;
    private MyApplication myApp;
    private User user;
    // 发件人信息
    private AddrMoare senderAm;
    private int mode;

    private PrintConnector printConnector;
    private WifiController wifiController;
    private PrintWifi printWifi;
    private ProgressDialog pDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // wifi扫描结束
            if (msg.what == WifiController.SCAN_FINISHED) {
                printWifi = myApp.getPrintWifi();
                if (printWifi != null) {
                    boolean isDestWifi = wifiController.isSpecifyWifi(printWifi.getSsid());
                    if (isDestWifi){
                        handler.obtainMessage(WifiController.CONN_SUCCESS).sendToTarget();
                        return;
                    }
                    // 检测wifi是否存在
                    boolean wifiExist = wifiController.isSpecifyWifiExist(printWifi.getSsid());
                    if (wifiExist) {
                        switch (printWifi.getPwMode()){
                            case 0:
                                wifiController.connect(printWifi.getSsid(), printWifi.getPw(), WifiController.CONNECT_NONE);
                                break;
                            case 1:
                                wifiController.connect(printWifi.getSsid(), printWifi.getPw(), WifiController.CONNECT_WEP);
                                break;
                            case 2:
                                wifiController.connect(printWifi.getSsid(), printWifi.getPw(), WifiController.CONNECT_WPA);
                                break;
                        }
                    } else {
                        Toast.makeText(getContext(), "您配置的WIFI不存在", Toast.LENGTH_SHORT).show();
                        if (dialogHandler != null){
                            dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_HIDEEN).sendToTarget();
                            dialogHandler.obtainMessage(DeliverGoodsFragment.RESET_WIFI).sendToTarget();
                        }
                    }
                }
            }

            // wifi连接成功
            else if (msg.what == WifiController.CONN_SUCCESS) {
                Log.e("qzj","--------------init print success------------");
                // 连接打印机
                printConnector = new PrintConnector(getContext(), printWifi.getIp(), printWifi.getPort());
                printConnector.setDialogHandler(dialogHandler);
                printConnector.setNoticeHandler(handler);
                printConnector.connect();
            }

            // 打印机连接成功
            else if (msg.what == PrintConnector.PRINT_CONN_SUCCESS) {
                if (bill != null && printConnector != null) {
                    bill.init(getContext(), printConnector).printBill(rar);
                    // 上传数据到服务器
                    sendPrintData(rar);
                }
            }
        }
    };

    Handler dialogHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 显示进度对话框
            if(msg.what == DIALOG_SHOW){
                if (pDialog != null) {
                    pDialog.setMessage(msg.obj==null?"":String.valueOf(msg.obj));
                    pDialog.show();
                }
            }

            // 隐藏进度对话框
            else if(msg.what == DIALOG_HIDEEN){
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }

            // 重置wifi
            else if(msg.what == RESET_WIFI){
                if (wifiController != null){
                    wifiController.resetOldWifi();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApplication) getContext().getApplicationContext();
        printWifi = myApp.getPrintWifi();
        initDialog();
        wifiController = new WifiController(getContext(), handler);
        wifiController.setDialogHandler(dialogHandler);
        if (printConnector != null) printConnector.setDialogHandler(dialogHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mode = bundle.getInt("mode");
        }
        inflater = LayoutInflater.from(getContext());
        hisReceivers = new ArrayList<>();
        rar = new RecordAndReceiver();
        user = ((MyApplication) getContext().getApplicationContext()).getUser();
        initFirstData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Activity activity = getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.deliver_goods_fragment_content, null);
            eListView = (ExpandableListView) view.findViewById(R.id.dgfc_list_view);
            fitHeaderView();
            fitFooterView();
            initSender();
            // 设置adapter
            adapter = new RecipientListAdapter(DeliverGoodsFragment.this, hisReceivers);
            eListView.setAdapter(adapter);
            eListView.setFocusable(false);
            eListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    View focusView = activity.getCurrentFocus();
                    if (focusView != null) {
                        focusView.clearFocus();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }

            });
        }
        eListView.expandGroup(0);

        return view;
    }

    /**
     * 填充header
     */
    private void fitHeaderView() {
        if (headerView == null) {
            headerView = inflater.inflate(R.layout.fragment_listview_header, null);
        }
        spannerPerson = (Spinner) headerView.findViewById(R.id.dgfc_spinner_person);
        spannerCompany = (Spinner) headerView.findViewById(R.id.dgfc_spinner_company);
        MyApplication application = (MyApplication) getContext().getApplicationContext();
        SpinnerAdapter senderAdapter = new SpinnerAdapter(getContext(),R.layout.spinner_item,application.getSenders());
        SpinnerAdapter companyAdapter = new SpinnerAdapter(getContext(),R.layout.spinner_item,application.getCompanies());
        spannerPerson.setAdapter(senderAdapter);
        spannerCompany.setAdapter(companyAdapter);
        spannerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddrMoare am = (AddrMoare) parent.getSelectedItem();
                if (am != null) {
                    senderAddress.setText(am.getFullAddr());
                    rar.setAm(am);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        eListView.addHeaderView(headerView);
    }

    /**
     * 填充footer
     */
    private void fitFooterView() {
        if (footerView == null) {
            footerView = inflater.inflate(R.layout.fragment_listview_footer, null);
        }
        senderAddress = (TextView) footerView.findViewById(R.id.dgfc_self_address_detail);
        TextView editAddress = (TextView) footerView.findViewById(R.id.dgfc_self_address_edit);
        DrawableCenterTextView add = (DrawableCenterTextView) footerView.findViewById(R.id.dgfc_add);
        DrawableCenterTextView print = (DrawableCenterTextView) footerView.findViewById(R.id.dgfc_print);
        editAddress.setOnClickListener(this);
        add.setOnClickListener(this);
        print.setOnClickListener(this);
        if (mode == MODE_MORE) add.setVisibility(View.VISIBLE);
        eListView.addFooterView(footerView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("qzj", "hint ::" + mode + "," + isVisibleToUser);
        if (isVisibleToUser && (mode == DeliverGoodsFragment.MODE_ONE
                || mode == DeliverGoodsFragment.MODE_MORE)) {
            if (wifiController != null) wifiController.registerWifiReceiver();
        }else{
            if (wifiController != null) wifiController.unregisterWifiReceiver();
        }
    }

    /**
     * 初始化发件人信息
     */
    private void initSender() {
        List<SpinnerAdapterIf> senders = myApp.getSenders();
        if (senders==null || senders.size()==0) return;
        boolean hasDefault = false;
        for (int i=0;i<senders.size();i++) {
            SpinnerAdapterIf sai = senders.get(i);
            if (sai instanceof AddrMoare) {
                AddrMoare am = (AddrMoare) sai;
                if (am.isDefault()){
                    hasDefault = true;
                    rar.setAm(am);
                    spannerPerson.setSelection(i);
                    senderAddress.setText(am.getFullAddr());
                }
            }
        }

        // 如果没有默认地址，则设置成第一个
        if (!hasDefault && senders.size() > 0){
            SpinnerAdapterIf sai = senders.get(0);
            if (sai instanceof AddrMoare) {
                AddrMoare am = (AddrMoare) sai;
                rar.setAm(am);
                spannerPerson.setSelection(0);
                senderAddress.setText(am.getFullAddr());
            }
        }
    }

    /**
     * 默认初始化一个收件人
     */
    private void initFirstData() {
        // 如果是多人发货就初始化2个收件人，否则1个
        int initNum = mode == MODE_MORE ? 2 : 1;
        for (int i = 0; i < initNum; i++) {
            createRecipient();
        }
    }

    /**
     * 创建一个收件人
     */
    private void createRecipient() {
        HisReceiver hisReceiver = new HisReceiver();
        hisReceivers.add(hisReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 编辑发件人地址
            case R.id.dgfc_self_address_edit:
                Intent intent = new Intent();
                intent.setClass(getContext(), SenderGoodsActivity.class);
                startActivity(intent);
                break;

            // 添加收件人
            case R.id.dgfc_add:
                createRecipient();
                adapter.setHisReceivers(hisReceivers);
                adapter.notifyDataSetChanged();
                break;

            // 打印
            case R.id.dgfc_print:
                printWifi = myApp.getPrintWifi();
                if (rar.getAm() == null){
                    Toast.makeText(getContext(), "还未选择寄件人地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<HisReceiver> hisReceivers = adapter.getHisReceivers();
                if (validateData(hisReceivers)) {
                    SpinnerAdapterIf saiSender = (SpinnerAdapterIf) spannerPerson.getSelectedItem();
                    SpinnerAdapterIf saiCompany = (SpinnerAdapterIf) spannerCompany.getSelectedItem();
                    if (saiSender != null) rar.setUser_id(saiSender.findSpinnerId());
                    if (saiCompany != null) rar.setCompany_id(saiCompany.findSpinnerId());
                    rar.setHisReceivers(hisReceivers);

                    Log.e("qzj", "打印收件人信息：");
                    Log.e("qzj", rar.toString());

                    // 检测打印单
                    bill = saiCompany.findBill();
                    if (bill == null) {
                        Toast.makeText(getContext(), "找不到匹配的打印单", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (printWifi == null) {
                        Toast.makeText(getContext(), "还未配置打印机信息", Toast.LENGTH_SHORT).show();
                        Intent wifiIntent = new Intent(getContext(), WifiSettingActivity.class);
                        startActivity(wifiIntent);
                        return;
                    }

                    if (printWifi.getIp() == null || printWifi.getIp().equals("")) {
                        Toast.makeText(getContext(), "未配置IP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (printWifi.getPort() == null || printWifi.getPort().equals("")) {
                        Toast.makeText(getContext(), "未配置端口", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 直连
                    if (printWifi != null && printWifi.isChecked()) {
                        if (printWifi.getSsid() == null || printWifi.getSsid().equals("")) {
                            Toast.makeText(getContext(), "未配置wifi名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (printWifi.getPwMode()>0 && (printWifi.getPw() == null || printWifi.getPw().equals(""))) {
                            Toast.makeText(getContext(), "未配置wifi密码", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 开启wifi
                        if (wifiController.openWifi()) {
                            wifiController.scanWifi(handler);
                            if (dialogHandler != null)
                                dialogHandler.obtainMessage(DeliverGoodsFragment.DIALOG_SHOW,"正在搜索打印机...").sendToTarget();
                        }
                    }
                    // 非直连
                    else {
                        if (printConnector == null) {
                            Toast.makeText(getContext(), "找不到打印机", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (bill != null && printConnector != null) {
                            bill.init(getContext(), printConnector).printBill(rar);
                            // 上传数据到服务器
                            sendPrintData(rar);
                        }
                    }
                    break;
                }
        }
    }

    /**
     * 收件人信息填写验证
     *
     * @param hisReceivers
     * @return
     */
    private boolean validateData(ArrayList<HisReceiver> hisReceivers) {
        String toastMsg = "";
        for (int i = 0; i < hisReceivers.size(); i++) {
            HisReceiver hisReceiver = hisReceivers.get(i);

            // 类别验证
            if (hisReceiver != null && hisReceiver.getType() == 0) {
                toastMsg = "收件人" + (i + 1) + "类别未选择";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 姓名验证
            if (hisReceiver != null && (hisReceiver.getName() == null || hisReceiver.getName().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "姓名未填写";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 电话号码验证
            if (hisReceiver != null && (hisReceiver.getTelephone() == null || hisReceiver.getTelephone().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "电话号码未填写";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (!new ValidationUtil().isPhoneNumber(hisReceiver.getTelephone())) {
                    toastMsg = "收件人" + (i + 1) + "电话号码格式不正确";
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // 城市区划验证
            if (hisReceiver != null && (hisReceiver.getSelectedArea() == null || hisReceiver.getSelectedArea().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "城市区划未选择";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 地址验证
            if (hisReceiver != null && (hisReceiver.getAddr() == null || hisReceiver.getAddr().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "地址未填写";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 邮编验证
            if (hisReceiver != null && (hisReceiver.getMail_code() == null || hisReceiver.getMail_code().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "邮编未填写";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (!new ValidationUtil().isPostCode(hisReceiver.getMail_code())) {
                    toastMsg = "收件人" + (i + 1) + "邮编格式不正确";
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // 单号验证
            if (hisReceiver != null && (hisReceiver.getCourier_number() == null || hisReceiver.getCourier_number().equals(""))) {
                toastMsg = "收件人" + (i + 1) + "单号未填写";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void sendPrintData(RecordAndReceiver rar) {
        if (rar == null) return;
        Map<String, Object> params = new HashMap<>();
        params.put("receiver", rar);
        JsonOkHttpUtils.getInstance().post(Host.PRINT_BILLS, params).executeJson(new JsonCallBack() {

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e("qzj", "onError:" + e.getMessage());
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:" + response.toString());
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        // 返回扫描单号
        if (resultCode == RESULT_CODE_NUMBER) {
            Log.e(TAG, "扫描单号: request:" + requestCode + ",code=" + data.getStringExtra("code"));
            hisReceivers.get(requestCode).setCourier_number(data.getStringExtra("code"));
            adapter.notifyDataSetChanged();
        }
        // 返回历史联系人
        else if (resultCode == RESULT_CODE_HISTORY) {
            Log.e(TAG, "历史联系人: request:" + requestCode + ",his_receiver=" + data.getSerializableExtra("his_receiver"));
            HisReceiver item = (HisReceiver) data.getSerializableExtra("his_receiver");
            if (item != null) {
                hisReceivers.get(requestCode).setName(item.getName());
                hisReceivers.get(requestCode).setTelephone(item.getTelephone());
                hisReceivers.get(requestCode).setP_name(item.getP_name());
                hisReceivers.get(requestCode).setC_name(item.getC_name());
                hisReceivers.get(requestCode).setR_name(item.getR_name());
                hisReceivers.get(requestCode).setAddr(item.getAddr());
                hisReceivers.get(requestCode).setMail_code(item.getMail_code());
                adapter.notifyDataSetChanged();
            }
        }
        // 返回区划
        else if (resultCode == SelectLocationActivity.RESULT_CODE && data != null) {
            hisReceivers.get(requestCode).setP_name(data.getStringExtra("province_name"));
            hisReceivers.get(requestCode).setC_name(data.getStringExtra("city_name"));
            hisReceivers.get(requestCode).setR_name(data.getStringExtra("area_name"));
            hisReceivers.get(requestCode).setSelectedArea(data.getStringExtra("address"));

            adapter.setHisReceivers(hisReceivers);
            adapter.notifyDataSetChanged();
        }
        /*// 返回发件地址
        else if (resultCode == SelectSenderAddrActivity.RESULT_CODE_SENDER_ADDR) {
            AddrMoare am = (AddrMoare) data.getSerializableExtra("addr_moare");
            if (am != null) {
                senderAddress.setText(am.getFullAddr());
                rar.setCid(am.getCid());
                rar.setRid(am.getRid());
                rar.setAddr(am.getAddr());
                rar.setAm(am);
            }
        }*/

    }

    private void initDialog() {
        //显示连接进度对话框
        pDialog = new ProgressDialog(getContext());
        pDialog.setTitle("提示");
        pDialog.setMessage("正在连接打印机...");
        pDialog.setIndeterminate(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    public void setPrintConnector(PrintConnector printConnector) {
        this.printConnector = printConnector;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myApp.isDefaultAddrChanged()){
            myApp.setIsDefaultAddrChanged(false);
            Object obj = spannerPerson.getSelectedItem();
            List<SpinnerAdapterIf> senders = myApp.getSenders();
            SpinnerAdapter senderAdapter = (SpinnerAdapter) spannerPerson.getAdapter();
            // 无发货人地址将当前发货人信息置空
            if (senders==null || senders.size()==0) {
                senderAdapter.setList(null);
                senderAdapter.notifyDataSetChanged();
                return;
            } else {
                senderAdapter.setList(senders);
                senderAdapter.notifyDataSetChanged();
            }

            if (obj != null && obj instanceof AddrMoare){
                AddrMoare selectedAm = (AddrMoare) obj;
                int addrId = selectedAm.getAddr_id();
                boolean isDefault = selectedAm.isDefault();

                // 有发货人地址，则刷新发货人地址
                AddrMoare defaultSender = null;
                AddrMoare selectedSender = null;
                AddrMoare firstSender = null;
                int defaultSenderIndex = 0;
                int selectedSenderIndex = 0;
                for (int i=0;i<senders.size();i++) {
                    SpinnerAdapterIf sai = senders.get(i);
                    if (sai instanceof AddrMoare) {
                        AddrMoare am = (AddrMoare) sai;
                        if (i == 0) firstSender = am;
                        if (am.isDefault()){
                            defaultSenderIndex = i;
                            defaultSender = am;
                        }
                        if (am.getAddr_id() == addrId) {
                            selectedSenderIndex = i;
                            selectedSender = am;
                        }
                    }
                }

                if (isDefault && defaultSender != null){
                    rar.setAm(defaultSender);
                    spannerPerson.setSelection(defaultSenderIndex);
                    senderAddress.setText(defaultSender.getFullAddr());
                } else if (selectedSender != null){
                    rar.setAm(selectedSender);
                    spannerPerson.setSelection(selectedSenderIndex);
                    senderAddress.setText(selectedSender.getFullAddr());
                } else if (defaultSender != null){
                    rar.setAm(defaultSender);
                    spannerPerson.setSelection(defaultSenderIndex);
                    senderAddress.setText(defaultSender.getFullAddr());
                } else {
                    rar.setAm(firstSender);
                    spannerPerson.setSelection(0);
                    senderAddress.setText(firstSender.getFullAddr());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wifiController != null)
            wifiController.unregisterWifiReceiver();
    }

}
