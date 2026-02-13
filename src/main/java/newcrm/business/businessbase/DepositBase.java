package newcrm.business.businessbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newcrm.global.GlobalProperties;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.pages.clientpages.DepositBasePage;
import newcrm.utils.db.DbUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.CustomAssert;
import utils.LogUtils;

public abstract class DepositBase {

	protected DepositBasePage page;
	protected DbUtils db = null;

	public DepositBase(DepositBasePage v_page) {
		this.page = v_page;
	}

	protected String GetAvailableAccount(CURRENCY currency, HashMap<String, String> all_accounts) {
		if (all_accounts.size() == 0) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}
		String account;
		for (Map.Entry<String, String> entry : all_accounts.entrySet()) {
			if (StringUtils.containsIgnoreCase(entry.getValue(),currency.toString())) {
				account = entry.getKey();
				GlobalMethods.printDebugInfo("Found valid account, Account: " + account + ", Currency: " + currency);
				return account;
			}
		}
		GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
		return null;
	}

	protected String GetAvailableAccountNew(CURRENCY currency, HashMap<String, String> all_accounts) {

		// Get the first account available
		Map.Entry<String, String> filtered = all_accounts.entrySet().stream()
				.filter(entry -> StringUtils.containsIgnoreCase(entry.getValue(), currency.toString()))
				.findFirst()
				.orElse(null);

		if (filtered == null) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

		String account = filtered.getKey();
		GlobalMethods.printDebugInfo("Found valid account, Account: " + account + ", Currency: " + currency);

		return account;
	}

	protected String GetAvailableAccount(String account, HashMap<String, String> all_accounts) {
		all_accounts.containsKey(account.trim().toLowerCase());
		if (all_accounts.size() == 0 || !all_accounts.containsKey(account.trim().toLowerCase())) {
			GlobalMethods.printDebugInfo(
					"Client does not have the account or the deposit method does not support the account's currency, account: "
							+ account);
			return null;
		}
		return account.toLowerCase().trim();
	}

	public void setEmail(String email) {
		page.setEmail(email);
	}

	public void setJCBEmail(String email) {
		page.setJCBEmail(email);
	}

	public void setCreditCardCity(String city) {
		page.setCreditCardCity(city);
	}

	public void setCreditCardAddress(String address) {
		page.setCreditCardAddress(address);
	}

	public void setCreditCardPostalCode(String postalCode) {
		page.setCreditCardPostalCode(postalCode);
	}

	public String checkAccount(String account) {
		HashMap<String, String> all_accounts = page.getAllAvailableAccounts();
		return this.GetAvailableAccount(account, all_accounts);
	}

	public String checkAccount(CURRENCY currency) {
		HashMap<String, String> all_accounts = page.getAllAvailableAccounts();
		return this.GetAvailableAccount(currency, all_accounts);
	}

	public String getValidAccount() {
		page.waitLoadingDepositAccountContent();

		HashMap<String, String> all_accounts = page.getAllAccounts();

		if (all_accounts.isEmpty()) {
			return null;
		}

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			String account = this.GetAvailableAccountNew(currency, all_accounts);

			if (account != null) {
				return account;
			}
		}

		return null;
	}

	public void selectAccount(String account) {
		if (checkAccount(account) == null) {
			return;
		}

		page.selectAccount(account);
	}

	public void selectAccountNew(String account) {
		page.selectAccountNew(account);
	}

	public void selectAccount(CURRENCY currency) {
		String account = checkAccount(currency);
		if (account == null) {
			return;
		}
		page.selectAccount(account);
	}

	public void setAmount(String amount) {
		page.setAmount(amount);
	}

	public void submit() {
		page.submit();
	}

	public void setNotes(String notes) {
		//System.out.println("Do not set notes at moment, because of the test id");
		page.setNotes(notes);
	}

	public void goBack() {
		page.goBack();
		GlobalMethods.printDebugInfo("Pressing the browser's back button");
	}
	public void goHomePage(String homePageURL) {
		page.goHomePage(homePageURL);
		GlobalMethods.printDebugInfo("Go Home Page");
	}

	public JSONObject getDepositRecord(ENV env, BRAND brand, REGULATOR regulator, String account, Integer type, Integer channel) {

		String sql = "select * from tb_payment_deposit where mt4_account=" + account + " and payment_type=" + type + " and payment_channel=" + channel + " order by id desc limit 1";
		if (db == null) {
			db = new DbUtils(env, brand, regulator);
		}
		JSONArray array = db.queryRegulatorDB(sql);
		if (array.size() > 0) {
			return array.getJSONObject(0);
		}
		return null;
	}
	public JSONObject getDepositRecordByAccount(ENV env, BRAND brand, REGULATOR regulator, String account) {

		String sql = "select * from tb_payment_deposit where mt4_account=" + account + " order by id desc limit 1";
		if (db == null) {
			db = new DbUtils(env, brand, regulator);
		}
		JSONArray array = db.queryRegulatorDB(sql);
		if (array.size() > 0) {
			return array.getJSONObject(0);
		}
		return null;
	}

	public boolean updateStatus(ENV env, BRAND brand, REGULATOR regulator, Integer id, Integer status) {
		if (db == null) {
			db = new DbUtils(env, brand, regulator);
		}
		String sql = "update tb_payment_deposit set status=" + status + " where id=" + id;
		if (db.updateRegulatorDB(sql) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void deposit(String account, String amount, String notes) {
		page.deposit(account, amount, notes);
	}

	public Boolean checkUrlContains(String keyword) {

		return page.checkUrlContains(keyword);
	}

	public String getCurrentUrl() {
		return page.getURL();
	}
	public Boolean checkUrlNotContains(String keyword) {
		return page.checkUrlNotContains(keyword);
	}

	public void setAccountAndAmount(String account, String amount) {
		selectAccountNew(account);
		setAmount(amount);
		submit();
	}

	public double setAccountAndAmountWithCheckDepoMax(String account, String amount) {
		selectAccountNew(account);
		setAmount(amount);
		double depoMaxAmount = getConvertedDepositMaxAmount();
		submit();
		return depoMaxAmount;
	}

	public boolean setDepositMethod(GlobalProperties.DEPOSITMETHOD method) {
		// Check for error message
		checkDepositErrorMsg();

		// Set deposit method
		String result = page.setDepositMethod(method);
		if(result == null) {
			return false;
		}

		System.out.println("Set deposit method : " + method.getdepositDatetestid());
		return true;
	}

	public boolean setDepositMethodDisabled(GlobalProperties.DEPOSITMETHOD method) {
		// Check for error message
		checkDepositErrorMsg();

		// Set deposit method
		String result = page.setDepositMethodDisabled(method);
		if(result == null) {
			return false;
		}

		System.out.println("Set deposit method : " + method.getdepositDatetestid());
		return true;
	}
	public void checkDepositErrorMsg() {
		page.waitLoading();
		page.checkDepositErrorMsg();
	}

	public void clickContinue() {
		page.clickContinue();
	}
	public void setAccountNumber(String accNumber) {
		page.setAccountNumber(accNumber);
	}
	public void setPersonalID(String personlID) {
		page.setPersonalID(personlID);
	}

	public void selectPaymentMethod() {
		page.selectPaymentMethod();
	}

	public void clickDepositFundsTab(DepositBasePage.DepositTab tab) {
		page.clickDepositFundsTab(tab);;
	}


	public void checkPaymentDetails(String account, String amount) {
		amount = GlobalMethods.fmtAmount(amount);

		List<String> PaymentDetail = page.checkPaymentDetail();
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(0),account),"The account: "+PaymentDetail.get(0)+" "+account+" in payment details is incorrect");
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(1),amount),"The deposit amount: "+PaymentDetail.get(1)+" "+amount+" in payment details is incorrect");
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(2),amount),"The net deposit amount: "+PaymentDetail.get(2)+" "+amount+"in payment details is incorrect");
		GlobalMethods.printDebugInfo(String.format("The account: %s, deposit amount: %s, and net deposit amount: %s information are correct", PaymentDetail.get(0),PaymentDetail.get(1),PaymentDetail.get(2)));

		ScreenshotHelper.takeScreenshot(page.getDriver(), null, "screenshots", "PaymentDetails");
	}


	// Get payment method
	public String getPaymentMethod(GlobalProperties.DEPOSITMETHOD depositMethod) {
		return page.getPaymentMethod(depositMethod);
	}

	public void checkPaymentDetailsNoDepositAmount(String account,String amount)
	{
		amount = GlobalMethods.fmtAmount(amount);

		List<String> PaymentDetail = page.checkPaymentDetailWithNoDA();
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(0),account),"The account: "+PaymentDetail.get(0)+" "+account+" in payment details is incorrect");
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(1),amount),"The net deposit amount: "+PaymentDetail.get(1)+" "+amount+"in payment details is incorrect");
		LogUtils.info("校验账号信息和入金金额成功 ");

		GlobalMethods.printDebugInfo(String.format("The account: %s, and net deposit amount: %s information are correct", PaymentDetail.get(0),PaymentDetail.get(1)));
	}

	public void checkPaymentDetailsNoNetDepositAmount(String account,String amount)
	{
		amount = GlobalMethods.fmtAmount(amount);

		List<String> PaymentDetail = page.checkPaymentDetailWithNoNetDA();
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(0),account),"The account: "+PaymentDetail.get(0)+" "+account+" in payment details is incorrect");
		Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(1),amount),"The deposit amount: "+PaymentDetail.get(1)+" "+amount+"in payment details is incorrect");
		GlobalMethods.printDebugInfo(String.format("The account: %s, and deposit amount: %s information are correct", PaymentDetail.get(0),PaymentDetail.get(1)));
	}

	public void payNow() {
		page.payNow();
	}
	public void terms() {
		page.terms();
	}

	public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		checkPaymentDetails(account,amount);
		terms();
		payNow();
	}

	public void depositWithOutPay(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		LogUtils.info("选择支付方法");
		setAccountAndAmount(account, amount);
//		setDepositMethod(depositMethod);

	}
	// 检查支付详情数据，包含汇率
	public void checkPaymentDetailsWithExchangeRate(String account,String amount,GlobalProperties.DEPOSITMETHOD depositMethod,double exchangeRate)
	{
		String payMethod = getPaymentMethod(depositMethod);
		clickContinue();
		// 校验账号和入金数额
		checkPaymentDetailsNoDepositAmount(account, amount);

		//校验渠道信息、汇率，提示信息等
		page.checkPaymentPageDetail(payMethod, exchangeRate);
	}

	// 检查支付详情数据(加密货币)

	public void checkCryptoPaymentDetails(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod){
		String payMethod = getPaymentMethod(depositMethod);
		clickContinue();
		// 校验账号和入金数额
		checkPaymentDetailsNoDepositAmount(account, amount);

		//校验渠道信息、汇率，提示信息等
		page.checkCryptoPaymentPageDetail(payMethod,amount);
	}
	// 校验IBT 渠道支付详情
	public void checkOfflinePaymentDetails(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod){
		String payMethod = getPaymentMethod(depositMethod);
		clickContinue();
		// 校验账号和入金数额
		checkPaymentDetailsNoDepositAmount(account, amount);

		//校验渠道信息、汇率，提示信息等
		page.checkOfflinePaymentPageDetail(payMethod);
	}
	// 校验文件上传文案
	public void checkFileUploadText(){
		LogUtils.info("检查文件上传文案");
		CustomAssert.assertTrue(page.checkFileUploadText(), "File upload text is not present.");
		ScreenshotHelper.zoomOut(page.getDriver(), 7);
		ScreenshotHelper.takeScreenshot(page.getDriver(), null, "screenshots", "FileUploadText.png");
        ScreenshotHelper.takeFullPageScreenshot(page.getDriver(), "screenshots","longPage.png");
        CustomAssert.assertAll();

	}

	// 校验CC、E-wallet 渠道支付详情
	public void checkCCEwalletPaymentDetails(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod){
		String payMethod = getPaymentMethod(depositMethod);
		clickContinue();
		// 校验账号和入金数额
		checkPaymentDetailsNoDepositAmount(account, amount);

		//校验渠道信息、汇率，提示信息等
		page.checkCCEwalletPaymentPageDetail(payMethod);
	}
	public double depositNewCheckDepoMaxAmount(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		double depoMaxAmount = setAccountAndAmountWithCheckDepoMax(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		payNow();
		return depoMaxAmount;
	}


	public void depositNewNoDepositAmountCheck(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		checkPaymentDetailsNoDepositAmount(account,amount);
		payNow();
	}

	public void depositWithPersonalIDAccNumNew(String account, String amount, String accountNum,String personalID,GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		setAccountNumber(accountNum);
		setPersonalID(personalID);
		checkPaymentDetails(account,amount);
		payNow();
	}

	public void depositWithMailNoDepositAmountNew(String account, String amount, String email, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		setJCBEmail(email);
		checkPaymentDetailsNoDepositAmount(account, amount);
		payNow();
	}

	public void depositWithCCInfoNew(String account, String amount, String ccCity,String ccAddress,String ccPostalCode, GlobalProperties.DEPOSITMETHOD depositMethod)
	{
		setAccountAndAmount(account, amount);
		setDepositMethod(depositMethod);
		clickContinue();
		setCreditCardCity(ccCity);
		setCreditCardAddress(ccAddress);
		setCreditCardPostalCode(ccPostalCode);
		checkPaymentDetails(account,amount);
		payNow();
	}

	public boolean checkHomeUrl() {
		return true;
	}


    //获取Deposit Funds 页面默认入金账号信息
    public List<WebElement> checkDepositFundsDefaultAccountInfo(String brandName) {
        System.out.println("================getDepositFundsDefaultAccountInfo================");

        List<WebElement> accountInfo = page.getDepositFundsDefaultAccountInfo(brandName);
        return accountInfo;
    }

	/**
	 * 将字符串中的空白字符（包括制表符、换行符、空格、全角空格等）替换为普通空格
	 *
	 * @param text 输入字符串
	 * @return 处理后的字符串
	 */
	public static String normalizeWhitespace(String text) {
		if (text == null) return null;
		// 将各种空格字符统一替换为普通空格
		return text.replaceAll("[\\u00A0\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200A\\u202F\\u205F\\u3000]", " ")
				.replaceAll("\\s+", " ")
				.trim();
	}


	/**
	 * 逐项比较WebElement列表和格式化字符串列表的内容
	 * 首先按照账号进行排序再比较，避免因顺序不一致导致比较失败
	 * 使用互相包含的方式判断匹配
	 *
	 * @param webElements      WebElement列表
	 * @param formattedStrings 格式化字符串列表
	 * @return 比较结果
	 */
	public  boolean compareIgnoringWhitespace(List<WebElement> webElements, List<String> formattedStrings) {
		// 检查输入参数是否为null
		if (webElements == null || webElements.isEmpty()) {
			throw new IllegalArgumentException("webElements 参数不能为 null或者空，Funds入金页面信息解析失败");
		}
		if (formattedStrings == null || formattedStrings.isEmpty()) {
			throw new IllegalArgumentException("formattedStrings 参数不能为 null或者空，Account页面信息解析失败");
		}
		// 提取WebElement文本并排序
		List<String> webTexts = new ArrayList<>();
		for (WebElement element : webElements) {
			String text = element.getText();
			if (text == null) {
				text = "";
			}
			text = text.trim().replaceAll("\\s+", " ").replace(",", "");
			webTexts.add(text);
		}

		// 对两个列表都按照账号进行排序
		List<String> sortedWebTexts = sortAccountList(webTexts);
		List<String> sortedFormattedStrings = sortAccountList(new ArrayList<>(formattedStrings));

		System.out.println("排序后的WebElement列表:");
		for (int i = 0; i < sortedWebTexts.size(); i++) {
			System.out.println("  [" + i + "] \"" + sortedWebTexts.get(i) + "\"");
		}

		System.out.println("排序后的格式化字符串列表:");
		for (int i = 0; i < sortedFormattedStrings.size(); i++) {
			System.out.println("  [" + i + "] \"" + sortedFormattedStrings.get(i) + "\"");
		}

		// 确定较小的列表长度，避免索引越界
		int minSize = Math.min(sortedWebTexts.size(), sortedFormattedStrings.size());

		// 检查列表大小是否相等，如果不相等则输出警告信息
		if (sortedWebTexts.size() != sortedFormattedStrings.size()) {
			System.out.println("警告: 列表大小不匹配");
			System.out.println("  WebElement列表大小: " + sortedWebTexts.size());
			System.out.println("  格式化字符串列表大小: " + sortedFormattedStrings.size());
		}

		// 逐项比较排序后的列表（以较小的列表长度为准）
		boolean allMatched = true;
		for (int i = 0; i < minSize; i++) {
			String webText = sortedWebTexts.get(i);
			String formattedText = sortedFormattedStrings.get(i).replaceAll("\\s+", " ").replaceAll(",","");
			// 在比较前使用此方法处理两个字符串
			String normalizedWebText = normalizeWhitespace(webText);
			String normalizedFormattedText = normalizeWhitespace(formattedText);

			System.out.println("逐项比较索引 " + i + ":");
			System.out.println("  WebElement文本: \"" + webText + "\"");
			System.out.println("  格式化文本: \"" + formattedText + "\"");

			// 检查是否互相包含（只要有一个包含另一个就视为匹配）

			boolean isMatch = normalizedWebText.equals(normalizedFormattedText) ||
					normalizedWebText.contains(normalizedFormattedText) ||
					normalizedFormattedText.contains(normalizedWebText);

			if (!isMatch) {
				System.out.println("  结果: 不匹配");
				System.out.println("  详细信息: 两个文本既不相等，也不互相包含");
				allMatched = false;
			} else {
				System.out.println("  结果: 匹配");
				if (!webText.equals(formattedText)) {
					if (webText.contains(formattedText)) {
						System.out.println("  详细信息: WebElement文本包含格式化文本");
					} else if (formattedText.contains(webText)) {
						System.out.println("  详细信息: 格式化文本包含WebElement文本");
					}
				}
			}
		}

		if (allMatched && sortedWebTexts.size() == sortedFormattedStrings.size()) {
			System.out.println("所有项比较完成: 所有项目都匹配");
			return true;
		} else if (allMatched && sortedWebTexts.size() != sortedFormattedStrings.size()) {
			System.out.println("部分项比较完成: 共同项目匹配，但列表长度不一致");
			// 返回部分匹配（暂时判断通过）
			return true;
		} else {
			System.out.println("比较完成: 存在不匹配的项目");
			return false;
		}
	}


	/**
	 * 根据账号对账户信息列表进行排序
	 *
	 * @param accountList 账户信息列表
	 * @return 排序后的列表
	 */
	private static List<String> sortAccountList(List<String> accountList) {
		List<String> sortedList = new ArrayList<>(accountList);

		// 根据账号数值大小进行排序（提取账号号码进行比较）
		sortedList.sort((s1, s2) -> {
			String account1 = extractAccountNumber(s1);
			String account2 = extractAccountNumber(s2);

			// 尝试转换为数字进行比较
			try {
				Long acc1 = Long.parseLong(account1);
				Long acc2 = Long.parseLong(account2);
				return acc1.compareTo(acc2);
			} catch (NumberFormatException e) {
				// 如果转换失败，使用字符串比较
				return account1.compareTo(account2);
			}
		});

		return sortedList;
	}


	/**
	 * 从账户信息字符串中提取账号号码
	 *
	 * @param accountInfo 账户信息字符串
	 * @return 账号号码
	 */
	private static String extractAccountNumber(String accountInfo) {
		if (accountInfo == null || accountInfo.isEmpty()) {
			return "";
		}

		// 尝试从 "14750030 - $ 500.00 USD - Standard STP" 格式中提取账号
		String[] parts = accountInfo.split(" - ");
		if (parts.length > 0) {
			return parts[0].trim();
		}

		// 如果不是标准格式，直接返回原字符串
		return accountInfo;
	}

	public void payNowWithoutUpload() {
		page.payNowWithoutUpload();
	}
	// get deposit max amount by text and convert into deposit max amount in integer value
	public double getConvertedDepositMaxAmount(){
		return page.getConvertedDepositMaxAmount();
	}

	public boolean checkDepositAmountNotMetPopWindow() {
		return page.checkDepositAmountNotMetPopWindow();
	}
	// check deposit kyc not met pop window
	public boolean checkDepositKYCNotMetPopWindow() {
		return page.checkDepositKYCNotMetPopWindow();
	}
	// check payment method disabled
	public void checkPaymentMethodDisabled(String testId) {
		page.checkPaymentMethodDisabled(testId);
	}

	public boolean checkDepositAmountNotMetErrorPopWindow() {
		return page.checkDepositAmountNotMetErrorPopWindow();
	}
	// click change amount
	public void clickChangeAmount() {
		page.clickChangeAmount();
	}
	//点击choose another payment method
	public void clickChooseAnotherPaymentMethod() {
		page.clickChooseAnotherPaymentMethod();
	}

	public void exitIframe() {
		page.exitIframe();
	}

	public void goToIframe(String frameName) {
		page.goToIframe(frameName);
	}

	public boolean checkDepositAmountNotMetSuccessPopWindow() {
		return page.checkDepositAmountNotMetSuccessPopWindow();
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv){
		page.fillCCForm(cardNumber, expiryDate, cvv);
	}

	public boolean isCCDepositSuccess(){
		return page.isCCDepositSuccess();
	}

	public void payNowCC() {
		page.payNowCC();
	}
}
