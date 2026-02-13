package newcrm.pages.pugclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.MenuPage;

import org.openqa.selenium.*;
import utils.LogUtils;

public class PUMenuPage extends MenuPage {
    public PUMenuPage(WebDriver driver)
    {
        super(driver);
    }

    // region [ Client Portal ]

    // region [ General ]

    public void switchToCP() {
        String title = driver.getTitle();
        closeNotificationDialog();
        closeBannerDialog();
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
    public void clickHome() {
        waitLoading();
        waitLoading();
        closeBannerDialog();
        WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Home page");
        closeBannerDialog();
    }

    @Override
    public void clickDepositFunds() {
        clickFund();
        closeBannerDialog();
        closeUSDTDialogPopup();
        WebElement funds = assertClickableElementExists(By.xpath("//div[@data-testid='menu.depositFund']"), "Deposit Funds menu");
        triggerElementClickEvent_withoutMoveElement(funds);
        LogUtils.info("Go to Deposit Funds page");
        closeBannerDialog();
    }

    @Override
    public void clickTransactionHis() {
        waitLoading();
        clickFund();
        closeBannerDialog();
        closeUSDTDialogPopup();
        waitLoading();
        WebElement e = assertVisibleElementExists(By.xpath("//div[@data-testid='menu.transactionHis']/span"), "Transaction History menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Transaction History page");
    }

    @Override
    public void clickFund() {
        waitLoading();
        closeBannerDialog();

        WebElement funds = assertClickableElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");

        String cls = funds.getAttribute("class");
        if(cls != null && cls.trim().toLowerCase().contains("is-active") && driver.findElements(By.xpath("//div[@id='depositPCS'] | //div[@id='withdraw'] | //div[@id='transactionHistory'] | //div[@id='transferFunds']")).isEmpty()) {
            clickProfile();
        }

        String expand = funds.getAttribute("aria-expanded");
        //expanded,return
        if(expand !=null && expand.toLowerCase().trim().equals("true")) {
            return;
        }

        triggerElementClickEvent_withoutMoveElement(funds);
        LogUtils.info("Go to Funds page");
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
    public void clickWithdrawFunds() {
        clickFund();
        closeBannerDialog();
        closeUSDTDialogPopup();
        waitLoading();
        WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.withdrawFunds']/span"), "Withdraw Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Withdraw Funds page");
    }

    // endregion

    // region [ CP Dialog ]

    public void closeBannerDialog()
    {
        WebElement popup = checkElementExists(By.xpath("//img[@data-testid='closeImg'] | (//img[@data-testid='closeImg'])[1]"));
        if (popup != null) {
            clickElement(popup);
            LogUtils.info("Close Banner Dialog");
        }
    }

    public void closeNotificationDialog()
    {
        WebElement popup = checkElementExists(By.cssSelector("div[data-testid='notificationDialog']:not([style*='display: none']) svg.ht-icon-close"));
        if (popup != null) {
            popup.click();
            LogUtils.info("Close Notification Dialog");
        }
    }

    public void closeUSDTDialogPopup() {
        try {
            WebElement img = driver.findElement(By.xpath("//div[@class='USDT_dialog']//button[@aria-label='Close']"));
            js.executeScript("arguments[0].click()", img);
        }
        catch(Exception e) {
            LogUtils.info("No dialog pop up");
        }
    }

    // endregion

    // endregion

    // region [ IB Portal ]

    // region [ IB Menu ]

    @Override
    protected WebElement getIBReportMenuEle() {
        return assertElementExists(By.xpath("//span[contains(@data-testid, 'ibreport')]"), "IB Report Menu");
    }

    @Override
    public void ibDashBoard() {
        waitLoading();
        closeSkipDialog();
        closeBannerDialog();
        closeIBNotificationDialog();

        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
        triggerElementClickEvent(e);
        LogUtils.info("Go to IB Dashboard page");
    }

    @Override
    public void ibRebateReport() {
        triggerElementClickEvent_withoutMoveElement(this.getIBReportMenuEle());
        this.waitLoading();
        triggerElementClickEvent_withoutMoveElement(this.getRebateReportTabEle());
        closeIBReportPopUp();
        LogUtils.info("Go to IB Report - Rebate Report menu page");
    }

    // endregion

    // region [ IB Dialog ]

    public void closeSkipDialog() {
        WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')] | //button[contains(@class,'driver-close-btn')]"));
        if (popup != null) {
            clickElement(popup);
            waitLoading();
            LogUtils.info("Close Skip Dialog");
        }
    }

    @Override
    public void closeIBNotificationDialog()
    {
        try
        {
            WebElement notShow = driver.findElement(By.xpath("//div[@class='el-dialog__wrapper' and not(contains(@style, 'display: none'))]//button[@class='el-dialog__headerbtn']"));

            js.executeScript("arguments[0].click()", notShow);

        }
        catch (Exception e)
        {
            LogUtils.info("no announcement notification window");
        }
    }

    // endregion

    // endregion

    // region [ Copy Trading ]

    @Override
    protected WebElement getCopyTradingEle() {
        return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.copy_trading']"), "Copy Trading menu");
    }

    @Override
    protected WebElement getCopyTradingDiscoverEle() {
        return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.discover']"), "Copy Trading - Discover menu");
    }

    @Override
    protected WebElement getCopyTradingCopierEle() {
        return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.Copier']"), "Copy Trading - Copier menu");
    }

    @Override
    protected WebElement getCopyTradingSignalProviderEle() {
        return assertClickableElementExists(By.xpath("(//li[@data-testid='copyTrading.signal_provider'])[1]"), "Copy Trading - Signal Provider menu");
    }

    @Override
    protected WebElement getTradeEle() {
        return assertClickableElementExists(By.xpath("//li[@data-testid='menu.webTrading']"), "Trade menu");
    }

    // endregion

}
