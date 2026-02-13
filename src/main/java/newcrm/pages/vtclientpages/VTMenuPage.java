package newcrm.pages.vtclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.clientpages.elements.MenuElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.LogUtils;

import java.util.List;
import java.util.Map;

public class VTMenuPage extends MenuPage {

    public VTMenuPage(WebDriver driver)
    {
        super(driver);
    }

    // region [ Client Portal ]

    // region [ General ]

    @Override
    public void switchToCP() {
        String title = driver.getTitle();
        closeCouponGuideDialog();
        closeNotificationDialog();
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

    // endregion

    // region [ CP Menu ]

    protected WebElement getLiveAccountConfigTabMenuEle() {
        return assertElementExists(By.cssSelector("div.switch_btn"), "Live Account Tab");
    }

    @Override
    protected WebElement getDemoAccountConfigTabMenuEle() {
        return assertElementExists(By.cssSelector("div.switch_btn"), "Demo Account Tab");
    }

    @Override
    protected WebElement getProfileSecurityMgmtMenuEle() {
        return assertClickableElementExists(By.xpath("//li[@data-testid='menu.securityManagement']"), "Security Management Tab");
    }

    @Override
    public void clickSecurityManagement() {
        WebElement webElement = getProfileSecurityMgmtMenuEle();
        triggerClickEvent_withoutMoveElement(webElement);
        LogUtils.info("Go to Security Management page");
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
        closeImgSec();
        WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.transferBetweenAccs']"), "Transfer Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Transfer Funds page");
    }

    @Override
    public void clickDepositFunds() {
        clickFund();
        closeImgSec();
        WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']"), "Deposit Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Deposit Funds page");
        closeImgSec();
    }

    @Override
    public void clickWithdrawFunds() {
        clickFund();
        closeImgSec();
        waitLoading();
        WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.withdrawFunds']"), "Withdraw Funds menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Withdraw Funds page");
    }

    @Override
    public void clickFund() {
        waitLoading();
        waitLoading();
        closeImgSec();
        refresh();
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
    }

    @Override
    public void clickLiveAccount() {
        clickAccount();

        // Switch to live account tab if current tab is not live account
        WebElement e = checkElementExists(By.cssSelector("div.card_view"), "Live Account Tab");
        if (e == null) {
            triggerElementClickEvent_withoutMoveElement(getLiveAccountConfigTabMenuEle());
            LogUtils.info("Click Live Account tab");
        }
    }

    @Override
    public void clickDemoAccount() {
        clickAccount();

        // Switch to demo account tab if current tab is not demo account
        WebElement e = checkElementExists(By.cssSelector("div#homeDemo"), "Demo Account Tab");
        if (e == null) {
            triggerElementClickEvent_withoutMoveElement(getDemoAccountConfigTabMenuEle());
            LogUtils.info("Click Demo Account tab");
        }
    }

    @Override
    public void clickTransactionHis() {
        waitLoading();
        clickFund();
        waitLoading();
        closeImgSec();
        WebElement e = assertVisibleElementExists(By.xpath("//li[@data-testid='menu.transactionHis']"), "Transaction History menu");
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Transaction History page");
    }

    @Override
    public void clickPromotion() {

        refresh();
        MenuElements els = PageFactory.initElements(driver, MenuElements.class);
        String expand = els.promotion.getAttribute("aria-expanded");
        //expanded,return
        if(expand !=null && expand.toLowerCase().trim().equals("true")) {
            return;
        }
        this.moveElementToVisible(els.promotion);
        els.promotion.click();
    }

    @Override
    public void clickProfile() {
        clickCPWebMenu(List.of(
                Map.entry(getProfileMenu(), "Profile menu"),
                Map.entry(getMyProfileMenu(), "My Profile menu")
        ));

        LogUtils.info("Go to Profile page");
    }

    // endregion

    // region [ CP Dialog ]

    @Override
    protected WebElement getCloseCouponDialogEle() {
        return checkElementExists(By.cssSelector("div#driver-popover-content footer button.driver-popover-next-btn"));
    }

    @Override
    public void closeCouponGuideDialog() {
        WebElement popup = getCloseCouponDialogEle();
        if (popup != null) {
            triggerClickEvent_withoutMoveElement(popup);
            LogUtils.info("Close Coupon Guide Dialog");
        }
    }

    public void closeNotificationDialog()
    {
        try
        {
            WebElement popup = driver.findElement(By.xpath("//div[@class='el-dialog__wrapper' and not(contains(@style, 'display: none'))]//div[@class='el-dialog__header']//img"));
            js.executeScript("arguments[0].click()", popup);
        }
        catch (Exception e)
        {
            LogUtils.info("No notification dialog");
        }
    }

    // endregion

    // endregion

    // region [ IB Portal ]

    // region [ General ]

    public void changeIBLanguage(String language){
        this.waitLoading();

        WebElement chooseLan = assertElementExists(By.xpath("//ul[@class='el-dropdown-menu el-popper lang_dropdown_box']/li[@class='el-dropdown-menu__item active']/span"), "Language List");

        String usedLanguage = chooseLan.getAttribute("innerText");
        if(usedLanguage.toLowerCase().contains(language.trim().toLowerCase())) {
            LogUtils.info("Already use the language: " + language + ". Do not need switch");
            return;
        }

        //open list
        WebElement ul = assertElementExists(By.xpath("//div[@class='el-dropdown']//ul[@class='el-dropdown-menu el-popper lang_dropdown_box']"), "Language List");
        String style = ul.getAttribute("style");
        if(style.contains("display")){
            WebElement langList = assertElementExists(By.xpath("//div[@class='el-dropdown'][img]"), "Language Icon");
            triggerElementClickEvent_withoutMoveElement(langList);
        }

        WebElement lang = assertVisibleElementExists(By.xpath("//ul[@class='el-dropdown-menu el-popper lang_dropdown_box']/li/span[contains(text(),'" + language.trim() + "')]"), language.trim() + " language");
        triggerElementClickEvent(lang);

        LogUtils.info("Change to language: " + language);

        closeImgSec();
    }

    // endregion

    // region [ IB Menu ]

    @Override
    protected WebElement getIBReportMenuEle() {
        return assertElementExists(By.xpath("//span[contains(@data-testid, 'ibReport')]"), "IB Report Menu");
    }

    @Override
    protected WebElement getRebateReportTabEle() {
        return assertElementExists(By.xpath("//div[@class='contact_title']/ul/li[2]"), "Rebate Report Tab");
    }

    @Override
    protected WebElement getIBAgreementAgreeButton() {
        return assertElementExists(By.xpath("//div[@class='el-dialog el-dialog--center agreementCustom' and not(contains(@style,'display: none'))] //div[@class='agree']/button"), "Agree IB Agreement Button");
    }

    @Override
    public void ibDashBoard() {
        waitLoading();
        closeSkipDialog();
        closeIBNotification();
        closeImgSec();
        changeIBLanguage("English");

        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
        triggerElementClickEvent(e);
        LogUtils.info("Go to IB Dashboard page");
        closeImgSec();
    }

    @Override
    public void ibTransactionHistory() {
        this.waitLoading();
        closeImgSec();
        WebElement e = assertElementExists(By.xpath("//*[@data-testid='click_/RebatePaymentHistory' or @data-testid='click_/rebatePaymentHistory']"), "IB Transaction History menu");
        triggerElementClickEvent(e);
        LogUtils.info("Go to IB Transaction History page");
    }

    @Override
    public void ibRebateReport() {
        triggerElementClickEvent(this.getIBReportMenuEle());
        this.waitLoading();
        triggerElementClickEvent(this.getRebateReportTabEle());
        LogUtils.info("Go to IB Report - Rebate Report menu page");
    }

    @Override
    public void ibAccountReport() {
        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/ibAccounts']"), "IB Account Report menu");
        triggerElementClickEvent(e);
        this.waitLoading();
        LogUtils.info("Go to IB Account Report menu page");
        closeIBReportPopUp();
    }

    @Override
    public void ibCampaignLinksMenu() {
        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/CampaignLinks']"), "IB Campaign Links menu");
        triggerElementClickEvent(e);
        this.waitLoading();
        LogUtils.info("Go to IB Campaign Links menu page");
        closeIBReportPopUp();
    }

    // endregion

    // region [ IB Dialog ]

    public void closeSkipDialog() {
        WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')]"));
        if (popup != null) {
            clickElement(popup);
            waitLoading();
            LogUtils.info("Close Skip Dialog");
        }
    }

    public void closeIBNotification()
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

    // region [ General Dialog ]

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
    }

    // endregion
    public void clickCopyTrading() {
        waitLoading();
        try{
            driver.findElement(By.xpath("//button[@class='driver-popover-next-btn']")).click();
        }
        catch(Exception e){
            LogUtils.info("No ad dialog display");
        }
        try{
            driver.findElement(By.xpath("//img[contains(@class,'closeImg') and not(ancestor::*[contains(@style,'display: none')])]")).click();
        }
        catch(Exception e){
            LogUtils.info("No ad dialog display");
        }
        WebElement e = this.getCopyTradingEle();
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Open Copy Trading menu");
    }

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

}
