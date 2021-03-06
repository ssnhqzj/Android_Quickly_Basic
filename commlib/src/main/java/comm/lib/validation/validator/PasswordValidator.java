package comm.lib.validation.validator;

import android.content.Context;

import comm.lib.validation.ValidationExecutor;
import comm.lib.validation.ValidationModel;

/**
 * 密码一致性验证器
 */
public class PasswordValidator extends ValidationExecutor {

    private static final String PASSWORD_HINT = "两次输入的密码不一致";

    @Override
    public boolean doValidate(Context context, ValidationModel model) {
        EmptyValidator emptyValidator = new EmptyValidator();
        boolean isNotEmpty = emptyValidator.doValidateForRelate(context, model);
        if(isNotEmpty){
            String password = model.getEditText().getText().toString();
            String repeatPassword = model.getRelateEditText().getText().toString();
            if(password.equals(repeatPassword)){
                return true;
            }else{
                if (model.getHintView() != null)
                    model.getHintView().setText(PASSWORD_HINT);
            }
        }

        return false;
    }
}
