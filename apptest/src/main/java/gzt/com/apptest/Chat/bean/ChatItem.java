package gzt.com.apptest.Chat.bean;

import android.text.SpannableString;

import gzt.com.apptest.R;

/**
 * Created by qzj on 2015/12/7.
 */
public class ChatItem {
    // 方向--left
    public static final int DIRECTION_LEFT = 1;
    // 方向--right
    public static final int DIRECTION_RIGHT = 2;
    // 布局类型--左文本
    public static final int LAYOUT_LEFT_TEXT = R.layout.chat_item_left_text;
    // 布局类型--右文本
    public static final int LAYOUT_RIGHT_TEXT = R.layout.chat_item_right_text;
    // 布局类型--左图片
    public static final int LAYOUT_LEFT_IMAGE = R.layout.chat_item_left_image;
    // 布局类型--右图片
    public static final int LAYOUT_RIGHT_IMAGE = R.layout.chat_item_right_image;
    // 布局类型--左语音
    public static final int LAYOUT_LEFT_VOICE = 5;
    // 布局类型--右语音
    public static final int LAYOUT_RIGHT_VOICE = 6;

    // item id
    private int itemId;

    // item方向，左或右
    private int direction;

    // item布局类型
    private int layoutType;

    // 文本内容
    private SpannableString text;

    // 图片url
    private String imageUrl;

    // 语音url
    private String voiceUrl;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public SpannableString getText() {
        return text;
    }

    public void setText(SpannableString text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "itemId=" + itemId +
                ", direction=" + direction +
                ", layoutType=" + layoutType +
                ", text=" + text +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceUrl='" + voiceUrl + '\'' +
                '}';
    }
}
