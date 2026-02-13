package utils;

import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;

public class CustomAssert {
    // 使用静态ThreadLocal保证多线程安全
    private static final ThreadLocal<List<String>> errors = ThreadLocal.withInitial(ArrayList::new);

    // 基础断言（硬断言，失败立即抛出）
    public static void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }
    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }
    // 软断言：记录失败但不立即抛出
    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (AssertionError e) {
            addError(formatError("assertEquals", actual, expected, message, e));
        }
    }

    public static void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
        } catch (AssertionError e) {
            addError(formatError("assertTrue", condition, true, message, e));
        }
    }

    // 统一汇总所有断言结果
    public static void assertAll() {
        List<String> errorList = errors.get();
        if (!errorList.isEmpty()) {
            String fullError = String.join("\n", errorList);
            errors.remove(); // 清除当前线程的错误记录
            throw new AssertionError("Accumulated errors:\n" + fullError);
        }
    }

    // 私有方法：格式化错误信息
    private static String formatError(String methodName, Object actual, Object expected, String message, AssertionError e) {
        return String.format(
                "[%s Failed] %s\n" +
                        "  Actual  : %s\n" +
                        "  Expected: %s\n" +
                        "  Details : %s",
                methodName, message, actual, expected, e.getMessage()
        );
    }

    // 私有方法：记录错误
    private static void addError(String error) {
        errors.get().add(error);
    }

    public static void fail(String s) {
        Assert.fail(s);
    }
}