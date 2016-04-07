package com.qzj.logistics.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;

/**
 * Created by Administrator on 2015/9/1.
 */
public class VersionUtils {

    private Context context;

    public VersionUtils(Context context){
        this.context = context;
    }

    /**
     * 初始化版本更新
     */
    public void initVersion(){
        AnyVersion.init(context, new VersionParser() {
            @Override
            public Version onParse(String response) {
                if (response == null || response.equals(""))
                    return null;

                JSONObject rootJson = JSON.parseObject(response);
                if (rootJson != null && rootJson.getIntValue("resultCode") == 1) {
                    JSONObject versionJson = rootJson.getJSONObject("edition");
                    if (versionJson == null)
                        return null;
                    ((MyApplication)context.getApplicationContext()).setHasVersionChange(
                            hasVersionUpdate(versionJson.getIntValue("edition"))
                    );
                    return new Version(
                            versionJson.getString("name"),
                            versionJson.getString("note"),
                            versionJson.getString("url"),
                            versionJson.getIntValue("edition")
                    );
                }

                return null;
            }
        });
    }

    /**
     * 检查版本更新
     */
    public void checkAppVersion(){
        AnyVersion version = AnyVersion.getInstance();
        version.setURL(Host.CHECK_VERSION_APK);
        version.check(NotifyStyle.Dialog);
    }

    /**
     * 检测是否有新版本
     * @param newCode
     * @return
     */
    private boolean hasVersionUpdate(int newCode){
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            if (pi.versionCode < newCode) return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VersionUtils", e.getMessage());
        }

        return false;
    }

}
