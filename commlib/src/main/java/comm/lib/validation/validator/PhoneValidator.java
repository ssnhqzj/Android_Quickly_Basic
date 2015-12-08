package comm.lib.validation.validator;

import android.content.Context;

import comm.lib.validation.ValidationExecutor;
import comm.lib.validation.ValidationModel;

/**
 * 手机号验证器
 */
public class PhoneValidator extends ValidationExecutor {
    @Override
    public boolean doValidate(Context context, ValidationModel model) {
        EmptyValidator emptyValidator = new EmptyValidator();
        boolean isEmpty = emptyValidator.doValidate(context,model);
        if(isEmpty){
            if(isPhoneNumber(model.getEditText().getText().toString())){
                return true;
            }else{
                if (model.getHintView() != null)
                    model.getHintView().setText(model.getModelName() + "格式不正确");
            }
        }

        return false;
    }
}
