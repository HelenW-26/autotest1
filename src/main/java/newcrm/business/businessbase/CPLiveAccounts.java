package newcrm.business.businessbase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACC_STATUS;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.clientpages.LiveAccountsPage.Account;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import javax.imageio.ImageIO;

public class CPLiveAccounts {

	protected LiveAccountsPage page;

	public CPLiveAccounts(LiveAccountsPage v_page) {
		this.page = v_page;
	}

	public CPLiveAccounts(WebDriver driver) {
		this.page = new LiveAccountsPage(driver);
	}
	
	public List<Account> getAccounts(PLATFORM platform) {
		return page.getFirstPageAccountsByPlatform(platform);
	}

	public List<Account> getAccounts_ListMode(PLATFORM platform) {
		return page.getFirstPageAccountsByPlatform_ListMode(platform);
	}

	public List<Account> getDemoAccountsByPlatform(PLATFORM platform) {
		return page.getFirstPageDemoAccountsByPlatform(platform);
	}

	public boolean checkNewAccount(PLATFORM platform, ACCOUNTTYPE type, CURRENCY currency, ACC_STATUS status) {
		List<Account> accounts = page.getFirstPageAccountsByPlatform(platform);
		if(accounts==null || accounts.size() == 0) {
			return false;
		}
		for(Account account : accounts) {
			if(account.compaire(type, currency, status)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAccountExist()
	{
		return page.checkAccountExist();
	}

	public void selectPlatform(PLATFORM platform) {
		page.selectPlatform(platform);
		page.waitLoading();
	}

	public void selectAccStatus(ACC_STATUS accStatus) {
		page.selectAccStatus(accStatus);
		page.waitLoading();
	}

	public void selectDemoAccStatus(ACC_STATUS accStatus) {
		page.selectDemoAccStatus(accStatus);
		page.waitLoading();
	}

	public void selectDemoAcc() {
		page.selectDemoAcc("Demo Account");
		page.waitLoading();
	}

	public void selectLiveAcc() {
		page.selectLiveAcc("Live Account");
		page.waitLoading();
	}

	public void waitLoadingAccountListContent() {
		page.waitLoadingAccountListContent();
	}

	public void setViewContentGridMode() {
		page.setViewContentGridMode();
		waitLoadingAccountListContent();
	}

	public void setViewContentListMode() {
		page.setViewContentListMode();
		waitLoadingAccountListContent();
	}

	public List<WebElement> filterLeverage(List<WebElement> leverageList, List<String> excludeList) {
		// Filter out excluded values and empty strings
		return leverageList.stream()
				.filter(e -> {
					String text = e.getText().replace(" ", "").trim();
					return !excludeList.contains(text) && !text.isEmpty();
				})
				.toList();
	}

	public String getMaxLeverage() {
		// Click leverage list
		page.clickLeverageList();
		// Get all leverage
		List<WebElement> leverageListEle = page.getLeverageList();

		String maxLeverage = leverageListEle.stream()
				.max(Comparator.comparingInt(e -> {
					String text = e.getText().replace(" ", "").replace(":1", "").trim();
					return Integer.parseInt(text);
				}))
				.map(WebElement::getText)
				.orElse("");

		return maxLeverage;
	}

	public List<WebElement> getLeverage(String leverage) {
		// Click leverage list
		page.clickLeverageList();
		// Get all leverage
		List<WebElement> leverageListEle = page.getLeverageList();
		// Filter leverage
		List<WebElement> filteredElements = filterLeverage(leverageListEle, List.of("1000:1", "2000:1", leverage));

		// Check if leverage list contains high leverage only
		if (filteredElements.isEmpty()) {
			Assert.fail("Set Leverage to " + leverage + " failed. High leverage selection is available but excluded from automation. Please select it manually");
		}

		return filteredElements;
	}

	public String setAccountLeverage(String leverage) {
		List<WebElement> lst = getLeverage(leverage);

		// Randomly select one
		Random rand = new Random();
		WebElement randomOption = lst.get(rand.nextInt(lst.size()));
		String selectedVal = randomOption.getText();
		System.out.println("Randomly select leverage: " + selectedVal);
		randomOption.click();
		LogUtils.info("Set Leverage to " + selectedVal);

		return selectedVal;
	}

	public String setAccountBalance(String balance) {
		Random rand = new Random();
		int randomNum;
		int originalBalance = new BigDecimal(balance).intValue();
		String newBalance;

		do {
			randomNum = rand.nextInt(1001); // 0 to 1000 inclusive
			newBalance = String.valueOf(randomNum);
		} while (randomNum == originalBalance); // regenerate if equal to original balance

		page.setAccountBalance(newBalance);

		return newBalance;
	}

	public void setAccountNickname(PLATFORM platform, String accNum, String accNickname) {
		page.clickAccountSettingBtn(platform, accNum, false);
		page.clickSetAccountNicknameBtn(platform, accNum);
		page.setAccountNickname(accNickname);
	}

	public String setAccountCurrentPwd(String pwd) {
		page.waitLoading();
		page.setAccountCurrentPwd(pwd);

		// Check for error
		page.checkAlertMsg("Change Password");

		// Get password validation message
		String validationMsg = page.checkAccountCurrentPwdErrMsg();
		LogUtils.info("Password Validation Msg: " + validationMsg);

		return validationMsg;
	}

	public void setAccountNewPwd(String pwd) {
		page.waitLoading();
		page.setAccountNewPwd(pwd);
	}

	public void setAccountConfirmNewPwd(String pwd) {
		page.waitLoading();
		page.setAccountConfirmNewPwd(pwd);
	}

	public List<Account> getCopyTradingAccountWithBalance(PLATFORM platform){
		List<Account> accounts = page.getFirstPageMTSAccountsWithBalance(platform);

		return accounts;
	}

	// 获取账号区域显示的信息
	public String getAccountInfo() {

		String accountInfo = page.findVisibleElemntByCss("#LivePage > div.page_card > div > div > div > div").getText();

		System.out.println(accountInfo);
		return accountInfo;
	}

	/**
	 * 解析账户信息（忽略账号类型）
	 * 输出格式：账号 - $ 余额 货币
	 * @param accountInfoText 原始账户信息文本
	 * @return 格式化后的账户信息列表
	 */
	public  List<String> parseAccountData(String accountInfoText) {
		List<String> results = new ArrayList<>();
		if (accountInfoText == null || accountInfoText.trim().isEmpty()) {
			return results;
		}

		String[] lines = accountInfoText.split("\n");
		List<Integer> accountStartIndices = new ArrayList<>();

		// 收集所有账户起始行（MT4/MT5开头）
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.startsWith("MT4") || line.startsWith("MT5")) {
				accountStartIndices.add(i);
			}
		}

		// 解析每个账户
		for (int startIdx : accountStartIndices) {
			try {
				AccountInfo info = parseSingleAccount(lines, startIdx, accountStartIndices);
				if (info != null && info.isValid()) {
					results.add(info.format());
				}
			} catch (Exception e) {
				System.out.println("账户解析异常（起始行 " + startIdx + "）: " + e.getMessage());
			}
		}

		return results;
	}

	/**
	 * 账户信息类（忽略类型）
	 */
	private static class AccountInfo {
		String accountNumber;
		String balance; // 保留原始格式
		String currency;

		AccountInfo(String accountNumber, String balance, String currency) {
			this.accountNumber = accountNumber;
			this.balance = balance;
			this.currency = currency;
		}

		// 验证只需账号、余额和货币
		boolean isValid() {
			return !isEmpty(accountNumber) && !isEmpty(balance) && !isEmpty(currency);
		}

		// 格式化输出（不含类型）
		String format() {
			switch (currency.toUpperCase()) {
				case "USD":
					return accountNumber + " - $ " + balance + " " + currency;
				case "EUR":
					return accountNumber + " - € " + balance + " " + currency;
				case "GBP":
					return accountNumber + " - ￡ " + balance + " " + currency;
				case "JPY":
					return accountNumber + " - ¥ " + balance + " " + currency;
				case "CNY":
					return accountNumber + " - ¥ " + balance + " " + currency;
				case "CAD":
					return accountNumber + " - CA$ " + balance + " " + currency;
				case "AUD":
					return accountNumber + " - A$ " + balance + " " + currency;
				case "CHF":
					return accountNumber + " - CHF " + balance;
				case "HKD":
					return accountNumber + " - HK$ " + balance + " " + currency;
				case "SGD":
					return accountNumber + " - S$ " + balance + " " + currency;
				case "NZD":
					return accountNumber + " - NZ$ " + balance + " " + currency;
				default:
					return accountNumber + " - " + currency + " " + balance;
			}
		}


		private boolean isEmpty(String s) {
			return s == null || s.trim().isEmpty();
		}
	}

	/**
	 * 解析单个账户信息（忽略类型）
	 */
	private static AccountInfo parseSingleAccount(String[] lines, int startIdx, List<Integer> allStarts) {
		// 确定账户结束位置
		int endIdx = lines.length;
		for (int s : allStarts) {
			if (s > startIdx) {
				endIdx = s;
				break;
			}
		}

		// 提取账号
		String accountLine = lines[startIdx].trim();
		String accountNumber = extractAccountNumber(accountLine, lines, startIdx, endIdx);
		if (accountNumber == null) {
			return null;
		}

		// 提取余额和货币
		String balance = "";
		String currency = "";
		List<String> rawBalances = new ArrayList<>(); // 存储原始格式余额

		// 遍历账户区块提取信息
		for (int i = startIdx; i < endIdx; i++) {
			String line = lines[i].trim();
			if (line.isEmpty()) continue;

			// 收集原始余额候选
			if (line.matches("[\\d,]+\\.\\d{2}")) {
				rawBalances.add(line);
			}

			// 提取带货币的余额（如"1,900.00 USD"）
			if (line.matches("[\\d,]+\\.\\d{2}\\s+[A-Z]{3}") && balance.isEmpty()) {
				String[] parts = line.split("\\s+");
				balance = parts[0]; // 保留原始格式
				currency = parts[1];
			}

			// 提取Balance标签后的余额（如"Balance: 1,900.00"）
			if (line.startsWith("Balance:") && balance.isEmpty()) {
				String balPart = line.replace("Balance:", "").trim()
						.replace(" ", " ").replace(" ", " "); // 处理特殊空格
				if (balPart.matches("[\\d,]+\\.\\d{2}")) {
					balance = balPart;
				}
			}

			// 提取独立货币代码（如单独的"USD"行）
			if (line.matches("[A-Z]{3}") && currency.isEmpty()) {
				currency = line;
			}
		}

		// 余额兜底
		if (balance.isEmpty()) {
			balance = rawBalances.isEmpty() ? "0.00" : rawBalances.get(0);
		}

		// 货币兜底
		if (currency.isEmpty()) {
			currency = "USD";
		}

		return new AccountInfo(accountNumber, balance, currency);
	}

	/**
	 * 提取账号（修复前缀问题）
	 * 处理两种格式：
	 * 1. 账号与MT4/MT5分离（如"MT4"下一行是"8350095"）
	 * 2. 账号与MT4/MT5连在一起（如"MT57850324" → 提取"7850324"）
	 */
	private static String extractAccountNumber(String accountLine, String[] lines, int startIdx, int endIdx) {
		// 情况1：账号在MT4/MT5后直接跟随（如"MT4123456"或"MT57850324"）
		// 提取前缀后的纯数字部分
		if (accountLine.startsWith("MT4")) {
			String numberPart = accountLine.substring(3); // 去掉"MT4"
			if (numberPart.matches("\\d+")) {
				return numberPart;
			}
		} else if (accountLine.startsWith("MT5")) {
			String numberPart = accountLine.substring(3); // 去掉"MT5"
			if (numberPart.matches("\\d+")) {
				return numberPart;
			}
		}

		// 情况2：账号在下行（如"MT4"行的下一行是数字）
		if (startIdx + 1 < endIdx) {
			String nextLine = lines[startIdx + 1].trim();
			if (nextLine.matches("\\d+")) {
				return nextLine;
			}
		}

		return null;
	}

	public void clickAccountLeverageBtn(PLATFORM platform, String accNum) {
		page.clickAccountSettingBtn(platform, accNum, false);
		page.clickAccountLeverageBtn(platform);
	}

	public void clickDemoAccountLeverageBtn(PLATFORM platform, String accNum) {
		page.clickAccountSettingBtn(platform, accNum, true);
		page.clickDemoAccountLeverageBtn(platform);
	}

	public void clickDemoAccountBalanceBtn(PLATFORM platform, String accNum) {
		page.clickDemoAccountBalanceBtn(platform, accNum);
	}

	public void clickRemoveCreditBtn(PLATFORM platform, String accNum) {
		page.clickAccountSettingBtn(platform, accNum, false);
		page.clickRemoveCreditBtn(platform, accNum);
	}

	public void clickChgAccountPwdBtn(PLATFORM platform, String accNum) {
		page.clickAccountSettingBtn(platform, accNum, false);
		page.clickAccountChangePwdBtn(platform);
	}

	public Map.Entry<Boolean, String> submitLeverage() {
		page.tickbox();
		page.submitLeverage();
		String response = page.getChgLeverageResponse();
		LogUtils.info("Update Resp Msg: " + response);

		if (response == null || response.isEmpty()) {
			return new AbstractMap.SimpleEntry<>(false, "Update leverage response message is empty");
		}

		if (!response.contains("Your leverage change request is completed")) {
			return new AbstractMap.SimpleEntry<>(false, String.format("%s, Expected Resp Msg: Your leverage change request is completed", response));
		}

		return new AbstractMap.SimpleEntry<>(response.contains("Your leverage change request is completed"), response);
	}

	public Map.Entry<Boolean, String> submitLeverageDemo() {
		page.submitLeverage();
		page.checkChgLeverageConfirmationDialog();
		String response = page.getChgLeverageResponse();
		LogUtils.info("Update Resp Msg: " + response);

		if (response == null || response.isEmpty()) {
			return new AbstractMap.SimpleEntry<>(false, "Update leverage response message is empty");
		}

		if (!response.contains("Your leverage change request is completed")) {
			return new AbstractMap.SimpleEntry<>(false, String.format("%s, Expected Resp Msg: Your leverage change request is completed", response));
		}

		return new AbstractMap.SimpleEntry<>(response.contains("Your leverage change request is completed"), response);
	}

	public void submitChgDemoAccountBalance() {
		page.submitChgDemoAccountBalance();
	}

	public void submitChgAccountNickname(String accNo) {
		// Submit
		page.submitChgAccountNickname();

		// Get submission response
		Map.Entry<Boolean, String> resp = page.checkAlertMsg("Update Account Nickname");

		if (resp.getKey() && !resp.getValue().contains("created successfully")) {
			Assert.fail("Failed to change account nickname for account " + accNo + ". Resp Msg: " + resp.getValue());
		}
	}

	public Map.Entry<Boolean, String> submitRemoveAccountCredit() {
		page.submitRemoveAccountCredit();

		String response = page.getRemoveAccountCreditResponse().getText();
		LogUtils.info("Update Resp Msg: " + response);

		if (response == null || response.isEmpty()) {
			return new AbstractMap.SimpleEntry<>(false, "Remove account credit response message is empty");
		}

		page.closeRemoveCreditDialog();

		return new AbstractMap.SimpleEntry<>(response.contains("credits have been removed"), response);
	}

	public void submitChgAccountPassword(String accNo) {
		// Submit
		page.submitChgAccountPassword();

		// Check for error
		page.checkAlertMsg("Change Password");

		// Get update response
		String respMsg = page.getChgAccountPwdResponse();
		LogUtils.info("Update Resp Msg: " + respMsg);

		// Check empty on update response msg
		if (respMsg == null || respMsg.isEmpty()) {
			Assert.fail("Update account password response message is empty");
		}

		if (!respMsg.contains("password has been updated")) {
			Assert.fail("Failed to change password for trading account " + accNo + ". Resp Msg: " + respMsg);
		}

		// Close change password dialog
		page.closeChgAccountPwdDialog();
	}

	public String getProfileUserId() {
		page.clickProfileAvatar();
		String userId = page.getProfileUserId();
		page.closeProfileAvatar();

		return userId;
	}

	public String getAssetsCurrencyImg() {
		return page.getAssetsCurrencyImg();
	}

	public String getAssetsAmount() {
		return page.getAssetsAmount();
	}

	public String getAssetsCurrency() {
		return page.getAssetsCurrency();
	}

	public void validateAssetsInfo(JSONObject result, String assetsAmount, String assetsCurrency, String assetsCurrencyImgSrc) {

		JSONObject jsonData = JSONObject.parseObject(result.getString("data"));
		String apiAssetsAmount = jsonData.getString("equity");

		if (apiAssetsAmount == null || apiAssetsAmount.trim().isEmpty()) {
			Assert.fail("Actual Assets Currency value is empty");
		}

		// Cross-check amount with API value
		if (!GlobalMethods.compareNumericValues(apiAssetsAmount, assetsAmount)) {
			Assert.fail("Assets Amount mismatch. Actual: " + apiAssetsAmount + ", Display: " + assetsAmount);
		}

		// Check for broken image
		try {
			BufferedImage img = null;

			if (assetsCurrencyImgSrc.startsWith("data:image")) {
				// Handle base64 image
				String base64Data = assetsCurrencyImgSrc.substring(assetsCurrencyImgSrc.indexOf("base64,") + 7);
				byte[] imageBytes = Base64.getDecoder().decode(base64Data);
				img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			} else {
				// Handle URL image
				URL imageUrl = new URL(assetsCurrencyImgSrc);
				img = ImageIO.read(imageUrl);
			}

			if (img != null) {
				GlobalMethods.printDebugInfo("Assets Currency Image found");

				if (!assetsCurrencyImgSrc.startsWith("data:image")) {
					if (!assetsCurrencyImgSrc.toLowerCase().contains(assetsCurrency.toLowerCase())) {
						// Check image data source match with currency
						Assert.fail("Assets Currency Image Data Source mismatch. Currency: " + assetsCurrency + ", Image Data Src: " + assetsCurrencyImgSrc);
					}
				}

			} else {
				Assert.fail("Assets Currency Image broken");
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void validateTradingAccountInfo(JSONObject result) {

		// Check empty on api
		if (result == null || result.getJSONArray("data") == null || result.getJSONArray("data").isEmpty()){
			Assert.fail("No trading account found");
		}

		JSONArray data = result.getJSONArray("data");

		// Get trading account from UI
		List<WebElement> tradingAccountList = page.getTradingAccount();

		// Compare API & UI data size
		if (tradingAccountList == null || tradingAccountList.isEmpty()) {
			Assert.fail("Trading Account found in API but not display in dashboard");
		}

		// Convert JSONArray → List<JSONObject>
		List<JSONObject> apiTradingAccountList = IntStream.range(0, data.size())
				.mapToObj(data::getJSONObject)
				.limit(2)
				.toList();

		tradingAccountList = tradingAccountList.stream().limit(2).toList();

		for (int i = 0; i < apiTradingAccountList.size(); i++) {

			// Get API value
			JSONObject obj = apiTradingAccountList.get(i);
			String apiServerCat = obj.getString("serverCategory");
			String apiTradingAccNo = obj.getString("mt4_account");
			String apiAmount = obj.getString("equity");
			String apiCurrency = obj.getString("currency");
			String apiAccTypeCode = obj.getString("mt4_account_type");
			String apiStatus = obj.getString("status");
			String apiIsArchive  = obj.getString("isArchive");

			// Get UI value
			WebElement ele = tradingAccountList.get(i);
			String platform = page.getTradingAccountPlatform(ele);
			String tradingAccNo = page.getTradingAccountNo(ele);
			String amount = page.getTradingAccountAmount(ele);
			String currency = page.getTradingAccountCurrency(ele);
			String accountTypeDesc = page.getTradingAccountAccountType(ele);

			// Compare UI and API values
			// Cross-check platform value
			PLATFORM apiPlatform = GlobalProperties.PLATFORM.getRecByServerCategory(apiServerCat);
			String apiPlatformDesc = apiPlatform == null ? "" : String.valueOf(apiPlatform);

			if (!platform.equalsIgnoreCase(apiPlatformDesc)) {
				Assert.fail("Trading Account Platform mismatch. Account No.: " + apiTradingAccNo + ", Actual: " + apiPlatformDesc + " (" + apiServerCat + "), Display: " + platform);
			}

			// Cross-check account number value
			if (apiTradingAccNo == null || apiTradingAccNo.isEmpty()) {
				if (!tradingAccNo.equalsIgnoreCase("--")) {
					Assert.fail("Trading Account Number display incorrect. Actual: " + apiTradingAccNo + ", Display: " + tradingAccNo);
				}
			} else {
				if (!tradingAccNo.contains(apiTradingAccNo)) {
					Assert.fail("Trading Account Number mismatch. Actual: " + apiTradingAccNo + ", Display: " + tradingAccNo);
				}
			}

			// Cross-check amount value
			if (!"1".equals(apiStatus) || ("1".equals(apiIsArchive) || "2".equals(apiIsArchive))) {
				if (!amount.equalsIgnoreCase("--")) {
					Assert.fail("Trading Account Amount display incorrect. Account No.: " + apiTradingAccNo + ", Actual: " + apiAmount + ", Display: " + amount);
				}
			} else {
				if (!GlobalMethods.compareNumericValues(apiAmount, amount)) {
					Assert.fail("Trading Account Amount mismatch. Account No.: " + apiTradingAccNo + ", Actual: " + apiAmount + ", Display: " + amount);
				}
			}

			// Cross-check currency value
			if ("1".equals(apiIsArchive) || "2".equals(apiIsArchive)) {
				if (!currency.equalsIgnoreCase("--")) {
					Assert.fail("Trading Account Currency display incorrect. Account No.: " + apiTradingAccNo + ", Actual: " + apiCurrency + ", Display: " + currency);
				}
			} else {
				if (!currency.equalsIgnoreCase(apiCurrency)) {
					Assert.fail("Trading Account Currency mismatch. Account No.: " + apiTradingAccNo + ", Actual: " + apiCurrency + ", Display: " + currency);
				}
			}

			// Cross-check account type value
			ACCOUNTTYPE apiAccType = ACCOUNTTYPE.getRecByAccTypeCode(apiAccTypeCode, apiPlatform);
			String apiAccountTypeDesc = apiAccType == null ? "" : apiAccType.getLiveAccountName();

			if (!accountTypeDesc.equalsIgnoreCase(apiAccountTypeDesc)) {
				Assert.fail("Trading Account Account Type mismatch. Account No.: " + apiTradingAccNo + ", Actual: " + apiAccountTypeDesc + ", Display: " + accountTypeDesc);
			}

			if (i != apiTradingAccountList.size() - 1) {
				GlobalMethods.printDebugInfo("********************************************");
			}

		}
	}

	public Account randomSelectAccount(List<Account> accountList) {
		Random random = new Random();
		Account acc = accountList.get(random.nextInt(accountList.size()));
		System.out.println("Choose account:");
		acc.printAccount();

		return acc;
	}

	public void closeDialog() {
		page.closeDialog();
	}

	public void checkExistsDialog() {
		page.checkExistsDialog();
	}

}

