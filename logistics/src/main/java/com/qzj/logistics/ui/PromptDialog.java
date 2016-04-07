package com.qzj.logistics.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.yoojia.anyversion.DisplayUtils;
import com.qzj.logistics.R;

public class PromptDialog extends Dialog {
    public static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private Context context;
    private LayoutInflater inflater = null;
    private View view = null;

    private TextView note;
    private TextView sure;
    private TextView cancel;

    private String text;
    private PromptButtonListener listener;
    private Object tag;

    public PromptDialog(final Context context) {
        super(context, com.github.yoojia.anyversion.R.style.DialogTheme);
        this.context = context;
        if(view == null){
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.dialog_prompt, null);

            note = (TextView) view.findViewById(R.id.prompt_dialog_note);
            sure = (TextView) view.findViewById(R.id.prompt_dialog_sure);
            cancel = (TextView) view.findViewById(R.id.prompt_dialog_cancel);

            note.setMovementMethod(ScrollingMovementMethod.getInstance());
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSureClick(tag);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCancelClick(tag);
                }
            });
        }

        // 设置内容
        setContentView(view);
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、对齐方式
        params.width = DisplayUtils.getDisplayWidth(context)*4/5;
        params.height = WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void setText(String text) {
        this.text = text;
        note.setText(Html.fromHtml(text));
    }

    public View getView(){
        return view;
    }

    public void setListener(PromptButtonListener listener) {
        this.listener = listener;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public interface PromptButtonListener {
        public void onSureClick(Object tag);
        public void onCancelClick(Object tag);
    }
}
