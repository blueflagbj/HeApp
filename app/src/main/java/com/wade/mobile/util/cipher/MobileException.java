package com.wade.mobile.util.cipher;

import com.wade.mobile.util.Constant;

public class MobileException extends RuntimeException {
    private static final long serialVersionUID = -7839187636195662457L;
    private String message;
    private Throwable throwable;

    public MobileException(String detailMessage) {
        super(detailMessage);
        this.throwable = this;
    }

    public MobileException(Throwable throwable2) {
        super(throwable2);
        this.throwable = throwable2;
    }

    public MobileException(String detailMessage, Throwable throwable2) {
        super(detailMessage, throwable2);
        this.throwable = throwable2;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public String getDetailMessage() {
        if (this.message == null) {
            StringBuilder msg = new StringBuilder();
            appentStackTrace(msg.append(Constant.LINE_SEPARATOR));
            this.message = msg.toString();
        }
        return this.message;
    }

    private String getToString(Throwable e) {
        String message2;
        String s = e.getClass().getName();
        if (e instanceof MobileException) {
            message2 = ((MobileException) e).getMessage();
        } else {
            message2 = e.getLocalizedMessage();
        }
        return message2 != null ? s + ": " + message2 : s;
    }

    private void appentStackTrace(StringBuilder msg) {
        synchronized (msg) {
            msg.append(getToString(this.throwable)).append(Constant.LINE_SEPARATOR);
            StackTraceElement[] trace = this.throwable.getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                msg.append("\tat " + trace[i]).append(Constant.LINE_SEPARATOR);
            }
            Throwable ourCause = this.throwable.getCause();
            if (ourCause != null) {
                appentStackTraceAsCause(ourCause, msg, trace);
            }
        }
    }

    private void appentStackTraceAsCause(Throwable cause, StringBuilder msg, StackTraceElement[] causedTrace) {
        StackTraceElement[] trace = cause.getStackTrace();
        int m = trace.length - 1;
        int n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = (trace.length - 1) - m;
        msg.append("Caused by: " + getToString(cause)).append(Constant.LINE_SEPARATOR);
        for (int i = 0; i <= m; i++) {
            msg.append("\tat " + trace[i]).append(Constant.LINE_SEPARATOR);
        }
        if (framesInCommon != 0) {
            msg.append("\t... " + framesInCommon + " more").append(Constant.LINE_SEPARATOR);
        }
        Throwable ourCause = cause.getCause();
        if (ourCause != null) {
            appentStackTraceAsCause(ourCause, msg, trace);
        }
    }

}
