package newcrm.pages.starclientpages;

import org.openqa.selenium.*;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.MenuPage;
import utils.LogUtils;

public class STARMenuPage extends MenuPage {

	JavascriptExecutor js = (JavascriptExecutor) driver;

	public STARMenuPage(WebDriver driver) {
		super(driver);
		
	}

	// region [ Client Portal ]

	// region [ General ]

	@Override
	public void switchToCP() {
		String title = driver.getTitle();
		closeCouponGuideDialog();
		close2FADialog();
		closeNotificationDialog();
		if(!title.toLowerCase().contains("ib")) {
			LogUtils.info("At CP portal. Do not need switch");
			return;
		}
		WebElement cp = this.findClickableElemntByTestId("redirectToCp");
		this.moveElementToVisible(cp);
		cp.click();
		this.waitLoading();
	}

	// endregion

	// region [ CP Menu ]

	@Override
	protected WebElement getProfileSecurityMgmtMenuEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='menu.security']"), "Security Management Tab");
	}

	@Override
	public void clickSecurityManagement() {
		WebElement webElement = getProfileSecurityMgmtMenuEle();
		triggerClickEvent_withoutMoveElement(webElement);
		LogUtils.info("Go to Security Management page");
	}

	// endregion

	// region [ CP Dialog ]

	public void closeNotificationDialog()
	{
		WebElement popup = checkElementExists(By.cssSelector("div[data-testid='notificationDialog']:not([style*='display: none']) svg.ht-icon-close"));
		if (popup != null) {
			popup.click();
			LogUtils.info("Close Notification Dialog");
		}
	}

	public void closeTnCDialog()
	{
		WebElement popup = checkElementExists(By.cssSelector("div.tncDialog:not([style*='display: none']) button"));
		if (popup != null) {
			popup.click();
			LogUtils.info("Close Withdrawal Processing Times Reminder Dialog");
		}
	}

	@Override
	public void closeCouponGuideDialog() {
		WebElement popup = getCloseCouponDialogEle();
		if (popup != null) {
			triggerClickEvent_withoutMoveElement(popup);
			LogUtils.info("Close Coupon Guide Dialog");
		}
	}

	// endregion

	// endregion

    @Override
    protected WebElement getIBReportMenuEle() {
        return assertElementExists(By.xpath("//span[contains(@data-testid, '/rebatereport')]"), "IB Report Menu");
    }

	@Override
	public void clickHome() {
		waitLoading();
		waitLoading();
		close2FADialog();
		closeBannerDialog();
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
		triggerClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Home page");
	}

	@Override
	public void clickFund() {
		//refresh();
		waitLoading();
		closeIndonesiaDiag();
		closeBannerDialog();

		//WebElement funds = assertElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");

		By fundsLocator = By.xpath("//li[@data-testid='menu.funds']");
		WebElement funds;
		try {
			funds = assertElementExists(fundsLocator, "Funds menu");
		} catch (AssertionError first) {
			refresh();
			waitLoading();
			try {
				funds = assertElementExists(fundsLocator, "Funds menu (after refresh)");
			} catch (AssertionError second) {
				throw new AssertionError("Funds menu not found after refresh", second);
			}
		}
		String cls = funds.getAttribute("class");
		if(cls != null && cls.trim().toLowerCase().contains("is-active") && driver.findElements(By.xpath("//div[@id='depositPCS'] | //div[@id='withdraw'] | //div[@id='transactionHistory'] | //div[@id='transferFunds']")).isEmpty()) {
			clickProfile();
		}

		String expand = funds.getAttribute("aria-expanded");
		//expanded,return
		if(expand !=null && expand.toLowerCase().trim().equals("true")) {
			return;
		}

		close2FADialog();
		triggerElementClickEvent_withoutMoveElement(funds);
		LogUtils.info("Go to Funds page");
	}

	@Override
	public void clickWithdrawFunds() {
		waitLoading();
		clickFund();
		closeBannerDialog();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.withdrawFunds']/span"), "Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Withdraw Funds page");
		closeTnCDialog();
	}

	@Override
	public void clickDepositFunds() {
		clickFund();
		closeBannerDialog();
		WebElement funds = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']/span"), "Deposit Funds menu");
		triggerElementClickEvent_withoutMoveElement(funds);
		LogUtils.info("Go to Deposit Funds page");
		closeBannerDialog();
	}

	@Override
	public void clickTransferAcc() {
		clickFund();
		closeBannerDialog();
		//use span for compatibility with both Windows and macOS
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.transferBetweenAccs']/span"), "Transfer Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transfer Funds page");
	}

	@Override
	public void clickTransactionHis() {
		waitLoading();
		clickFund();
		closeBannerDialog();
		WebElement e = assertVisibleElementExists(By.xpath("//*[@data-testid='menu.transactionHis']/span"), "Transaction History menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transaction History page");
	}

	@Override
	public void clickLiveAccount() {
		waitLoading();
		closeNotificationDialog();
		clickAccount();
		close2FADialog();
		closeNotificationDialog();
		closeBannerDialog();
	}

	@Override
	public void ibDashBoard() {
		waitLoading();
		closeSkipDialog();
		closeIBNotificationDialog();
		closeBannerDialog();

		WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
		triggerElementClickEvent(e);
		LogUtils.info("Go to IB Dashboard page");
	}

    @Override
    public void ibRebateReport() {
        triggerElementClickEvent_withoutMoveElement(this.getIBReportMenuEle());
        this.waitLoading();
        closeIBReportPopUp();
        triggerElementClickEvent_withoutMoveElement(this.getRebateReportTabEle());
        LogUtils.info("Go to IB Report - Rebate Report menu page");
    }

	public void closeSkipDialog() {
		WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')]"));
		if (popup != null) {
			clickElement(popup);
			waitLoading();
			LogUtils.info("Close Skip Dialog");
		}
	}

	@Override
	public void refresh() {
		driver.navigate().refresh();
		LogUtils.info("Refresh page");
		this.waitLoading();
		closeNotificationDialog();
	}

    // Indonesia dialog
 	public void closeIndonesiaDiag()
 	{
 		try
 		{
 			WebElement guideDialog = driver.findElement(By.xpath("(//div[@class='page-dialogs-box']/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn'])[1]"));
 			js.executeScript("arguments[0].click()", guideDialog);

 		}
 		catch (Exception e)
 		{
 			LogUtils.info("no indonesia guide dialog");
 		}
 	}
 	
 	// Announcement banner
	public void closeBannerDialog()
	{
		WebElement popup = checkElementExists(By.xpath("//img[@data-testid='closeImg'] | (//img[@data-testid='closeImg'])[1]"));
		if (popup != null) {
			clickElement(popup);
			LogUtils.info("Close Banner Dialog");
		}
	}

	@Override
	public void closeIBNotificationDialog()
	{
		WebElement popup = checkElementExists(By.xpath("//div[contains(@class,'el-dialog__wrapper ht-dialog') and not(contains(@style,'display: none'))]"));
		if (popup != null) {
			WebElement closeIcon = checkElementExists(By.cssSelector("svg.ht-icon-close"));
			closeIcon.click();
			LogUtils.info("Close IB Notification Dialog");
		}
	}

	public void close2FADialog()
	{
		try
		{
			WebElement popup = driver.findElement(By.xpath("//el-button[@class='experience-button']"));
			js.executeScript("arguments[0].click()", popup);
		}
		catch (Exception e)
		{
			LogUtils.info("No 2fa dialog");
		}
	}

    @Override
    public void closeAccQuizDialogPopup() {
        try {
            WebElement closeBtn = driver.findElement(By.xpath("//div[contains(@class, 'Popup')] //div[@aria-label='dialog'] //button[@aria-label='Close']"));
            triggerElementClickEvent_withoutMoveElement(closeBtn);
        }
        catch(Exception e) {
			LogUtils.info("No quiz pop up");
        }
    }

}
