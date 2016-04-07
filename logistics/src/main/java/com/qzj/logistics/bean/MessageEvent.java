package com.qzj.logistics.bean;

/**
 * EventBus消息传递bean
 */
public class MessageEvent {

    /**
     * 发件人地址编辑
     */
    public static final int FLAG_SENDER_ADDRESS = 1;

    public final int message;

    public MessageEvent(int flag) {
        this.message = flag;
    }
}
