package com.qzj.logistics.view.usercenter;

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
import com.qzj.logistics.base.BaseBean;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.bean.User;
import com.qzj.logistics.validation.EditTextValidator;
import com.qzj.logistics.validation.ValidationModel;
import com.qzj.logistics.validation.validator.EmailValidator;
import com.qzj.logistics.validation.validator.EmptyValidator;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.HashMap;
import java.util.Map;

public class SelfMsgUpdateActivity extends BaseActivity {

    private User user;
    private EditText userName;
    private EditText email;
    private RelativeLayout submit;
    private TextView submitText;
    private TextView hint;
    private DotsTextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_msg_update);
        Toolbar toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText("信息修改");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        initView();
    }

    private void initView(){
        userName = (EditText) findViewById(R.id.self_msg_user_name);
        email = (EditText) findViewById(R.id.self_msg_email);
        submit = (RelativeLayout) findViewById(R.id.self_msg_submit);
        submitText = (TextView) findViewById(R.id.self_msg_btn_text);
        hint = (TextView) findViewById(R.id.self_msg_hint);
        loading = (DotsTextView) findViewById(R.id.self_msg_loading);
        submit.setOnClickListener(this);

        user = ((MyApplication)getApplication()).getUser();
        initData();
        validate();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        if (user != null){
            userName.setText(user.getUser_name()==null?"":user.getUser_name());
            email.setText(user.getEmail()==null?"":user.getEmail());
        }
    }

    /**
     * 表单验证
     */
    private boolean validate(){
        // 初始化验证器
        EditTextValidator validator = new EditTextValidator(this, null);
        boolean result = validator.add(new ValidationModel("用户名", userName, hint, new EmptyValidator()))
                .add(new ValidationModel("Email", email, hint, new EmailValidator()))
                .executeOnce();

        return result;
    }

    private void updateMsg(){
        if (!validate()){
            hint.setText("信息填写有误");
            return;
        }
        boolean isUpdate = false;
        if (!userName.getText().toString().equals(user.getUser_name())) isUpdate = true;
        if (!email.getText().toString().equals(user.getEmail())) isUpdate = true;
        if (!isUpdate){
            hint.setText("你还未修改任何内容");
            return;
        }
        Map<String,Object> params = new HashMap<>();
        User sendUser = new User();
        sendUser.setUser_name(userName.getText().toString());
        sendUser.setEmail(email.getText().toString());
        sendUser.setUser_id(user.getUser_id());
        params.put("user",sendUser);
        JsonOkHttpUtils.getInstance().post(Host.USER_UPDATE, params).executeJson(new JsonCallBack() {

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                loading.showAndPlay();
                submitText.setText("正在修改");
            }

            @Override
            public void onError(Request request, Exception e) {
                Log.e("qzj", "onError:" + e.getMessage());
                hint.setText("修改失败");
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.e("qzj", "onResponse:" + response.toString());
                BaseBean baseBean = JSON.toJavaObject(response, BaseBean.class);
                if (baseBean.getResultCode() == 1) {
                    user.setUser_name(userName.getText().toString());
                    user.setEmail(email.getText().toString());
                    ((MyApplication) getApplication()).setUser(user);
                    hint.setText("修改成功");
                } else {
                    hint.setText(baseBean.getResultMsg());
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                loading.hideAndStop();
                submitText.setText("确定修改");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.toolbar_left:
                this.finish();
                break;

            // 提交
            case R.id.self_msg_submit:
                updateMsg();
                break;
        }
    }

}
