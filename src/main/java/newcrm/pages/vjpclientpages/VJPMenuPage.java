package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.MenuPage;
import org.openqa.selenium.*;
import org.testng.Assert;
import utils.LogUtils;

public class VJPMenuPage extends MenuPage {

    JavascriptExecutor js = (JavascriptExecutor) driver;

    public VJPMenuPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void switchToCP() {
        String title = driver.getTitle();
        try
        {
            WebElement notification = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']//button[@class='el-dialog__headerbtn']"));
            js.executeScript("arguments[0].click()", notification);
        }
        catch(Exception e)
        {
            System.out.println("Notification dialog do not show");
        }
        closeImg();
        closeImgSec();
        if(!title.toLowerCase().contains("ib")) {
            LogUtils.info("At CP portal. Do not need switch");
            return;
        }
        WebElement cp = this.findClickableElemntByTestId("redirectToCp");
        this.moveElementToVisible(cp);
        cp.click();
        this.waitLoading();

    }

    @Override
    public void clickHome() {
        waitLoading();
        waitLoading();
        closeImgSec();
        WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Home page");
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

    @Override
    public void clickDepositFunds() {
        clickFund();
        closeImgSec();
        WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']/span"), "Deposit Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Deposit Funds page");
        closeImgSec();
    }

    @Override
    public void clickLiveAccount() {
        closeImg();
        closeImgSec();
        clickAccount();
    }

    @Override
    public void clickFund() {
        waitLoading();
        closePopup();
        closeGuideDiag();
        closeImgSec();
        WebElement funds = assertElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");

        String cls = funds.getAttribute("class");
        if(cls != null && cls.trim().toLowerCase().contains("is-active") && driver.findElements(By.xpath("//div[@id='depositPCS'] | //div[@id='withdraw'] | //div[@id='transactionHistory'] | //div[@id='transferFunds']")).isEmpty()) {
            clickProfile();
        }

        String expand = funds.getAttribute("aria-expanded");
        //expanded,return
        if(expand !=null && expand.toLowerCase().trim().equals("true")) {
            return;
        }

        waitLoading();
        triggerElementClickEvent_withoutMoveElement(funds);
        LogUtils.info("Go to Funds page");
        closePopup();
    }

    @Override
    public void clickTransactionHis() {
        waitLoading();
        clickFund();
        waitLoading();
        closeImgSec();
        assertVisibleElementExists(By.xpath("//*[@data-testid='menu.transactionHis']/span"), "Transaction History menu").click();
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
            WebElement img = driver.findElement(By.xpath("//div[@data-testid='notificationDialog']"));
            js.executeScript("arguments[0].click()", img);

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
            WebElement img = driver.findElement(By.xpath("//img[@data-testid='closeImg'] | (//img[@data-testid='closeImg'])[1]"));
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
        closeGuideDiag();
        closeIBNotification();
        closeImgSec();

        try
        {
            WebElement popup = driver.findElement(By.xpath("//body/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn']"));
            js.executeScript("arguments[0].click()", popup);
            this.waitLoading();
        }
        catch (Exception e)
        {
            LogUtils.info("no pop up window");
        }

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
        WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')]"));
        if (popup != null) {
            clickElement(popup);
            waitLoading();
            LogUtils.info("Close Skip Dialog");
        }
    }

    public void clickDepositFundsMobile() {
        WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Deposit Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Mobile Deposit Funds page");
        closePopup();
        closeImgSec();
    }

    public void clickWithdrawFundsMobile() {
        WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Withdraw Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        closePopup();
        WebElement withdraw = assertElementExists(By.xpath("//div[@data-testid='menu.withdrawFunds']"), "Mobile Withdraw Funds menu");
        triggerElementClickEvent_withoutMoveElement(withdraw);
        LogUtils.info("Go to Mobile Withdraw Funds page");
        closePopup();
        closeImgSec();
    }

    public void clickTransactionHisMobile() {
        this.goBack();
        closePopup();
        WebElement e = assertClickableElementExists(By.xpath("(//div[@class='tab-bar-item'])[3]"), "Mobile Transaction History menu");
        triggerElementClickEvent_withoutMoveElement(e);
        e = assertVisibleElementExists(By.xpath("//div[@data-testid='menu.transactionHis']"), "Mobile Transaction History menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Mobile Transaction History page");
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

    @Override
    public void clickCopyTrading() {
        waitLoading();
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
            LogUtils.info("No ad image display");
        }

        WebElement e = this.getCopyTradingEle();
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Open Copy Trading menu");
    }

}
