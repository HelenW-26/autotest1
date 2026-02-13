package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.MenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class UMMenuPage extends MenuPage {

    public UMMenuPage(WebDriver driver) {
        super(driver);
    }

	// region [ Client Portal ]

	// region [ General ]

	@Override
	public void switchToCP() {
		String title = driver.getTitle();
		closeCouponGuideDialog();
		closeImg();
		closeBanner();
		this.waitLoading();
		close2FADialog();
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
	public void clickFund() {
		waitLoading();

		List<WebElement> fundsList = driver.findElements(By.xpath("//li[@data-testid='menu.funds']"));
		int count = 0;

		while(fundsList.isEmpty() && count < 2) {
			refresh();
			waitLoading();
			fundsList = driver.findElements(By.xpath("//li[@data-testid='menu.funds']"));
			count++;
			LogUtils.info("Retry to find Funds menu. count:" + count);
		}

		WebElement funds = assertElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");
		funds.click();


		String cls = funds.getAttribute("class");
		if(cls != null && cls.trim().toLowerCase().contains("is-active") && driver.findElements(By.xpath("//div[@id='depositPCS'] | //div[@id='withdrawPcs'] | //div[@id='transactionHistory'] | //div[@id='transferFunds']")).isEmpty()) {
			clickProfile();
		}

		String expand = funds.getAttribute("aria-expanded");
		//expanded,return
		if(expand !=null && expand.toLowerCase().trim().equals("true")) {
			return;
		}
		closePopup();
		closeBanner();
		closeNotification();
		closeBanner();
		close2FADialog();
		triggerElementClickEvent_withoutMoveElement(funds);
		LogUtils.info("Go to Funds page");
		closeBanner();
	}

	public void closePopup() {
		try {
			WebElement img = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']//button[@class='el-dialog__headerbtn']"));
			js.executeScript("arguments[0].click()", img);
		} catch (Exception e) {
			LogUtils.info("no img window");
		}
	}

	@Override
	public void clickDepositFunds() {
		clickFund();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']/span"), "Deposit Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Deposit Funds page");
	}

	@Override
	public void clickWithdrawFunds() {
		clickFund();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.withdrawFunds']/span"), "Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Withdraw Funds page");
	}

	@Override
	public void clickTransactionHis() {
		waitLoading();
		clickFund();
		waitLoading();
		WebElement e = assertVisibleElementExists(By.xpath("//*[@data-testid='menu.transactionHis']/span"), "Transaction History menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transaction History page");
	}

	@Override
	public void clickTransferAcc() {
		clickFund();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.transferBetweenAccs']/span"), "Transfer Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transfer Funds page");
	}

	@Override
	public void clickLiveAccount() {
		closeNotification();
		clickAccount();
		close2FADialog();
		closeNotification();
	}

	@Override
	public void clickOpenAdditionAccount() {
		clickLiveAccount();
		triggerClickEvent_withoutMoveElement(getOpenAdditionalAccBtnEle());
		LogUtils.info("Go to Open Additional Account page");
	}

	@Override
	public void clickHome() {
		waitLoading();
		waitLoading();
		close2FADialog();
		closeBanner();
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Home page");
	}

	// endregion

	// region [ CP Dialog ]

	@Override
	public void closeCouponGuideDialog() {
		WebElement popup = getCloseCouponDialogEle();
		if (popup != null) {
			triggerClickEvent_withoutMoveElement(popup);
			LogUtils.info("Close Coupon Guide Dialog");
		}
	}

	public void closeImg()
	{
		try
		{
			WebElement img = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']"));
			js.executeScript("arguments[0].click()", img);

		}
		catch (Exception e)
		{
			LogUtils.info("no notification Dialog");
		}

	}

	public void close2FADialog()
	{
		WebElement popup = checkElementExists(By.xpath("//el-button[@id='experience-button']"));
		if (popup != null) {
			clickElement(popup);
			LogUtils.info("Close 2FA Dialog");
		}
	}

	// endregion

	// endregion

	// region [ IB Portal ]

	// region [ IB Menu ]

	@Override
	public void ibDashBoard() {
		waitLoading();
		closeSkipDialog();
		closeNotification();
		closeBanner();

		WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
		triggerElementClickEvent(e);
		LogUtils.info("Go to IB Dashboard page");
	}

	@Override
	public void ibTransactionHistory() {
		this.waitLoading();
		WebElement e = assertElementExists(By.xpath("(//li[@data-testid='/rebatePaymentHistory'])[1]"), "IB Transaction History menu");
		String style = e.getAttribute("style");
		String xpath = "";

		if (style != null && style.contains("visibility: hidden")) {
			e = driver.findElement(By.xpath("//li[@id='overflowItems']"));
			e.click();

			xpath = "//*[not(ancestor-or-self::*[contains(@style, 'visibility: hidden')]) and translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'click_/rebatepaymenthistory']";

		} else {
			xpath = "//*[not(ancestor-or-self::*[contains(@style, 'display: none')]) and translate(@data-testid, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'click_/rebatepaymenthistory']";
		}

		e = assertElementExists(By.xpath(xpath), "IB Transaction History menu");
		triggerElementClickEvent(e);
		LogUtils.info("Go to IB Transaction History page");
	}

	// endregion

	// region [ IB Dialog ]

	public void closeSkipDialog() {
		WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')]|//*[contains(text(), '跳过')]|//*[contains(@class, 'driver-close-btn')]"));
		if (popup != null) {
			clickElement(popup);
			waitLoading();
			LogUtils.info("Close Skip Dialog");
		}
	}

	// endregion

	// endregion

	// region [ General Dialog ]

	public void closeBanner()
	{
		try
		{
			WebElement img = driver.findElement(By.xpath("//img[@data-testid='closeImg']"));;
			js.executeScript("arguments[0].click()", img);
		}
		catch (Exception e)
		{
			LogUtils.info("No banner");
		}
	}

	public void closeNotification()
	{
		try
		{
			WebElement closeIcon = driver.findElement(By.cssSelector("svg.ht-icon-close"));
			closeIcon.click();
		}
		catch (Exception e)
		{
			LogUtils.info("no notification Dialog");
		}
	}

	// endregion

	public void closeAnnouncement()
	{
		try
        {
            //WebElement closeBtn = driver.findElement(By.xpath("//body/div[2]//button[@aria-label='Close']"));
			WebElement closeBtn = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']//button[@aria-label='Close'] | //body/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn']"));
            closeBtn.click();
        }
		catch (Exception e)
		{
			LogUtils.info("No popup dialog");
		}
	}

	public void clickCopyTrading() {
		waitLoading();
		try{
			driver.findElement(By.xpath("//*[@id='coupon-tip-button']")).click();
		}
		catch(Exception e){
			LogUtils.info("No coupon tip button display");
		}
		try{
			driver.findElement(By.xpath("//div[@class='ht-dialog__close']")).click();
		}
		catch(Exception e){
			LogUtils.info("No ad dialog display");
		}

		WebElement e = this.getCopyTradingEle();
		moveElementToVisible(e);
		e.click();
		LogUtils.info("Open Copy Trading menu");
	}

	public void clickCopier() {
		clickCopyTrading();
		WebElement e = this.getCopyTradingCopierEle();
		clickElement(e);
		waitLoading();
		waitLoadingInCopyTrading();
		LogUtils.info("Go to Copy Trading - Copier page");
	}
	@Override
	protected WebElement getCopyTradingCopierEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.Copier']"), "Copy Trading - Copier menu");
	}

	@Override
	protected WebElement getCopyTradingDiscoverEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.discover']"), "Copy Trading - Discover menu");
	}

	@Override
	protected WebElement getCopyTradingSignalProviderEle() {
		return assertClickableElementExists(By.xpath("(//li[@data-testid='copyTrading.signal_provider'])"), "Copy Trading - Signal Provider menu");
	}

}


