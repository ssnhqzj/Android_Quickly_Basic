package com.qzj.logistics.view.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qzj.logistics.R;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.PrintWifi;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;
import com.qzj.logistics.templete.BaseBill;
import com.qzj.logistics.validation.EditTextValidator;
import com.qzj.logistics.validation.ValidationModel;
import com.qzj.logistics.validation.validator.EmptyValidator;
import com.qzj.logistics.validation.validator.IpValidator;
import com.qzj.logistics.view.deliver.adapter.SpinnerAdapter;

import java.util.ArrayList;

public class WifiSettingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private MyApplication myApp;

    private TextView wifiPwName;
    private EditText wifiSSID;
    private EditText wifiPw;
    private EditText deviceIp;
    private EditText devicePort;
    private RadioButton radioButton;
    private RelativeLayout checkView;
    private RelativeLayout save;
    private Spinner pwMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_conn);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText(getResources().getString(R.string.title_activity_wifi_conn));
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        myApp = (MyApplication) getApplication();
        initView();
    }

    private void initView(){
        wifiSSID = (EditText) findViewById(R.id.conn_wifi_ssid);
        wifiPw = (EditText) findViewById(R.id.conn_wifi_pw);
        deviceIp = (EditText) findViewById(R.id.conn_wifi_ip);
        devicePort = (EditText) findViewById(R.id.conn_wifi_port);
        radioButton = (RadioButton) findViewById(R.id.conn_wifi_rb);
        checkView = (RelativeLayout) findViewById(R.id.conn_wifi_checked);
        save = (RelativeLayout) findViewById(R.id.conn_wifi_save);
        pwMode = (Spinner) findViewById(R.id.conn_wifi_pw_mode);
        wifiPwName = (TextView) findViewById(R.id.conn_wifi_pw_name);
        SpinnerAdapter adapter = new SpinnerAdapter(this,R.layout.spinner_item_wifi_setting,buildPwModeData());
        pwMode.setAdapter(adapter);
        pwMode.setSelection(2);
        pwMode.setOnItemSelectedListener(this);
        checkView.setOnClickListener(this);
        save.setOnClickListener(this);

        if (myApp != null){
            PrintWifi printWifi = myApp.getPrintWifi();
            if (printWifi != null){
                wifiSSID.setText(printWifi.getSsid());
                wifiPw.setText(printWifi.getPw()==null?"":printWifi.getPw());
                deviceIp.setText(printWifi.getIp());
                devicePort.setText(printWifi.getPort());
                radioButton.setChecked(printWifi.isChecked());
                pwMode.setSelection(printWifi.getPwMode());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.toolbar_left:
                finish();
                break;

            // 选择直连
            case R.id.conn_wifi_checked:
                radioButton.setChecked(!radioButton.isChecked());
                break;

            // 保存
            case R.id.conn_wifi_save:
                if (validate()){
                    PrintWifi printWifi = buildWifiPrint();
                    if (myApp != null) myApp.setPrintWifi(printWifi);

                    finish();
                }
                break;
        }
    }

    private boolean validate(){
        PwMode pm = (PwMode) pwMode.getSelectedItem();
        EditTextValidator validator = new EditTextValidator(this);
        if (radioButton.isChecked()){
            validator.add(new ValidationModel("SSID", wifiSSID, null, new EmptyValidator()));
            if (pm.findSpinnerId() > 0)
                validator.add(new ValidationModel("wifi密码", wifiPw, null, new EmptyValidator()));
        }
        validator.add(new ValidationModel("ip", deviceIp, null, new IpValidator()));
        validator.add(new ValidationModel("端口", devicePort, null, new EmptyValidator()));
        boolean isSuccess = validator.executeOnce();
        if (!isSuccess){
            Toast.makeText(this,validator.getFailMsg(),Toast.LENGTH_SHORT).show();
        }

        return isSuccess;
    }

    private PrintWifi buildWifiPrint() {
        PrintWifi printWifi =  new PrintWifi();
        String ssid = wifiSSID.getText().toString();
        String pw = wifiPw.getText().toString();
        String ip = deviceIp.getText().toString();
        String port = devicePort.getText().toString();

        PwMode pm = (PwMode) pwMode.getSelectedItem();

        if (ssid != null && !"".equals(ssid)) {
            printWifi.setSsid(ssid);
        }

        if (pm.findSpinnerId() > 0) {
            if (pw != null && !"".equals(pw)) {
                printWifi.setPw(pw);
            }
        }else{
            printWifi.setPw(null);
        }

        if (ip != null && !"".equals(ip)) {
            printWifi.setIp(ip);
        }

        if (port != null && !"".equals(port)) {
            printWifi.setPort(port);
        }

        printWifi.setPwMode(pm.findSpinnerId());
        printWifi.setChecked(radioButton.isChecked());

        return printWifi;
    }

    private ArrayList buildPwModeData(){
        ArrayList<SpinnerAdapterIf> list = new ArrayList<>();
        PwMode pm1 = new PwMode(0,"无密码");
        PwMode pm2 = new PwMode(1,"OPENWEP");
        PwMode pm3 = new PwMode(2,"WPA-PSK/WPA2-PSK");
        list.add(pm1);
        list.add(pm2);
        list.add(pm3);

        return list;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SpinnerAdapterIf sai = (SpinnerAdapterIf) parent.getSelectedItem();
        if (sai.findSpinnerId() == 0){
            wifiPwName.setVisibility(View.GONE);
            wifiPw.setVisibility(View.GONE);
        }else{
            wifiPwName.setVisibility(View.VISIBLE);
            wifiPw.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class PwMode implements SpinnerAdapterIf {

        private int modeId;
        private String modeName;

        public PwMode(int id, String name){
            modeId = id;
            modeName = name;
        }

        public int getModeId() {
            return modeId;
        }

        public void setModeId(int modeId) {
            this.modeId = modeId;
        }

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }

        @Override
        public String findSpinnerText() {
            return modeName;
        }

        @Override
        public int findSpinnerId() {
            return modeId;
        }

        @Override
        public BaseBill findBill() {
            return null;
        }

    }
}
