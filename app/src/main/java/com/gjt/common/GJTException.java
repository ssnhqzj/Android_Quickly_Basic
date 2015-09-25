package com.gjt.common;

/**
 * Created by Administrator on 2015/6/24.
 */
public class GJTException extends Throwable {
    private static final long serialVersionUID = -3387516993124229948L;

    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public GJTException() {
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public GJTException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage
     *            the detail message for this exception.
     * @param throwable
     *            the cause of this exception.
     */
    public GJTException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable
     *            the cause of this exception.
     */
    public GJTException(Throwable throwable) {
        super(throwable);
    }
}
