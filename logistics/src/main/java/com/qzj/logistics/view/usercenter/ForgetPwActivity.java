package com.qzj.logistics.view.usercenter;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.loading.DotsTextView;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.utils.TimeCount;
import com.qzj.logistics.validation.EditTextValidator;
import com.qzj.logistics.validation.ValidationModel;
import com.qzj.logistics.validation.utils.ValidationUtil;
import com.qzj.logistics.validation.validator.EmptyValidator;
import com.qzj.logistics.validation.validator.PasswordValidator;
import com.qzj.logistics.validation.validator.PhoneValidator;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.HashMap;
import java.util.Map;

public class ForgetPwActivity extends BaseActivity {

    private EditText phone;
    private EditText authCode;
    private EditText pw;
    private EditText surePw;
    private RelativeLayout submit;
    private TextView submitBtn;
    private TextView hint;
    private String code;
    private Button getAuthCode;
    private LinearLayout root;
    private TextView successHint;
    private TimeCount verCodeTimer;
    private DotsTextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(Color.TRANSPARENT);
        toolbar.setCenterText("找回密码");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        initView();
    }

    private void initView(){
        phone = (EditText) findViewById(R.id.forget_pw_phone);
        authCode = (EditText) findViewById(R.id.put_auth_code);
        pw = (EditText) findViewById(R.id.forget_pw_pw);
        surePw = (EditText) findViewById(R.id.forget_pw_sure_pw);
        submit = (RelativeLayout) findViewById(R.id.forget_pw_commit);
        hint = (TextView) findViewById(R.id.forget_pw_hint);
        getAuthCode = (Button) findViewById(R.id.get_auth_code);
        root = (LinearLayout) findViewById(R.id.forget_pw_root);
        successHint = (TextView) findViewById(R.id.forget_pw_success_hint);
        submitBtn = (TextView) findViewById(R.id.forget_pw_commit_btn_text);
        loading = (DotsTextView) findViewById(R.id.forget_pw_loading);
        getAuthCode.setOnClickListener(this);
        submit.setOnClickListener(this);

        // 初始化验证器
        EditTextValidator validator = new EditTextValidator(this,submit);
        validator.add(new ValidationModel("电话号码", phone, hint, new PhoneValidator()))
                .add(new ValidationModel("验证码",authCode,hint,new EmptyValidator()))
                .add(new ValidationModel("密码", pw, surePw, hint, new PasswordValidator()).setRelateModelName("确认密码"))
                .execute();

        verCodeTimer = new TimeCount(60000,1000);
        verCodeTimer.setListener(new TimeCount.OnTimeCountListener() {
            @Override
            public void onFinish() {
                getAuthCode.setText("重新获取");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                getAuthCode.setText(millisUntilFinished / 1000 + "秒");
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerCode(String phone){
        Map<String,Object> params = new HashMap<>();
        params.put("telephone",phone);
        JsonOkHttpUtils.getInstance().post(Host.GET_VAR_CODE, params).executeJson(new JsonCallBack() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ForgetPwActivity.this, "无法获取验证码", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:VarCode:" + response.toString());
                code = response.getString("code");
            }
        });
    }

    private void resetPw(){
        if (code == null) {
            setHint("重新获取验证码");
            return;
        }
        if (!code.equals(authCode.getText().toString())){
            setHint("验证码不正确");
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("telephone", phone.getText().toString());
        params.put("password",surePw.getText().toString());
        JsonOkHttpUtils.getInstance().post(Host.RESET_PW, params).executeJson(new JsonCallBack() {

            @Override
            public void onBefore(Request request) {
                loading.showAndPlay();
                submit.setClickable(false);
                submitBtn.setText("提交修改");
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e("qzj", "onError:" + e.getMessage());
                hint.setText("密码修改失败,请重试");
                code = null;
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:" + response.toString());
                BaseBean baseBean = JSON.toJavaObject(response, BaseBean.class);
                if (baseBean != null && baseBean.getResultCode()==1){
                    root.setVisibility(View.GONE);
                    successHint.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAfter() {
                loading.hideAndStop();
                submit.setClickable(true);
                submitBtn.setText("完成");
            }
        });
    }

    private void setHint(String hintText){
        hint.setText(hintText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.toolbar_left:
                this.finish();
                break;

            // 获取验证码
            case R.id.get_auth_code:
                if (phone.getText().toString().equals("")) {
                    Toast.makeText(ForgetPwActivity.this,"请填写电话号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!new ValidationUtil().isPhoneNumber(phone.getText().toString())){
                    Toast.makeText(ForgetPwActivity.this,"电话号码格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                getVerCode(phone.getText().toString());
                verCodeTimer.start();
                break;

            // 提交
            case R.id.forget_pw_commit:
                resetPw();
                break;
        }
    }

}
