package com.qzj.logistics.view.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.loading.DotsTextView;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.bean.UserData;
import com.qzj.logistics.utils.VersionUtils;
import com.qzj.logistics.validation.EditTextValidator;
import com.qzj.logistics.validation.ValidationModel;
import com.qzj.logistics.validation.validator.EmptyValidator;
import com.qzj.logistics.view.deliver.DeliverGoodsActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements EditTextValidator.OnRelationBtnStateListener {

    private EditText userName;
    private EditText passWord;
    private TextView hint;
    private RelativeLayout login;
    private TextView loginText;
    private DotsTextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(Color.TRANSPARENT);
        toolbar.setCenterText(getResources().getString(R.string.title_activity_login));
        toolbar.setRightText(getResources().getString(R.string.title_activity_register));
        initToolBar(toolbar);

        initView();
        checkVersion();
    }

    private void initView(){
        userName = (EditText) findViewById(R.id.login_edit_user);
        passWord = (EditText) findViewById(R.id.login_edit_pw);
        hint = (TextView) findViewById(R.id.login_hint);
        loading = (DotsTextView) findViewById(R.id.login_loading);
        login = (RelativeLayout) findViewById(R.id.login_btn_login);
        login.setOnClickListener(this);
        loginText = (TextView) findViewById(R.id.login_btn_login_text);
        TextView forgetPw = (TextView) findViewById(R.id.login_forget_pw);
        forgetPw.setOnClickListener(this);

        // 初始化验证器
        EditTextValidator validator = new EditTextValidator(this,login);
        validator.setRelationListener(this);
        validator.add(new ValidationModel("用户名", userName, hint, new EmptyValidator()))
                .add(new ValidationModel("密码", passWord, hint, new EmptyValidator()))
                .execute();

        User user = ((MyApplication)getApplication()).getUser();
        if (user != null && user.getTelephone() != null){
            userName.setText(user.getTelephone());
            userName.setSelection(user.getTelephone().length());
        }
    }

    // 检测版本更新
    private void checkVersion(){
        VersionUtils version = new VersionUtils(this);
        version.initVersion();
        version.checkAppVersion();
    }

    private void login(){
        Map<String,Object> params = new HashMap<>();
        User user = new User();
        user.setTelephone(userName.getText().toString());
        user.setPassword(passWord.getText().toString());
        params.put("user",user);
        JsonOkHttpUtils.getInstance().post(Host.LOGIN, params).executeJson(new JsonCallBack() {

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                loginText.setText("登录中");
                loading.showAndPlay();
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e("qzj", "onError:" + e.getMessage());
                hint.setText("登录失败");
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:" + response.toString());
                UserData userData = JSON.toJavaObject(response,UserData.class);
                if (userData.getResultCode() == 1){
                    ((MyApplication)getApplication()).setUser(userData.getUserData());
                    ((MyApplication)getApplication()).setSenders(userData.getAddrMores());
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, DeliverGoodsActivity.class);
                    startActivity(intent);
                }else{
                    hint.setText(userData.getResultMsg());
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                loginText.setText("登录");
                loading.hideAndStop();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            // 返回
            case R.id.toolbar_right:
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                break;

            // 忘记密码
            case R.id.login_forget_pw:
                intent.setClass(this,ForgetPwActivity.class);
                startActivity(intent);
                break;

            // 登陆
            case R.id.login_btn_login:
                login();
                break;
        }
    }

    @Override
    public void onStateChange(boolean isEnable) {
        loginText.setEnabled(isEnable);
    }
}
