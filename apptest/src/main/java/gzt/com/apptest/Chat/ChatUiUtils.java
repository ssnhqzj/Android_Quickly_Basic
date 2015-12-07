package gzt.com.apptest.Chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by qzj on 2015/12/5.
 */
public class ChatUiUtils {
    // 文本
    public static final int CHAT_ITEM_TYPE_TEXT = 1;
    // 图片
    public static final int CHAT_ITEM_TYPE_IMAGE = 2;
    // 声音
    public static final int CHAT_ITEM_TYPE_VOICE = 3;

    public static View createLeftItem(Context context,ChatItemConfig config){
        switch (config.type){
            case CHAT_ITEM_TYPE_TEXT:

                ChatItemLayout rootLayout = createTextRootView(context,config.bgId,config.coveringAlpha,config.coveringColor);

                // 头像
                rootLayout.addView(createHeadView(context, config.srcId));

                // 文本
                rootLayout.addView(createTextView(context,config.text));

                return rootLayout;

            case CHAT_ITEM_TYPE_IMAGE:

                break;

            case CHAT_ITEM_TYPE_VOICE:

                break;
        }

        return null;
    }

    public static View createRightItem(Context context,ChatItemConfig config){
        switch (config.type){
            case CHAT_ITEM_TYPE_TEXT:

                ChatItemLayout rootLayout = createTextRootView(context,config.bgId,config.coveringAlpha,config.coveringColor);

                // 文本
                rootLayout.addView(createTextView(context, config.text));

                // 头像
                rootLayout.addView(createHeadView(context,config.srcId));

                return rootLayout;

            case CHAT_ITEM_TYPE_IMAGE:

                break;

            case CHAT_ITEM_TYPE_VOICE:

                break;
        }

        return null;
    }

    private static ChatItemLayout createTextRootView(Context context,int bgId,int coveringAlpha,int coveringColor){
        ChatItemLayout rootLayout = new ChatItemLayout(context)
                .setBgId(bgId)
                .setCoveringAlpha(coveringAlpha)
                .setCoveringColor(coveringColor);
        rootLayout.setOrientation(ChatItemLayout.HORIZONTAL);

        return rootLayout;
    }

    private static ImageView createHeadView(Context context,int srcId){
        ImageView head = new ImageView(context);
        head.setImageResource(srcId);
        LinearLayout.LayoutParams headParams = new LinearLayout.LayoutParams(60,60);
        head.setLayoutParams(headParams);

        return head;
    }

    private static TextView createTextView(Context context,String text){
        TextView textView = new TextView(context);
        textView.setText(text);

        return textView;
    }

}
