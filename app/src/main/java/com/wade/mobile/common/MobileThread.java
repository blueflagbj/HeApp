package com.wade.mobile.common;


import com.wade.mobile.util.Messages;

public abstract class MobileThread extends Thread {
    private static MobileThreadManage manage = MobileThreadManage.getInstance();
    /* access modifiers changed from: private */
    public boolean flag;
    private Thread thread;
    /* access modifiers changed from: private */
    public String threadName;
    /* access modifiers changed from: private */
    public long waitoutTime;

    /* access modifiers changed from: protected */
    public abstract void execute() throws Exception;

    public MobileThread(String threadName2) {
        this.waitoutTime = -1;
        this.flag = true;
        this.threadName = threadName2;
    }

    public MobileThread(String threadName2, long waitoutTime2) {
        this(threadName2);
        this.waitoutTime = waitoutTime2;
    }

    public final void run() {
        String threadKey = null;
        try {
            if (this.waitoutTime > 0) {
                this.thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(MobileThread.this.waitoutTime);
                            }
                            if (MobileThread.this.isAlive()) {
                                MobileThread.this.interrupt();
                                boolean unused = MobileThread.this.flag = false;
                                System.out.println(MobileThread.this.threadName + Messages.THREAD_STOP);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                });
            }

            threadKey = manage.addThread(this.threadName, this);
            if (this.thread != null) {
                this.thread.start();
            }
            execute();
            if (this.thread != null) {
                this.thread.interrupt();
            }
        } catch (Exception e) {
            error(e);
        } finally {
            manage.remove(this.threadName, threadKey);
            callback(this.flag);
        }
    }

    /* access modifiers changed from: protected */
    public void callback(boolean flag2) {
    }

    /* access modifiers changed from: protected */
    public void error(Exception e) {
        e.printStackTrace();
    }

    public static void main(String[] args) {
    }
}