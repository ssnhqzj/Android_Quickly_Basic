package com.github.yoojia.anyversion;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
class Downloads {

    static final Set<Long> KEEPS = new HashSet<>();

    public void destroy(Context context){
        DownloadManager download = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        for (long id : KEEPS){
            download.remove(id);
            KEEPS.remove(id);
        }
    }

    public long submit(Context context, Version version){
        if(version == null){
            Toast.makeText(context,"目标版本信息缺失",Toast.LENGTH_SHORT).show();
            return 0;
        }

        if(version.URL == null || version.URL.equals("")){
            Toast.makeText(context,"下载路径错误",Toast.LENGTH_SHORT).show();
            return 0;
        }

        DownloadManager download = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(version.URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(version.name);
        long id = download.enqueue(request);
        KEEPS.add(id);

        return id;
    }
}
