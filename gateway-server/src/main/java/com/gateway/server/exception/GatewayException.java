package com.gateway.server.exception;

public class GatewayException extends RuntimeException {

    private static final long serialVersionUID = 8068509879445395353L;

    /**
     * Instantiates a new Shenyu exception.
     *
     * @param e the e
     */
    public GatewayException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Shenyu exception.
     *
     * @param message the message
     */
    public GatewayException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Shenyu exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public GatewayException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
