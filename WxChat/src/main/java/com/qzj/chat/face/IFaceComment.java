package com.qzj.chat.face;

import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2015/9/14.
 */
public interface IFaceComment {
    void sendImage(View v);
    void editClick(View v);
    void faceOpening(View v);
    void sendMessage(EditText v);
    void faceDelete(ChatEmoji emoji);
    void faceItemSelected(SpannableString spannableString);
}
