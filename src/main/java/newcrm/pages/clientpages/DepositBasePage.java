package newcrm.pages.clientpages;

import java.util.*;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newcrm.business.dbbusiness.PaymentDB;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.DepositPageCommElements;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import tools.ScreenshotHelper;
import utils.CustomAssert;
import utils.LogUtils;

import static tools.ScreenshotHelper.takeScreenshot;

public abstract class DepositBasePage extends Page {
	public DepositBasePage(WebDriver driver) {
		super(driver);
	}

	protected HashMap<String,String> getAccounts(List<WebElement> all_accounts){
		HashMap<String,String> result = new HashMap<>();
		for(WebElement element: all_accounts) {
			String inner_text = element.getText();
			if(inner_text!=null && inner_text.trim() !="") {
				String inner_text_trim = inner_text.trim();
				String values[] = inner_text_trim.split("-");
				//String find_account = inner_text_trim.substring(0,inner_text_trim.indexOf('-')).trim();
				//String currency = inner_text_trim.substring(inner_text_trim.indexOf('-')+1).trim();
				//result.put(find_account, currency);
				result.put(values[0].trim(), values[1].trim());
			}
		}
		return result;
	}

	/**
	 *
	 * @param account client's account number
	 * @return
	 */
	protected String setAccount(String account, List<WebElement> all_accounts) {
		for(WebElement element: all_accounts) {
			String inner_text = element.getText();
			if(inner_text!=null && inner_text.trim() !="") {
				String inner_text_trim = inner_text.trim();
				String find_account = inner_text_trim.substring(0,inner_text_trim.indexOf('-')).trim();
				if(find_account.equalsIgnoreCase(account)) {
					String currency = inner_text_trim.substring(inner_text_trim.indexOf('-')+1).trim();
					this.moveElementToVisible(element);
					element.click();
					waitLoading();
					System.out.println("Select Account: " + find_account + " Currency: " + currency);
					return find_account;
				}
			}
		}
		System.out.println("*** This Deposit method does not support this account or The client does not have this account: " +account);
		return null;
	}

	public void setAmount(String amount) {
		DepositPageCommElements pg = PageFactory.initElements(driver, DepositPageCommElements.class);
		WebElement amount_element = pg.e_amount;
		amount_element.clear();
		amount_element.sendKeys(amount);
		System.out.println("Set amount to: " + amount);
	}
	public void setEmail(String email) {
		WebElement email_element = this.findClickableElemntByTestId("email");
		email_element.clear();
		email_element.sendKeys(email);
		GlobalMethods.printDebugInfo("Set Email to: " + email);
	}

	public void setJCBEmail(String email) {
		WebElement email_element = this.findVisibleElemntBy(By.xpath("//input[@id='form_item_email']"));
		email_element.clear();
		email_element.sendKeys(email);
		GlobalMethods.printDebugInfo("Set Email to: " + email);
	}

	public void setCreditCardCity(String city) {}
	public void setCreditCardAddress(String address) {}
	public void setCreditCardPostalCode(String postalCode) {}

	protected WebElement getAccountDropDown() {
		DepositPageCommElements pg = PageFactory.initElements(driver, DepositPageCommElements.class);
		//WebElement dropdown_account = this.findClickableElemntBy(By.id("accountNumber"));
		//WebElement dropdown_account = this.findClickableElemntByTestId("accountNumber");
		return pg.e_account;
	}


	/**
	 *
	 * @param account client's account number
	 * @return
	 */
	public String selectAccount(String account) {
		getAccountDropDown().click();
		waitLoading();
		List<WebElement> all_accounts = this.getAllOpendElements();
		String result = this.setAccount(account, all_accounts);
		if(result == null) {
			getAccountDropDown().click();
			waitLoading();
		}
		return result;
	}

	public void selectAccountNew(String account) {
		WebElement element = assertElementExists(By.xpath("//li[@data-testid='" + account + "']"), "Account Number: " + account);
		String info = element.getText().trim();
		triggerClickEvent(element);
		System.out.println("Select account: " + info);
	}

	public HashMap<String,String> getAllAvailableAccounts(){
		getAccountDropDown().click();
		waitLoading();

		List<WebElement> all_accounts = this.getAllOpendElements();
		HashMap<String,String> result = this.getAccounts(all_accounts);

		getAccountDropDown().click();
		waitLoading();

		return result;
	}


	public HashMap<String,String> getAllAccounts() {
		getAccountDropDown().click();
		waitLoading();

		List<WebElement> all_accounts = this.getAllOpendElements();
		HashMap<String,String> result = this.getAccounts(all_accounts);

		return result;
	}


	public void waitLoadingDepositAccountContent() {
		waitLoading();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(driver -> {
				try {
					boolean inputPresent = !driver.findElements(By.xpath("//div[@data-testid='accountNumber']")).isEmpty();
					boolean imagePresent = !driver.findElements(By.xpath("//div[@data-testid='formAccount']")).isEmpty();
					return inputPresent || imagePresent;

				} catch (Exception ex) {
					return false;
				}
			});
		} catch (Exception ex) {
			Assert.fail("No account available or timeout waiting for account listing");
		}
	}

	public void submit() {
		DepositPageCommElements pg = PageFactory.initElements(driver, DepositPageCommElements.class);
		WebElement submit_button = pg.e_submit;
		this.moveElementToVisible(submit_button);
		//submit_button.click();
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		javascript.executeScript("arguments[0].click()", submit_button);
		this.waitLoading();
		GlobalMethods.printDebugInfo("Submit deposit");
		this.moveContainerToTop();
	}

	public void setNotes(String notes) {
		DepositPageCommElements pg = PageFactory.initElements(driver, DepositPageCommElements.class);
		WebElement note_element = pg.e_notes;
		note_element.clear();
		note_element.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important notes to: " + notes);
	}

	public List<String> checkPaymentDetail()
	{
		waitLoading();
		List<String> paymentDetail = new ArrayList<>();

        try {
			WebElement accountDetail = findClickableElementByXpath("//div[@data-testid='payToAccount']");
			moveElementToVisible(accountDetail);
			paymentDetail.add(accountDetail.getText());

			WebElement depositAmount = findClickableElementByXpath("//div[@data-testid='depositAmount']");
			moveElementToVisible(accountDetail);
			if (depositAmount != null){
				paymentDetail.add(depositAmount.getText());

			}

			WebElement netDepositAmount = findClickableElementByXpath("//div[@data-testid='netDepositAmount']");
			moveElementToVisible(accountDetail);
			paymentDetail.add(netDepositAmount.getText());


		} catch (Exception e) {
           System.err.println("Error in checkPaymentDetail:"+ e.getMessage());
			e.printStackTrace();

        }

		return paymentDetail;
	}
	// 检查付款详情页信息(账号、入款金额、渠道、汇率)
	public double getConvertedAmount(){
		waitLoading();

		WebElement accountDetail = findVisibleElemntByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		WebElement convertedAmount = findVisibleElemntByXpath("//div[@data-testid='convertedAmount']");
        return extractNumericValue(convertedAmount.getText());
	}
	// 获取入金金额
	public double getNetDepositAmount(){
		waitLoading();

		WebElement accountDetail = findVisibleElemntByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		WebElement convertedAmount = findVisibleElemntByXpath("//div[@data-testid='netDepositAmount']");
		return extractNumericValue(convertedAmount.getText());
	}

	// 获取入金金额
	public double getDepositAmount(){
		waitLoading();

		WebElement accountDetail = findVisibleElemntByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		WebElement convertedAmount = findVisibleElemntByXpath("//div[@data-testid='depositAmount']");
		return extractNumericValue(convertedAmount.getText());
	}
	/**
	 * 从包含数值和货币代码的字符串中提取数值部分并转换为double类型
	 * 支持多种格式，如 "238.17 MYR", "1,000.50 USD", "$100.00" 等
	 *
	 * @param text 包含数值和货币的字符串
	 * @return 提取的数值
	 */
	public double extractNumericValue(String text) {
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("输入文本不能为空");
		}

		// 移除货币符号、逗号等非数值字符，保留数字、小数点和负号
		String numericPart = text.trim()
				.replaceAll("[^0-9\\.\\-]", "")  // 移除除数字、小数点、负号外的所有字符
				.replaceAll("\\.+", ".")         // 多个小数点替换为一个
				.replaceAll("^\\.", "")          // 移除开头的小数点
				.replaceAll("\\.$", "");         // 移除末尾的小数点

		// 确保只有一个负号且在开头
		boolean isNegative = numericPart.contains("-");
		numericPart = numericPart.replaceAll("[^0-9\\.]", "");

		if (numericPart.isEmpty()) {
			throw new RuntimeException("无法从文本中提取数值: " + text);
		}

		try {
			double value = Double.parseDouble(numericPart);
			return isNegative ? -value : value;
		} catch (NumberFormatException e) {
			throw new RuntimeException("无法解析数值: " + text, e);
		}
	}


	// 检查付款详情页信息
	public void checkPaymentPageDetail(String payMethod,double exchangeRate){
		waitLoading();
		WebElement element = findClickableElementByCss("div.channel-area");
		String paymentDetailText=element.getText();
		String paymentConfirmMethod=extractPaymentMethod(paymentDetailText);
		//校验支付方法
		LogUtils.info("支付确认详情页Payment method: " + paymentConfirmMethod);
		LogUtils.info("传入payMethod: " + payMethod);
		// 提取第一行作为支付方式名称
		if (payMethod != null && !payMethod.isEmpty()) {
			// 按行分割并取第一行
			String[] lines = payMethod.split("\\r?\\n");
			if (lines.length > 0) {
				payMethod= lines[0].trim();
			}
		}


		//校验渠道信息
		CustomAssert.assertTrue(paymentConfirmMethod != null && !paymentConfirmMethod.trim().isEmpty(), "Payment method is not correct. Expected: " + payMethod + ", Actual: " + paymentConfirmMethod);
		LogUtils.info(payMethod+"渠道信息校验成功");

		List<String> payNotice   = extractImportantNotes(paymentDetailText);
		//校验重要提示
		CustomAssert.assertNotNull(payNotice);

		//校验汇率
		double convertedAmount =getConvertedAmount();//获取页面转换值

		double netDepositAmount = getNetDepositAmount();//获取入金数额

		double calculatedRate = convertedAmount/netDepositAmount ;//计算汇率

		LogUtils.info("ConvertedAmount : " + convertedAmount+"  netDepositAmount : " + netDepositAmount+"  calculatedRate : " + calculatedRate);
		// 保留2位小数进行比较
		double expectedRateRounded = Math.round(exchangeRate * 100.0) / 100.0;
		double actualRateRounded = Math.round(calculatedRate * 100.0) / 100.0;

		LogUtils.info("Expected exchange rate (2 decimal places): " + expectedRateRounded);
		LogUtils.info("Actual exchange rate (2 decimal places): " + actualRateRounded);
		CustomAssert.assertEquals(expectedRateRounded, actualRateRounded, "Exchange rate is not correct. Expected: " + exchangeRate + ", Actual: " + actualRateRounded);
		LogUtils.info("汇率校验成功");
		CustomAssert.assertAll();

	}
	// 检查付款详情页信息(加密货币)
	public void checkCryptoPaymentPageDetail(String payMethod,String amount){
		waitLoading();
		WebElement element = findClickableElementByCss("div.channel-area");
		String paymentDetailText=element.getText();
		String paymentConfirmMethod=extractPaymentMethod(paymentDetailText);
		//校验支付方法
		LogUtils.info("支付确认详情页Payment method: " + paymentConfirmMethod);
		LogUtils.info("传入payMethod: " + payMethod);
		// 提取第一行作为支付方式名称
		if (payMethod != null && !payMethod.isEmpty()) {
			// 按行分割并取第一行
			String[] lines = payMethod.split("\\r?\\n");
			if (lines.length > 0) {
				payMethod= lines[0].trim();
			}
		}

		//校验渠道信息
		CustomAssert.assertTrue(paymentConfirmMethod.contains(payMethod), "Payment method is not correct. Expected: " + payMethod + ", Actual: " + paymentConfirmMethod);
		LogUtils.info(payMethod+"渠道信息校验成功");

		List<String> payNotice   = extractImportantNotes(paymentDetailText);
		//校验重要提示
		CustomAssert.assertNotNull(payNotice);
		// 校验入金地址和金额
		String cryptoAddress = extractDepositAddress(paymentDetailText);
		String cryptoAmount = extractDepositCurrencyAndAmount(paymentDetailText);
		CustomAssert.assertNotNull(cryptoAddress);
		LogUtils.info("Crypto address: " + cryptoAddress+"校验成功");
		CustomAssert.assertNotNull(cryptoAmount);
//		CustomAssert.assertTrue(cryptoAmount.contains(payMethod)&&cryptoAmount.contains(amount), "Crypto amount is not correct. Expected: " + payMethod + " "+amount+", Actual: " + cryptoAmount);
		LogUtils.info("Crypto Amount: " + cryptoAmount+"校验成功");

		CustomAssert.assertAll();

	}

	// 检查付款详情页信息(加密货币)
	public void checkOfflinePaymentPageDetail(String payMethod){
		waitLoading();
		WebElement element = findClickableElementByCss("div.channel-area");
		ScreenshotHelper.takeScreenshot(driver, element, "screenshots", "offline_payment_page.png");
		String paymentDetailText=element.getText();
		String paymentConfirmMethod=extractPaymentMethod(paymentDetailText);
		//校验支付方法
		LogUtils.info("支付确认详情页Payment method: " + paymentConfirmMethod);
		LogUtils.info("传入payMethod: " + payMethod);
		// 提取第一行作为支付方式名称
		if (payMethod != null && !payMethod.isEmpty()) {
			// 按行分割并取第一行
			String[] lines = payMethod.split("\\r?\\n");
			if (lines.length > 0) {
				payMethod= lines[0].trim();
			}
		}

		//校验渠道信息
		CustomAssert.assertTrue(paymentConfirmMethod != null && !paymentConfirmMethod.trim().isEmpty(), "Payment method is not correct. Expected: " + payMethod + ", Actual: " + paymentConfirmMethod);
		LogUtils.info(payMethod+"渠道信息校验成功");

		List<String> payNotice   = extractImportantNotes(paymentDetailText);
		//校验重要提示
		CustomAssert.assertNotNull(payNotice);
		// 校验文本上传
		CustomAssert.assertTrue(paymentDetailText.contains("Upload Receipt"), "Upload Receipt is not present.");
		//校验银行信息
		CustomAssert.assertTrue(paymentDetailText.contains("Bank Detail"), "Bank Detail is not present.");


		CustomAssert.assertAll();

	}
	// 检查信用卡、电子钱包付款详情页信息(CCEwallet)
	public void checkCCEwalletPaymentPageDetail(String payMethod) {
		waitLoading();
		WebElement element = findClickableElementByCss("div.channel-area");
		ScreenshotHelper.takeScreenshot(driver, element, "screenshots", "offline_payment_page.png");
		String paymentDetailText=element.getText();
		String paymentConfirmMethod=extractPaymentMethod(paymentDetailText);
		//校验支付方法
		LogUtils.info("支付确认详情页Payment method: " + paymentConfirmMethod);
		LogUtils.info("传入payMethod: " + payMethod);
		// 提取第一行作为支付方式名称
		if (payMethod != null && !payMethod.isEmpty()) {
			// 按行分割并取第一行
			String[] lines = payMethod.split("\\r?\\n");
			if (lines.length > 0) {
				payMethod= lines[0].trim();
			}
		}

		//校验渠道信息
		CustomAssert.assertTrue(paymentConfirmMethod != null && !paymentConfirmMethod.trim().isEmpty(), "Payment method is not correct. Expected: " + payMethod + ", Actual: " + paymentConfirmMethod);
		LogUtils.info(payMethod+"渠道信息校验成功");

		List<String> payNotice   = extractImportantNotes(paymentDetailText);
		//校验重要提示
		CustomAssert.assertNotNull(payNotice);


		CustomAssert.assertAll();
	}

	public List<String> checkPaymentDetailWithNoDA()
	{
		waitLoading();
		List<String> paymentDetail = new ArrayList<>();
		WebElement accountDetail = findClickableElementByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(accountDetail.getText());
		WebElement netDepositAmount = findClickableElementByXpath("//div[@data-testid='netDepositAmount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(netDepositAmount.getText());
		return paymentDetail;
	}

	public List<String> checkPaymentDetailWithNoNetDA()
	{
		waitLoading();
		List<String> paymentDetail = new ArrayList<>();
		WebElement accountDetail = findClickableElementByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(accountDetail.getText());
		WebElement netDepositAmount = findClickableElementByXpath("//div[@data-testid='depositAmount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(netDepositAmount.getText());
		return paymentDetail;
	}

	public void selectPaymentMethod() {
		WebElement inputPaymentMethod = driver.findElement(By.xpath("//input[@id='form_item_paymentMethod']"));
		inputPaymentMethod.click();

		List<WebElement> paymentMethods = driver.findElements(By.xpath("//div[@class='ant-select-item-option-content']"));
		Random random = new Random();
		paymentMethods.get(random.nextInt(paymentMethods.size())).click();
	}

	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
		submit();
	}
	//Select deposit method
	public String setDepositMethod(GlobalProperties.DEPOSITMETHOD method) {
		waitLoadingForCustomise(120);

		this.findVisibleElemntByXpath("//iframe[@id='pcs-iframe']");
		waitLoading();
		driver.switchTo().frame("pcs-iframe");
		waitLoading();

		// Wait for deposit method listing to load before selecting
		waitPaymentLoader();

		try {
			/*WebElement m_element = driver.findElement(By.xpath("//div[contains(translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"+
					method.getdepositDatetestid().toLowerCase() +"')]"));*/
			WebElement m_element = driver.findElement(By.xpath(
					"//div[contains(translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" +
							method.getdepositDatetestid().toLowerCase() +
							"') and not(.//div[@class='disable-mask'])]"
			));

			this.moveElementToVisible(m_element);
			m_element.click();
			waitLoading();
			System.out.println("Select deposit method: " + method.getWebName());
			return method.getWebName();
		} catch(Exception e) {
			e.printStackTrace();
			GlobalMethods.printDebugInfo("Could not find the deposit method: " + method.getWithdrawName());
			Assert.fail("Could not find the deposit method: " + method.getWithdrawName());
		}

		return null;
	}

	public String setDepositMethodDisabled(GlobalProperties.DEPOSITMETHOD method) {
		waitLoadingForCustomise(120);

		this.findVisibleElemntByXpath("//iframe[@id='pcs-iframe']");
		waitLoading();
		driver.switchTo().frame("pcs-iframe");
		waitLoading();

		// Wait for deposit method listing to load before selecting
		waitPaymentLoader();

		try {
			WebElement m_element = driver.findElement(By.xpath(
					"//div[contains(translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" +
							method.getdepositDatetestid().toLowerCase() +
							"')]"
			));

			this.moveElementToVisible(m_element);
			m_element.click();
			waitLoading();
			System.out.println("Select deposit method: " + method.getWebName());
			return method.getWebName();
		} catch(Exception e) {
			e.printStackTrace();
			GlobalMethods.printDebugInfo("Could not find the deposit method: " + method.getWithdrawName());
		}

		return null;
	}
	// 检查入金金额不足弹框
	public boolean checkDepositAmountNotMetPopWindow() {
	WebElement popWindow = findVisibleElemntByXpath("//div[./div[contains(@class, 'el-dialog__body')]//div[text()='Deposit Amount Not Met']]");
	ScreenshotHelper.highlightElement(driver, popWindow);
	boolean isDisplayed =popWindow.isDisplayed();
	return isDisplayed;
	}
	// 检查KYC未通过弹框
	public boolean checkDepositKYCNotMetPopWindow() {
	WebElement popWindow = findVisibleElemntByXpath("//div[contains(@class, 'title') and normalize-space(text())='Advance KYC Authentication Required']|//div[contains(@class, 'title') and normalize-space(text())='Advance KYC Verification Required']");
	ScreenshotHelper.highlightElement(driver, popWindow);
	boolean isDisplayed =popWindow.isDisplayed();
	return isDisplayed;
	}

	//点击 Choose Another Payment Method
	public void clickChooseAnotherPaymentMethod() {
		WebElement chooseAnotherPaymentMethod = findVisibleElemntByXpath("//button[contains(., 'Choose Another Payment Method')]");
		ScreenshotHelper.highlightElement(driver, chooseAnotherPaymentMethod);

		chooseAnotherPaymentMethod.click();

	}


	public void checkPaymentMethodDisabled(String testId) {
		WebElement paymentMethod = findVisibleElementByContainTestId(testId);
		ScreenshotHelper.highlightElement(driver, paymentMethod);

		boolean isDisabled =paymentMethod.findElement(By.xpath("./div[@class='disable-mask']")).isDisplayed();
		CustomAssert.assertTrue(isDisabled, "Payment method is not disabled.");
		CustomAssert.assertAll();
		LogUtils.info("Payment method is disabled.");
	}
	//点击Change Amount
	public void clickChangeAmount() {
        try {
            WebElement changeAmount = findVisibleElemntByXpath("//button[.//span[text()='Change Amount']]");
			ScreenshotHelper.highlightElement(driver, changeAmount);

			changeAmount.click();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	// 检查change Amount Not Met Error Pop Window
	public boolean checkDepositAmountNotMetErrorPopWindow() {
		WebElement popWindow = findVisibleElemntByXpath("//div[contains(@class, 'el-message--error')]//p[text()='Amount Change Failed']");
		boolean isDisplayed =popWindow.isDisplayed();
		return isDisplayed;
	}

	public boolean checkDepositAmountNotMetSuccessPopWindow() {
		WebElement popWindow = findVisibleElemntByXpath("//div[contains(@class, 'el-message')]//p[text()='Amount Change Success']");
		ScreenshotHelper.highlightElement(driver, popWindow);
		boolean isDisplayed =popWindow.isDisplayed();
		return isDisplayed;
	}
	public void setAccountNumber(String accountNumber) {
		WebElement accountNum = findVisibleElemntBy(By.xpath("//input[contains(@id,'card_number')]"));
		accountNum.sendKeys(accountNumber);

	}

	public void setPersonalID(String personalID) {
		WebElement personalIDEle= findVisibleElemntBy(By.xpath("//input[contains(@id,'personal_id')]"));
		personalIDEle.sendKeys(personalID);

	}

	public void clickContinue() {
		WebElement continueBtn = findVisibleElemntBy(By.xpath("//button[@data-testid='continue']"));
		this.moveElementToVisible(continueBtn);
		js.executeScript("arguments[0].click()",continueBtn);
		waitLoadingForCustomise(120);

		waitPaymentGreyLoader();
	}

	public void payNow() {
		waitLoading();
		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);
		GlobalMethods.printDebugInfo("Submit Payment");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        try {
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-testid='payNow']")));
        } catch (Exception e) {
            e.printStackTrace();
        }
//		waitLoadingForCustomise(120);
	}
	public void goToIframe(String frameName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));

		try {
			// 等待iframe元素出现并可交互
			WebElement iframeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(frameName)));

			// 等待iframe可见且可切换
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frameName)));

			// 额外等待iframe内部文档加载完成
			wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

		} catch (WebDriverException e) {
			if (e.getMessage().contains("target frame detached")) {
				// 如果出现frame detached错误，稍等后重试一次
				try {
					Thread.sleep(2000);
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frameName)));
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Thread interrupted while handling frame detachment", ie);
				}
			} else {
				throw e; // 重新抛出其他异常
			}
		}
	}
	public void payNowCC(){
		waitLoading();
		boolean isElementPresent = !driver.findElements(By.xpath("//div[./span[@class='popup-text']]")).isEmpty();
		if (isElementPresent) {
			WebElement terms = findVisibleElemntByXpath("//div[./span[@class='popup-text']]");
			terms.click();
		} else {
			System.out.println("no need to agree terms and conditions");
		}
		terms();
		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);
        GlobalMethods.printDebugInfo("Submit Payment");
		waitLoading();

		waitLoadingForCustomise(120);
	}
	public void terms()
	{
		try {
			WebElement terms = driver.findElement(By.xpath("//input[@type='checkbox']"));
			this.moveElementToVisible(terms);
			js.executeScript("arguments[0].click()", terms);
		}catch(Exception e)
		{
			GlobalMethods.printDebugInfo(e.toString());
		}
	}

	public void checkDepositErrorMsg() {
		WebElement eleErr = checkElementExists(By.cssSelector("div.el-message.el-message--error>p"));

		if (eleErr != null) {
			String errMsg = eleErr.getText();
			Assert.fail(errMsg);
		}
	}

	public boolean checkFileUploadText() {
		String pageSource = getPageTextContent();
		GlobalMethods.printDebugInfo("pageSource:" + pageSource);

		boolean hasUploadErrorText = pageSource.contains("Please upload a receipt");
		boolean hasErrorElement = false;

		try {
			WebElement errorElement = driver.findElement(By.xpath("//div[@class='ant-form-item-explain-error']"));
			hasErrorElement = errorElement.isDisplayed();
			GlobalMethods.printDebugInfo("uploadErrorInfo:" + hasErrorElement);
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("Error element not found or not visible: " + e.getMessage());
			hasErrorElement = false;
		}

		return hasUploadErrorText || hasErrorElement;
	}


	public void payNowWithoutUpload() {

		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);

		GlobalMethods.printDebugInfo("Submit Payment");
	}

	public void setVerificationCode() {

	}

	// 在合适的包中创建此枚举类
	public enum DepositTab {
		LOCAL_BANK_TRANSFER(0, "Local Bank Transfer"),
		CREDIT_CARD(1, "Credit Card"),
		CRYPTOCURRENCY(2, "Cryptocurrency"),
		E_WALLET(3, "E-wallet"),
		OFFLINE_TRANSFER(4, "Offline Transfer");

		private final int index;
		private final String tabName;

		DepositTab(int index, String tabName) {
			this.index = index;
			this.tabName = tabName;
		}

		public int getIndex() {
			return index;
		}

		public String getTabName() {
			return tabName;
		}

		public static DepositTab fromIndex(int index) {
			for (DepositTab tab : DepositTab.values()) {
				if (tab.getIndex() == index) {
					return tab;
				}
			}
			throw new IllegalArgumentException("Invalid tab index: " + index);
		}
	}

	// 点击入金 tab 栏
	public void clickDepositFundsTab(DepositTab tab) {
		String tabName = tab.getTabName();
		System.out.println("Click deposit funds tab: " + tabName);
		LogUtils.info("Click deposit funds tab: " + tabName);
		System.out.println("Click deposit funds tab: " + String.format("//div[contains(@data-testid,'%s')]", tabName));
        WebElement ele;
        try {
            ele = findVisibleElemntByXpath(String.format("//div[contains(@data-testid,'%s')]", tabName));
			ScreenshotHelper.highlightElement(driver, ele);
			clickElement( ele);
//			ele.click();

		} catch (Exception e) {
           ScreenshotHelper.addTextWatermark(driver,e.getMessage());
        }


	}

	// 如果需要保留原来的方法以保持向后兼容性，可以重载这个方法
	public void clickDepositFundsTab(int index) {
		clickDepositFundsTab(DepositTab.fromIndex(index));
	}

//	//获取Deposit Funds 页面默认入金账号信息
	public List<WebElement> getDepositFundsDefaultAccountInfo(String brandName)  {
		System.out.println("================getDepositFundsDefaultAccountInfo================");
        WebElement wl= null;
        try {
            wl = super.findVisibleElemntByTestId("formAccount");
			String accountInfo = wl.getText();
			System.out.println(accountInfo);
			wl.click();
			List<WebElement> accountListInfo= super.getAllOpendElements();

			return accountListInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
			takeScreenshot(driver, wl, "screenshots", brandName);

		}

	}

    public String extractPaymentMethod(String text) {
		// 提取第一行作为支付方式名称
		String[] lines = text.split("\n");
		if (lines.length > 0) {
			return lines[0].trim();
		}
		return "";
	}

	public String extractFeeInfo(String text) {
		// 查找包含"Fee"关键词的信息
		Pattern feePattern = Pattern.compile(".*([Ff]ee.*?)[\n|.]");
		Matcher matcher = feePattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}

	private String extractProcessingTime(String text) {
		// 查找包含处理时间的信息
		Pattern timePattern = Pattern.compile("([0-9]+\\s*[-]?\\s*[0-9]*\\s*business\\s*day.*)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = timePattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}

	public List<String> extractImportantNotes(String text) {
		List<String> notes = new ArrayList<>();

		// 查找以数字开头的列表项
		Pattern notePattern = Pattern.compile("[0-9]+\\.\\s*(.*)");
		Matcher matcher = notePattern.matcher(text);

		while (matcher.find()) {
			notes.add(matcher.group(1).trim());
		}

		return notes;
	}

	private Map<String, Object> extractFileRequirements(String text) {
		Map<String, Object> fileReq = new HashMap<>();

		// 提取支持的文件类型
		Pattern typePattern = Pattern.compile("Supported file types:\\s*([^\n]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher typeMatcher = typePattern.matcher(text);
		if (typeMatcher.find()) {
			String[] types = typeMatcher.group(1).split(",");
			for (int i = 0; i < types.length; i++) {
				types[i] = types[i].trim();
			}
			fileReq.put("supportedTypes", types);
		}

		// 提取最大文件大小
		Pattern sizePattern = Pattern.compile("Maximum upload file size:\\s*([^\n]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher sizeMatcher = sizePattern.matcher(text);
		if (sizeMatcher.find()) {
			fileReq.put("maxFileSize", sizeMatcher.group(1).trim());
		}

		// 提取最大文件数量
		Pattern numPattern = Pattern.compile("Maximum file number:\\s*([0-9]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher numMatcher = numPattern.matcher(text);
		if (numMatcher.find()) {
			fileReq.put("maxFileNumber", Integer.parseInt(numMatcher.group(1)));
		}

		return fileReq;
	}

	public Map<String, String> extractPaymentDetails(String text) {
		Map<String, String> details = new HashMap<>();

		// 提取支付详情中的键值对
		Pattern detailPattern = Pattern.compile("([A-Za-z ]+)\\s*\\n\\s*([0-9A-Za-z\\. \\-]+)");
		Matcher matcher = detailPattern.matcher(text);

		while (matcher.find()) {
			String key = matcher.group(1).trim();
			String value = matcher.group(2).trim();
			if (!key.isEmpty() && !value.isEmpty()) {
				// 标准化键名
				String normalizedKey = key.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
				details.put(key, value);
			}
		}

		return details;
	}
	// 获取支付渠道名称
	public String getPaymentMethod(GlobalProperties.DEPOSITMETHOD depositMethod) {
		String depositMethodDataTestId=depositMethod.getdepositDatetestid();
		LogUtils.info("getPaymentMethod:"+depositMethodDataTestId);
		String paymentMethod = findVisibleElementByContainTestId(depositMethodDataTestId).getText();
		System.out.println("paymentMethod:"+paymentMethod);
		if (paymentMethod.contains("$")) {
			String result = paymentMethod.substring(0, paymentMethod.indexOf('$')).trim();
			return result;
		}else {
			return paymentMethod;
		}

	}


	// 支付详情页相关方法（加密货币）
	public static String extractDepositAddress(String text) {

		// 提取入金地址 - 支持多种加密货币地址格式
		// 以0x开头的以太坊类地址（ETH, USDT-ERC20, USDC等）
		String depositAddress = "";
		Pattern ethAddressPattern = Pattern.compile("\\b(0x[a-fA-F0-9]{40})\\b");
		Matcher ethAddressMatcher = ethAddressPattern.matcher(text);
		if (ethAddressMatcher.find()) {
			return ethAddressMatcher.group(1);
		} else {
			// 比特币地址格式1（P2PKH）
			Pattern btcLegacyPattern = Pattern.compile("\\b([1][a-km-zA-HJ-NP-Z1-9]{25,34})\\b");
			Matcher btcLegacyMatcher = btcLegacyPattern.matcher(text);
			if (btcLegacyMatcher.find()) {
				depositAddress = btcLegacyMatcher.group(1);
//				return btcLegacyMatcher.group(1);
			} else {
				// 比特币地址格式3（P2SH）
				Pattern btcScriptPattern = Pattern.compile("\\b([3][a-km-zA-HJ-NP-Z1-9]{25,34})\\b");
				Matcher btcScriptMatcher = btcScriptPattern.matcher(text);
				if (btcScriptMatcher.find()) {
					depositAddress = btcScriptMatcher.group(1);
//					return btcScriptMatcher.group(1);
				} else {
					// 比特币Bech32地址格式（bc1）
					Pattern btcBech32Pattern = Pattern.compile("\\b(bc1[a-z0-9]{39,59})\\b");
					Matcher btcBech32Matcher = btcBech32Pattern.matcher(text);
					if (btcBech32Matcher.find()) {
						depositAddress = btcBech32Matcher.group(1);
//						return btcBech32Matcher.group(1);
					}
				}
			}
		}
		return depositAddress;
	}

	//提取币种和金额（加密货币）
	public static String extractDepositCurrencyAndAmount(String text) {

		String depositAmount = "";
		// 提取入金金额和币种
		// 首先找到"Enter the amount you want to deposit"附近的币种和金额
		int depositAmountIndex = text.indexOf("Enter the amount you want to deposit");
		if (depositAmountIndex != -1) {
			// 在该位置之后查找币种和金额
			int searchStart = depositAmountIndex;
			int searchEnd = Math.min(text.length(), depositAmountIndex + 500);
			String searchText = text.substring(searchStart, searchEnd);

			// 匹配格式如 "ETH 0.01164388" 或 "BTC 0.00045204" 或 "USDC-BEP20 50.000000"
			Pattern amountPattern = Pattern.compile("\\b([A-Z][A-Z0-9]*(?:-[A-Z0-9]+)*)\\s+([0-9]+\\.?[0-9]*)\\b");
			Matcher amountMatcher = amountPattern.matcher(searchText);

			if (amountMatcher.find()) {
				depositAmount=amountMatcher.group(1)+amountMatcher.group(2);

			}
		} else {
			// 如果找不到特定标识，则在整个文本中查找
			Pattern amountPattern = Pattern.compile("\\b([A-Z][A-Z0-9]*(?:-[A-Z0-9]+)*)\\s+([0-9]+\\.?[0-9]*)\\b");
			Matcher amountMatcher = amountPattern.matcher(text);

			if (amountMatcher.find()) {
				depositAmount=amountMatcher.group(1)+amountMatcher.group(2);

			}
		}

		return depositAmount;
	}
	// 返回到上级菜单
	public void back() {
		clickElement(findVisibleElemntByXpath("//p[contains(text(),'Back')]"));
	}

	//current open for AU golden flow
	public double getConvertedDepositMaxAmount() {
		try {
			String depositLimitEle = driver.findElement(By.xpath("//div[@class='kyc-verify']")).getText();
			GlobalMethods.printDebugInfo("Deposit Funds Page found text: " + depositLimitEle);

			Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
			Matcher matcher = pattern.matcher(depositLimitEle);
			double depositMaxAmount = 0.0;
			if (matcher.find()) {
				String maxAmount = matcher.group();
				 depositMaxAmount = Double.parseDouble(maxAmount);
			}
			return depositMaxAmount;
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("Deposit Funds Page - Not found text of Max deposit amount.");
			return -1;
		}
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv) {
		WebElement state=findVisibleElemntByXpath("//*[@id=\"mat-input-2\"]");
		state.click();
		WebElement country=findVisibleElemntByXpath("//*[@id=\"mat-option-0\"]");
		country.click();
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-0\"]"), cardNumber);
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-1\"]"), expiryDate);
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-3\"]"), cvv);
		WebElement submit = this.findClickableElementByXpath("//button[contains(@class, 'btn-submit') and @type='submit']");
		submit.click();


	}


	public boolean isCCDepositSuccess() {
		try {
			// 1. 定位iframe元素（关键步骤）

			WebElement iframeElement =this.findVisibleElemntByXpath("//*[contains(@class, 'slogan') and contains(., 'Your deposit was successful')]");

			// 2. 判断iframe是否展示
			boolean isIframeDisplayed = iframeElement.isDisplayed();

			if (isIframeDisplayed) {
				LogUtils.info("iframe 在页面上是可见的。");
				return true;
			} else {
				LogUtils.info("iframe 在页面上不可见（可能被CSS隐藏）。");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.info("在页面上未找到指定的iframe元素，它可能不存在或尚未加载。");
		}
		return false;
	}

}

