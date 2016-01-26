package com.github.yoojia.anyversion;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.text.Html;
import android.view.WindowManager;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
class VersionDialog {

    private final AlertDialog dialog;

    public VersionDialog(final Application context, final Version version, final Downloads downloads) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Dialog)
                .setTitle(version.name)
                .setMessage(Html.fromHtml(version.note))
                .setCancelable(false)
                .setNegativeButton("稍后", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloads.submit(context, version);
                        dialog.cancel();
                    }
                })
                ;
        this.dialog = builder.create();

    }

    public void show(){
        this.dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        try{
            dialog.show();
        }catch (Exception e){
            throw new IllegalArgumentException("Required " +
                    "'<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />' !");
        }
    }
}
