package gzt.com.apptest.Chat.face;

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

}
