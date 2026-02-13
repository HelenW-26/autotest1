package newcrm.pages.clientpages;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import newcrm.global.GlobalProperties;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.global.GlobalProperties.ACC_STATUS;
import newcrm.pages.Page;
import tools.ScreenshotHelper;
import utils.LogUtils;

public class LiveAccountsPage  extends Page {

	public class Account {
		private String accnum;
		private String server;
		private ACCOUNTTYPE type;
		private CURRENCY currency;
		private String leverage;
		private STATUS status;
		private ACC_STATUS acc_status;
		private PLATFORM platform;
		private String equity;
		private String balance;
		private String credit;
		private String acc_nickname;

		public Account(String v_accnum, String v_server, ACCOUNTTYPE v_type, CURRENCY v_currency, String v_leverage, STATUS v_status) {
			accnum = v_accnum.trim().toLowerCase();
			server = v_server.trim().toLowerCase();
			type = v_type;
			currency = v_currency;
			leverage = v_leverage;
			status = v_status;
		}

		public Account(String v_accnum, String v_server, ACCOUNTTYPE v_type, CURRENCY v_currency, String v_leverage, STATUS v_status, PLATFORM v_platform) {
			accnum = v_accnum.trim().toLowerCase();
			server = v_server.trim().toLowerCase();
			type = v_type;
			currency = v_currency;
			leverage = v_leverage;
			status = v_status;
			platform = v_platform;
		}

		public Account(String v_accnum, String v_server, ACCOUNTTYPE v_type, CURRENCY v_currency, String v_leverage, ACC_STATUS v_acc_status,
					   String v_equity, String v_balance, String v_credit, PLATFORM v_platform, String v_nickname) {

			accnum = v_accnum.trim();
			server = v_server.trim();
			type = v_type;
			currency = v_currency;
			leverage = v_leverage;
			acc_status = v_acc_status;
			equity = v_equity;
			balance = v_balance;
			credit	= v_credit;
			platform = v_platform;
			acc_nickname = v_nickname.replaceAll("[()]", "").trim();
		}

		public PLATFORM getPlatform() {
			return platform;
		}

		public String getAccountNum() {
			return accnum;
		}

		public ACCOUNTTYPE getType() {
			return type;
		}

		public CURRENCY getCurrency() {
			return currency;
		}

		public STATUS getStatus() {
			return status;
		}

		public ACC_STATUS getAccStatus() {
			return acc_status;
		}

		public String getLeverage() {
			return leverage;
		}

		public String getBalance() {
			return balance;
		}

		public  String getEquity() {
			return equity;
		}

		public String getCredit() {
			return credit;
		}

		public String getServer() {
			return server;
		}

		public String getAccountNickname() {
			return acc_nickname;
		}

		public boolean compaire(Account acc) {
			if (accnum.equalsIgnoreCase(acc.getAccountNum())) {
				return true;
			} else {
				if (accnum.equals("")) {
					if (!acc.getAccountNum().equals("")) {
						return false;
					} else {
						if (type.equals(acc.getType()) && currency.equals(acc.getCurrency()) && status.equals(acc.getStatus())) {
							return true;
						} else {
							return false;
						}
					}
				} else {
					// accnum is not empty and not equal to another one
					return false;
				}
			}
		}

		public boolean compaire(ACCOUNTTYPE v_type, CURRENCY v_currency, ACC_STATUS v_status) {

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			LogUtils.info("type:" + type);
			LogUtils.info("currency:" + currency);
			LogUtils.info("status:" + acc_status);

			if (type.equals(v_type) && currency.equals(v_currency) && acc_status.equals(v_status)) {
				return true;
			} else {
				return false;
			}
		}

		public void printAccount() {
			System.out.println("****************Account Info*****************");
			System.out.printf("%-30s : %s\n", "Account Number", accnum);
			System.out.printf("%-30s : %s\n", "Platform", (platform == null ? "" : platform.getPlatformDesc()));
			System.out.printf("%-30s : %s\n", "Account Type", (type == null ? "" : type.getLiveAccountName()));
			System.out.printf("%-30s : %s\n", "Server", server);
			System.out.printf("%-30s : %s\n", "Leverage", leverage);
			System.out.printf("%-30s : %s\n", "Status", (acc_status == null ? "" : acc_status.getStatusDesc()));
			System.out.printf("%-30s : %s\n", "Currency", (currency == null ? "" : currency.getCurrencyDesc()));
			System.out.printf("%-30s : %s\n", "Equity", equity);
			System.out.printf("%-30s : %s\n", "Credit", credit);
			System.out.printf("%-30s : %s\n", "Balance", balance);
			System.out.printf("%-30s : %s\n", "Account Nickname", acc_nickname);
			System.out.println("*********************************************");
		}

		public void printDemoAccountCheck() {
			System.out.println("****************Account Info*****************");
			System.out.printf("%-30s : %s\n", "Account Number", accnum);
			System.out.printf("%-30s : %s\n", "Platform", (platform == null ? "" : platform.getPlatformDesc()));
			System.out.printf("%-30s : %s\n", "Account Type", (type == null ? "" : type.getLiveAccountName()));
			System.out.printf("%-30s : %s\n", "Server", server);
			System.out.printf("%-30s : %s\n", "Leverage", leverage);
			System.out.printf("%-30s : %s\n", "Currency", (currency == null ? "" : currency.getCurrencyDesc()));
			System.out.printf("%-30s : %s\n", "Equity", equity);
			System.out.println("*********************************************");
		}
	}

	public LiveAccountsPage(WebDriver driver) {
		super(driver);
	}

	protected WebElement getPlatformEle() {
		return assertElementExists(By.cssSelector("div.platform_select"), "Platform");
	}

	protected WebElement getAccStatusEle() {
		return assertElementExists(By.cssSelector("div.status_select"), "Status");
	}

	protected WebElement getTradingAccountTypeEle() {
		return assertElementExists(By.cssSelector("div.account_select"), "Trading Account Type");
	}

	protected WebElement getPlatformListEle(String platform) {
		return assertElementExists(By.cssSelector("div.ht-select-dropdown:not([style*='display: none']) ul > li > span"), "Platform List", e -> e.getText().trim().equalsIgnoreCase(platform));
	}

	protected WebElement getAccStatusListEle(ACC_STATUS accStatus) {
		return assertElementExists(By.cssSelector("div.ht-select-dropdown:not([style*='display: none']) ul > li > span"), "Status List", e -> e.getText().trim().equalsIgnoreCase(accStatus.getStatusDesc()));
	}

	protected WebElement getTradingAccountListEle(String desc) {
		return assertElementExists(By.cssSelector("div.ht-select-dropdown:not([style*='display: none']) ul > li > span"), "Trading Account Type List", e -> e.getText().trim().equalsIgnoreCase(desc));
	}

	protected List<WebElement> getAccountListEle() {
		return assertElementsExists(By.cssSelector("div#LivePage div.account_card_box > div > div:not(:has(div.copyTrading_box)), div#LivePage div.account_list_box div.el-card > div.el-card__body"), "Live Account List");
	}

	protected WebElement getGridModeEle() {
		return assertElementExists(By.cssSelector("svg.ht-icon-menu-block"), "Grid Mode icon");
	}

	protected WebElement getListModeEle() {
		return assertElementExists(By.cssSelector("svg.ht-icon-menu-line"), "List Mode icon");
	}

	protected List<WebElement> getDemoAccountListEle() {
		return null;
	}

	protected void getAccountListContentEle() {
		WebElement contentEle = assertVisibleElementExists(By.xpath("//div[not(ancestor::div[contains(@style, 'display: none')])]/div[@class='page_card']"), "Account Listing Content");

		try {
			waitvisible.until(driver -> !contentEle.getText().trim().isEmpty());
			System.out.println("*********************************************");
			System.out.println("Account found in list:\n" + contentEle.getText());
			System.out.println("*********************************************");
		} catch (Exception e) {
			Assert.fail("Account Listing Content not found");
		}
	}

	protected WebElement getEditBalanceEle(int pos) {
		return assertClickableElementExists(By.xpath("(//span[@data-testid='resetBal'])" + "[" + pos + "]"), "Edit Balance icon");
	}

	protected WebElement getAccountSettingIconEle(int pos) {
		return assertClickableElementExists(By.xpath("(//div[@id='LivePage']//div[@class='setting_box'])" + "[" + pos + "]"), "Account Setting icon");
	}

	protected WebElement getDemoAccountSettingIconEle(int pos) {
		return assertClickableElementExists(By.xpath("(//div[@id='HomeDemo']//div[@class='setting_box'])" + "[" + pos + "]"), "Account Setting icon");
	}

	protected WebElement getChangeLeverageBtnEle(PLATFORM platform) {
		return assertElementExists(By.cssSelector("div.el-popover:not([style*='display: none']) button[data-testid='leverageMt" + platform.getServerCategory() + "']"), "Change Leverage button");
	}

	protected WebElement getDemoChangeLeverageBtnEle(PLATFORM platform) {
		return assertElementExists(By.cssSelector("div.el-popover:not([style*='display: none']) button[data-testid='leverageMt" + platform.getServerCategory() + "']"), "Change Leverage button");
	}

	protected List<WebElement> getLeverageListEle(){
		return this.getAllOpendElements();
	}

	protected WebElement getNewLeverageEle() {
		return assertElementExists(By.xpath("//div[@data-testid='newLeverage']/div/input"), "New Leverage");
	}

	protected WebElement getAccountListNoDataEle() {
		return checkElementExists(By.cssSelector("#LivePage div.page_card div.ht-empty"), "No Data Content");
	}

	protected WebElement getDemoAccountListNoDataEle() {
		return checkElementExists(By.cssSelector("#HomeDemo div.page_card div.ht-empty"), "No Data Content");
	}

	protected WebElement getProfileAvatarEle() {
		return assertElementExists(By.cssSelector("span.profile-panel-popover > span > img"), "Profile Avatar");
	}

	protected WebElement getProfileUserIdEle() {
		return assertElementExists(By.xpath("//div[@class='copyOperate']"), "User ID");
	}

	protected WebElement getProfileUserIdLabelEle() {
		return assertElementExists(By.xpath("//span[@class='copyLabel']"), "User ID");
	}

	protected WebElement getAssetsCurrencyImgEle() {
		return assertElementExists(By.cssSelector("div.el-card__body > div > div > div.amount > img"), "Assets Currency Img");
	}

	protected WebElement getAssetsAmountEle() {
		return assertElementExists(By.cssSelector("span.currency-select__amount-format"), "Assets Amount");
	}

	protected WebElement getAssetsCurrencyEle() {
		return assertElementExists(By.cssSelector("div.currency-dropdown > span"), "Assets Currency");
	}

	protected WebElement getTradingAccountPlatformEle(WebElement ele) {
		return assertElementExists(By.cssSelector("div:nth-of-type(1) > span:nth-of-type(1)"), "Trading Account Platform", ele);
	}

	protected WebElement getTradingAccountNoEle(WebElement ele) {
		return assertElementExists(By.cssSelector("div:nth-of-type(1) > span:nth-of-type(2) > span > span"), "Trading Account Number", ele);
	}

	protected WebElement getTradingAccountAmountEle(WebElement ele) {
		return assertElementExists(By.cssSelector("div:nth-of-type(2) > span:nth-of-type(1)"), "Trading Account Amount", ele);
	}

	protected WebElement getTradingAccountCurrencyEle(WebElement ele) {
		return assertElementExists(By.cssSelector("div:nth-of-type(2) > span:nth-of-type(2)"), "Trading Account Currency", ele);
	}

	protected WebElement getTradingAccountAccountTypeEle(WebElement ele) {
		// Get direct span. Without :scope, Selenium treats > span as invalid.
		return assertElementExists(By.cssSelector(":scope > span"), "Trading Account Account Type", ele);
	}

	protected List<WebElement> getTradingAccountEle() {
		return assertElementsExists(By.xpath("//div[@class='total_trading']//div[contains(@class, 'flex_between_grid')]"), "Trading Account");
	}

	protected WebElement getAccountSettingEle(int idx) {
		return assertElementExists(By.xpath("(//div[@class='setting_box'])" + "[" + idx + "]"), "Account Setting icon");
	}

	protected WebElement getSetAccountNicknameBtnEle() {
		return assertElementExists(By.xpath("//div[contains(@class,'el-popover') and not(contains(@style, 'display: none'))]//button[@data-testid='button']"), "Set Nickname button");
	}

	protected WebElement getRemoveCreditBtnEle(PLATFORM platform) {
		return assertElementExists(By.xpath("//div[contains(@class,'el-popover') and not(contains(@style, 'display: none'))]//button[@data-testid='removeCreditMT" + platform.getServerCategory() + "']"), "Remove Credit button");
	}

	protected WebElement getAccountChangePwdBtnEle(PLATFORM platform) {
		return assertElementExists(By.xpath("//div[contains(@class,'el-popover') and not(contains(@style, 'display: none'))]//button[@data-testid='resetMT" + platform.getServerCategory() + "PW']"), "Remove Credit button");
	}

	protected WebElement getAccountNicknameEle() {
		return assertElementExists(By.xpath("//div[@class='nickname_dialog']//input"), "Account Nickname");
	}

	protected WebElement getAccountBalanceEle() {
		return assertElementExists(By.cssSelector("input[data-testid='resetBalTo']"), "Account Balance");
	}

	protected WebElement getAccountCurrentPwdEle() {
		return assertElementExists(By.cssSelector("input[data-testid='currentPassword']"), "Current Password");
	}

	protected WebElement getAccountCurrentPwdErrMsgEle() {
		return checkElementExists(By.cssSelector("div:has(input[data-testid='currentPassword']) + div.el-form-item__error"));
	}

	protected WebElement getAccountNewPwdEle() {
		return assertElementExists(By.cssSelector("input[data-testid='newPassword']"), "New Password");
	}

	protected WebElement getAccountConfirmNewPwdEle() {
		return assertElementExists(By.cssSelector("input[data-testid='confirmNewPassword']"), "Confirm New Password");
	}

    protected WebElement getAccountChgPwdSubmitBtnEle() {
        return assertElementExists(By.cssSelector("button[data-testid='changePw']"), "Change Password button");
    }

	protected WebElement getResetBalanceDialogSubmitBtn() {
		return assertElementExists(By.cssSelector("div.reset_balance_dialog button[data-testid='confirm']"), "Confirm button");
	}

	protected WebElement getChgAccountNicknameSubmitBtn() {
		return assertElementExists(By.xpath("//div[@class='nickname_dialog']//button"), "Confirm button", e -> e.getText().toLowerCase().contains("confirm"));
	}

	protected WebElement getRemoveAccountCreditSubmitBtn() {
		return assertElementExists(By.cssSelector("div.remove_credit_dialog button"), "Confirm button", e -> e.getText().toLowerCase().contains("confirm"));
	}

	protected WebElement getRemoveAccountCreditOkBtn() {
		return assertElementExists(By.cssSelector("div.success_dialog div.el-dialog__wrapper:not([style*='display: none']) button"), "OK button", e -> e.getText().toLowerCase().contains("ok"));
	}

	protected WebElement getChgLeverageResponseContentEle() {
		return assertVisibleElementExists(By.xpath("//div[@class='result_info_dialog']"), "Update Leverage Response Content");
	}

	protected WebElement getChgAccountPwdResponseContentEle() {
		return assertVisibleElementExists(By.cssSelector("div.result-info-dialog"), "Change Password Response Content");
	}

	protected By getAlertMsgBy() {
		return By.cssSelector("div.el-message.ht-message");
	}

	protected WebElement getLeverageConfirmationDialogEle() {
		return checkElementExists(By.cssSelector("div#HomeDemo div.change_leverage_dialog div.ht-dialog:nth-of-type(2):not([style*='display: none'])"), "Leverage Confirmation Dialog");
	}

	protected WebElement getLeverageConfirmationBtnEle() {
		return assertElementExists(By.cssSelector("div#HomeDemo div.change_leverage_dialog div.ht-dialog:nth-of-type(2) button[data-testid='confirm']"), "Confirm button");
	}

	protected WebElement getDialogCloseBtnEle() {
		return checkElementExists(By.cssSelector("div.ht-dialog:not([style*='display: none']) svg.ht-icon-close"));
	}

	protected WebElement getSideDialogCloseBtnEle() {
		return checkElementExists(By.xpath("(//div[contains(@class,'ht-drawer') and not(contains(@style,'display'))]//button[@class='el-drawer__close-btn'])[last()]"));
	}

	protected WebElement getChgAccountPwdDialogCloseBtnEle() {
		return assertElementExists(By.cssSelector("div.ht-dialog:not([style*='display: none']) svg.ht-icon-close"), "Change Password Dialog Close button");
	}

	protected WebElement getSubmitLeverageBtn() {
		return assertClickableElementExists(By.cssSelector("div.change_leverage_dialog div.el-dialog__wrapper:not([style*='display: none']) button[data-testid='confirmChangeLeverage']"), "Submit button");
	}

	protected WebElement getLeverageAgreementEle() {
		return assertVisibleElementExists(By.cssSelector("[data-testid='tncConfirm'] > :first-of-type"), "Leverage Agreement tick box");
	}

	protected List<Account> getAccounts(List<WebElement> trs) {
		return new ArrayList<>();
	}

	protected List<Account> getAccounts_ListMode(List<WebElement> trs) {
		return new ArrayList<>();
	}

	protected List<Account> getDemoAccounts(List<WebElement> trs) {
		return new ArrayList<>();
	}

	public List<Account> getFirstPageAccountsByPlatform(PLATFORM platform) {
		// Check empty list
		WebElement e = getAccountListNoDataEle();

		if (e != null) {
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		// Get account list
		List<WebElement> divs = getAccountListEle();

		if(divs == null || divs.isEmpty())
		{
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		return getAccounts(divs);
	}

	public List<Account> getFirstPageAccountsByPlatform_ListMode(PLATFORM platform) {
		// Check empty list
		WebElement e = getAccountListNoDataEle();

		if (e != null) {
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		// Get account list
		List<WebElement> divs = getAccountListEle();

		if(divs == null || divs.isEmpty())
		{
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		return getAccounts_ListMode(divs);
	}

	public List<Account> getFirstPageDemoAccountsByPlatform(PLATFORM platform) {
		// Check empty list
		WebElement e = getDemoAccountListNoDataEle();

		if (e != null) {
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		// Get account list
		List<WebElement> divs = getDemoAccountListEle();

		if(divs == null || divs.isEmpty())
		{
			LogUtils.info("Did not find any " + platform.toString() + " account info");
			return new ArrayList<>();
		}

		return getDemoAccounts(divs);
	}

	public List<WebElement> getLeverageList() {
		List<WebElement> leverageListEle = this.getLeverageListEle();
		System.out.println("Leverage available before filter (" + leverageListEle.stream().map(e -> e.getText().trim()).collect(Collectors.joining(", ")) + ")");
		return leverageListEle;
	}

	public void clickLeverageList() {
		WebElement leverageEle = this.getNewLeverageEle();
		this.moveElementToVisible(leverageEle);
		leverageEle.click();
		LogUtils.info("Open Leverage List");
		getAllOpendElements();
	}

	public boolean checkAccountExist()
	{
		WebElement e = getAccountListNoDataEle();

		if (e != null) {
			LogUtils.info("account size is: 0");
			return false;
		}

		// Get account list
		List<WebElement> divs = getAccountListEle();

		if (divs.isEmpty()) {
			LogUtils.info("account size is: 0");
			return false;
		}

		LogUtils.info("Account size is: " + divs.size());

		return true;
	}

	public void selectPlatform(PLATFORM platform) {
		WebElement e = getPlatformEle();
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Open Platform List");

		e = getPlatformListEle(platform.toString());
		triggerElementClickEvent_withoutMoveElement(e);

		LogUtils.info("Filter account list by " + platform.getPlatformDesc() + " platform");
	}

	public void selectAccStatus(ACC_STATUS accStatus) {
		WebElement e = getAccStatusEle();
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Open Status List");

		e = getAccStatusListEle(accStatus);
		triggerElementClickEvent_withoutMoveElement(e);

		LogUtils.info("Filter account list by " + accStatus.getStatusDesc() + " status");
	}

	public void selectDemoAccStatus(ACC_STATUS accStatus) {
		selectAccStatus(accStatus);
	}

	public void selectDemoAcc(String desc) {
		selectLiveAcc(desc);
	}

	public void selectLiveAcc(String desc) {
		WebElement e = getTradingAccountTypeEle();
		triggerElementClickEvent_withoutMoveElement(e);

		e = getTradingAccountListEle(desc);
		triggerElementClickEvent_withoutMoveElement(e);

		LogUtils.info("Filter " + desc);
	}

	public void setViewContentGridMode() {
		WebElement mode = getGridModeEle();
		WebElement modeParent = mode.findElement(By.xpath("./parent::div"));
		String class_value = modeParent.getAttribute("class");

		// Set to grid mode if current data is not display in grid mode
		if(!class_value.contains("checked")) {
			mode.click();
			LogUtils.info("View account list in grid mode.");
		}
	}

	public void setViewContentListMode() {
		WebElement mode = getListModeEle();
		WebElement modeParent = mode.findElement(By.xpath("./parent::div"));
		String class_value = modeParent.getAttribute("class");

		// Set to list mode if current data is not display in list mode
		if(!class_value.contains("checked")) {
			mode.click();
			LogUtils.info("View account list in list mode.");
		}
	}

	public void setAccountNickname(String accNickname) {
		WebElement e = getAccountNicknameEle();
		setInputValue(e, accNickname);
		LogUtils.info("Set Nickname: " + accNickname);
	}

	public void setAccountBalance(String balance) {
		WebElement e = getAccountBalanceEle();
		setInputValue(e, balance);
		LogUtils.info("Set Balance: " + balance);
	}

	public void setAccountCurrentPwd(String pwd)
	{
		// Set password
		WebElement e = getAccountCurrentPwdEle();
		setInputValue_withoutMoveElement(e, pwd);
		LogUtils.info("Set Current Pwd to: " + pwd);
		waitLoading();
		// Click at other element for page to check password validity
		triggerElementClickEvent_withoutMoveElement(getAccountNewPwdEle());
		LogUtils.info("Click New Password textbox");
	}

	public void setAccountNewPwd(String pwd)
	{
		// Set password
		WebElement e = getAccountNewPwdEle();
		setInputValue_withoutMoveElement(e, pwd);
		LogUtils.info("Set New Pwd to: " + pwd);
		waitLoading();
		// Click at other element for page to check password validity
		triggerElementClickEvent_withoutMoveElement(getAccountConfirmNewPwdEle());
		LogUtils.info("Click Confirm New Password textbox");
	}

	public void setAccountConfirmNewPwd(String pwd)
	{
		// Set password
		WebElement e = getAccountConfirmNewPwdEle();
		setInputValue_withoutMoveElement(e, pwd);
		LogUtils.info("Set Confirm New Pwd to: " + pwd);
		// Click at other element for page to check password validity
		waitLoading();
//		triggerElementClickEvent_withoutMoveElement(getAccountNewPwdEle());
	}

	public void tickbox() {
		triggerElementClickEvent(getLeverageAgreementEle());
		LogUtils.info("Tick leverage agreement");
	}

	public void submitLeverage() {
		triggerElementClickEvent(getSubmitLeverageBtn());
		LogUtils.info("Click Submit button");
	}

	public void submitChgDemoAccountBalance() {
		triggerElementClickEvent(getResetBalanceDialogSubmitBtn());
		LogUtils.info("Click Confirm button");
	}

	public void submitChgAccountNickname() {
		WebElement e = getChgAccountNicknameSubmitBtn();
		triggerElementClickEvent(e);
		waitLoading();
		ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "submitChgAccNickname");
		LogUtils.info("Click Confirm button");
	}

	public void submitRemoveAccountCredit() {
		WebElement e = getRemoveAccountCreditSubmitBtn();
		triggerElementClickEvent(e);
		LogUtils.info("Click Confirm button");
	}

    public void submitChgAccountPassword() {
        WebElement e = getAccountChgPwdSubmitBtnEle();
        triggerElementClickEvent(e);
		LogUtils.info("Click Change Password button");
    }

	public void checkChgLeverageConfirmationDialog() {
		WebElement e = getLeverageConfirmationDialogEle();

		if (e != null) {
			LogUtils.info("Leverage Confirmation Dialog found");
			triggerElementClickEvent(getLeverageConfirmationBtnEle());
		} else {
			LogUtils.info("No Leverage Confirmation Dialog found");
		}
	}

	public void closeDialog() {
		WebElement e = getDialogCloseBtnEle();
		if (e == null) {
			Assert.fail("Dialog Close button");
		}
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Close Dialog");
	}

	public void checkExistsDialog() {
		WebElement dialogCloseBtnEleEle = getDialogCloseBtnEle();
		if (dialogCloseBtnEleEle != null) {
			triggerElementClickEvent_withoutMoveElement(dialogCloseBtnEleEle);
			LogUtils.info("Close dialog found");
		}

		WebElement sideDialogCloseBtnEleEle = getSideDialogCloseBtnEle();
		if (sideDialogCloseBtnEleEle != null) {
			triggerElementClickEvent_withoutMoveElement(sideDialogCloseBtnEleEle);
			LogUtils.info("Close side dialog found");
		}
	}

	public void closeRemoveCreditDialog() {
		triggerElementClickEvent_withoutMoveElement(getRemoveAccountCreditOkBtn());
		LogUtils.info("Close Remove Credit Dialog");
	}

	public void closeChgAccountPwdDialog() {
		triggerElementClickEvent_withoutMoveElement(getChgAccountPwdDialogCloseBtnEle());
		LogUtils.info("Close Change Password Dialog");
	}

	public String getChgLeverageResponse() {
		WebElement dialog = getChgLeverageResponseContentEle();
		return dialog.getText();
	}

	public WebElement getRemoveAccountCreditResponse() {
		return assertVisibleElementExists(By.cssSelector("div.success_dialog p.status_message"), "Remove Credit Response Content");
	}

	public String getChgAccountPwdResponse() {
		return getChgAccountPwdResponseContentEle().getText();
	}

	public void waitLoadingAccountListContent() {
		getAccountListContentEle();
	}

	public List<Account> getFirstPageMTSAccountsWithBalance(GlobalProperties.PLATFORM platform){
		waitLoading();
		String xpath = "//div[@class='account_card_box']/div/div[not(.//div[contains(@class, 'copyTrading_box')])]";

		List<WebElement> divs = null;
		try {
			divs = driver.findElements(By.xpath(xpath));
		}catch(Exception e) {

			LogUtils.info("LiveAccountsPage: * ERROR: Some Error appeared at live accounts page!");
		}

		if(divs == null || divs.size() == 0)
		{
			LogUtils.info("LiveAccountsPage: Have not found any " + platform.toString() +"  account info");
		}

		return getMTSAccountsWithbalance(divs);
	}

	protected List<LiveAccountsPage.Account> getMTSAccountsWithbalance(List<WebElement> trs){
		ArrayList<Account> result = new ArrayList<>();
		for(WebElement tr:trs) {
			String info = tr.getText();
			LogUtils.info("info:" + info);
			String values[] = info.split("\n");
			int leh = values.length;

			for(int i = 0; i<leh;i++)
			{
				String v= values[i];
				System.out.println(v);
			}
			if(values.length < 2)
			{
				continue;
			}
			if(values[0].trim().equalsIgnoreCase("Documentary account") || StringUtils.containsIgnoreCase(values[1].trim(),"Set up your account to start trading live"))
			{
				continue;
			}
			else {
				String accnum = "";
				String server = "";
				String equity = "";
				String balance = "";
				String credit = "";
				String leve="";
				String acctype="";
				GlobalProperties.ACCOUNTTYPE type = null;
				GlobalProperties.CURRENCY currency = null;
				String leverage = "";
				GlobalProperties.ACC_STATUS status = null;
				GlobalProperties.PLATFORM platform = null;
				System.out.println("values[2].trim()" + values[2].trim());

				if(values[2].trim().equals("--.--")||values[3].trim().equals("--.--")) {//for new application
					//find account type
					LogUtils.info("finding type from: " + values[0].trim());
					for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
						if(t.getLiveAccountName().equalsIgnoreCase(values[6].trim())) {
							type=t;
							LogUtils.info("found type " +t);
							break;
						}
					}
					if (type == null) {
						LogUtils.info("Didn't find type 1: " + values[0].trim());
					}
					//find currency
					for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
						if(c.toString().equalsIgnoreCase(values[3].trim())) {
							currency = c;
							break;
						}
					}
					if (currency == null) {
						LogUtils.info("Didn't find currency: " + values[2].trim());
					}
					//find status
					for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
						if(s.toString().equalsIgnoreCase(values[0].trim())) {
							status = s;
							break;
						}
					}
				}else {
					accnum = values[2].trim();
					// server = values[9].trim();
					equity = values[4].trim();
					credit = values[6].split("\\s")[1];

					Pattern pattern = Pattern.compile("Balance\\D+(\\d+\\.\\d+)");
					Matcher matcher = pattern.matcher(values[7]);
					if (matcher.find()) {
						balance = matcher.group(1);
					}

					String item = values[3];
					// Split the string using the regular expression pattern
					if(item.contains("MTS"))
					{
						// Match the leverage at the beginning: digits, colon, digits
						pattern = Pattern.compile("^(\\d+\\s*:\\s*\\d+)\\s+(.*)");
						matcher = pattern.matcher(item);

						if (matcher.find()) {
							leve = matcher.group(1);
							String details = matcher.group(2);

							String[] typeServer = details.split("(?=\\bMTS[-\\w]*)", 2);
							acctype = typeServer[0];
							server = typeServer[1];

						} else {
							System.out.println("No match found.");
						}

					}
					else {
						/*String[] splitString = item.split("\\s+(?=\\p{Lu}\\p{Ll})");

						// Extract the desired values
						leve = splitString[0].trim();
						acctype = splitString[1].trim();
						server = splitString[2].trim();*/

						pattern = Pattern.compile("^(\\d+\\s*:\\s*\\d+)\\s+([A-Za-z]+(?:\\s+[A-Za-z]+)*)\\s+(\\S+)$");
						matcher = pattern.matcher(item);

						if (matcher.find()) {
							leverage = matcher.group(1).trim();
							acctype = matcher.group(2).trim();
							server = matcher.group(3).trim();

							System.out.println("Leverage: " + leverage);
							System.out.println("Account Type: " + acctype);
							System.out.println("Server: " + server);
						} else {
							System.out.println("Pattern not matched.");
						}

					}

					LogUtils.info("leverage: " + leve);
					LogUtils.info("acctype: " + acctype);
					LogUtils.info("server: " + server);
					// find account type
					for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
						if(t.getLiveAccountName().equalsIgnoreCase(acctype)) {
							type=t;
							break;
						}
					}
					if (type == null) {
						LogUtils.info("Didn't find type 2: " + values[9].trim());
					}
					// find currency
					for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
						if(c.toString().equalsIgnoreCase(values[5].trim())) {
							currency = c;
							break;
						}
					}
					// find platform
					for(GlobalProperties.PLATFORM p: GlobalProperties.PLATFORM.values()) {
						if(p.toString().equalsIgnoreCase(values[1].trim())) {
							platform = p;
							break;
						}
					}
					// find status
					for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
						if(s.toString().equalsIgnoreCase(values[0].trim())) {
							status = s;
							break;
						}
					}

					// leverage = values[7];
					leverage = leve.trim();
				}

				LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accnum,server,type,currency,leverage,status,equity,balance,credit, platform, "");
				result.add(acc);
			}
		}

		return result;
	}

	// 获取账号区域显示的信息
	public String getAccountInfo() {

		String accountInfo = findVisibleElemntByCss("#LivePage > div.page_card > div > div > div > div").getText();

		System.out.println(accountInfo);
		return accountInfo;
	}

	public void clickAccountSettingBtn(PLATFORM platform, String accNum, boolean bIsDemo) {

		// Get account row number
		int pos = this.getRowNum(platform, accNum, bIsDemo);

		if (pos < 0) {
			return;
		}

		// Click Account Setting Icon
		waitLoading();
		WebElement e = bIsDemo ? getDemoAccountSettingIconEle(pos) : getAccountSettingIconEle(pos);
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Click at account position [" + pos +  "] Account Setting icon");
	}

	public void clickAccountLeverageBtn(PLATFORM platform) {
		triggerClickEvent(getChangeLeverageBtnEle(platform));
		waitLoading();
		LogUtils.info("Click Change Leverage button");
	}

	public void clickDemoAccountLeverageBtn(PLATFORM platform) {
		triggerClickEvent(getDemoChangeLeverageBtnEle(platform));
		waitLoading();
		LogUtils.info("Click Demo Change Leverage button");
	}

	public void clickDemoAccountBalanceBtn(PLATFORM platform,String accNum) {

		int pos = this.getRowNum(platform, accNum, true);

		if (pos < 0) {
			return;
		}

		waitLoading();
		triggerElementClickEvent_withoutMoveElement(getEditBalanceEle(pos));
		LogUtils.info("Click at account position [" + pos +  "] Edit Balance icon");
	}

	public void clickSetAccountNicknameBtn(PLATFORM platform, String accNum) {
		triggerClickEvent(getSetAccountNicknameBtnEle());
		LogUtils.info("Click Set Nickname button");
	}

	public void clickRemoveCreditBtn(PLATFORM platform, String accNum) {
		triggerClickEvent(getRemoveCreditBtnEle(platform));
		LogUtils.info("Click Remove Credit button");
	}

	public void clickAccountChangePwdBtn(PLATFORM platform) {
		triggerClickEvent(getAccountChangePwdBtnEle(platform));
		LogUtils.info("Click Change Password button");
	}

	public void clickProfileAvatar() {
		waitLoading();
		WebElement e = getProfileAvatarEle();
		String val = e.getAttribute("value");

		if(val == null) {
			e.click();
			waitLoading();
			LogUtils.info("Open Profile Avatar");
		}
	}

	public void clickAssetsCurrencyDropdown() {}

	public void closeProfileAvatar() {
		WebElement e = getProfileAvatarEle();
		String val = e.getAttribute("value");

		if(val != null) {
			e.click();
			LogUtils.info("Close Profile Avatar");
		}
	}

	public String getProfileUserId() {
		WebElement e = getProfileUserIdEle();
		WebElement lbl = getProfileUserIdLabelEle();
		String userId = e.getText().replace(lbl.getText(), "").trim();
		LogUtils.info("Profile User ID: " + userId);

		return userId;
	}

	public String getAssetsCurrencyImg() {
		WebElement e = getAssetsCurrencyImgEle();
		String src = e.getAttribute("src");
		String imgSrc = e.getAttribute("data-src");

		if (src == null) {
			Assert.fail("Assets Currency Image Source is empty");
		} else {
			LogUtils.info("Assets Currency Image found");
		}

		if (imgSrc != null) {
			LogUtils.info("Assets Currency Image Source: " + imgSrc);
		}

		return imgSrc;
	}

	public String getAssetsAmount() {
		WebElement e = getAssetsAmountEle();
		String desc = e.getText();
		LogUtils.info("Assets Amount: " + desc);

		return desc;
	}

	public String getAssetsCurrency() {
		WebElement e = getAssetsCurrencyEle();
		String desc = e.getText();
		LogUtils.info("Assets Currency: " + desc);

		return desc;
	}

	public List<WebElement> getTradingAccount() {
		List<WebElement> e = getTradingAccountEle();

		return e;
	}

	public String getTradingAccountPlatform(WebElement ele) {
		WebElement e = getTradingAccountPlatformEle(ele);
		String desc = e.getText();
		LogUtils.info("Trading Account Platform: " + desc);

		return desc;
	}

	public String getTradingAccountNo(WebElement ele) {
		WebElement e = getTradingAccountNoEle(ele);
		String desc = e.getText();
		LogUtils.info("Trading Account No.: " + desc);

		return desc;
	}

	public String getTradingAccountAmount(WebElement ele) {
		WebElement e = getTradingAccountAmountEle(ele);
		String desc = e.getText();
		LogUtils.info("Trading Account Amount: " + desc);

		return desc;
	}

	public String getTradingAccountCurrency(WebElement ele) {
		WebElement e = getTradingAccountCurrencyEle(ele);
		String desc = e.getText();
		LogUtils.info("Trading Account Currency: " + desc);

		return desc;
	}

	public String getTradingAccountAccountType(WebElement ele) {
		WebElement e = getTradingAccountAccountTypeEle(ele);
		String desc = e.getText();
		LogUtils.info("Trading Account Account Type: " + desc);

		return desc;
	}

	public String checkAccountCurrentPwdErrMsg() {
		WebElement e = getAccountCurrentPwdErrMsgEle();
		return e == null ? "" : e.getText();
	}

	protected int getRowNum(PLATFORM platform, String accNum, boolean bIsDemo) {
		int pos = 0;
		boolean find = false;

		List<Account> accountList = bIsDemo ? this.getFirstPageDemoAccountsByPlatform(platform) : this.getFirstPageAccountsByPlatform(platform);

		for(Account acc: accountList) {
			if(acc.getAccountNum().equalsIgnoreCase(accNum.trim())) {
				find = true;
				break;
			}
			if(!acc.getAccountNum().isEmpty())
				pos++;
		}
		if(!find) {
			LogUtils.info("Did not find the account : " + accNum + " at Platform " + platform);
			return -1;
		}
		return pos+1;
	}

	public Map.Entry<Boolean, String> checkAlertMsg(String label) {
		return checkAlertMsg(this::getAlertMsgBy, label);
	}

	public Map.Entry<Boolean, String> checkAlertMsg(Supplier<By> elementSupplier, String label) {
		// Capture ss
		ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", label.replace(" ", ""));

		// Element path
		By locator  = elementSupplier.get();
		if (locator == null) return new AbstractMap.SimpleEntry<>(false, null);

		// Get element by locator
		WebElement alertMsgEle = checkElementExists(locator);
		if (alertMsgEle == null) return new AbstractMap.SimpleEntry<>(false, null);

		// Get element class
		String cls = alertMsgEle.getAttribute("class");
		if (cls == null || cls.isEmpty()) return new AbstractMap.SimpleEntry<>(false, null);
		cls = cls.trim().toLowerCase();

		// Get message content element text
		String alertMsg = alertMsgEle.findElement(By.tagName("p")).getText();
		LogUtils.info(label + " Resp Msg: " + alertMsg);

		boolean bIsSuccessMsg = cls.contains("success");
		boolean bIsErrorMsg = cls.contains("error") || cls.contains("warning");

		// Check empty on msg
		if (alertMsg == null || alertMsg.isEmpty()) {
			Assert.fail(String.format("%s %s but response message is empty", label, (bIsSuccessMsg ? "success" : "failed")));
		}

		// Check for error / warning msg
		if(bIsErrorMsg) {
			Assert.fail(label + " failed. Resp Msg: " + alertMsg);
		}

		// Check for  success msg
		if(bIsSuccessMsg) {
			return new AbstractMap.SimpleEntry<>(true, alertMsg);
		}

		return new AbstractMap.SimpleEntry<>(false, null);
	}

}
