package newcrm.pages.clientpages.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import newcrm.pages.clientpages.LiveAccountsPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SignalProviderPage extends Page {
    private int totalOfferCount;
    private int activeOfferCount;
    private int totalOfferCountInPage;
    private int activeOfferCountInPage;
    protected WebElement getPositionDivEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title-info']//input"), "position div");
    }
    protected WebElement getPageTitleEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title']"), "page title");
    }
    protected List<WebElement> getAccountListEle() {
        return assertVisibleElementsExists(By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[./span[contains(text(),'Copy Trading')]]"), "copying trading account list");
    }

    protected WebElement getStrategiesDivEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='strategies-wrapper']//input"), "strategies list div");
    }
    protected List<WebElement> getStrategiesListEle() {
        return assertVisibleElementsExists(By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list' and .//span[contains(normalize-space(.), 'Public')]]/li"), "strategies list");
    }
    protected WebElement getSignalProviderCenterTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-center']"), "signal provider center tab");
    }

    protected WebElement getStrategiesTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-strategies']"), "strategies tab");
    }

    protected WebElement getPositionsTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-position']"), "position tab");
    }

    protected WebElement getHistoryTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-history']"), "history tab");
    }

    protected List<WebElement> getDateEle() {
        return assertVisibleElementsExists(By.xpath("//div[@id='pane-position']//div[contains(@class,'el-date-editor')]/input"), "date input in history tab");
    }

    protected WebElement getMorebtnEle(){
        return  findVisibleElemntBy(By.xpath("(//div[contains(@class,'more-btn-wrapper')])[1]"));
    }

    protected WebElement getDelistEle(){
        return assertVisibleElementExists(By.xpath("(//ul[@class='el-dropdown-menu el-popper' and not (contains(@style,'display: none'))]/li[contains(normalize-space(.), 'Delist')])[1]"), "delist button");
    }

    protected WebElement getPublicEle(){
        return assertVisibleElementExists(By.xpath("//ul[@class='el-dropdown-menu el-popper' and not (contains(@style,'display: none'))]/li[contains(normalize-space(.), 'Public')]"), "public button");
    }

    protected WebElement getProfitSharingStatementEle(){
        return assertVisibleElementExists(By.xpath("(//ul[@class='el-dropdown-menu el-popper' and not (contains(@style,'display: none'))]/li[contains(normalize-space(.), 'Profit Sharing Statement')])[1]"), "profit sharing statement");
    }
    protected WebElement getStrategyHomeEle(){
        return assertVisibleElementExists(By.xpath("(//ul[@class='el-dropdown-menu el-popper' and not (contains(@style,'display: none'))]/li[contains(normalize-space(.), 'Strategy Homepage')])[1]"), "Strategy Homepage");
    }

    protected WebElement getBackBtnEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='back-box']"), "back button");
    }
    protected WebElement getSummaryTitleEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='top-left-text']"), "summary title in profit sharing statement page");
    }
    protected WebElement getStrategyUserInfoEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='strategy-user-info']"), "strategy user info");
    }
    protected WebElement getStrategyEquityTitleEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='strategy-equity-wrapper']"), "strategy equity title");
    }
    protected WebElement getStrategyDetailTabEle(){
        return assertVisibleElementExists(By.xpath("//div[contains(@class,'discover-detail-tabs')]//div[@role='tablist']"), "strategy details tab");
    }

    protected WebElement getSummaryContentEle(){

        return assertVisibleElementExists(By.xpath("//div[@class='content-box']"), "summary content in profit sharing statement page");
    }

    protected WebElement getProfitSharingTitleTextEle(){

        return assertVisibleElementExists(By.xpath("//div[@class='title-text']"), "summary breakdown in profit sharing statement page");
    }
    protected WebElement getStrategyHomepageEle(){
        return assertVisibleElementExists(By.xpath("(//ul[@class='el-dropdown-menu el-popper' and not (contains(@style,'display: none'))]/li[contains(normalize-space(.), 'Strategy Homepage')])[1]"), "Strategy Homepage");
    }
    protected WebElement getConfirmBtnEle(){
        return assertVisibleElementExists(By.xpath("//div[@title='Confirm Delist']//span[contains(normalize-space(.), 'Confirm')]"), "confirm Delist button");
    }

    protected WebElement getOkBtnEle(){
        return assertVisibleElementExists(By.xpath("//div[@title='Publish Successful']//span[contains(normalize-space(.), 'Ok')]"), "ok button");
    }

    protected WebElement getOkEle(){
        return assertVisibleElementExists(By.xpath("//span[contains(text(),'OK')][ not(ancestor::*[contains(@style,'display: none')])]"), "ok button");
    }



    protected WebElement getPublicMsgEle(){
        return assertVisibleElementExists(By.xpath("//div[@title='Publish Successful']//div[@class='ht-dialog__title-content']"), "publish strategy to public message");
    }

    protected WebElement getStrategyTextEle(){
        return assertVisibleElementExists(By.xpath("//div[@id='pane-strategies']//input[@placeholder='Select']"), "strategy mode text");
    }

    protected WebElement getCreateStrategyBtnEle(){
        return assertVisibleElementExists(By.xpath("//div[@id='pane-strategies']//span[contains(text(),'Create New Strategy')][not(ancestor::*[contains(@style,'display: none')])]"), "create strategy button");
    }
    /*protected List<WebElement> gettableBodyRows_strategyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-strategies']//div[@class='list-pc-wrap']"));
    }*/
    protected List<WebElement> gettableBodyRows_strategyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-strategies']//table[@class='el-table__body']//tr"));
    }
    protected WebElement gettableBodyRows_activatedOfferEle(){
        return driver.findElement(By.xpath("//div[@id='pane-strategies']//table[@class='el-table__body']//tr[1]//td[2]"));
    }
    protected List<WebElement> gettableBodyRows_positionEle(){
        return driver.findElements(By.xpath("//div[@id='pane-position']//table[@class='el-table__body']//tr"));
    }
    protected List<WebElement> gettableBodyRows_historyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-position']//table[@class='el-table__body']//tr"));
    }

    protected WebElement getNickNameEle() {
        return assertVisibleElementExists(By.xpath("//div[@label='Nickname']//input[not(ancestor::*[contains(@style,'display: none')])]"), "create new strategy nick name input");
    }

    protected WebElement getSourceAccountSpanEle(){
        return assertVisibleElementExists(By.xpath("//div[@prop='sourceAccount']//span[@class='el-input__suffix'][not(ancestor::*[contains(@style,'display: none')])]"), "source account input");
    }

    protected WebElement getPaymentAccountSpanEle(){
        return assertVisibleElementExists(By.xpath("//div[@prop='paymentAccount']//span[@class='el-input__suffix'][not(ancestor::*[contains(@style,'display: none')])]"), "source account input");
    }

    protected List<WebElement> getSourceAccountListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'form-account-select-dropdown')]//ul//li[ not(ancestor::*[contains(@style,'display: none')])]"), "source account list");
    }

    protected WebElement getPublishBtnEle(){
        return assertVisibleElementExists(By.xpath("//button/span[contains(text(),'Publish')][not(ancestor::*[contains(@style,'display: none')])]"), "Publish button");
    }

    protected WebElement getEditBtnEle(){
        return assertVisibleElementExists(By.xpath("//button/span[contains(text(),'Edit')][not(ancestor::*[contains(@style,'display: none')])]"), "Edit button");
    }

    protected WebElement getUpdateBtnEle(){
        return assertVisibleElementExists(By.xpath("//button/span[contains(text(),'Update')][not(ancestor::*[contains(@style,'display: none')])]"), "Edit button");
    }

    protected List<WebElement> getActiveOffer(){
        return driver.findElements(By.xpath("//span[contains(@class,'offer-status')][contains(text(),'Activated')]"));
    }
    protected List<WebElement> getTotalOffer(){
        return driver.findElements(By.xpath("//span[contains(@class,'offer-status')]"));
    }

    protected List<WebElement> isCheckedActivateOffer(){
        return driver.findElements(By.xpath("//div[@class='signal-form-offer-switch' and not(ancestor-or-self::*[contains(@style,'display: none')])]//div[contains(@class,'el-switch')]"));
    }

    protected WebElement getCopierReviewTabEle(){
        return findVisibleElemntByXpath("//div[@id='tab-review']");
    }
    protected WebElement getAgentLinkTabEle(){
        return findVisibleElemntByXpath("//div[@id='tab-agentLink']");
    }

    protected WebElement getCopierReviewWrapper(){
        return findVisibleElemntByXpath("//div[@class='copier-review-wrapper']");
    }
    protected WebElement getAgentLinkPane(){
        return findVisibleElemntByXpath("//div[@id='pane-agentLink']");
    }

    protected WebElement getReviewArrowEle(){
        return assertElementExists(By.xpath("//div[@id='pane-review']//i[contains(@class,'el-icon-arrow-up')]"), "Copier review tab arrow");
    }
    protected WebElement getAgentStatusEle(String status){
        return assertElementExists(By.xpath("//button/span[contains(text(),'"+status+"')]"), "Agent Link status");
    }

    protected WebElement getCopierReviewTableHeaderEle(){
        return assertElementExists(By.xpath("//div[@class='el-table__header-wrapper']"),"Copier review table header");
    }

    protected WebElement getAgentLinkHeaderEle(){
        return assertElementExists(By.xpath("//thead[@class='has-gutter']"),"Agent link header");
    }

    protected WebElement getAgentLinkDescEle(){
        return assertElementExists(By.xpath("//p[@class='m-desc']"),"Agent link desc");
    }
    protected WebElement getCopierReviewTableDetailEle(){
        return assertElementExists(By.xpath("//div[@class='table-offer-detail-info']"),"Copier review table offer detail info");
    }

    protected WebElement getAgentLinkApproveDetailEle(){
        return assertElementExists(By.xpath("//div[contains(@class,'table__body')]//table"),"Agent link approve detail info");
    }

    protected WebElement getCopierReviewEmptyDesEle(){
        return assertElementExists(By.xpath("//div[@class='el-empty__description']"),"Copier review table empty description");
    }

    protected WebElement moreBtn(String strategyAccount){
        return assertElementExists(By.xpath("//td[(preceding-sibling::td//span[contains(normalize-space(.),'"+strategyAccount+"')])]//div[contains(@class,'more-btn-wrapper')]"),"More button");
    }

    protected List<WebElement> getTitleInfo(){
        return driver.findElements(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']/div"));
    }
    protected List<WebElement> getPostionHistory(){
        return driver.findElements(By.xpath("//div[contains(@class,'history_table')]//table[contains(@class,'el-table__body')]//tr [not(ancestor::*[@style and contains(@style,'display: none')])]"));
    }
    HashMap<String, String> copierAccount = new HashMap<>();

    public SignalProviderPage(WebDriver driver){
        super(driver);
    }

    public String getStrategyMode(){
        waitLoadingInCopyTrading();
        return getStrategyTextEle().getAttribute("value");
    }

    public void chooseReviewMode(String mode){
        waitLoadingInCopyTrading();
        getReviewArrowEle().click();
        findVisibleElemntByXpath("//span[contains(text(),'"+mode+"')]").click();
        waitLoadingInCopyTrading();
    }
    public void chooseAgentLinkStatus(String status){
        waitLoadingInCopyTrading();
        getAgentStatusEle(status).click();
        waitLoadingInCopyTrading();
    }

    public boolean checkReviewMode(String mode) {
        LogUtils.info("Check Copier Review mode: " + mode);

        if (StringUtils.containsIgnoreCase(mode, "Pending")) {
            chooseReviewMode(mode);
            String emptyDes = getCopierReviewEmptyDesEle().getText();
            return StringUtils.containsIgnoreCase(emptyDes, "No Records Found");
        }

        if (mode.matches("(?i)Approved|Rejected")) {
            chooseReviewMode(mode);

            String cleanHeader = getCopierReviewTableHeaderEle().getText().replaceAll("\\s+", "");
            String cleanDetail = getCopierReviewTableDetailEle().getText().replaceAll("\\s+", "");

            boolean isHeaderCorrect = verifyAllKeywordsPresent(cleanHeader,
                    "Copier", "Strategy", "Investment", "Offers"); // 删除了重复的 "Strategy"

            boolean isDetailCorrect = verifyAllKeywordsPresent(cleanDetail,
                    "ProfitSharing", "Settlement", "ActiveCopiers", "MaxCopiers");

            LogUtils.info(String.format("Review Check Results for [%s] - Header: %b, Detail: %b",
                    mode, isHeaderCorrect, isDetailCorrect));

            return isHeaderCorrect && isDetailCorrect;
        }

        LogUtils.info("Unknown review mode: " + mode);
        return false;
    }

    public boolean checkAgentStatus(String status,String agentName) {
        LogUtils.info("Check agent status: " + status);

        if (StringUtils.containsIgnoreCase(status, "Pending")) {
            chooseAgentLinkStatus(status);
            String desc = getAgentLinkDescEle().getText().replaceAll("\\s+", "");
            String header = getAgentLinkHeaderEle().getText().replaceAll("\\s+", "");

            String expectedDesc = "Let the agent help you grow the copier base and only pay them when you make money".replaceAll("\\s+", "");
            boolean isDescCorrect = verifyAllKeywordsPresent(desc,expectedDesc);
            boolean isHeaderCorrect = verifyAllKeywordsPresent(header,
                    "AgentNickname", "AgentFeePercentage", "Scope", "PotentialClientBase");

            LogUtils.info(String.format("Review Check Results for [%s] - Desc: %b, Header: %b",
                    status, isDescCorrect, isHeaderCorrect));

            return isHeaderCorrect && isDescCorrect;
        }
        if (StringUtils.containsIgnoreCase(status, "Approved")) {
            chooseAgentLinkStatus(status);
            String desc = getAgentLinkDescEle().getText().replaceAll("\\s+", "");
            String header = getAgentLinkHeaderEle().getText().replaceAll("\\s+", "");
            String detail = getAgentLinkApproveDetailEle().getText().replaceAll("\\s+", "");

            String expectedDesc = "Let the agent help you grow the copier base and only pay them when you make money".replaceAll("\\s+", "");
            boolean isDescCorrect = verifyAllKeywordsPresent(desc,expectedDesc);
            boolean isHeaderCorrect = verifyAllKeywordsPresent(header,
                    "AgentNickname", "AgentFeePercentage", "Scope", "ActiveCopiersIntroduced", "AgentFeePaid","More");
            boolean isDetailCorrect = verifyAllKeywordsPresent(
                    detail,agentName, "Allexistingandfuturestrategies","View");

            LogUtils.info(String.format("Review Check Results for [%s] - Desc: %b, Header: %b, Detail: %b",
                    status, isDescCorrect, isHeaderCorrect, isDetailCorrect));


            return isHeaderCorrect && isDescCorrect && isDetailCorrect;
        }

        LogUtils.info("Unknown review status: " + status);
        return false;
    }
    /**
     * 关键字校验工具方法
     */
    private boolean verifyAllKeywordsPresent(String targetText, String... keywords) {
        for (String key : keywords) {
            if (!StringUtils.containsIgnoreCase(targetText, key)) {
                LogUtils.info("Missing expected keyword: " + key);
                return false;
            }
        }
        return true;
    }
    public boolean getCopierReview(){
        waitLoadingInCopyTrading();
        getCopierReviewTabEle().click();
        waitLoadingInCopyTrading();
        LogUtils.info("Check Copier review tab");
        return getCopierReviewWrapper().isDisplayed();
    }

    public boolean getAgentLink(){
        waitLoadingInCopyTrading();
        getAgentLinkTabEle().click();
        waitLoadingInCopyTrading();
        LogUtils.info("Check Agent Link tab");
        return getAgentLinkPane().isDisplayed();
    }

    public void clickMoreBtn(String strategyAccount){
        waitLoadingInCopyTrading();
        moreBtn(strategyAccount).click();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }
    public void clickMoreBtn(){
        waitLoadingInCopyTrading();
        getMorebtnEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }

    public void clickCreateStgyBtn(){
        waitLoadingInCopyTrading();
        getCreateStrategyBtnEle().click();
        waitLoadingInCopyTrading();

        GlobalMethods.printDebugInfo("Click create new strategy button");
    }

    public void clickEditStgyBtn(){
        waitLoadingInCopyTrading();
        getEditBtnEle().click();
        waitLoadingInCopyTrading();

        GlobalMethods.printDebugInfo("Click edit new strategy button");
    }

    public void deListStrategy(){
        getDelistEle().click();
        getConfirmBtnEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click delist button at strategy list");
    }

    public void publicStrategy(){
        waitLoadingInCopyTrading();
        getPublicEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click public button at strategy list");
    }

    public String getPublishMsg(){
        String text = getPublicMsgEle().getText();
        GlobalMethods.printDebugInfo("Publish message:" + text);
        return text;
    }

    public void clickProfitSharingStatement(){
        waitLoadingInCopyTrading();
        getProfitSharingStatementEle().click();
        waitLoadingInCopyTrading();
        waitLoading();
        GlobalMethods.printDebugInfo("Start to check profit sharing statement button at strategy list");
    }

    public void clickStrategyHome(){
        waitLoadingInCopyTrading();
        getStrategyHomeEle().click();
        waitLoadingInCopyTrading();
        waitLoading();
        GlobalMethods.printDebugInfo("Start to check strategy home button at strategy list in signal provider page");
    }


    public void backToSignalProviderPage(){
        getBackBtnEle().click();
        waitLoading();
    }
    public boolean checkProfitSharingStatementSummary(String strategyAcc)
    {
        waitLoading();
        String title = driver.getCurrentUrl();
        String summaryTitle = getSummaryTitleEle().getText();
        String summaryContent = getSummaryContentEle().getText();
        String breakdownText = getProfitSharingTitleTextEle().getText();

        LogUtils.info("Profit sharing statement title: " + title + "Summary title: " + summaryTitle+ "Summary content: " + summaryContent+ "Breakdown text: " + breakdownText);

        boolean basicCheck = StringUtils.containsIgnoreCase(title, "profitsharingstatement")
                && StringUtils.containsIgnoreCase(summaryTitle, "summary")
                && StringUtils.containsIgnoreCase(breakdownText, "Breakdown");

        if (!basicCheck) return false;

        List<String> expectedKeywords = Arrays.asList(
                "Strategy ID",
                "Current Period's Payout(USD)",
                "Unpaid Amount(USD)",
                "Payment Account",
                strategyAcc
        );

        return expectedKeywords.stream()
                .allMatch(keyword -> StringUtils.containsIgnoreCase(summaryContent, keyword));

    }

    public boolean checkStrategyHomePage(String strategyAcc) {
        waitLoading();

        String currentUrl = driver.getCurrentUrl();
        String userInfoText = getStrategyUserInfoEle().getText();

        if (!StringUtils.containsIgnoreCase(currentUrl, "discoverDetail")) {
            LogUtils.info("Strategy Home Page Check Failed: URL does not contain 'discoverDetail'. Current URL: " + currentUrl);
            return false;
        }

        if (!checkKeywords("User Info Section", userInfoText,
                strategyAcc)) {
            return false;
        }
      /*  if (!checkKeywords("User Info Section", userInfoText,
                strategyAcc, "Return", "Copiers", "Risk", "Profit Sharing")) {
            return false;
        }

        if (!checkKeywords("Equity Title Section", equityTitleText,
                "Strategy Equity", "Equity", "Balance", "Credit")) {
            return false;
        }

        if (!checkKeywords("Detail Tab Section", detailTabText,
                "Overview", "Portfolio", "Copiers", "Watchers")) {
            return false;
        }*/

        LogUtils.info("Strategy Home Page Check: All checks passed.");
        return true;
    }

    protected boolean checkKeywords(String sectionName, String actualText, String... expectedKeywords) {
        for (String keyword : expectedKeywords) {
            if (!StringUtils.containsIgnoreCase(actualText, keyword)) {
                LogUtils.info(String.format("[%s] Check Failed! \nMissing Keyword: [%s] \nActual Text: [%s]",
                        sectionName, keyword, actualText));
                return false;
            }
        }
        return true;
    }
    public void clickOkInPublishMsg()
    {
        getOkBtnEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click ok button in publish message");
    }
    public void selectStrategy(String strategyName)
    {
        getStrategiesDivEle().click();
        List<WebElement> strategiesList = getStrategiesListEle();

        for(WebElement e:strategiesList)
        {
            if(e.getText().contains(strategyName))
            {
                e.click();
                waitLoadingInCopyTrading();
                return;
            }
        }
    }

    public void clickPositionsTab()
    {
        getPositionsTabEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click positions tab");
    }
    public void clickStrategiesTab()
    {
        getStrategiesTabEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click strategies tab");
    }

    public void clickSignalProviderCenterTab()
    {
        getSignalProviderCenterTabEle().click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click signal provider center tab");
    }
    public boolean checkCopierAccount(String accNum)
    {
        //sometimes need to refresh to make it sync the data
        refresh();
        String pageTitle = getPageTitleEle().getText();
        System.out.println("SignalProvider page Title:" + pageTitle);

        WebElement positionEle = getPositionDivEle();
        js.executeScript("arguments[0].click();",positionEle);
        waitLoading();
        List<WebElement> copierAccountList = getAccountListEle();
        waitLoadingInCopyTrading();

        for(WebElement e:copierAccountList)
        {
            String accInfo = e.getText();
            System.out.println("accInfo:" + accInfo);
            if(accInfo.contains(accNum)) {
                String[] accInfoArr = accInfo.split("-");

                String accNumInList = accInfoArr[0];
                String accCurrency = accInfoArr[1];
                String accTypeInfo = accInfoArr[2].replaceAll("\\s+", "");

                System.out.println(accNumInList + " " + accCurrency + " " + accTypeInfo + " " +"accNum:" +  accNum.equalsIgnoreCase(accNumInList) + " "+
                       "accCurrency:" +  accCurrency.equalsIgnoreCase(GlobalProperties.CURRENCY.USD.toString())+ " " + "accTypeInfo:"+
                        StringUtils.containsIgnoreCase(accTypeInfo, "CopyTrading"));

                if (accNum.equalsIgnoreCase(accNumInList) && accCurrency.equalsIgnoreCase(GlobalProperties.CURRENCY.USD.toString())
                        && StringUtils.containsIgnoreCase(accTypeInfo, "CopyTrading")) {
                    copierAccount.put("accNum", accNumInList);
                    copierAccount.put("currency", accCurrency);
                    copierAccount.put("type", accTypeInfo);

                    e.click();
                    waitLoadingInCopyTrading();
                    return true;
                }
            }
        }
        return false;
    }

    public void getCopierAccTitleInfo() {
        waitLoading();
        waitLoadingInCopyTrading();
        //List<WebElement> titleInfoList = driver.findElements(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']/div"));

        List<WebElement> titleInfoList = getTitleInfo();
        for (WebElement element : titleInfoList) {
            String titleInfo = element.getText();

            System.out.println("copying trading account title Info:" + titleInfo);
            if (titleInfo == null || !titleInfo.contains("\n")) continue;

            String[] lines = titleInfo.split("\\n");
            if (lines.length < 2) continue;

            String value = lines[0].trim();
            String label = lines[1].toLowerCase().trim();

            if (label.contains("equity")) {
                copierAccount.put("equity",value);
            }else if (label.contains("balance")) {
                copierAccount.put("balance",value);
            } else if (label.contains("credit")) {
                copierAccount.put("credit",value);
            }
        }
        System.out.println("copierAccount balance in signal provide page:" + copierAccount.get("balance"));
    }

    //compare copier account info in signal page with account info in liveaccount page
   public boolean compareCopierAccountInfo(LiveAccountsPage.Account liveAccount)
   {
       waitLoading();
       waitLoadingInCopyTrading();
       System.out.println("copierAccount.get(\"accNum\")" + copierAccount.get("accNum")+ " liveAccount.getAccountNum():" + liveAccount.getAccountNum());
        boolean accNumCompare = liveAccount.getAccountNum().equalsIgnoreCase(copierAccount.get("accNum"));
        //boolean equityCompare = liveAccount.getEquity().equalsIgnoreCase(copierAccount.get("equity"));
        System.out.println("liveAccount.getBalance()" + liveAccount.getBalance()+ "copierAccount.get(\"balance\")" + copierAccount.get("balance"));
        boolean balanceCompare = StringUtils.containsIgnoreCase(liveAccount.getBalance(),copierAccount.get("balance"));

        GlobalMethods.printDebugInfo("accNumCompare: " + accNumCompare  + " balanceCompare:" +balanceCompare);
        return accNumCompare && balanceCompare;
   }

   public int getStrategyCount() {
        waitLoadingInCopyTrading();
       List<WebElement> rows = gettableBodyRows_strategyEle();
        GlobalMethods.printDebugInfo("strategy rows size:" + rows.size());
        return rows.size();
    }
    public String getStrategyName() {
        List<WebElement> rows = gettableBodyRows_strategyEle();
        if (!rows.isEmpty()) {
            return rows.get(0).getText().trim();
        } else {
            GlobalMethods.printDebugInfo("No strategy in signal provider page");
            return null;
        }
    }

    public List<String> getStrategyNameList() {
        List<WebElement> rows = gettableBodyRows_strategyEle();
        List<String> texts = new ArrayList<>();

        if (rows == null || rows.isEmpty()) {
            GlobalMethods.printDebugInfo("No strategy in signal provider page");
            return texts;
        }

        for (WebElement row : rows) {
            if (row == null) continue;
            String t = row.getText();
            if (t != null) {
                t = t.trim();
                if (!t.isEmpty()) {
                    texts.add(t);
                }
            }
        }

        return texts;
    }


    public void  getActivatedOfferFromSPPage(){
        String offer = gettableBodyRows_activatedOfferEle().getText();

        String[] offerCount = offer.split("\\s*/\\s*");
        activeOfferCountInPage = Integer.parseInt(offerCount[0]);
        totalOfferCountInPage = Integer.parseInt(offerCount[1]);

        LogUtils.info("activeOfferCountInPage:" + activeOfferCountInPage + " totalOfferCountInPage:" + totalOfferCountInPage);

    }

    public boolean checkOfferCount() {
        getActivatedOfferFromSPPage();

        return activeOfferCountInPage == activeOfferCount && totalOfferCountInPage == totalOfferCount;
    }
    public void clickHistoryTab(){
        getHistoryTabEle().click();
        waitLoading();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click history tab");
    }
    public int getHistoryCount() {
        waitLoadingInCopyTrading();
        List<WebElement> historyRows = gettableBodyRows_historyEle();
        GlobalMethods.printDebugInfo("historyRows size:" + historyRows.size());
        return historyRows.size();
    }

    public int getPositionCount() {
        waitLoadingInCopyTrading();
        List<WebElement> positionRows = gettableBodyRows_positionEle();
        GlobalMethods.printDebugInfo("positionRows size:" + positionRows.size());
        return positionRows.size();
    }

    public boolean checkPositionHistoryDate(){
        waitvisible.until(d -> {
            List<WebElement> ins = getDateEle();
            return ins.size() >= 2 && !ins.get(0).getAttribute("value").isBlank();
        });

        List<WebElement> inputs = getDateEle();
        String startDate = inputs.get(0).getAttribute("value").trim();
        String endDate   = inputs.get(1).getAttribute("value").trim();

        System.out.println(startDate + " -> " + endDate);

        String start = "2025/10/30";
        String end   = "2025/11/06";

        setDateByJS( inputs.get(0), start);
        setDateByJS( inputs.get(1), end);

        inputs.get(1).sendKeys(Keys.ENTER);

        waitLoadingInCopyTrading();

        //ist<WebElement> positonHis = driver.findElements(By.xpath("//div[contains(@class,'history_table')]//table[contains(@class,'el-table__body')]//tr [not(ancestor::*[@style and contains(@style,'display: none')])]"));

        List<WebElement> positonHis = getPostionHistory();
        int positionCount = positonHis.size();

        GlobalMethods.printDebugInfo("Search position history between 2025/10/30 and 2025/11/06, positionCount: " + positionCount);
        return isWithinInclusive(
                LocalDate.now(),
                parseUiDate(startDate),
                parseUiDate(endDate)
        );
    }

    public void createNewStrategy(String nickName) {
        getNickNameEle().sendKeys(nickName);
        LogUtils.info("Create new strategy: " + nickName);

        getSourceAccountSpanEle().click();
        String sourceAccount = selectRandomValueFromDropDownList(getSourceAccountListEle());
        LogUtils.info("Select source Account" + sourceAccount);
        waitLoadingInCopyTrading();

        getPaymentAccountSpanEle().click();
        String paymentAccount = selectRandomValueFromDropDownList(getSourceAccountListEle());
        LogUtils.info("Select payment Account" + paymentAccount);
        waitLoadingInCopyTrading();

        if(isCheckedActivateOffer().size()!=0)
        {
            GlobalMethods.printDebugInfo("Activate offer available");
            boolean isChecked = isCheckedActivateOffer().get(0).getAttribute("class").contains("is-checked");
            if(!isChecked)
            {
                isCheckedActivateOffer().get(0).click();
                LogUtils.info("Click activate offer");
            }

        }

        this.activeOfferCount  =getActiveOffer().size();

        LogUtils.info("Active offer count: " + activeOfferCount);

        this.totalOfferCount = getTotalOffer().size();
        LogUtils.info("Total offer count: " + totalOfferCount);

        getPublishBtnEle().click();
        LogUtils.info("Click publish button");

        //click ok
        getOkEle().click();
        waitLoadingInCopyTrading();
        waitLoading();

    }

    public int getSavedActiveOfferCount() {
        return this.activeOfferCount;
    }
    public int getTotalActiveOfferCount() {
        return this.totalOfferCount;
    }

    public void editNewStrategy(String editNickName) {
        getNickNameEle().clear();
        getNickNameEle().sendKeys(editNickName);
        LogUtils.info("Edit new strategy: " + editNickName);


        getUpdateBtnEle().click();
        LogUtils.info("Click update button");

        getOkEle().click();
        LogUtils.info("Click ok button");

        waitLoadingInCopyTrading();
        waitLoading();

    }
    protected void setDateByJS(WebElement el, String value) {
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                el, value
        );
    }
    /** Parse a UI date string (format: yyyy/MM/dd) into a LocalDate. */
    public LocalDate parseUiDate(String s) {
        DateTimeFormatter UI_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        try {
            return LocalDate.parse(s.trim(), UI_FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid UI date: " + s, e);
        }
    }

    /** Inclusive range check: is target within [start, end]? */
    public boolean isWithinInclusive(LocalDate target, LocalDate start, LocalDate end) {
        GlobalMethods.printDebugInfo("today's date is:" + target);
        return !(target.isBefore(start) || target.isAfter(end));
    }

}
