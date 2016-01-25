package gzt.com.apptest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ValidationActivity extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText repeatPassword;
    private TextView hint;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

//        name = (EditText) findViewById(R.id.name);
//        phone = (EditText) findViewById(R.id.phone);
//        password = (EditText) findViewById(R.id.password);
//        repeatPassword = (EditText) findViewById(R.id.repeat_password);
//        hint = (TextView) findViewById(R.id.hint);
//        submit = (Button) findViewById(R.id.submit);
//
//        EditTextValidator validator = new EditTextValidator(this,submit);
//        validator.add(new ValidationModel("姓名",name,hint,new EmptyValidator()))
//                .add(new ValidationModel("电话号码",phone,hint,new PhoneValidator()))
//                .add(new ValidationModel("密码",password,repeatPassword,hint,new PasswordValidator()).setRelateModelName("重复密码"))
//                .setButton(submit)
//                .execute();
    }

}
