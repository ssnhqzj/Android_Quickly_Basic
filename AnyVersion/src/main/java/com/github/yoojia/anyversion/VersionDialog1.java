package com.github.yoojia.anyversion;

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

public class VersionDialog1 extends Dialog {
    public static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private Context context;
    private LayoutInflater inflater = null;
    private View view = null;

    private TextView title;
    private TextView note;
    private TextView upload;
    private TextView cancel;

    public VersionDialog1(final Context context, final Version version, final Downloads downloads) {
        super(context, R.style.DialogTheme);
        this.context = context;
        if(view == null){
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.version_dialog1, null);

            title = (TextView) view.findViewById(R.id.version_dialog_title);
            note = (TextView) view.findViewById(R.id.version_dialog_note);
            upload = (TextView) view.findViewById(R.id.version_dialog_upload);
            cancel = (TextView) view.findViewById(R.id.version_dialog_cancel);

            title.setText(version.name);
            note.setMovementMethod(ScrollingMovementMethod.getInstance());
            note.setText(Html.fromHtml(version.note));
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloads.submit(context, version);
                    VersionDialog1.this.cancel();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VersionDialog1.this.cancel();
                }
            });
        }

        // 设置内容
        setContentView(view);
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // 设置宽度、高度、对齐方式
        params.width = DisplayUtils.getDisplayWidth(context)*4/5;
        params.height = WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public View getView(){
        return view;
    }
}
