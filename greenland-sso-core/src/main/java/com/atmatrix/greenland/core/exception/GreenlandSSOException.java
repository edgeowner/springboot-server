package com.atmatrix.greenland.core.exception;

public class GreenlandSSOException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public GreenlandSSOException(String msg) {
        super(msg);
    }

    public GreenlandSSOException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public GreenlandSSOException(Throwable cause) {
        super(cause);
    }

}
