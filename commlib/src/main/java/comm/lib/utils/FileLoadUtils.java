package comm.lib.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.sql.SQLException;

import comm.lib.downloader.DownloadListener;
import comm.lib.downloader.DownloadManager;
import comm.lib.downloader.DownloadTask;
import comm.lib.downloader.dao.ISql;
import comm.lib.downloader.dao.ISqlImpl;

/**
 * Created by Administrator on 2015/8/18.
 */
public class FileLoadUtils {
    /**
     * 文件上传超时默认时间
     */
    private static final int DEFAULT_UP_TIMEOUT = 300000;
    /**
     * 默认下载文件保存路径
     * 从SD卡的根目录开始
     */
    private static final String DEFAULT_DOWN_DIR = "gjtFiles/images";

    private Context context;
    private static FileLoadUtils instance;
    private static AsyncHttpClient client;
    private static DownloadManager downloadManager;
    private static ISql iSql;

    private FileLoadUtils(Context context){
        this.context = context;
    }

    public static FileLoadUtils getInstance(Context context){
        if (instance==null){
            synchronized (FileLoadUtils.class){
                instance = new FileLoadUtils(context);
            }
        }

        return instance;
    }

    /**
     * 初始化上传
     * @return
     */
    public FileLoadUtils initUpLoader(){
        if(client == null){
            client = new AsyncHttpClient();
        }

        return this;
    }

    /**
     * 初始化下载
     * @return
     */
    public FileLoadUtils initDownLoader(){
        downloadManager = new DownloadManager(context);
        iSql = new ISqlImpl(context);

        return this;
    }

    /**
     * 上传
     * @param url
     * @param params
     * @param handler
     */
    public void upLoad(String url,RequestParams params,AsyncHttpResponseHandler handler){
        client.setTimeout(DEFAULT_UP_TIMEOUT);
        client.post(url, params, handler);
    }

    /**
     * 下载
     * @param url
     * @param listener
     */
    public void downLoad(String url, DownloadListener listener, String downLoadDir)  {
        if(url == null || "".equals(url)) return;
        if(listener == null) return;
        if(downLoadDir == null || "".equals(downLoadDir)){
            downLoadDir = DEFAULT_DOWN_DIR;
        }

        DownloadTask initTask = new DownloadTask(context);
        initTask.setUrl(url);
        initTask.setSTORE_PATH(downLoadDir);
        try {
            DownloadTask temptask = iSql.queryDownloadTask(initTask);
            if(temptask != null && temptask.isComplete()){
                listener.onFinish();
            }else{
                initTask.start(context,listener,false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询下载任务
     * @param task
     * @return
     */
    public DownloadTask queryDownLoadTask(DownloadTask task){
        try {
            DownloadTask temptask = iSql.queryDownloadTask(task);
            if(temptask != null && temptask.isComplete()){
                return temptask;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
