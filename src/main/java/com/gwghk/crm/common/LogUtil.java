package com.gwghk.crm.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogUtil {
    public static final ThreadLocal<String> TRACK = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    //操作日志线程池
    public static final ExecutorService LOG_ACTION_TPOOL = Executors.newFixedThreadPool(10, new ThreadFactoryBuilder().setUncaughtExceptionHandler(LogUtil::uncaughtException).setNameFormat("action-pool-%d").build());
    //用户日志线程池
    public static final ExecutorService LOG_USER_TPOOL = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setUncaughtExceptionHandler(LogUtil::uncaughtException).setNameFormat("query-pool-%d").build());

    static {
        TRACK.set("system");
    }

    private static void uncaughtException(Thread t, Throwable e) {
        logger.error("记录日志异常" + t.getName(), e);
    }
}
