package com.qzj.logistics.utils;

/**
 * Created by Administrator on 2015/12/30.
 * EditText抖动效果
 * 调用方法：new EditTextShakeHelper(getActivity()).shake(editText);
 */
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class EditTextShakeHelper {

    private Animation shakeAnimation;

    private CycleInterpolator cycleInterpolator;

    private Vibrator shakeVibrator;

    public EditTextShakeHelper(Context context) {
        shakeVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(300);
        cycleInterpolator = new CycleInterpolator(8);
        shakeAnimation.setInterpolator(cycleInterpolator);
    }

    public void shake(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.startAnimation(shakeAnimation);
        }
        shakeVibrator.vibrate(new long[]{0, 500}, -1);
    }

}

