package com.base_util.error;

public class NetworkError extends Exception{
    private static final long serialVersionUID = 11548468L;

    public NetworkError() {
    }

    public NetworkError(String detailMessage) {
        super(detailMessage);
    }

    public NetworkError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkError(Throwable throwable) {
        super(throwable);
    }
}
