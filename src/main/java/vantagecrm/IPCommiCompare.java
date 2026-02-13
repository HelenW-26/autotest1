package vantagecrm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * This class is to test the displayed Rebate and Total Value are consistent between Home Page & Rebate Report
 */

public class IPCommiCompare {

	WebDriver driver;
	String IpURL = "http://ib.vantagefx.com/?login_id=";
	String userID = "69127";

	/*
	 * String IpURL="http://ib-new.vantagefx.com/?login_id=";
	 * String userID="23203";
	 */

	WebDriverWait wait50;
	ArrayList<String> winHandles;
	// different wait time among different environments. Test environment will use 1 while beta & production will use 2.
	// Initialized in LaunchBrowser function.
	int waitIndex = 1;
	int dateIndex = 0;

	SoftAssert saAssert = new SoftAssert();

	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{

		// System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		WebDriverManager.chromedriver().setup();
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		context.setAttribute("driver", driver);

		if (TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod")) {
			waitIndex = 2;
		}

		wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));

	}

	@Test(priority = 0)
	@Parameters(value = { "TraderName", "TraderPass", "Brand", "TraderURL" })
	void IPLogIn(String TraderName, String TraderPass, String Brand, String TraderURL) throws Exception

	{

		String ibURL;

		// Login AU IB Portal and keep Home Page
		driver.get(TraderURL);
		Utils.funcLogInIP(driver, TraderName, TraderPass, Brand);

		ibURL = driver.getCurrentUrl();

		// Open another tab
		((JavascriptExecutor) driver).executeScript("window.open()");

		winHandles = new ArrayList<String>(driver.getWindowHandles());

		// Login AU IB Portal and go to IB Accounts
		driver.switchTo().window(winHandles.get(1));
		driver.get(ibURL);

		/*
		 * //Switch language to English
		 * Thread.sleep(1000);
		 * driver.findElement(By.cssSelector("div.login input.el-input__inner")).click();
		 * Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper").get(0).click();
		 */

		// Thread.sleep(2000);
		// wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));
		Utils.waitUntilLoaded(driver);

		Thread.sleep(1000);
		wait50.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='REBATE REPORT']"))).click();
		Thread.sleep(500);

	}

	@Test(dependsOnMethods = "IPLogIn")
	@Parameters(value = "Brand")
	void CompareCommission(String Brand) throws Exception {
		int i = 0;
		String hpRebate, hpVolume;
		String[] rebateReport = new String[] {};

		String cssSel = "";

		driver.switchTo().window(winHandles.get(0));

		// Get all date shortcut options
		Thread.sleep(1000);
		driver.findElement(By
				.cssSelector("div.calendar div.calendar_shortcut div.el-input.el-input--suffix input.el-input__inner"))
				.click();
		Thread.sleep(500);
		List<WebElement> shortcutDates = driver.findElements(By.cssSelector(
				"body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));
		// System.out.println(shortcutDates.size());

		List<WebElement> dataElements;

		for (i = 0; i < shortcutDates.size(); i++) {

			driver.switchTo().window(winHandles.get(0));
			driver.navigate().refresh();
			Thread.sleep(2000);
			// wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));

			// Get all date shortcut options
			// Thread.sleep(1000);
			Utils.waitUntilLoaded(driver);
			driver.findElement(By.cssSelector(
					"div.calendar div.calendar_shortcut div.el-input.el-input--suffix input.el-input__inner")).click();

			Thread.sleep(500);
			shortcutDates = driver.findElements(By.cssSelector(
					"body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));

			if (i > 6) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", shortcutDates.get(i));
				Thread.sleep(500);

			}

			// Choose the first date range shortcut
			System.out.println("Date option is " + shortcutDates.get(i).getText());
			shortcutDates.get(i).click();

			Thread.sleep(1000);
			// Click Update button
			// driver.findElement(By.cssSelector("div.calendar div.right button.el-button.el-button--default")).click();
			driver.findElement(By.xpath("//button[@class='el-button el-button--default']//span[text()='UPDATE']"))
					.click();

			// Allow sometime for search
			Thread.sleep(2000);
			// wait50.until(ExpectedConditions.attributeToBe(By.cssSelector("div.calendar_content>div.calendar_content_bottom>div.el-loading-mask"), "display", "none"));

			// Get Rebate and Total Volume

			switch (Brand) {

			case "vt":
				cssSel = "div.calendar_content ul.clearfix>li>div.right p";
				break;

			default:
				cssSel = "div.calendar_content ul.clearfix>li>div.bottom p";

			}

			dataElements = driver.findElements(By.cssSelector(cssSel));

			// System.out.println("The number of function blocks are: "+dataElements.size());

			hpRebate = dataElements.get(0).getText().trim().replace(" ", "");
			hpVolume = dataElements.get(1).getText().trim().replace(",", ""); // remove thousand separator

			System.out.println("Home Page total Rebate is " + hpRebate + "  and total volume is " + hpVolume);

			rebateReport = getRebateReportData(driver, winHandles.get(1), i);

			saAssert.assertTrue(hpRebate.equals(rebateReport[0]),
					"Home Page Rebate is " + hpRebate + " Rebate Report value is " + rebateReport[0]);
			saAssert.assertTrue(hpVolume.equals(rebateReport[1]),
					"Home Page Volumn is " + hpVolume + " Rebate Report value is " + rebateReport[1]);
		}

		saAssert.assertAll();
	}

	String[] getRebateReportData(WebDriver driver, String winHandle, int shortcutIndex) throws Exception {
		String[] data = new String[2];
		List<WebElement> dataElements;
		WebElement rebateFunc;
		int i;
		BigDecimal volume = new BigDecimal("0.00");

		// Switch to rebate report page
		driver.switchTo().window(winHandle);
		driver.navigate().refresh();

		// comment out this line. Confirmed with Lulu this is no longer used for loading.
		/* wait50.until(ExpectedConditions.attributeToBe(By.xpath("//div[@class='calendar_box']/div"), "display", "none")); */

		// Get all date shortcut options
		// Thread.sleep(1000);
		Utils.waitUntilLoaded(driver);
		driver.findElement(By.xpath("//div[@class='calendar_shortcut']/div/div/input")).click();

		Thread.sleep(500);
		List<WebElement> shortcutDates = driver.findElements(By.cssSelector(
				"body>div.el-select-dropdown.el-popper ul.el-scrollbar__view.el-select-dropdown__list>li>span"));
		// System.out.println("getRebateReportData:"+shortcutDates.size());

		if (shortcutIndex > 6) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					shortcutDates.get(shortcutIndex));
			Thread.sleep(1000);

		}

		// Choose the first date range shortcut
		shortcutDates.get(shortcutIndex).click();
		Thread.sleep(1000);

		// Need to scroll to the top after selecting date range option. Otherwise, "Update" button can't be clicked
		if (shortcutIndex > 6) {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
			Thread.sleep(1000);
		}

		// Click Update button
		// driver.findElement(By.cssSelector("div.calendar div.right_box div.right button.el-button.el-button--primary")).click();
		driver.findElement(By.xpath("//button[@class='el-button el-button--primary']")).click();

		// Allow sometime for search
		Utils.waitUntilLoaded(driver);

		// Get Rebate
		rebateFunc = driver.findElement(By.xpath("//div[@class='bottom clearfix']/p"));
		data[0] = rebateFunc.getText().trim();
		data[0] = data[0].replace(" ", "");   // remove the space after dollar sign

		// Get Volume
		dataElements = driver.findElements(By.cssSelector("div.calendar_box>ul.clearfix>li div.bottom_left>p"));
		// System.out.println("Get rebate report: The number of function blocks are: "+dataElements.size());

		// data[1]=dataElements.get(1).getText().trim();

		for (i = 0; i < dataElements.size(); i++) {

			data[1] = dataElements.get(i).getText();
			// System.out.println("The volume is:" + data[1]);
			volume = volume.add(new BigDecimal(data[1]));

		}

		data[1] = volume.toString();

		System.out.println("Rebate Report total Rebate is: " + data[0] + " and total volume is: " + data[1]);
		System.out.println();

		return data;
	}

	// Compare PROD IB commission data between tb_commission_ib_day and tb_commission_ib_day_total
	// This function is used to monitor PROD online data
	@Test// (priority=0)
	@Parameters(value = { "Brand", "TestEnv" })
	void monitorProdCommiData(String Brand, String TestEnv) throws Exception {
		String[] ibUserID = null;
		String newTestEnv = "beta";
		String oldTestEnv = TestEnv;
		Calendar calInst = Calendar.getInstance();
		SimpleDateFormat simDF = new SimpleDateFormat("yyyy-MM-dd");
		// String selectOldComm="select ib_id, mt4Account, deposit_amount, volume, commission_flag from tb_commission_ib_day where user_id = XXX and payment_date = 'DATEDATE' order by mt4Account desc";
		String selectComm = "select ib_id, mt4Account, deposit_amount, volume, commission_flag from tb_commission_ib_day_total where user_id = XXX and commission_date = 'DATEDATE'  order by mt4Account desc";

		// String selectOldSum = "select count(*) as old_total_records, sum(deposit_amount) as old_total_commission from tb_commission_ib_day where payment_date = 'DATEDATE';";
		String selectSum = "select count(*) as new_total_records, sum(deposit_amount) as new_total_commission from tb_commission_ib_day_total where commission_date = 'DATEDATE';";

		String asicDBIdentifier = "dev_m_regulator_asic";
		String cimaDBIdentifier = "dev_m_regulator_cima";
		String vfscDBIdentifier = "dev_m_regulator_vfsc";
		String vfsc2DBIdentifier = "dev_m_regulator_vfsc2";

		String ibID, selectSQL, dbIdentifier = null;

		switch (Brand) {
		case "ky":

			ibUserID = new String[] { "160788", "48696", "329758", "314453", "251469", "175361", "170211", "161962",
					"128343", "272168", "256818", "171557", "133684", "223331", "138027", "201208", "282279", "273213",
					"324045" };

			// ibUserID = new String[] {"201208"};
			// ibUserID = new String[] {"133684", "223331", "138027", "201208"};
			dbIdentifier = cimaDBIdentifier;
			break;

		case "au":
			ibUserID = new String[] { "282306", "282337", "45888", "282299", "301325", "282303" };
			// ibUserID = new String[] {"282306", "282337", "45888"};
			dbIdentifier = asicDBIdentifier;
			break;

		case "vfsc":
			ibUserID = new String[] { "638901", "638787", "626699", "608836" };
			// ibUserID = new String[] {"282306", "282337", "45888"};
			dbIdentifier = vfscDBIdentifier;
			break;

		case "regulator2":
			System.out.println("Brand " + Brand + " needs to collect more data.");

		default:
			System.out.println("Brand " + Brand + " is not supported.");
			return;

		}

		// Set commission date: iff today is Sunday/Monday, last working day is Friday; else, it will be today-1
		if (calInst.get(Calendar.DAY_OF_WEEK) == 1) {
			calInst.add(Calendar.DATE, -2);
		} else if (calInst.get(Calendar.DAY_OF_WEEK) == 2) {
			calInst.add(Calendar.DATE, -3);
		} else {
			calInst.add(Calendar.DATE, -1);
		}

		String lastWorkingday = simDF.format(calInst.getTime());

		System.out.println("Commission Date: " + lastWorkingday);

		// selectOldSum = selectOldSum.replace("DATEDATE", lastWorkingday);
		selectSum = selectSum.replace("DATEDATE", lastWorkingday);

		RestAPI.APIReadDB(dbIdentifier, selectSum, oldTestEnv);
		RestAPI.APIReadDB(dbIdentifier, selectSum, newTestEnv);

		// selectOldComm = selectOldComm.replace("DATEDATE", lastWorkingday);
		selectComm = selectComm.replace("DATEDATE", lastWorkingday);

		for (int i = 0; i < ibUserID.length; i++) {
			ibID = ibUserID[i];
			selectSQL = selectComm;
			// selectOld = selectOldComm;

			selectSQL = selectSQL.replace("XXX", ibID);
			// selectOld = selectOld.replace("XXX", ibID);

			// Send API to get tb_commission_ib_day data
			System.out.println();
			System.out.println("OLD commission for IB userid=" + ibID);
			// System.out.println(selectOld);
			RestAPI.APIReadDB(dbIdentifier, selectSQL, oldTestEnv);

			// Send API to get tb_commission_ib_day_total data
			System.out.println();
			System.out.println("NEW commission for IB userid=" + ibID);
			RestAPI.APIReadDB(dbIdentifier, selectSQL, newTestEnv);

		}

	}

	// Developed by Yanni on 16/12/2020. For removing the same MT4/5 restriction when calculationg commision
	@Test// (priority=0)
	@Parameters(value = { "Brand", "TestEnv" })
	void RemoveMTServerTestCommi(String Brand, String TestEnv) throws Exception {

		String newTestEnv = "betanew";
		String oldTestEnv = "beta1209";
		Calendar calInst = Calendar.getInstance();
		SimpleDateFormat simDF = new SimpleDateFormat("yyyy-MM-dd");
		// String selectOldComm="select ib_id, mt4Account, deposit_amount, volume, commission_flag from tb_commission_ib_day where user_id = XXX and payment_date = 'DATEDATE' order by mt4Account desc";
		String selectComm = "select mt4Account, ib_id, deposit_amount, volume, commission_flag from tb_commission_ib_day_total where commission_date = 'DATEDATE'  order by mt4Account desc";

		// String selectOldSum = "select count(*) as old_total_records, sum(deposit_amount) as old_total_commission from tb_commission_ib_day where payment_date = 'DATEDATE';";
		String selectSum = "select count(*) as new_total_records, sum(deposit_amount) as new_total_commission from tb_commission_ib_day_total where commission_date = 'DATEDATE';";

		String asicDBIdentifier = "dev_m_regulator_asic";
		String cimaDBIdentifier = "dev_m_regulator_cima";
		String vfscDBIdentifier = "dev_m_regulator_vfsc";
		String vfsc2DBIdentifier = "dev_m_regulator_vfsc2";
		String commiA, commiB;

		String selectSQL, dbIdentifier = null;

		switch (Brand) {
		case "ky":

			dbIdentifier = cimaDBIdentifier;
			break;

		case "au":

			dbIdentifier = asicDBIdentifier;
			break;

		case "vfsc":
			
			dbIdentifier = vfscDBIdentifier;
			break;
			
		case "vfsc2":

			dbIdentifier = vfsc2DBIdentifier;
			break;
			
		case "regulator2":
			System.out.println("Brand " + Brand + " needs to collect more data.");

		default:
			System.out.println("Brand " + Brand + " is not supported.");
			return;

		}

		// Set commission date: if today is Sunday/Monday, last working day is Friday; else, it will be today-1
		if (calInst.get(Calendar.DAY_OF_WEEK) == 1) {
			calInst.add(Calendar.DATE, -2);
		} else if (calInst.get(Calendar.DAY_OF_WEEK) == 2) {
			calInst.add(Calendar.DATE, -3);
		} else {
			calInst.add(Calendar.DATE, -1);
		}

		String lastWorkingday = simDF.format(calInst.getTime());

		System.out.println("Commission Date: " + lastWorkingday);

		// selectOldSum = selectOldSum.replace("DATEDATE", lastWorkingday);
		selectSum = selectSum.replace("DATEDATE", lastWorkingday);

		/*
		 * RestAPI.APIReadDB(dbIdentifier, selectSum, oldTestEnv);
		 * RestAPI.APIReadDB(dbIdentifier, selectSum, newTestEnv);
		 */

		// selectOldComm = selectOldComm.replace("DATEDATE", lastWorkingday);
		selectComm = selectComm.replace("DATEDATE", lastWorkingday);

		selectSQL = selectComm;
		// selectOld = selectOldComm;

		// Send API to get tb_commission_ib_day data
		System.out.println();
		// commiA = DBUtils.funcReadDBReturnAll(dbIdentifier, selectSQL, "beta1209");
		commiA = RestAPI.APIReadDBReturnAll(dbIdentifier, selectSQL, oldTestEnv);
		// System.out.println(selectOld);

		// Send API to get tb_commission_ib_day_total data
		System.out.println();
		// commiB = DBUtils.funcReadDBReturnAll(dbIdentifier, selectSQL, "betanew");
		commiB = RestAPI.APIReadDBReturnAll(dbIdentifier, selectSQL, newTestEnv);

		System.out.println("");

		compareHash(parseResult(commiA), parseResult(commiB));

	}

	// API result format: {"data":[{"mt4Account":1800875,"ib_id":"189180","deposit_amount":1.6,"volume":0.8,"commission_flag":0}]},
	// Parse API result to a HashMap, mt4Account as the map's key, other values are combined together as the value.
	// map: {mt4Account=1800875, volume=0.8, deposit_amount=1.6, ib_id=189180, commission_flag=0}
	private HashMap<String, String> parseResult(String apiResult) throws ParseException {
		HashMap<String, String> commResult = new HashMap<String, String>();

		JSONParser parser = new JSONParser();
		JSONObject objAPIResult, objRow;

		String tempA, mt4Account;

		if (apiResult != null) {

			objAPIResult = (JSONObject) parser.parse(apiResult);
			JSONArray data = (JSONArray) objAPIResult.get("data");

			for (int i = 0; i < data.size(); i++) {
				tempA = "";
				objRow = (JSONObject) data.get(i);
				mt4Account = objRow.get("mt4Account").toString() + "; ";
				mt4Account = mt4Account + objRow.get("commission_flag").toString();

				tempA = tempA + objRow.get("ib_id").toString() + "; ";
				tempA = tempA + objRow.get("deposit_amount").toString() + "; ";
				tempA = tempA + objRow.get("volume").toString();
				// tempA = tempA + objRow.get("commission_flag").toString() + "; ";

				// System.out.println("mt4Account: " + mt4Account + "value: " + tempA);

				commResult.put(mt4Account, tempA);
			}

		}

		return commResult;

	}

	// Compare two hashmaps
	private void compareHash(HashMap<String, String> srcMapA, HashMap<String, String> tarMapB) throws ParseException {

		int i;
		int count = 0, diffCount = 0, sameVolumeCount = 0;

		HashMap<String, String> dupSrc = new HashMap<String, String>();
		HashMap<String, String> dupTar = new HashMap<String, String>();
		
		HashMap<String, String> diffVolumeSrc = new HashMap<String, String>();
		HashMap<String, String> diffVolumeTar = new HashMap<String, String>();
		HashMap<String, String> sameVolumeSrc = new HashMap<String, String>();
		HashMap<String, String> sameVolumeTar = new HashMap<String, String>();

		dupSrc = deepCopyHash(srcMapA);
		dupTar = deepCopyHash(tarMapB);

		if (srcMapA.isEmpty() || tarMapB.isEmpty()) {

			if (srcMapA.isEmpty()) {
				System.out.println("Source result is null. Can't compare.");
			}

			if (tarMapB.isEmpty()) {
				System.out.println("Target result is null. Can't compare.");

			}

			return;
		}

		if (srcMapA.size() != tarMapB.size()) {
			System.out.println("Number of records is not same! Source Records:" + srcMapA.size() + " Target Records:"
					+ tarMapB.size());

			if (srcMapA.size() < tarMapB.size()) {

				System.out.println("Target Result has more records! ");

			} else {

				System.out.println("Source Result has more records!");
			}

		}

		for (String mt4Account : srcMapA.keySet()) {

			if (tarMapB.containsKey(mt4Account)) { // if key exists in target map
				if (!srcMapA.get(mt4Account).equals(tarMapB.get(mt4Account))) { // If source value & target value are different
		
					//Get value to extract volume
					String srcValue = srcMapA.get(mt4Account);
					String tarValue = tarMapB.get(mt4Account);

					srcValue = srcValue.substring(srcValue.lastIndexOf("; ")).trim();
					tarValue = tarValue.substring(tarValue.lastIndexOf("; ")).trim();

					if (srcValue.equals(tarValue)) { // if volume is the same, count and copy to another two maps
					//	System.out.println("Difference found! The same volume but different amount ");
						sameVolumeSrc.put(mt4Account,srcMapA.get(mt4Account));
						sameVolumeTar.put(mt4Account, tarMapB.get(mt4Account));
						sameVolumeCount ++;

					} else {

					//	System.out.println("Difference found! Both amount and volume are different ");
						diffVolumeSrc.put(mt4Account,srcMapA.get(mt4Account));
						diffVolumeTar.put(mt4Account, tarMapB.get(mt4Account));
					}

					/*
					 * printInfo(false, mt4Account, srcMapA.get(mt4Account));
					 * printInfo(true, mt4Account, tarMapB.get(mt4Account));
					 */

					diffCount++;

				}

				dupSrc.remove(mt4Account, dupSrc.get(mt4Account));
				dupTar.remove(mt4Account, dupTar.get(mt4Account));

			}

			count++;

		}

		System.out.println("\nCompared " + count + " records! Found different records: " + diffCount);
		System.out.println("Records Number of same volume but different amount: " + sameVolumeCount );
		System.out.println("Records Number of both volume & amount are diffrent: " + (diffCount - sameVolumeCount)); 
		
		// Output the sameVolume map
		if (sameVolumeSrc.size() > 0) {

			System.out.println();
			System.out.println("Difference found! The same volume but different amount ");

			for (String mt4Account : sameVolumeSrc.keySet()) {

				//System.out.println("mt4Account: " + mt4Account + ", value:" + dupSrc.get(mt4Account));
				printInfo(false, mt4Account, sameVolumeSrc.get(mt4Account));
				printInfo(true, mt4Account, sameVolumeTar.get(mt4Account));
				System.out.println();

			}
		}		
		

		// Output the diffVolume map
		if (diffVolumeSrc.size() > 0) {

			System.out.println();
			System.out.println("Difference found! Both amount and volume are different ");

			for (String mt4Account : diffVolumeSrc.keySet()) {

				//System.out.println("mt4Account: " + mt4Account + ", value:" + dupSrc.get(mt4Account));
				printInfo(false, mt4Account, diffVolumeSrc.get(mt4Account));
				printInfo(true, mt4Account, diffVolumeTar.get(mt4Account));
				System.out.println();

			}
		}		
		
		// Output the extra results in source map
		if (dupSrc.size() > 0) {

			System.out.println();
			System.out.println(dupSrc.size() + " more results are found in Source");

			for (String mt4Account : dupSrc.keySet()) {

				//System.out.println("mt4Account: " + mt4Account + ", value:" + dupSrc.get(mt4Account));
				printInfo(false, mt4Account, dupSrc.get(mt4Account));

			}
		}

		// Output the extra results in target map
		if (dupTar.size() > 0) {

			System.out.println();
			System.out.println(dupTar.size() + " more results are found in Target");
			for (String mt4Account : dupTar.keySet()) {

				//System.out.println("mt4Account: " + mt4Account + ", value:" + dupTar.get(mt4Account));
				printInfo(true, mt4Account, dupTar.get(mt4Account));

			}
		}
	}

	private HashMap<String, String> deepCopyHash(HashMap<String, String> srcMap) {
		HashMap<String, String> tarMap = new HashMap<String, String>();

		for (String keyStr : srcMap.keySet()) {
			tarMap.put(keyStr, srcMap.get(keyStr));
		}

		return tarMap;
	}

	private void printInfo(boolean isTarget, String mt4Account, String value)

	{
		String title;

		if (!isTarget)

		{
			title = "VFSC_BUSINESS_BETA_1209 mt4Account: ";
		} else {
			title = "VFSC_BUSINESS_BETA mt4Account:      ";
		}

		System.out.println(title + mt4Account.split("; ")[0] + " commission_flag: " + mt4Account.split("; ")[1]
				+ ", ib_id :" + value.split("; ")[0] + ", amount: " + value.split("; ")[1] + ", volume: "
				+ value.split("; ")[2]);
	}

	@AfterClass(alwaysRun = true)
	void ExitBrowser() throws Exception {

		// Utils.funcLogOutIP(driver);
		// Close all browsers
		driver.quit();
	}
}
