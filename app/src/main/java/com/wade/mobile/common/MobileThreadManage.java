package com.wade.mobile.common;


import java.util.HashMap;
import java.util.Map;

public class MobileThreadManage {
    private static MobileThreadManage manage;
    private HashMap<String, MobileThread> cache = new HashMap<>();
    private int cacheCount = 0;

    private MobileThreadManage() {
    }

    public static MobileThreadManage getInstance() {
        if (manage == null) {
            manage = new MobileThreadManage();
        }
        return manage;
    }

    public synchronized String addThread(String threadName, MobileThread thread) {
        if (this.cache.get(threadName) != null) {
            StringBuilder append = new StringBuilder().append(threadName);
            int i = this.cacheCount;
            this.cacheCount = i + 1;
            threadName = append.append(i).toString();
        }
        this.cache.put(threadName, thread);
        return threadName;
    }

    public synchronized Thread getThread(String threadKey) {
        return this.cache.get(threadKey);
    }

    public synchronized void remove(String threadName, String threadKey) {
        HashMap<String, MobileThread> hashMap = this.cache;
        if (threadKey != null) {
            threadName = threadKey;
        }
        hashMap.remove(threadName);
    }

    public synchronized void destroy() {
        for (Map.Entry<String, MobileThread> entry : this.cache.entrySet()) {
            if (entry.getValue().isAlive()) {
                entry.getValue().interrupt();
            }
        }
        this.cache.clear();
    }
}