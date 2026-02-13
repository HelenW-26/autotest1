package newcrm.pages.auclientpages;

import org.openqa.selenium.*;

import newcrm.pages.clientpages.MenuPage;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.util.Map;

public class AUMenuPage extends MenuPage {
	JavascriptExecutor js = (JavascriptExecutor) driver;

	public AUMenuPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void clickHome() {
		waitLoading();
		closeImgSec();
		closeWelcomeDialog();
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Home page");
		closeImgSec();
	}

	@Override
	public void clickTransferAcc() {
		clickFund();
		closePopup();
		closeImgSec();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.transferBetweenAccs']/span"), "Transfer Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transfer Funds page");
		closePopup();
		waitLoading();
	}

	@Override
	public void clickWithdrawFunds() {
		clickFund();
		closeImgSec();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.withdrawFunds']/span"), "Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Withdraw Funds page");
		closePopup();
	}

	public void clickDepositFunds() {
		clickFund();
		closeImgSec();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']"), "Deposit Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Deposit Funds page");
	}

	@Override
	public void clickLiveAccount() {
		closeImgSec();
		clickAccount();
	}

	public void clickCopier() {
		int maxRetry = 3;
		int count = 0;
		List<WebElement> titleList;

		// Retry and check if the page refreshes successfully
		while (count <= maxRetry) {
			clickCopyTrading();
			WebElement e = this.getCopyTradingCopierEle();
			triggerElementClickEvent_withoutMoveElement(e);

			waitLoading();
			waitLoadingInCopyTrading();

			// Locate the title element to verify if the page has switched
			titleList = driver.findElements(By.cssSelector("div.title"));

			if (!titleList.isEmpty()) {
				LogUtils.info("Page refreshed successfully, target copier page reached.");
				break;
			}

			count++;
			LogUtils.info("Page not refreshed, retrying... Attempt: " + count);
		}

		WebElement title = driver.findElement(By.cssSelector("div.title"));
		Assert.assertTrue(title.getText().toLowerCase().contains("copier"), "Copier page is not opened");
		LogUtils.info("Successfully navigated to Copy Trading - Copier page");
	}

	public void clickCopyTrading() {
		try{
			driver.findElement(By.xpath("//div[@class='ht-dialog__close']")).click();
		}
		catch(Exception e){
			LogUtils.info("No ad dialog display");
		}

		try{
			driver.findElement(By.xpath("//*[@data-testid='closeImg']")).click();
		}
		catch(Exception e){
			LogUtils.info("No ad dialog display");
		}

		waitLoading();
		try{
			driver.findElement(By.xpath("//li[@id='overflowItems']//div[@class='el-submenu__title']")).click();
		}
		catch(Exception e){
			LogUtils.info("submenu not found");
		}


		WebElement e = this.getCopyTradingEle();
		triggerElementClickEvent(e);
		LogUtils.info("Open Copy Trading menu");
	}

	@Override
	public void clickFund() {
		waitLoading();
		closeImg();
		closeImgSec();
		closeWelcomeDialog();
		waitLoading();
        WebElement funds = null;
        try {
            funds = assertElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");
        } catch (ElementNotInteractableException e) {
           refresh();
			waitLoading();
			funds = assertElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");
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
		//closeGuideDiag();
		closePopup();
		closeGuideDiag();
		waitLoading();
		closeImgSec();
		triggerElementClickEvent_withoutMoveElement(funds);
		LogUtils.info("Go to Funds page");
		closeUSDTDialogPopup();
		closePopup();
		closeImgSec();
	}

	@Override
	public void clickDownload() {
		clickCPWebMenu(List.of(Map.entry(getDownloadMenu(), "Download menu")));
		closeImgSec();
		LogUtils.info("Go to Download page");
	}

	@Override
	public void switchToCP() {
		String title = driver.getTitle();
		//closeGuideDiag();
		closeImg();
		closeImgSec();
		closeAccQuizDialogPopup();
		//closeIndonesiaDiag();
		if(!title.toLowerCase().contains("ib")) {
			LogUtils.info("At CP portal. Do not need switch");
			return;
		}
        WebElement profilePanel = this.findClickableElementByXpath("//span[@class='ht-popover profile-panel-popover']");
        this.moveElementToVisible(profilePanel);
        profilePanel.click();
		WebElement cp = this.findClickableElementByXpath("//div[@class='ht-switcher__item' and text()='Client']");
		this.moveElementToVisible(cp);
		cp.click();
		this.waitLoading();
	}

	@Override
	public void clickTransactionHis() {
		waitLoading();
		refresh();
		clickFund();
		//waitLoading();
		//closeGuideDiag();
		//closeImg();
		waitLoading();
		WebElement transactionHis = assertVisibleElementExists(By.xpath("//*[@data-testid='menu.transactionHis']/span"), "Transaction History menu");
		js.executeScript("arguments[0].click();",transactionHis);
		waitLoadingForCustomise(120);
		LogUtils.info("Go to Transaction History page");
	}

	public void closePopup()
	{
		try
		{
			//findClickableElementByXpath("//div[@class='not_show_again']");
			WebElement notShow = driver.findElement(By.xpath("//*[@class='not_show_again']"));

			js.executeScript("arguments[0].click()", notShow);

		}
		catch (Exception e)
		{
			LogUtils.info("no show again window");
		}
		try
		{

			//findClickableElementByXpath("//button[@data-testid='pleasenoteDig']");
			WebElement popup = driver.findElement(By.xpath("//button[@data-testid='pleasenoteDig']"));

			js.executeScript("arguments[0].click()", popup);

		}
		catch (Exception e)
		{
			LogUtils.info("no pop up window");
		}
	}

	public void closeImg()
	{
		try
		{
			// Notification dialog for CP
			WebElement img = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']"));
			js.executeScript("arguments[0].click()", img);
		}
		catch (Exception e)
		{
			LogUtils.info("no notification Dialog");
		}
	}
	/**
	 * Close welcome dialog
	 */
	public void closeWelcomeDialog()
	{
		try
		{
			// Welcome Notification dialog for CP
			WebElement img = driver.findElement(By.xpath("//div[contains(@class,'ht-dialog__close')]"));
			img.click();

		}
		catch (Exception e)
		{
			LogUtils.info("no welcome notification Dialog");
		}
	}

	public void closeIBNotification()
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

	public void closeImgSec()
	{
		try
		{
			WebElement img = driver.findElement(By.xpath("//img[@data-testid='closeImg'] | (//img[@data-testid='closeImg'])[1]|//*[local-name()='svg' and @class= 'iconfont ht-icon-close']"));
			js.executeScript("arguments[0].click()", img);
		}
		catch (Exception e)
		{
			LogUtils.info("no img window");
		}
		try
		{
			WebElement img = driver.findElement(By.xpath("//a[@class='introjs-skipbutton'] | (//div[@class='page-dialogs-box']/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn'])[1]"));
			js.executeScript("arguments[0].click()", img);

		}
		catch (Exception e)
		{
			LogUtils.info("no tools popup window");
		}
	}

	public void closeGuideDiag()
	{
		try
		{
			//findClickableElementByXpath(("//div[@class='el-dialog__wrapper guide_dialog']//button[@class='el-dialog__headerbtn']"));
			WebElement guideDialog = driver.findElement(By.xpath("//div[@class='el-dialog__wrapper guide_dialog']//button[@class='el-dialog__headerbtn']"));
			js.executeScript("arguments[0].click()", guideDialog);

		}
		catch (Exception e)
		{
			LogUtils.info("no guide dialog");
		}
		//Announcement Setting - Notification
		try
		{
			WebElement popup = driver.findElement(By.xpath("//body/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn'] | //div[@class='el-dialog__wrapper question-dialog']//button[@aria-label='Close']"));
			js.executeScript("arguments[0].click()", popup);
		}
		catch (Exception e)
		{
			LogUtils.info("No notification dialog");
		}
	}

	public void closeUSDTDialogPopup() {
		try {
			WebElement img = driver.findElement(By.xpath("//div[@class='USDT_dialog']//div[@class='ht-dialog__close']/img"));
			js.executeScript("arguments[0].click()", img);
		}
		catch(Exception e) {
			LogUtils.info("No dialog pop up");
		}
	}
    @Override
	public void closeAccQuizDialogPopup() {
		try {
			WebElement img = driver.findElement(By.xpath("//div[contains(@class,'el-drawer__wrapper ht-drawer') and not(contains(@style, 'display: none'))]//button[@aria-label='close drawer']"));
			js.executeScript("arguments[0].click()", img);
		}
		catch(Exception e) {
			LogUtils.info("No quiz pop up");
		}
	}

	public void closeDialog()
	{
		try
		{
			// Use when dialog close button is not available. E.g. mobile view - change language
			WebElement dialog = driver.findElement(By.xpath("//div[@class='el-drawer__wrapper ht-drawer']"));
			js.executeScript("arguments[0].style.display = 'none';", dialog);

			dialog = driver.findElement(By.xpath("//div[@class='v-modal']"));
			js.executeScript("arguments[0].style.display = 'none';", dialog);
		}
		catch (Exception e)
		{
			LogUtils.info("No Dialog");
		}
	}

	// AU Indonesia dialog
	/*public void closeIndonesiaDiag()
	{
		try
		{
			//findClickableElementByXpath(("(//div[@class='page-dialogs-box']/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn'])[1]"));
			WebElement guideDialog = driver.findElement(By.xpath("(//div[@class='page-dialogs-box']/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn'])[1]"));
			js.executeScript("arguments[0].click()", guideDialog);

		}
		catch (Exception e)
		{
			LogUtils.info("no indonesia guide dialog");
		}
	}*/ //Move to closeImgSec()

	//IB menu
	@Override
	public void ibDashBoard() {
		waitLoading();
		closeSkipDialog();
		closeImgSec();
		closeIBNotification();
		closeGuideDiag();
		WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
		triggerElementClickEvent(e);
		LogUtils.info("Go to IB Dashboard page");
	}

	@Override
	public void ibTransactionHistory() {
		this.waitLoading();
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

	public void closeSkipDialog() {
		WebElement popup = checkElementExists(By.xpath("//*[contains(@class, 'driver-close-btn')]"));
        try {
//            WebElement popup = findVisibleElemntByXpath("//*[contains(@class, 'driver-close-btn')]");

            if (popup != null) {
                clickElement(popup);
                waitLoading();
                LogUtils.info("Close Skip Dialog");
            }else {
                LogUtils.info("No Skip Dialog");
            }
        } catch (Exception e) {
           e.printStackTrace();

        }
    }

	public void clickDepositFundsMobile() {
		closeImgSec();
		WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Deposit Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Mobile Deposit Funds page");
		closeUSDTDialogPopup();
		closePopup();
		closeImgSec();
	}

	public void clickWithdrawFundsMobile() {
		closeImgSec();
		closePopup();
		WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		closeImgSec();
		WebElement withdraw = assertElementExists(By.xpath("//div[@data-testid='menu.withdrawFunds']"), "Mobile Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(withdraw);
		LogUtils.info("Go to Mobile Withdraw Funds page");
		closePopup();
		closeImgSec();
	}

	public void clickTransactionHisMobile() {
		//this.goBack();
		closeImgSec();
		closePopup();
		WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Transaction History menu");
		triggerElementClickEvent_withoutMoveElement(e);
		closeImgSec();
		e = assertVisibleElementExists(By.xpath("//div[@data-testid='menu.transactionHis']"), "Mobile Transaction History menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Mobile Transaction History page");
	}

	public void clickOpenAdditionAccountMobile() {
		closeImgSec();
		WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[2]"), "Mobile Open Account button");
		triggerElementClickEvent_withoutMoveElement(e);
		e = assertClickableElementExists(By.xpath("//span[@class='open-account']"), "Mobile Open Account button");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Mobile Open Additional Account page");
	}

}
