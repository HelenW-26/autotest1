package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {

    // 获取调用者的类
    private static Class<?> getCallerClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 跳过 getStackTrace, getCallerClass, info/error 方法
        for (int i = 3; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(Thread.class.getName()) &&
                    !className.equals(LogUtils.class.getName())) {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("无法加载类: " + className, e);
                }
            }
        }
        throw new IllegalStateException("无法识别调用者类");
    }

    // 获取调用者的位置信息
    private static StackTraceElement getCallerStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 跳过 getStackTrace, getCallerStackTrace, info/error 方法
        for (int i = 3; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(Thread.class.getName()) &&
                    !className.equals(LogUtils.class.getName())) {
                return stackTrace[i];
            }
        }
        throw new IllegalStateException("无法识别调用者类");
    }

    public static void info(String message) {
        Class<?> callerClass = getCallerClass();
        StackTraceElement callerTrace = getCallerStackTrace();
        Logger logger = LogManager.getLogger(callerClass);
        // 使用 withLocation(StackTraceElement) 指定具体位置
        logger.atInfo().withLocation(callerTrace).log(message);
    }

    public static void error(String message, Throwable throwable) {
        Class<?> callerClass = getCallerClass();
        StackTraceElement callerTrace = getCallerStackTrace();
        Logger logger = LogManager.getLogger(callerClass);
        // 使用 withLocation(StackTraceElement) 指定具体位置
        logger.atError().withLocation(callerTrace).log(message, throwable);
    }
}
