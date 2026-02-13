package newcrm.listeners;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import io.qameta.allure.model.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Pattern;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureTestListener implements ITestListener, IConfigurationListener {
    // 定义需要掩码的敏感参数名称
    
    private static final String[] SENSITIVE_KEYS = {"TraderPass", "password", "pwd", "secret"};
    
    // 用于记录失败的类
    private static final Set<String> FAILED_CLASSES = ConcurrentHashMap.newKeySet();
    
    // 定义匹配数字编号的正则表达式（允许行中任意位置出现数字编号）
    private static final Pattern STEP_PATTERN = Pattern.compile("\\d+\\.");

    @Override
    public void onStart(ITestContext context) {
        // 配置 Http 全局过滤器
        RestAssured.filters(
                new AllureHttpFilter()
        );

        System.out.println("测试套件初始化完成：Http过滤器已加载");
    }

    @Override
    public void onTestStart(ITestResult result) {
        // 直接从ITestResult获取测试方法
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        // 获取测试方法的@Test注解
        Test testAnnotation = method.getAnnotation(Test.class);

        // 获取description作为Allure报告标题
        String description = null;
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            description = testAnnotation.description();

            // 如果description是类似"{钱包帐户页面},{<details...}"的格式，提取第一部分
            if (description.startsWith("{") && description.contains("},")) {
                description = description.substring(1, description.indexOf("},"));
            }
        }

        // 如果没有description，使用方法名作为后备
        if (description == null || description.isEmpty()) {
            description = method.getName();
        }
        // 设置Allure报告标题
        final String finalDescription = description;
        Allure.getLifecycle().updateTestCase(test -> test.setName(finalDescription));
        
        // 将测试描述按照breakLine分割，每一行作为一个测试步骤
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            String fullDescription = testAnnotation.description();
            
            // 使用testCaseDescUtils中的breakLine常量值"<br/>"进行分割
            String[] steps = fullDescription.split("<br/>");
            for (String step : steps) {
                // 过滤掉HTML标签和空内容
                String cleanStep = step.replaceAll("<[^>]*>", "").trim();
                // 只提取包含数字编号的步骤（允许编号在行中任意位置出现）
                if (!cleanStep.isEmpty() && STEP_PATTERN.matcher(cleanStep).find()) {
                    // 进一步清理步骤描述，去除可能的前缀内容
                    String finalStep = cleanStep.replaceAll("^\\{[^}]*\\}\\s*,\\s*", "").replaceAll("^\\{[^1]*", "");
                    if (!finalStep.isEmpty()) {
                        Allure.step(finalStep);
                    }
                }
            }
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        maskSensitiveParameters(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        maskSensitiveParameters(result);
        // 添加截图逻辑
        takeScreenshotOnTest(result);
        // 记录失败的类
        String className = result.getTestClass().getName();
        FAILED_CLASSES.add(className);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        maskSensitiveParameters(result);
        takeScreenshotOnTest(result);
    }
    
    private void takeScreenshotOnTest(ITestResult result) {
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("driver");
        if (driver != null) {
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                // 添加截图附件
                Allure.addAttachment(result.getMethod().getMethodName() + "测试截图", "image/png", new ByteArrayInputStream(screenshot), "png");
            } catch (Exception e) {
                System.err.println("截图失败: " + e.getMessage());
            }
        }
    }
    
    private void maskSensitiveParameters(ITestResult result) {
        // 从测试上下文中获取XML参数
        ITestContext context = result.getTestContext();
        // 检查是否存在敏感参数
        boolean hasSensitiveParams = false;
        for (String key : SENSITIVE_KEYS) {
            if (context.getCurrentXmlTest().getParameter(key) != null) {
                hasSensitiveParams = true;
                break;
            }
        }
        // 只有当XML中存在敏感参数时才处理
        if (hasSensitiveParams) {
            Allure.getLifecycle().updateTestCase(testResult -> {
                List<Parameter> currentParams = testResult.getParameters();
                if (currentParams == null) {
                    currentParams = new ArrayList<>();
                }
                // 创建一个新的参数列表
                List<Parameter> updatedParams = new ArrayList<>(currentParams);
                // 遍历所有敏感参数名称
                for (String sensitiveKey : SENSITIVE_KEYS) {
                    // 检查XML中是否存在此参数
                    String paramValue = context.getCurrentXmlTest().getParameter(sensitiveKey);
                    if (paramValue != null) {
                        // 查找并替换参数
                        for (int i = 0; i < updatedParams.size(); i++) {
                            Parameter param = updatedParams.get(i);
                            if (sensitiveKey.equals(param.getName())) {
                                updatedParams.set(i, new Parameter().setName(sensitiveKey).setValue("******"));
                                break;
                            }
                        }
                    }
                }
                // 更新参数列表
                testResult.setParameters(updatedParams);
            });
        }
    }

    // 统一错误信息报告
    public void logTestFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            // 创建包含完整异常信息的附件
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);

            // 获取测试方法信息
            String methodName = result.getMethod().getMethodName();
            String className = result.getMethod().getTestClass().getName();

            // 构建详细错误报告
            String errorReport = "测试方法: " + className + "." + methodName + "\n" +
                    "异常类型: " + throwable.getClass().getName() + "\n" +
                    "异常信息: " + throwable.getMessage() + "\n" +
                    "堆栈轨迹:\n" + sw.toString() + "\n" +
                    "发生时间: " + new Date();

            // 添加到Allure报告
            Allure.step("测试执行失败", () -> {
                Allure.addAttachment("失败详情", "text/plain", errorReport);
            });
        }
    }
}