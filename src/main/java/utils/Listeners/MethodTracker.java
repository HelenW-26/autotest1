package utils.Listeners;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.lang.reflect.Method;
import java.util.*;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.SkipException;
import newcrm.utils.testCaseDescUtils;
import utils.LogUtils;
import java.text.SimpleDateFormat;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.Base64;

public class MethodTracker {

    public static class MethodExecutionInfo {

        private String sMethodName;
        private String sMethodDisplayDesc;
        private Status logStatusRes;
        private Throwable messageLog;
        private String methodScreenshot; // 专属截图

        public MethodExecutionInfo(String sMethodName, String sMethodDisplayDesc, Status logStatusRes, Throwable messageLog) {
            this.sMethodName = sMethodName;
            this.sMethodDisplayDesc = sMethodDisplayDesc;
            this.logStatusRes = logStatusRes;
            this.messageLog = messageLog;
            this.methodScreenshot = "";
        }

        public String getMethodScreenshot() {
            return methodScreenshot;
        }

        public void setMethodScreenshot(String methodScreenshot) {
            this.methodScreenshot = methodScreenshot;
        }

        public String getMethodName() { return sMethodName; }
        public String getMethodDisplayDesc() { return sMethodDisplayDesc; }
        public Status getLogStatusRes() { return logStatusRes; }
        public Throwable getMessageLog() { return messageLog; }

    }

    private static final ThreadLocal<Map<Integer, MethodExecutionInfo>> executedMethods = ThreadLocal.withInitial(LinkedHashMap::new);
    private static final ThreadLocal<Boolean> bIsHasResultFail = ThreadLocal.withInitial(() -> false);

    // 重载方法：增加 WebDriver 参数，支持即时截图+水印
    public static void trackMethodExecution(Object instance, String sMethodName, boolean bRequireCallMethod, Status logStatusRes, WebDriver driver, Object... args) {
        Integer iNextRecCnt = executedMethods.get().size() + 1;
        String sMethodDisplayDesc = "";

        try {
            Method method = findMethod(instance.getClass(), sMethodName);
            if (method == null) {
                LogUtils.info("未找到方法：" + sMethodName);
                return;
            }

            Test testAnnotation = method.getAnnotation(Test.class);
            sMethodDisplayDesc = (testAnnotation != null && !testAnnotation.description().isEmpty()) ? testAnnotation.description() : testCaseDescUtils.sMethodDescNotAvailable;

            // 执行方法
            if (bRequireCallMethod) {
                method.invoke(instance, args);
                // 核心：方法执行成功后，立即截图（带水印）
                String screenshot = captureInstantScreenshotWithWatermark(driver, sMethodName);
                executedMethods.get().put(iNextRecCnt, new MethodExecutionInfo(sMethodName, sMethodDisplayDesc, Status.PASS, null));
                // 立即绑定截图
                executedMethods.get().get(iNextRecCnt).setMethodScreenshot(screenshot);
                return;
            }

            executedMethods.get().put(iNextRecCnt, new MethodExecutionInfo(sMethodName, sMethodDisplayDesc, logStatusRes, null));
            if (!bIsHasResultFail.get() && logStatusRes == Status.FAIL) {
                bIsHasResultFail.set(true);
            }
        }
        catch (SkipException e) {
            // 方法跳过，立即截图（带水印）
            String screenshot = captureInstantScreenshotWithWatermark(driver, sMethodName);
            executedMethods.get().put(iNextRecCnt, new MethodExecutionInfo(sMethodName, sMethodDisplayDesc, Status.SKIP, e.getCause()));
            executedMethods.get().get(iNextRecCnt).setMethodScreenshot(screenshot);
            if(driver!=null){
                LogUtils.info("方法 " + sMethodName + " 跳过，已生成带水印截图：" + e.getMessage());
            }
        }
        catch (Exception e) {
            // 方法失败，立即截图（带水印）
            String screenshot = captureInstantScreenshotWithWatermark(driver, sMethodName);
            Status status = e.getCause() instanceof SkipException ? Status.SKIP : Status.FAIL;
            executedMethods.get().put(iNextRecCnt, new MethodExecutionInfo(sMethodName, sMethodDisplayDesc, status, e.getCause()));
            executedMethods.get().get(iNextRecCnt).setMethodScreenshot(screenshot);
            if(driver!=null){
                LogUtils.info("方法 " + sMethodName + " 执行失败，已生成带水印截图：" + e.getMessage());

            }
            if (!bIsHasResultFail.get() && !(e.getCause() instanceof SkipException)) {
                bIsHasResultFail.set(true);
            }
        }
    }

    // 原有方法兼容（无 driver 时调用重载方法）
    public static void trackMethodExecution(Object instance, String sMethodName, boolean bRequireCallMethod, Status logStatusRes, Object... args) {
        trackMethodExecution(instance, sMethodName, bRequireCallMethod, logStatusRes, null, args);
    }

    // 核心：即时截图+水印注入（修复后，报告截图也带水印）
    private static String captureInstantScreenshotWithWatermark(WebDriver driver, String methodName) {
        // 1. 校验 driver 非空
        if (driver == null) {
            LogUtils.info("driver 实例为空，无法生成截图");
            return null;
        }

        try {
            // ========== 1. 注入时间戳水印 ==========
            ZoneId zoneId = ZoneId.systemDefault();
            LogUtils.info("TimeZone ID: " + zoneId);

            TimeZone timeZone = TimeZone.getDefault();
            int offsetMillis = timeZone.getRawOffset();
            int offsetHours = offsetMillis / (1000 * 60 * 60);
            String offset = (offsetHours >= 0 ? "+" : "") + offsetHours;
            LogUtils.info("UTC Offset: UTC" + offset);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String timestamp = sdf.format(new Date());
            String timeZoneName = TimeZone.getTimeZone("Asia/Shanghai").getDisplayName();

            // JS 注入水印元素（固定在左上角，半透明不遮挡内容）
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "if (!document.getElementById('screenshot-timestamp')) {" +
                            "    var timestampDiv = document.createElement('div');" +
                            "    timestampDiv.id = 'screenshot-timestamp';" +
                            "    timestampDiv.style.position = 'fixed';" +
                            "    timestampDiv.style.top = '0px';" +
                            "    timestampDiv.style.left = '0px';" +
                            "    timestampDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.3)';" + // 提高不透明度，确保可见
                            "    timestampDiv.style.color = 'white';" +
                            "    timestampDiv.style.padding = '8px 12px';" +
                            "    timestampDiv.style.fontSize = '18px';" +
                            "    timestampDiv.style.fontFamily = 'Arial, sans-serif';" +
                            "    timestampDiv.style.fontWeight = 'bold';" +
                            "    timestampDiv.style.zIndex = '999999';" +
                            "    timestampDiv.style.borderRadius = '0px';" +
                            "    timestampDiv.style.border = '2px solid red';" + // 加红色边框，更显眼
                            "    timestampDiv.style.pointerEvents = 'none';" + // 不影响页面点击
                            "    timestampDiv.innerText = '" + timeZoneName + " UTC+8 " + timestamp + "';" +
                            "    document.body.appendChild(timestampDiv);" +
                            "}"
            );

            // 等待水印渲染（延长到2秒，确保完全显示）
            Thread.sleep(2000);

            // ========== 2. 修复工作目录 ==========
            String workingDir = "";
            try {
                workingDir = vantagecrm.Utils.workingDir;
            } catch (Exception e) {
                workingDir = System.getProperty("user.dir"); //  fallback 到项目根目录
                LogUtils.info("vantagecrm.Utils.workingDir 未定义，使用项目根目录：" + workingDir);
            }

            // ========== 3. 生成唯一截图文件名 ==========
            SimpleDateFormat sdfFile = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
            String fileTimestamp = sdfFile.format(new Date());
            String screenshotFileName = methodName + "_instant_" + fileTimestamp + ".png";

            // ========== 4. 保存截图到本地（带水印） ==========
            Path screenshotDir = Paths.get(workingDir, "screenshots");
            Files.createDirectories(screenshotDir);
            Path screenshotPath = screenshotDir.resolve(screenshotFileName);

            // ========== 5. 截取带水印的页面并保存 ==========
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            if (screenshotBytes == null || screenshotBytes.length == 0) {
                LogUtils.info("获取截图字节流为空");
                return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
            }
            Files.write(screenshotPath, screenshotBytes);
            LogUtils.info("带水印截图生成成功：" + screenshotPath.toAbsolutePath() + "，大小：" + screenshotBytes.length + " 字节");

            // ========== 6. 删除水印元素（避免影响后续测试） ==========
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "var elem = document.getElementById('screenshot-timestamp');" +
                            "if (elem) elem.remove();"
            );

            // ========== 7. 核心修复：将本地带水印的截图文件转为 Base64（而非重新截图） ==========
            byte[] localScreenshotBytes = Files.readAllBytes(screenshotPath);
            String base64Screenshot = Base64.getEncoder().encodeToString(localScreenshotBytes);
            if (base64Screenshot == null || base64Screenshot.isEmpty()) {
                LogUtils.info("本地截图转 Base64 为空");
                return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
            }
            return "data:image/png;base64," + base64Screenshot;

        } catch (InterruptedException e) {
            LogUtils.error("水印等待线程中断：" + e.getMessage(),e);
            Thread.currentThread().interrupt();
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
        } catch (IOException e) {
            LogUtils.error("即时截图写入失败：" + e.getMessage(),e);
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
        } catch (ClassCastException e) {
            LogUtils.error("driver 无法转换为 TakesScreenshot：" + e.getMessage(),e);
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
        } catch (Exception e) {
            LogUtils.error("即时截图异常：" + e.getMessage(),e);
            return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
        }
    }

//    private static Method findMethod(Class<?> clazz, String methodName) {
//        while (clazz != null) {
//            try{
//                return clazz.getMethod(methodName);
//            } catch (NoSuchMethodException ignored) {
//            }
//            try{
//                return clazz.getMethod(methodName, Map.class);
//            } catch (NoSuchMethodException ignored) {
//            }
//            try{
//                return clazz.getMethod(methodName, String.class);
//            } catch (NoSuchMethodException ignored) {
//            }
//            try{
//                return clazz.getMethod(methodName, String.class, String.class);
//            } catch (NoSuchMethodException ignored) {
//            }
//            clazz = clazz.getSuperclass();
//        }
//        return null;
//    }
    private static Method findMethod(Class<?> clazz, String methodName) {
        // 首先尝试查找无参数方法
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException ignored) {
        }

        // 查找各种参数组合
        Class<?>[][] paramTypes = {
                {Map.class},
                {String.class},
                {String.class, String.class},
                {Map.class, String.class},
                {String.class, Map.class},
                {String.class, String.class, String.class},
                {Map.class, Map.class},
                // 可以根据需要添加更多参数组合
        };

        for (Class<?>[] paramType : paramTypes) {
            try {
                return clazz.getMethod(methodName, paramType);
            } catch (NoSuchMethodException ignored) {
            }
        }

        // 如果在当前类中没找到，尝试在父类中查找
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return findMethod(superClass, methodName);
        }

        return null;
    }


    public static Map<Integer, MethodExecutionInfo> getExecutedMethods() {
        return new HashMap<>(executedMethods.get());
    }

    public static Boolean getHasResultFail() {
        return bIsHasResultFail.get();
    }

    public static void clearExecutedMethods() {
        executedMethods.get().clear();
        bIsHasResultFail.set(false);
    }

    public static void checkResultFail() {
        if (MethodTracker.getHasResultFail()) {
            ITestResult result = Reporter.getCurrentTestResult();
            Allure.addAttachment("测试失败", "text/plain", String.valueOf(Reporter.getCurrentTestResult()));
            result.setStatus(org.testng.ITestResult.FAILURE);
        }
    }

}