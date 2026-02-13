package utils.Listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;

import vantagecrm.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.Listeners.MethodTracker.MethodExecutionInfo;
import newcrm.utils.testCaseDescUtils;
import utils.LogUtils;

public class GroupTestListener implements ITestListener, ISuiteListener {

    private static Path reportPath;
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extentR = new ExtentReports();

    static {
        try {
            String workingDir = Utils.workingDir;
            reportPath = Paths.get(workingDir, "ExtentReports", "ExtentReportResults.html");
        } catch (Exception e) {
            String defaultWorkingDir = System.getProperty("user.dir");
            reportPath = Paths.get(defaultWorkingDir, "ExtentReports", "ExtentReportResults.html");
            LogUtils.info("Utils.workingDir 获取失败，使用默认项目根目录：" + defaultWorkingDir);
        }

        try {
            Files.createDirectories(reportPath.getParent());
        } catch (IOException e) {
            LogUtils.error("创建报告目录失败：" + e.getMessage(), e);
        }

        sparkReporter = new ExtentSparkReporter(reportPath.toString());
        sparkReporter.config().setDocumentTitle("Automation Report");
        extentR.attachReporter(sparkReporter);
    }

    private static ExtentTest extentT;
    public static WebDriver driver;

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("---------------------------------------------------");
        System.out.println("Method " + result.getName() + " Start");
        // 非分组场景强制初始化extentT，确保报告写入
        if (extentT == null) {
            extentT = extentR.createTest(result.getTestContext().getName(), "Test Start (Non-Group Scenario)");
            LogUtils.info("非分组场景，强制初始化extentT");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sMethodName = getMethodName(result);
        generateMethodDisplayDesc(result, sMethodName, Status.PASS);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sMethodName = getMethodName(result);
        generateMethodDisplayDesc(result, sMethodName, Status.FAIL);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sMethodName = getMethodName(result);
        generateMethodDisplayDesc(result, sMethodName, Status.SKIP);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Method " + result.getName() + " Failed but Within Success Percentage");
        if (extentT == null) {
            extentT = extentR.createTest(result.getTestContext().getName(), "Test Start (Fallback)");
        }
        extentT.log(Status.WARNING, "Test is failed but within Success Percentage: " + result.getName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String sMethodName = getMethodName(result);
        generateMethodDisplayDesc(result, sMethodName, Status.FAIL);
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test " + context.getName() + " Start");

        Set<String> includedGroups = new HashSet<>(Arrays.asList(context.getIncludedGroups()));

        boolean hasMatchingMethod = Arrays.stream(context.getAllTestMethods())
                .flatMap(method -> Arrays.stream(method.getGroups()))
                .anyMatch(includedGroups::contains);

        if (hasMatchingMethod) {
            extentT = extentR.createTest(context.getName(), "Test Start");
        } else {
            System.out.println("No matching groups for test: " + context.getName() + " (will init on test start)");
        }
    }

    @Override
    public void onStart(ISuite suite) {
        // Set report name
        String brand = suite.getParameter("Brand");
        brand = (brand != null && !brand.isEmpty()) ? brand : "";

        String suiteName = suite.getName();
        suiteName = (suiteName != null && !suiteName.isEmpty()) ? suiteName : "";

        String reportName = (brand.isEmpty() && suiteName.isEmpty()) ? "Automation Report" : brand + (!suiteName.isEmpty() ? " " : "") + suiteName;

        if (sparkReporter != null) {
            sparkReporter.config().setReportName(reportName);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test " + context.getName() + " End");

        if (extentR != null) {
            extentR.flush();
            LogUtils.info("Extent Report 已保存至：" + reportPath.toAbsolutePath());
        }

        MethodTracker.clearExecutedMethods();
        extentT = null;
    }

    public static void reportLog(String message, String screenshotPath) {
        if (extentT == null) {
            LogUtils.info("extentT 未初始化，尝试临时初始化后记录日志");
            ITestResult result = Reporter.getCurrentTestResult();
            if (result != null) {
                extentT = extentR.createTest(result.getTestContext().getName(), "Temporary Test");
            } else {
                extentT = extentR.createTest("Unknown Test", "Temporary Test");
            }
        }

        if (message != null && !message.isEmpty()) {
            extentT.log(Status.INFO, message);
        }

        if (screenshotPath != null && !screenshotPath.isEmpty() && !screenshotPath.equals("data:image/png;base64,")) {
            extentT.log(Status.FAIL, "Screenshot Attached").addScreenCaptureFromPath(screenshotPath);
        }
    }

    private String getMethodName(ITestResult result) {
        if (result == null || result.getTestContext() == null) {
            LogUtils.info("ITestResult或TestContext为空，无法获取方法名");
            return "Unknown Method";
        }
        if (result.getTestContext().getAttributeNames().contains("promotion")) {
            Object promotionObj = result.getTestContext().getAttribute("promotion");
            return promotionObj != null ? promotionObj.toString() : "Unknown Method";
        } else {
            return result.getName() != null ? result.getName() : "Unknown Method";
        }
    }

    private void generateMethodDisplayDesc(ITestResult result, String sMethodName, Status logStatusRes) {
        if (result == null || sMethodName == null || sMethodName.isEmpty()) {
            LogUtils.info("generateMethodDisplayDesc 入参无效，跳过执行");
            return;
        }

        if (extentT == null) {
            extentT = extentR.createTest(result.getTestContext().getName(), "Test Start (Fallback)");
        }

        Map<Integer, MethodExecutionInfo> executedMethods = MethodTracker.getExecutedMethods();
        boolean bRequireCallMethod = true;

        // Applicable for 单层API call.
        // E.g. In XML file declare as <include name="testWEBApiDeposit"></include>. From testWEBApiDeposit, it calls apiBitWalletDepositNew and apiJapanBTDepositNew methods.
        // Normal practice in xml file will call individual separate test case.
        // E.g. In XML file declare as <include name="apiBitWalletDepositNew"></include><include name="apiJapanBTDepositNew"></include>
        if (executedMethods.isEmpty()) {
            MethodTracker.trackMethodExecution(result.getInstance(), sMethodName, false, logStatusRes);
            executedMethods = MethodTracker.getExecutedMethods();
            bRequireCallMethod = false;
        }

        if (executedMethods == null || executedMethods.isEmpty()) {
            LogUtils.info("executedMethods 为空，无需生成方法描述");
            return;
        }

        StringBuilder methodDesc = new StringBuilder();
        ITestContext testContext = result.getTestContext();
        // 判断是否有driver传入（核心：无driver则走旧版截图）
        boolean hasDriver = testContext != null && testContext.getAttributeNames().contains("driver");

        for (Map.Entry<Integer, MethodExecutionInfo> entry : executedMethods.entrySet()) {
            methodDesc.setLength(0);
            MethodExecutionInfo methodInfo = entry.getValue();

            if (methodInfo == null) {
                LogUtils.info("MethodExecutionInfo 为空，跳过当前循环");
                continue;
            }

            logStatusRes = methodInfo.getLogStatusRes();
            Throwable errorCause = bRequireCallMethod ? methodInfo.getMessageLog() : result.getThrowable();

            String sStatusDesc;
            Status logStatus;
            switch (logStatusRes) {
                case PASS:
                    sStatusDesc = "Success";
                    logStatus = Status.PASS;
                    break;
                case SKIP:
                    sStatusDesc = "Skipped";
                    logStatus = Status.SKIP;
                    break;
                default:
                    sStatusDesc = "Failed";
                    logStatus = Status.FAIL;
                    break;
            }

            // 方法标题和描述处理（不变）
            String methodDisplayDesc = methodInfo.getMethodDisplayDesc();
            String[] lstMethodDesc = (methodDisplayDesc != null) ?
                    methodDisplayDesc.split(",(?=(?:[^}]*\\{[^}]*\\})*[^}]*$)+") : new String[0];
            String sMethodDescNotAvailable = testCaseDescUtils.generateProcessFlowNotAvaiDesc();

            String sMethodDisplayTitle;
            String sMethodDisplayDesc;
            if (testCaseDescUtils.sMethodDescNotAvailable.equals(methodDisplayDesc)) {
                sMethodDisplayTitle = methodInfo.getMethodName() != null ? methodInfo.getMethodName() : "Unknown Method";
                sMethodDisplayDesc = sMethodDescNotAvailable;
            } else if (lstMethodDesc.length == 1) {
                sMethodDisplayTitle = methodInfo.getMethodName() != null ? methodInfo.getMethodName() : "Unknown Method";
                lstMethodDesc[0] = (lstMethodDesc[0] != null) ? lstMethodDesc[0].trim() : "";
                sMethodDisplayDesc = (lstMethodDesc[0].startsWith("{") && lstMethodDesc[0].endsWith("}")) ?
                        lstMethodDesc[0].substring(1, lstMethodDesc[0].length() - 1).trim() : "";
                sMethodDisplayDesc = sMethodDisplayDesc.isEmpty() ? sMethodDescNotAvailable : sMethodDisplayDesc;
            } else if (lstMethodDesc.length >= 2) {
                lstMethodDesc[0] = (lstMethodDesc[0] != null) ? lstMethodDesc[0].trim() : "";
                lstMethodDesc[1] = (lstMethodDesc[1] != null) ? lstMethodDesc[1].trim() : "";
                sMethodDisplayTitle = (lstMethodDesc[0].startsWith("{") && lstMethodDesc[0].endsWith("}")) ?
                        lstMethodDesc[0].substring(1, lstMethodDesc[0].length() - 1).trim() : "";
                String methodName = methodInfo.getMethodName() != null ? methodInfo.getMethodName() : "Unknown Method";
                sMethodDisplayTitle = sMethodDisplayTitle.isEmpty() ? methodName : sMethodDisplayTitle + " (" + methodName + ")";
                sMethodDisplayDesc = (lstMethodDesc[1].startsWith("{") && lstMethodDesc[1].endsWith("}")) ?
                        lstMethodDesc[1].substring(1, lstMethodDesc[1].length() - 1).trim() : "";
                sMethodDisplayDesc = sMethodDisplayDesc.isEmpty() ? sMethodDescNotAvailable : sMethodDisplayDesc;
            } else {
                sMethodDisplayTitle = "Unknown Method";
                sMethodDisplayDesc = sMethodDescNotAvailable;
            }

            if (logStatusRes != Status.FAIL && logStatusRes != Status.SKIP) {
                sMethodDisplayDesc = (sMethodDisplayDesc != null) ?
                        sMethodDisplayDesc.replaceAll("</div>\\s*<br\\s*/?>\\s*</details>", "</div>\n</details>") : "";
            }

            methodDesc.append(sMethodDisplayDesc);
            LogUtils.info("Method " + methodInfo.getMethodName() + " " + sStatusDesc);

            // ========== 核心改造：无driver → 旧版手动截图；有driver → 优先水印截图 ==========
            if (logStatusRes == Status.FAIL || logStatusRes == Status.SKIP) {
                methodDesc.append(testCaseDescUtils.generateMessageLogDesc(errorCause, false));
            }

            if (!hasDriver) {
                // 无driver：完全复用旧版手动截图逻辑
                LogUtils.info("无driver传入，使用旧版手动截图逻辑");

            } else {
                // 有driver：优先MethodTracker水印截图，无效则兜底手动截图
                String methodScreenshot = methodInfo.getMethodScreenshot();
                if (methodScreenshot != null && !methodScreenshot.isEmpty() && !methodScreenshot.equals("data:image/png;base64,")) {
                    methodDesc.append(testCaseDescUtils.generateScreenshotDesc(methodScreenshot));
                    LogUtils.info("使用MethodTracker专属水印截图：" + methodInfo.getMethodName());
                } else {
                    Object driverObj = testContext.getAttribute("driver");
                    if (driverObj instanceof WebDriver && driverObj instanceof TakesScreenshot) {
                        try {
                            String base64Img = ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.BASE64);
                            String fallbackScreenshot = "data:image/png;base64," + (base64Img != null ? base64Img : "");
                            methodDesc.append(testCaseDescUtils.generateScreenshotDesc(fallbackScreenshot));
                            LogUtils.info("MethodTracker截图无效，使用手动兜底截图：" + methodInfo.getMethodName());
                        } catch (Exception e) {
                            LogUtils.error("获取兜底截图失败：" + e.getMessage(), e);
                        }
                    }
                }
            }

            if (extentT != null) {
                extentT.createNode(sMethodDisplayTitle).log(logStatus, methodDesc.toString());
            }
        }

        MethodTracker.clearExecutedMethods();
    }
}