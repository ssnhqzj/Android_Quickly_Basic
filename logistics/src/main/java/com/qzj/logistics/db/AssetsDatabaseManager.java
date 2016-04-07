package com.qzj.logistics.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsDatabaseManager {

    private static final String TAG = "AssetsDatabase";

    // 外部数据库文件名称
    public static final String DATABASE_NAME = "logistics.db";

    // %s is packageName  ，//注意自己记得替换成自己的包
    private static String databasepath = "/data/data/com.qzj.logistics/databases";
    // Singleton Pattern
    private static AssetsDatabaseManager mInstance = null;
    // Context of application
    private Context context = null;

    private AssetsDatabaseManager(Context context) {
        this.context = context;
    }

    /**
     * Initialize AssetsDatabaseManager
     *
     * @param context, context of application
     */
    public static void initManager(Context context) {
        if (mInstance == null) {
            mInstance = new AssetsDatabaseManager(context);
        }
    }

    /**
     * Get a AssetsDatabaseManager object
     *
     * @return, if success return a AssetsDatabaseManager object, else return null
     */
    public static AssetsDatabaseManager getManager() {
        return mInstance;
    }

    /**
     * Get a assets database, if this database is opened this method is only return a copy of the opened database
     */
    public void createDatabase() {
        if (context == null) return;

        Log.i(TAG, String.format("Create database %s", DATABASE_NAME));

        String spath = getDatabaseFilepath();

        String sfile = getDatabaseFile(DATABASE_NAME);

        File file = new File(sfile);

        SharedPreferences dbs = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);

        // Get Database file flag, if true means this database file was copied and valid
        boolean flag = dbs.getBoolean(DATABASE_NAME, false);

        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                Log.i(TAG, "Create \"" + spath + "\" fail!");
                return;
            }

            if (!copyAssetsToFilesystem(DATABASE_NAME, sfile)) {
                Log.i(TAG, String.format("Copy %s to %s fail!", DATABASE_NAME, sfile));
                return;
            }

            dbs.edit().putBoolean(DATABASE_NAME, true).commit();
        } else {
            Log.e(TAG, "数据库已经存在此文件了");
        }
    }

    private String getDatabaseFilepath() {
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    public String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {

        Log.i(TAG, "Copy " + assetsSrc + " to " + des);

        InputStream istream = null;

        OutputStream ostream = null;

        try {

            AssetManager am = context.getAssets();

            istream = am.open(assetsSrc);

            ostream = new FileOutputStream(des);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = istream.read(buffer)) > 0) {

                ostream.write(buffer, 0, length);

            }

            istream.close();

            ostream.close();

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (istream != null)

                    istream.close();

                if (ostream != null)

                    ostream.close();

            } catch (Exception ee) {

                ee.printStackTrace();

            }

            return false;

        }

        return true;

    }
}
