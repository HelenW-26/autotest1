package utils.Listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;
import utils.LogUtils;
import vantagecrm.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import utils.Listeners.MethodTracker.MethodExecutionInfo;
import newcrm.utils.testCaseDescUtils;

public class CustomListener implements ITestListener, ISuiteListener {

	// Report path configuration
	private static final Path REPORT_BASE_DIR;
	private static final Path LATEST_REPORT_PATH;
	private static final Path ARCHIVED_REPORT_DIR;

	// 静态初始化：修复报告/截图路径
	static {
		String workingDir = "";
		try {
			workingDir = vantagecrm.Utils.workingDir;
		} catch (Exception e) {
			workingDir = System.getProperty("user.dir");
			LogUtils.info("vantagecrm.Utils.workingDir 未定义，使用项目根目录：" + workingDir);
		}
		REPORT_BASE_DIR = Paths.get(workingDir, "ExtentReports");
		LATEST_REPORT_PATH = REPORT_BASE_DIR.resolve("ExtentReportResults.html");
		ARCHIVED_REPORT_DIR = REPORT_BASE_DIR.resolve("Archive");
	}

	// Extent Reports instances
	private static ExtentSparkReporter latestSparkReporter;
	private static ExtentSparkReporter archivedSparkReporter;
	private static ExtentReports extentR = new ExtentReports();

	private static final ThreadLocal<Map<Integer, TestMtd>> lstTestMethod = ThreadLocal.withInitial(LinkedHashMap::new);
	private static final Logger devlogger = LogManager.getLogger(CustomListener.class);
	// Static initialization block
	static {
		initializeReporters();
	}

	static class TestMtd {
		final String title;
		final String desc;
		final Status status;

		TestMtd(String title, String desc, Status status) {
			this.title = title;
			this.desc = desc;
			this.status = status;
		}
	}

	private static ExtentTest extentT;
	public static WebDriver driver;

	/**
	 * Initialize reporters
	 */
	private static void initializeReporters() {
		try {
			// Create report directories
			Files.createDirectories(REPORT_BASE_DIR);
			Files.createDirectories(ARCHIVED_REPORT_DIR);

			// Initialize latest report (without timestamp)
			latestSparkReporter = new ExtentSparkReporter(LATEST_REPORT_PATH.toString());
			setupReporterConfig(latestSparkReporter, "Latest");

			// Initialize archived report with timestamp
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			Path archivedReportPath = ARCHIVED_REPORT_DIR.resolve("ExtentReportResults_" + timeStamp + ".html");
			archivedSparkReporter = new ExtentSparkReporter(archivedReportPath.toString());
			setupReporterConfig(archivedSparkReporter, timeStamp);

			// Attach both reporters
			extentR.attachReporter(latestSparkReporter, archivedSparkReporter);

			// Set system info
			extentR.setSystemInfo("OS", System.getProperty("os.name"));
			extentR.setSystemInfo("Java Version", System.getProperty("java.version"));
			extentR.setSystemInfo("User Name", System.getProperty("user.name"));

			System.out.println("Report system initialized:");
			System.out.println("Latest report: " + LATEST_REPORT_PATH);
			System.out.println("Archive directory: " + ARCHIVED_REPORT_DIR);

		} catch (IOException e) {
			System.err.println("Failed to initialize reporters: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Add timestamp watermark to screenshot with better reliability
	 */
	private String addTimestampWatermarkToScreenshot(WebDriver driver,ITestResult  result) {
		try {
			// 获取默认时区
			ZoneId zoneId = ZoneId.systemDefault();
			LogUtils.info("TimeZone ID: " + zoneId);

			// 获取时区偏移量
			TimeZone timeZone = TimeZone.getDefault();
			int offsetMillis = timeZone.getRawOffset();
			int offsetHours = offsetMillis / (1000 * 60 * 60);
			String offset =(offsetHours >= 0 ? "+" : "") + offsetHours;
			LogUtils.info("UTC Offset: UTC" +offset);

			//显示当前时区信息
			LogUtils.info("TimeZone Name: " + timeZone.getDisplayName());
			// 添加时间戳水印
            // 创建带时区的SimpleDateFormat实例
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			TimeZone beijingTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
			String timestamp = sdf.format(new Date());

			// 执行JavaScript在页面上添加时间戳水印（左上角位置，贴近边缘）
			((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
					"if (!document.getElementById('screenshot-timestamp')) {" +
							"    var timestampDiv = document.createElement('div');" +
							"    timestampDiv.id = 'screenshot-timestamp';" +
							"    timestampDiv.style.position = 'fixed';" +
							"    timestampDiv.style.top = '0px';" +  // 顶部边缘
							"    timestampDiv.style.left = '0px';" +  // 左侧边缘
							"    timestampDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.3)';" +
							"    timestampDiv.style.color = 'white';" +
							"    timestampDiv.style.padding = '8px 12px';" +
							"    timestampDiv.style.fontSize = '18px';" +
							"    timestampDiv.style.fontFamily = 'Arial, sans-serif';" +
							"    timestampDiv.style.fontWeight = 'bold';" +
							"    timestampDiv.style.zIndex = '999999';" +
							"    timestampDiv.style.borderRadius = '0px';" +  // 移除圆角使贴近边缘
							"    timestampDiv.style.border = 'none';" +  // 移除边框
							"    timestampDiv.style.pointerEvents = 'none';" +
							"    timestampDiv.innerText = '" + beijingTimeZone.getDisplayName() + " UTC+8 "+ timestamp + "';" +
							"    document.body.appendChild(timestampDiv);" +
							"}"
			);

			// 增加等待时间确保元素渲染完成
			try {
				Thread.sleep(1000); // 增加到1秒等待时间
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}

			// 获取带水印的截图数据
			byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			String base64ScreenshotWithTimestamp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

			// 修复截图保存路径
			String workingDir = "";
			try {
				workingDir = vantagecrm.Utils.workingDir;
			} catch (Exception e) {
				workingDir = System.getProperty("user.dir");
			}
			Path screenshotDir = Paths.get(workingDir, "screenshots");
			Files.createDirectories(screenshotDir);

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
			String fileTimestamp = sf.format(new Date());
			String screenshotFileName = result.getName() + "_" + fileTimestamp + "_with_timestamp.png";
			Path screenshotPath = screenshotDir.resolve(screenshotFileName);

			Files.write(screenshotPath, screenshotBytes);
			try {
				((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
						"var elem = document.getElementById('screenshot-timestamp');" +
								"if (elem) elem.remove();"
				);
			} catch (Exception e) {
				System.err.println("Failed to remove timestamp element: " + e.getMessage());
			}

			return "data:image/png;base64," + base64ScreenshotWithTimestamp;

		} catch (Exception e) {
			System.err.println("Failed to add timestamp watermark: " + e.getMessage());
			e.printStackTrace();
			try {
				return "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			} catch (Exception ex) {
				System.err.println("Failed to capture screenshot: " + ex.getMessage());
				return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+P+/HgAAL+AFWbUqgAAAABJRU5ErkJggg==";
			}
		}
	}

	private static void setupReporterConfig(ExtentSparkReporter reporter, String suffix) {
		reporter.config().setDocumentTitle("Automation Report - " + suffix);
		reporter.config().setReportName("Test Execution Report - " + suffix);
		reporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);
		reporter.config().setEncoding("UTF-8");
		reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
	}

	private static void cleanupOldReports() {
		try {
			File archiveDir = ARCHIVED_REPORT_DIR.toFile();
			if (!archiveDir.exists() || !archiveDir.isDirectory()) {
				return;
			}

			File[] reportFiles = archiveDir.listFiles((dir, name) ->
					name.startsWith("ExtentReportResults_") && name.endsWith(".html"));

			if (reportFiles != null && reportFiles.length > 0) {
				long cutoffTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
				for (File reportFile : reportFiles) {
					if (reportFile.lastModified() < cutoffTime) {
						boolean deleted = Files.deleteIfExists(reportFile.toPath());
						if (deleted) {
							System.out.println("Deleted old report (older than 30 days): " + reportFile.getName());
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to cleanup old reports: " + e.getMessage());
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("---------------------------------------------------");
		System.out.println(timestamp + " - Method " + result.getName() + " Start");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String sMethodName = getMethodName(result);
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		LogUtils.info(timestamp + " - [SUCCESS] " + sMethodName);
		generateMethodDisplayDesc(result, sMethodName, Status.PASS);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String sMethodName = getMethodName(result);
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(timestamp + " - [FAILURE] " + sMethodName);
		if (result.getThrowable() != null) {
			result.getThrowable().printStackTrace();
		}
		generateMethodDisplayDesc(result, sMethodName, Status.FAIL);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String sMethodName = getMethodName(result);
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		if (result.getThrowable() != null) {
			Throwable errCause = result.getThrowable();
			String errMsg = errCause.getMessage();

			if (!(errCause instanceof SkipException)) {
				System.out.println(timestamp + " - [FAILURE] " + sMethodName);
				generateMethodDisplayDesc(result, sMethodName, Status.FAIL);
			} else {
				System.out.println(timestamp + " - [SKIPPED] " + sMethodName);
				System.out.println("Skip Reason: " + errMsg);
				generateMethodDisplayDesc(result, sMethodName, Status.SKIP);
			}
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(timestamp + " - [WARNING] " + result.getName() + " Failed but Within Success Percentage");
		if (result.getThrowable() != null) {
			result.getThrowable().printStackTrace();
		}
		if (extentT != null) {
			extentT.log(Status.WARNING, "Test is failed but within Success Percentage: " + result.getName());
		}
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		String sMethodName = getMethodName(result);
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(timestamp + " - [TIMEOUT FAILURE] " + sMethodName);
		if (result.getThrowable() != null) {
			result.getThrowable().printStackTrace();
		}
		generateMethodDisplayDesc(result, sMethodName, Status.FAIL);
	}

	@Override
	public void onStart(ITestContext context) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(timestamp + " - Test Suite " + context.getName() + " Start");
	}

	@Override
	public void onStart(ISuite suite) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		String brand = suite.getParameter("Brand");
		brand = (brand != null && !brand.isEmpty()) ? brand : "";

		String suiteName = suite.getName();
		suiteName = (suiteName != null && !suiteName.isEmpty()) ? suiteName : "";

		String reportName = (brand.isEmpty() && suiteName.isEmpty()) ?
				"Automation Report" : brand + (!suiteName.isEmpty() ? " " : "") + suiteName;

		// Update report names
		if (latestSparkReporter != null) {
			latestSparkReporter.config().setReportName(reportName + " - Latest");
		}
		if (archivedSparkReporter != null) {
			archivedSparkReporter.config().setReportName(reportName + " - " +
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}

		System.out.println(timestamp + " - Extent Report initialized");
		System.out.println("Latest report: " + LATEST_REPORT_PATH);
		System.out.println("Archive directory: " + ARCHIVED_REPORT_DIR);

	}

	@Override
	public void onFinish(ITestContext context) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		boolean bIsParentNodeCreated = false, bIsNotAvailableOnly = false;

		// Get test case counters based on each status
		int passCount = (int) lstTestMethod.get().values().stream()
				.filter(entry -> entry.status == Status.PASS)
				.count();

		int failCount = (int) lstTestMethod.get().values().stream()
				.filter(entry -> entry.status == Status.FAIL)
				.count();

		int skipCount = (int) lstTestMethod.get().values().stream()
				.filter(entry ->
						entry.status == Status.SKIP &&
								!Optional.ofNullable(entry.desc)
										.orElse("")
										.toLowerCase()
										.contains("skipping this test intentionally"))
				.count();

		int notAvailableCount = (int) lstTestMethod.get().values().stream()
				.filter(entry ->
						entry.status == Status.SKIP &&
								Optional.ofNullable(entry.desc)
										.orElse("")
										.toLowerCase()
										.contains("skipping this test intentionally"))
				.count();

		if (passCount == 0 && failCount == 0 && skipCount == 0 && notAvailableCount > 0) {
			bIsNotAvailableOnly = true;
			lstTestMethod.get().values().removeIf(entry ->
					entry.status != Status.SKIP ||
							!Optional.ofNullable(entry.desc)
									.orElse("")
									.toLowerCase()
									.contains("skipping this test intentionally")
			);
		} else {
			lstTestMethod.get().values().removeIf(entry ->
					entry.status == Status.SKIP &&
							Optional.ofNullable(entry.desc)
									.orElse("")
									.toLowerCase()
									.contains("skipping this test intentionally")
			);
		}

		// Create test nodes
		for (Map.Entry<Integer, TestMtd> entry : lstTestMethod.get().entrySet()) {
			TestMtd mtd = entry.getValue();

			if (!bIsParentNodeCreated) {
				String summary = bIsNotAvailableOnly ?
						String.format("Summary → ⚠️ Test Case Not Available: %d", notAvailableCount) :
						String.format("Summary → ✅ Pass: %d | ❌ Fail: %d | ⚠️ Unexpected Events: %d | ⚠️ Test Case Not Available: %d",
								passCount, failCount, skipCount, notAvailableCount);

				extentT = extentR.createTest(context.getName(), summary);
				bIsParentNodeCreated = true;
			}

			extentT.createNode(mtd.title).log(mtd.status, mtd.desc);
		}

		System.out.println(timestamp + " - Test Suite " + context.getName() + " End");

		// Flush reports
		extentR.flush();
		cleanupOldReports();

		// Clear data for next test
		lstTestMethod.get().clear();
		MethodTracker.clearExecutedMethods();
	}

	// Helper method to get method name
	private String getMethodName(ITestResult result) {
		if (result.getTestContext().getAttributeNames().contains("promotion")) {
			return result.getTestContext().getAttribute("promotion").toString();
		}
		return result.getName();
	}

	public static void reportLog(String message, String screenshotPath) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(timestamp + " - [LOG] " + message);

		if (extentT != null) {
			extentT.log(Status.INFO, message);
			if (screenshotPath != null && !screenshotPath.isEmpty()) {
				extentT.log(Status.INFO, "Screenshot: ").addScreenCaptureFromPath(screenshotPath);
			}
		}
	}

	private String captureScreenshot(WebDriver driver, String testName) {
		if (driver == null) {
			return null;
		}

		try {
			String workingDir = "";
			try {
				workingDir = vantagecrm.Utils.workingDir;
			} catch (Exception e) {
				workingDir = System.getProperty("user.dir");
			}
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
			String screenshotName = testName + "_" + timeStamp + ".png";
			Path screenshotDir = Paths.get(workingDir, "screenshots");
			Path screenshotPath = screenshotDir.resolve(screenshotName);

			Files.createDirectories(screenshotDir);
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			Files.write(screenshotPath, screenshot);

			return screenshotPath.toString();
		} catch (IOException e) {
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			return null;
		}
	}

	// 核心：直接使用已固化的即时截图，确保展示正常
	private void generateMethodDisplayDesc(ITestResult result, String sMethodName, Status logStatusRes) {
		Map<Integer, MethodExecutionInfo> executedMethods = MethodTracker.getExecutedMethods();
		boolean bRequireCallMethod = true;

		if (executedMethods.isEmpty()) {
			MethodTracker.trackMethodExecution(result.getInstance(), sMethodName, false, logStatusRes);
			executedMethods = MethodTracker.getExecutedMethods();
			bRequireCallMethod = false;
		}

//		StringBuilder methodDesc = new StringBuilder();

		for (Map.Entry<Integer, MethodExecutionInfo> entry : executedMethods.entrySet()) {
			Integer iNextMtdRecCnt = lstTestMethod.get().size() + 1;
			String sStatusDesc, sMethodDisplayTitle, sMethodDisplayDesc, base64Screenshot = "";
			Status logStatus;
			MethodExecutionInfo methodInfo = entry.getValue();
			logStatusRes = methodInfo.getLogStatusRes();
			Throwable errorCause = bRequireCallMethod ? methodInfo.getMessageLog() : result.getThrowable();
			StringBuilder currentMethodDesc = new StringBuilder();

			switch (logStatusRes){
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

			String[] lstMethodDesc = methodInfo.getMethodDisplayDesc().split(",(?=(?:[^}]*\\{[^}]*\\})*[^}]*$)+");
			String sMethodDescNotAvailable = testCaseDescUtils.generateProcessFlowNotAvaiDesc();

			if (methodInfo.getMethodDisplayDesc().equals(testCaseDescUtils.sMethodDescNotAvailable)) {
				sMethodDisplayTitle = methodInfo.getMethodName();
				sMethodDisplayDesc = sMethodDescNotAvailable;
			}
			else if (lstMethodDesc.length == 1) {
				sMethodDisplayTitle = methodInfo.getMethodName();
				lstMethodDesc[0] = lstMethodDesc[0].trim();
				sMethodDisplayDesc = (lstMethodDesc[0].startsWith("{") && lstMethodDesc[0].endsWith("}")) ?
						lstMethodDesc[0].substring(1, lstMethodDesc[0].length() - 1).trim() : "";
				sMethodDisplayDesc = sMethodDisplayDesc.isEmpty() ? sMethodDescNotAvailable : sMethodDisplayDesc;
			}
			else {
				lstMethodDesc[0] = lstMethodDesc[0].trim();
				lstMethodDesc[1] = lstMethodDesc[1].trim();
				sMethodDisplayTitle = (lstMethodDesc[0].startsWith("{") && lstMethodDesc[0].endsWith("}")) ?
						lstMethodDesc[0].substring(1, lstMethodDesc[0].length() - 1).trim() : "";
				sMethodDisplayTitle = sMethodDisplayTitle.isEmpty() ? methodInfo.getMethodName() :
						sMethodDisplayTitle + " (" + methodInfo.getMethodName() + ")";
				sMethodDisplayDesc = (lstMethodDesc[1].startsWith("{") && lstMethodDesc[1].endsWith("}")) ?
						lstMethodDesc[1].substring(1, lstMethodDesc[1].length() - 1).trim() : "";
				sMethodDisplayDesc = sMethodDisplayDesc.isEmpty() ? sMethodDescNotAvailable : sMethodDisplayDesc;
			}

			if (logStatusRes != Status.FAIL && logStatusRes != Status.SKIP) {
				sMethodDisplayDesc = sMethodDisplayDesc.replaceAll("</div>\\s*<br\\s*/?>\\s*</details>", "</div>\n</details>");
			}

			currentMethodDesc.append(sMethodDisplayDesc);

			// 核心：获取有效截图（非空）
			String instantScreenshot = methodInfo.getMethodScreenshot();
			if (instantScreenshot == null || instantScreenshot.isEmpty()) {
				driver = (WebDriver) result.getTestContext().getAttribute("driver");
				if(driver!= null){
					LogUtils.info("方法 " + methodInfo.getMethodName() + " 截图为空，使用默认driver截图");
					instantScreenshot = addTimestampWatermarkToScreenshot(driver,result);

				}

			}

			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			LogUtils.info(timestamp + " - [TEST RESULT] " + methodInfo.getMethodName() + " - " + sStatusDesc);
			if (errorCause != null) {
				System.out.println(timestamp + " - [ERROR DETAILS] " + errorCause.getMessage());
				errorCause.printStackTrace();
			}

			if (logStatusRes == Status.FAIL || logStatusRes == Status.SKIP || logStatusRes == Status.PASS) {
				if(!result.getTestContext().getAttributeNames().contains("driver")) {
					if (logStatusRes == Status.FAIL || logStatusRes == Status.SKIP) {
						currentMethodDesc.append(testCaseDescUtils.generateMessageLogDesc(errorCause, true));
					}
					lstTestMethod.get().put(iNextMtdRecCnt, new TestMtd(sMethodDisplayTitle, currentMethodDesc.toString(), logStatus));
					continue;
				}

//				driver = (WebDriver) result.getTestContext().getAttribute("driver");
//				base64Screenshot = "data:image/png;base64," + ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);

//				driver = (WebDriver) result.getTestContext().getAttribute("driver");
//				base64Screenshot = addTimestampWatermarkToScreenshot(driver,result);


				if (logStatusRes == Status.FAIL || logStatusRes == Status.SKIP) {
					currentMethodDesc.append(testCaseDescUtils.generateMessageLogDesc(errorCause, false));
				}

				// 使用有效截图拼接报告
				currentMethodDesc.append(testCaseDescUtils.generateScreenshotDesc(instantScreenshot));
			}

			lstTestMethod.get().put(iNextMtdRecCnt, new TestMtd(sMethodDisplayTitle, currentMethodDesc.toString(), logStatus));
		}

		MethodTracker.clearExecutedMethods();
	}
}