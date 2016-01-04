package com.qzj.chat.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by qzj on 2015/12/8.
 */
public class DeviceUtils {

    public static final String TAG = "DevicesUtils";
    public static final int REQUEST_CODE_ALBUM = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final String CAMERA_PHOTO = "CAMERA_PHOTO";
    private static String currCropPath;

    public static void toggleSoftKeyBoard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param context
     */
    public static void hideSoftKeyBoard(Activity context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     * @param context
     */
    public static void showSoftKeyBoard(Activity context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(context.getCurrentFocus(), 0);
    }

    /**
     * 保存键盘高度
     * @param context
     * @param height
     */
    public static void saveKeyBroadHeight(Context context, int height){
        PreferenceUtils.getInstance(context).putIntValue("key_broad_height",height);
    }

    /**
     * 获取保存的键盘高度
     * @param context
     * @return
     */
    public static int getKeyBroadHeight(Context context){
        int height = PreferenceUtils.getInstance(context).getIntValue("key_broad_height",0);
        if (height == 0){
            height = DisplayUtils.getDisplayHeight(context)*2/5;
        }
        return height;
    }

    /**
     * 是否调整窗口
     * @param activity
     * @param isAdjust
     */
    public static void isAdjustWindow(Activity activity,boolean isAdjust){
        Log.e("qzj", "isAdjust:" + isAdjust);
        if (isAdjust){
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    |WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }else{
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                    |WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    /**
     * 判断当前触摸点是否在v上
     * @param ev
     * @param v
     * @return
     */
    public static boolean isTouchThisView(MotionEvent ev, View v) {
        if (v != null) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if ( ev.getY()>top && ev.getY()<bottom && ev.getX()>left && ev.getX()<right) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断是否点击了输入工具条以上的区域
     * 如果点击以上的区域隐藏输入键盘及表情图片面板
     * @param ev
     * @param v
     * @return
     */
    public static boolean isTouchSpaceArea(MotionEvent ev, View v) {
        if (v != null) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if ( ev.getY() < top) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 打开相册
     * @param context
     */
    public static void openAlbum(Context context) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        } catch (Exception e1) {
            try {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } catch (Exception e2) {
            }
        }

        if(intent != null){
            ((Activity)context).startActivityForResult(intent,REQUEST_CODE_ALBUM);
        }
    }

    /**
     * 打开相机
     * @param context
     */
    public static void openCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = StorageUtils.getImageUpDir(context);
            String photographUrl = file+"/" + new SimpleDateFormat("yyyymmdd_hhmmss").format(new Date()) +System.currentTimeMillis()+ ".png";
            PreferenceUtils.getInstance(context).putStringValue(CAMERA_PHOTO, photographUrl);
            Uri uri = Uri.fromFile(new File(photographUrl));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        if(intent != null){
            ((Activity)context).startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 获取返回的路径
     * @param requestCode
     * @param resultCode
     * @param data
     * @param context
     * @return
     */
    public static String getReturnImagePath(int requestCode, int resultCode, Intent data,Context context){
        String imgPath = "";
        if(!(context instanceof Activity)) return imgPath;
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == ((Activity)context).RESULT_OK) {
            Uri selectedImage = data.getData();
            if(selectedImage != null){
                String scheme = selectedImage.getScheme();
                // 部分手机scheme为file
                if(scheme.equals("file")){
                    imgPath = selectedImage.getPath();
                }else if(scheme.equals("content")){
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();
                }
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == ((Activity)context).RESULT_OK) {
            imgPath = PreferenceUtils.getInstance(context).getStringValue(CAMERA_PHOTO, "");
        }

        return imgPath;
    }

    /**
     * 打开切图
     * @param context
     * @param imgPath
     */
    public static void openImageCrop(Context context,String imgPath){
        currCropPath = null;
        if(!(context instanceof Activity)) return ;
        if(imgPath == null || "".equals(imgPath)) return ;

        File fileDir = StorageUtils.getImageUpDir(context);
        Uri inputUri = Uri.fromFile(new File(imgPath));
        String outputFileName = new SimpleDateFormat("yyyymmdd_hhmmss").format(new Date()) +System.currentTimeMillis()+ ".png";
        currCropPath = StorageUtils.getImageUpDir(context).getAbsolutePath()+File.separator+outputFileName;
        Uri outputUri = Uri.fromFile(new File(currCropPath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        //不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        ((Activity)context).startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public static String getCurrCropPath() {
        return currCropPath;
    }
}
