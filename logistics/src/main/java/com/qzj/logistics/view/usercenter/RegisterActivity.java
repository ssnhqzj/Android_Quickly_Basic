package com.qzj.logistics.view.usercenter;

import android.content.Intent;
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
import com.qzj.logistics.bean.User;
import com.qzj.logistics.utils.TimeCount;
import com.qzj.logistics.validation.EditTextValidator;
import com.qzj.logistics.validation.ValidationModel;
import com.qzj.logistics.validation.utils.ValidationUtil;
import com.qzj.logistics.validation.validator.EmailValidator;
import com.qzj.logistics.validation.validator.EmptyValidator;
import com.qzj.logistics.validation.validator.PasswordValidator;
import com.qzj.logistics.validation.validator.PhoneValidator;
import com.qzj.logistics.validation.validator.PostCodeValidator;
import com.qzj.logistics.view.common.SelectLocationActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private TimeCount verCodeTimer;
    private LinearLayout root;
    private Button authCodeBtn;
    private EditText userName;
    private EditText phone;
    private EditText authCode;
    private EditText selectedLocation;
    private EditText locationDetail;
    private EditText email;
    private EditText postCode;
    private EditText pw;
    private EditText surePw;
    private TextView hint;
    private RelativeLayout commit;
    private TextView commitText;
    private DotsTextView loading;

    // 选择的城区id
    private String area_id;
    // 选择的城区父节点id
    private String area_father;
    // 獲取到的验证码
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(Color.TRANSPARENT);
        toolbar.setCenterText("注册新用户");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        initView();
    }

    private void initView(){
        root = (LinearLayout) findViewById(R.id.register_root);
        userName = (EditText) findViewById(R.id.register_user_name);
        phone = (EditText) findViewById(R.id.register_phone);
        authCode = (EditText) findViewById(R.id.register_put_auth_code);
        selectedLocation = (EditText) findViewById(R.id.register_location);
        locationDetail = (EditText) findViewById(R.id.register_location_detail);
        email = (EditText) findViewById(R.id.register_email);
        postCode = (EditText) findViewById(R.id.register_letter);
        pw = (EditText) findViewById(R.id.register_pw);
        surePw = (EditText) findViewById(R.id.register_pw_sure);
        hint = (TextView) findViewById(R.id.register_hint);
        commit = (RelativeLayout) findViewById(R.id.register_commit);
        authCodeBtn = (Button) findViewById(R.id.register_get_auth_code);
        commitText = (TextView) findViewById(R.id.register_btn_text);
        loading = (DotsTextView) findViewById(R.id.register_loading);
        selectedLocation.setOnClickListener(this);
        authCodeBtn.setOnClickListener(this);
        commit.setOnClickListener(this);

        verCodeTimer = new TimeCount(60000,1000);
        verCodeTimer.setListener(new TimeCount.OnTimeCountListener() {
            @Override
            public void onFinish() {
                authCodeBtn.setText("重新获取");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                authCodeBtn.setText(millisUntilFinished / 1000 + "秒");
            }
        });

        validate();
    }

    /**
     * 表单验证
     */
    private void validate(){
        // 初始化验证器
        EditTextValidator validator = new EditTextValidator(this,commit);
        validator.add(new ValidationModel("用户名", userName, hint, new EmptyValidator()))
                .add(new ValidationModel("电话号码", phone, hint, new PhoneValidator()))
                .add(new ValidationModel("验证码",authCode,hint,new EmptyValidator()))
                .add(new ValidationModel("城市区域",selectedLocation,hint,new EmptyValidator()))
                .add(new ValidationModel("详细地址", locationDetail, hint, new EmptyValidator()))
                .add(new ValidationModel("Email", email, hint, new EmailValidator()))
                .add(new ValidationModel("邮编", postCode, hint, new PostCodeValidator()))
                .add(new ValidationModel("密码", pw, surePw, hint, new PasswordValidator()).setRelateModelName("确认密码"))
                .execute();
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
                Toast.makeText(RegisterActivity.this, "无法获取验证码", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:VarCode:" + response.toString());
                code = response.getString("code");
            }
        });
    }

    /**
     * 提交表单
     */
    private void commit(){
        if (code == null) {
            setHint("重新获取验证码");
            return;
        }
        if (!code.equals(authCode.getText().toString())){
            setHint("验证码不正确");
            return;
        }
        Map<String,Object> params = new HashMap<>();
        User user = new User();
        user.setUser_name(userName.getText().toString());
        user.setTelephone(phone.getText().toString());
        user.setCid(area_father);
        user.setRid(area_id);
        user.setAddr(locationDetail.getText().toString());
        user.setEmail(email.getText().toString());
        user.setMail_code(postCode.getText().toString());
        user.setPassword(surePw.getText().toString());
        params.put("user",user);

        JsonOkHttpUtils.getInstance().post(Host.REGISTER, params).executeJson(new JsonCallBack() {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                commitText.setText("正在提交");
                loading.showAndPlay();
                commit.setClickable(false);
                isEnableAllEditState(false);
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e("qzj", "onError:" + e.getMessage());
                code = null;
                setHint("注册失败，请重试");
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:" + response.toString());
                BaseBean baseBean = JSON.toJavaObject(response,BaseBean.class);
                if (baseBean.getResultCode() == 1){
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    code = null;
                    setHint(baseBean.getResultMsg());
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                commitText.setText("注册");
                loading.hideAndStop();
                commit.setClickable(true);
                isEnableAllEditState(false);
            }
        });
    }

    /**
     * 是否开启EditText的编辑状态
     * @param isEnable
     */
    private void isEnableAllEditState(boolean isEnable){
        int childCount = root.getChildCount();
        if (isEnable){
            for (int i=0; i<childCount; i++){
                if (root.getChildAt(i) instanceof EditText){
                    ((EditText)root.getChildAt(i)).setFocusable(true);
                }
            }
        }else{
            for (int i=0; i<childCount; i++){
                if (root.getChildAt(i) instanceof EditText){
                    ((EditText)root.getChildAt(i)).setFocusable(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                this.finish();
                break;

            // 获取验证码
            case R.id.register_get_auth_code:
                if (phone.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this,"请填写电话号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!new ValidationUtil().isPhoneNumber(phone.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"电话号码格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                getVerCode(phone.getText().toString());
                verCodeTimer.start();
                break;

            // 选择区划
            case R.id.register_location:
                Intent locaIntent = new Intent();
                locaIntent.setClass(this, SelectLocationActivity.class);
                startActivityForResult(locaIntent,1);
                break;

            // 完成
            case R.id.register_commit:
                commit();
                break;
        }
    }

    private void setHint(String hintText){
        hint.setText(hintText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 返回区划
        if (resultCode == SelectLocationActivity.RESULT_CODE && data != null){
            selectedLocation.setText(data.getStringExtra("address"));
            area_id = data.getStringExtra("area_id");
            area_father = data.getStringExtra("area_father");
        }
    }
}
