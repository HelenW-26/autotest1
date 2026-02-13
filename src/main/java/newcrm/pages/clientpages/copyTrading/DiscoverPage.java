package newcrm.pages.clientpages.copyTrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.*;

public class DiscoverPage extends Page {
    public DiscoverPage(WebDriver driver){
        super(driver);
    }

    private static String copyAmount = "0";
    protected HashMap<String,String> copyStrategyDetail = new HashMap<>();
    protected static String strategyAccName = null;
    protected By warningMsg = By.xpath("//div[@class='el-message ht-message ht-message--warning']");

    public String getCopyAmount(){
        return copyAmount;
    }
   // ===== Search =====
   protected WebElement getSearchEle() {
       return assertVisibleElementExists(By.xpath("//span[@class='search-input-wraper']"), "Search wrapper");
   }
    protected WebElement getSearchStrategyInputEle() {
        return assertVisibleElementExists(By.xpath("//input[contains(@placeholder,'Search strategy')]"), "Search strategy input");
    }

    protected WebElement getSearchBtnEle() {
        return assertVisibleElementExists(By.xpath("//button[@data-testid='button']/span[contains(text(),'Search')]"), "Search button");
    }

    protected WebElement getCopyBtnEle() {
        return assertVisibleElementExists(By.xpath("//button/span[contains(text(),'Copy')]"), "Copy button");
    }

    protected WebElement getSubmitBtnEle() {
        return assertVisibleElementExists(By.xpath("//div[@aria-label='Copy']//button/span[contains(text(),'Submit')]"), "Submit button");
    }

    protected WebElement getSuccessMsgEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='success_box']"), "Success message");
    }

    protected WebElement getOkBtnEle() {
        return assertVisibleElementExists(By.xpath("//button/span[contains(text(),'Ok')]"), "OK button");
    }

    protected WebElement getCopierAccDivEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='account-select']"), "Copier account select");
    }

    protected WebElement getStrategyUserInfoInDetailsEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='strategy-user-info']"), "Strategy user info (details)");
    }

    protected WebElement getSignalProviderInfoInSignalPrvPageEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='content-box']//div[@class='name_box']//span"), "Signal provider name");
    }

    protected List<WebElement> getRecommendationContentEle() {
        return assertVisibleElementsExists(By.xpath("//div[@class='recommendation component_content']//div[@class='title']"), "Recommendation title");
    }

    protected WebElement getRankListTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='ht-switcher__item']"), "Rank list tab item");
    }

    protected WebElement getRecommendationTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='ht-switcher__item checked']"), "Recommendation tab (checked)");
    }

    protected WebElement getFirstCardInHighestReturnTabEle() {
        return assertVisibleElementExists(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[1]"),
                "First card in Highest Return");
    }

    protected WebElement getFirstCardImageEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='el-card__body']//img)[1]"), "First card image");
    }

    protected WebElement getFirstCopyBtnInHighestReturnEle() {
        return assertVisibleElementExists(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[1]//div[@class='top_right_operation']/button"),
                "First Copy button in Highest Return");
    }

    protected List<WebElement> firstCardInHighestReturnTabList(){
        return driver.findElements(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[1]"));
    }
    protected List<WebElement> secondCardInHighestReturnTabList() {
        return driver.findElements(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[2]"));
    }
    protected WebElement getSecondCardInHighestReturnTabEle() {
        return assertVisibleElementExists(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[2]"),
                "Second card in Highest Return");
    }

    protected WebElement getSecondCardImageEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='el-card__body']//img)[3]"), "Second card image");
    }

    protected WebElement getSecondCopyBtnInHighestReturnTabEle() {
        return assertVisibleElementExists(By.xpath("((//div[@class='discover_index']//div[@class='content'])[1]//div[@class='el-card__body'])[2]//div[@class='top_right_operation']/button"),
                "Second Copy button in Highest Return");
    }


    protected WebElement getOpenTradeSwitchEle(){
        return assertVisibleElementExists(By.xpath("//*[normalize-space(.)='Copy Opened Trades']/following::div[contains(@class,'el-switch ht-switch')]"),"Copy Opened Trades Switch");
    }

    // ===== Copy dialog =====
    protected List<WebElement> getCopyDialogTitleEle() {
        return driver.findElements(By.xpath("//span[@title='Copy'][not(ancestor::*[contains(@style,'display: none')])]"));
    }

    protected WebElement getStrategyNameInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='info_warp info_small']/div[@class='name_box']//span"),
                "Strategy name in Copy dialog");
    }

    protected WebElement getCloseBtnInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//button[@aria-label='close Copy']"),
                "Close Copy dialog button");
    }

    protected WebElement getBackToDiscoverEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='sub-page-back-text']"),
                "Back to Discover");
    }

    protected WebElement getReturnInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@aria-label='Copy']//span[contains(normalize-space(.), 'Return')]/ancestor::div[1]/following-sibling::div[1]/span"),
                "Return value in Copy dialog");
    }
    protected WebElement getCopyModeArrowEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='el-form-item__content']//i[@class='el-select__caret el-input__icon el-icon-arrow-up']"),
                "Arrow beside copy mode");
    }

    protected WebElement getSettlementInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@aria-label='Copy']//span[contains(normalize-space(.), 'Settlement')]/ancestor::div[1]/following-sibling::div[1]/span"),
                "Settlement value in Copy dialog");
    }

    protected WebElement getProfitSharingInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@aria-label='Copy']//span[contains(normalize-space(.), 'Profit Sharing')]/ancestor::div[1]/following-sibling::div[1]/span"),
                "Profit Sharing value in Copy dialog");
    }

    protected WebElement getCodeModeInCopyDialogInputEle() {
        return assertVisibleElementExists(By.xpath("(//div[contains(normalize-space(.),'Copy Mode')]/ancestor::div[contains(@class,'el-form-item')])[1]"
                        + "//input[contains(@class,'el-input__inner') and not(@type='hidden')][1]"),
                "Copy Mode input in Copy dialog");
    }

    protected WebElement getLotsInCopyDialogInputEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(text(),'Lots per Order')]/ancestor::div[contains(@class,'el-form-item')]//input"),
                "Copy Mode input in Copy dialog");
    }
    protected WebElement getMultiplesInCopyDialogInputEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(text(),'Multiples')]/ancestor::div[contains(@class,'el-form-item')]//input"),
                "Multiples per order input in Copy dialog");
    }

    protected WebElement getInvestmentInCopyDialogInputEle() {
        return assertVisibleElementExists(By.xpath("//div[normalize-space(text())='Investment']/following::input[1]"),
                "Investment input in Copy dialog");
    }

    protected WebElement getUsedCreditInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title_warp' and .//span[contains(normalize-space(.), 'Used Credit')]]/following-sibling::div[@class='value_warp']"),
                "Used Credit value");
    }

    protected WebElement getUsedBalanceInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title_warp' and .//span[contains(normalize-space(.), 'Investment Balance')]]/following-sibling::div[@class='value_warp']"),
                "Used Balance value");
    }

    protected WebElement getStopLossSliderInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@role='slider']"),
                "Stop Loss slider");
    }

    protected WebElement getAvailableInvestmentInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='form-item-investment-desc'][contains(normalize-space(text()),'Available Investment')]"),
                "Available Investment value");
    }

    protected WebElement getAvailableBalanceInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='form-item-subtitle-desc'][contains(normalize-space(.), 'Available Balance')]"),
                "Available Balance text");
    }

    // ===== Rank List =====
    protected WebElement getTabListInRankListEle() {
        return assertVisibleElementExists(By.xpath("//div[@role='tablist']"),
                "Rank list tablist");
    }

    protected List<WebElement> getTop3InRankListEle() {
        return assertVisibleElementsExists(By.xpath("//div[@class='rank_warp']/div"),
                "Top3 in Rank list");
    }

    protected List<WebElement> getTopRatingInRankListEle() {
        return assertVisibleElementsExists(By.xpath("//div[@class='list_warp']/div"),
                "Top rating list");
    }

    protected WebElement getViewMoreBtnEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='more_warp']/span"),
                "View more button");
    }

    protected WebElement getFourthViewBtnInRankListEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='list_warp']//div[@class='list_item'])[1]//div[@class='rank_icon']"),
                "Fourth view/rank icon (first)");
    }

    protected WebElement getFourthStrategyNameInRankListEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='list_warp']//div[@class='list_item'])[1]//span[@class='name']"),
                "Fourth strategy name (first)");
    }

    // ===== Search Tab =====
    protected WebElement getTopStrategyAndSiglePrvdEle() {
        return assertVisibleElementExists(By.xpath("//section[@class='el-drawer__body']//div[@class='main']//div[@role='tablist']"),
                "Search drawer top tabs");
    }

    protected WebElement getSearchResultEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='el-tabs__content' and ../preceding-sibling::div[@class='search_warp']]//div[@class='slot_info'])[1]"),
                "Search result (general)");
    }

    protected WebElement getSearchResultInTopSignalPrvEle() {
        return assertVisibleElementExists(By.xpath("(//div[@id='pane-1']//div[@class='item_name'])[1]"),
                "Search result in Top Signal Provider");
    }

    protected WebElement getStrategiesTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id = 'tab-0'][contains(text(),'Strategies')]"),
                "Strategies tab");
    }

    protected WebElement getSignalProviderTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id = 'tab-1'][contains(text(),'Signal Provider')]"),
                "Signal Provider tab");
    }

    protected WebElement copierAccount(String copierAcc)
    {
        return findVisibleElemntBy(By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[./span[contains(text(),'"+copierAcc +"')]]"));
    }

    protected WebElement strategyAcc(String strategyAcc)
    {
        return findVisibleElemntBy(By.xpath("//div[@id='pane-0']//div[./span[contains(text(),'"+ strategyAcc +"')]]"));
    }

    protected WebElement copyMode(String mode)
    {
        return findVisibleElemntBy(By.xpath("//span[contains(text(),'"+ mode +"')]"));
    }

    protected WebElement getOverviewTab(){
        return assertElementExists(By.xpath("//div[contains(text(),'Overview')][not(ancestor-or-self::*[contains(@style, 'display: none')])]"), "Overview tab");
    }

    protected WebElement getOrderTab(){
        return assertElementExists(By.xpath("//div[contains(text(),'Order')][not(ancestor-or-self::*[contains(@style, 'display: none')])]"), "Overview tab");
    }

    protected WebElement getCopiersTab(){
        return assertElementExists(By.xpath("//div[contains(text(),'Copiers')][not(ancestor-or-self::*[contains(@style, 'display: none')])]"), "Overview tab");
    }

    protected WebElement getWatchersTab(){
        return assertElementExists(By.xpath("//div[contains(text(),'Watchers')][not(ancestor-or-self::*[contains(@style, 'display: none')])]"), "Overview tab");
    }

    protected WebElement getDiscoverDetailTab(){
        return assertElementExists(By.xpath("//div[@class='discover_detail']//div[@role='tablist']"), "Discover Detail tab");
    }
    public HashMap<String,String> getStrategyDetail()
   {
       return copyStrategyDetail;
   }

   public boolean warnMsgNotExist()
   {
       waitLoadingInCopyTrading();
       return checkElementExists(warningMsg) == null;
   }

    public boolean isFirstCardInHighestReturnTabExist()
    {
        return !firstCardInHighestReturnTabList().isEmpty();
    }

   public boolean isSecondCardInHighestReturnTabExist()
   {
       return !secondCardInHighestReturnTabList().isEmpty();
   }

    public Optional<String> getWarningMessageText() {
        WebElement warnElement = checkElementExists(warningMsg);

        if (warnElement != null) {
            String msg = warnElement.getText();
            GlobalMethods.printDebugInfo("Warning message exists: " + msg);
            return Optional.of(msg);
        } else {
            GlobalMethods.printDebugInfo("Warning message does not exist");
            return Optional.empty();
        }
    }

   public void clickSignalProviderTab()
    {
        waitLoading();
        getSignalProviderTabEle().click();
        waitLoading();
    }

    public void clickOverviewTab()
    {
        waitLoading();
        getOverviewTab().click();
        LogUtils.info("Click Overview tab");
        waitLoading();
    }
    public void clickOrderTab()
    {
        waitLoading();
        getOrderTab().click();
        LogUtils.info("Click Order tab");
        waitLoading();
    }
    public void clickCopiersTab()
    {
        waitLoading();
        getCopiersTab().click();
        LogUtils.info("Click Copiers tab");
        waitLoading();
    }
    public void clickWatchersTab()
    {
        waitLoading();
        getWatchersTab().click();
        LogUtils.info("Click Watchers tab");
        waitLoading();
    }

    public String getFourthStrategyNameInRankList()
    {
        return getFourthStrategyNameInRankListEle().getText();
    }
    public void clickFourthViewBtnInRankList()
    {
        getFourthViewBtnInRankListEle().click();
        waitLoading();
        GlobalMethods.printDebugInfo("click fourth view btn in rank list");
    }

    public boolean checkDiscoverDetail(){
        waitLoadingInCopyTrading();
        String discoverDetailTab = getDiscoverDetailTab().getText();

        boolean basicCheck = StringUtils.containsIgnoreCase(discoverDetailTab, "OverviewOrderCopiersWatchers");
        LogUtils.info("Check discover detail tab text: " + discoverDetailTab + " check result: " + basicCheck);

        return basicCheck;
    }

    public boolean checkOverviewTab()
    {
        /*List<String> expectedKeywords = Arrays.asList(
                "Strategy ID",
                "Current Period's Payout(USD)",
                "Unpaid Amount(USD)",
                "Payment Account",
                strategyAcc
        );

        return expectedKeywords.stream()
                .allMatch(keyword -> StringUtils.containsIgnoreCase(summaryContent, keyword));*/
        return true;

    }
    public boolean clickFirstCopyBtnChekcMsg()
    {
        waitLoadingInCopyTrading();
        getFirstCopyBtnInHighestReturnEle().click();
         GlobalMethods.printDebugInfo("Click first copy btn");

       return checkWarnMsg();

    }

    private boolean checkWarnMsg(){
        String warnmessage;
        List<WebElement> warnMsg = Collections.emptyList();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
            warnMsg = wait.until(d -> {
                List<WebElement> list = d.findElements(
                        By.xpath("//div[contains(@class,'el-message ht-message ht-message--warning')]")
                );
                return list.isEmpty() ? null : list;
            });
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("No warning message appears within 30 seconds.");
        }

        if (!warnMsg.isEmpty()) {
            warnmessage = warnMsg.get(0).getText();
            GlobalMethods.printDebugInfo("Warning message: " + warnmessage);
            return false;
        }

        waitLoadingInCopyTrading();
        return true;
    }
    public boolean clickSecCopyBtn()
    {
        waitForCopyTrading(5000);
        getSecondCopyBtnInHighestReturnTabEle().click();
        GlobalMethods.printDebugInfo("Click second copy btn");
        return checkWarnMsg();
    }
    public void selectCopierAccount(String copier)
    {
        waitLoading();
        WebElement copierAccDropdown = getCopierAccDivEle();
        copierAccDropdown.click();
        copierAccount(copier).click();
        waitLoading();
        GlobalMethods.printDebugInfo("Select copier account: " + copier);

    }
    public void clickSearch()
    {
        moveElementToVisible(getSearchEle());
        WebElement searchBar = getSearchEle();
        searchBar.click();
        GlobalMethods.printDebugInfo("Click search");
        waitLoadingInCopyTrading();
    }
    public void searchProviderInDiscoverPage(String strategyAcc)
    {
        clickSearch();
        searchProviderInSearchPage(strategyAcc);
    }

    public void searchProviderInSearchPage(String strategyAcc)
    {
        waitLoading();
        getSearchStrategyInputEle().sendKeys(strategyAcc);
        waitLoading();
        js.executeScript("arguments[0].click()",getSearchBtnEle());
        waitLoading();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Search signal provider: " + strategyAcc);
    }

    public void clickSearchResultAndCopy(String copier){
        strategyAccName = strategyAcc(copier).getText();
        strategyAcc(copier).click();
        waitLoading();
        waitLoadingInCopyTrading();
        try {
            waitvisible.until(driver -> {
                try {
                    getCopyBtnEle().isDisplayed();
                    GlobalMethods.printDebugInfo("Find copy button");
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("No copy btn display");
        }

    }
    public void clickSearchResultInSignalPrv(){
        WebElement strategyAcc = getSearchResultInTopSignalPrvEle();
        strategyAcc.click();
        waitLoading();
    }

    /**
     * ：默认使用 Equivalent Used Margin 模式
     */
    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade) {
        return submitCopyRequest(menu, copier, needCopyDetail, isCopyOpenTrade, "Equivalent Used Margin");
    }

    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade, String copyMode) {
        prepareStrategyDetail(menu, copier);

        getCopyBtnEle().click();
        waitLoading();
        waitForCopyTrading(3000);
        waitLoadingInCopyTrading();

        if (copyMode != null && !StringUtils.containsIgnoreCase(copyMode, "Equivalent Used Margin")) {
            LogUtils.info("Switching to Copy Mode: " + copyMode);
            getCopyModeArrowEle().click();
            js.executeScript("arguments[0].click()", copyMode(copyMode));
            waitLoadingInCopyTrading();
        }

        if (needCopyDetail) {
            collectCopyDetails(copyMode);
        }

        handleOpenTradeSwitch(isCopyOpenTrade);

        return executeSubmitAndCheck();
    }

    protected void prepareStrategyDetail(CPMenu menu, String copier) {
        waitLoading();
        waitLoadingInCopyTrading();
        waitForCopyTrading(5000);

        strategyAccName = strategyAcc(copier).getText();
        strategyAcc(copier).click();
        waitLoading();

        boolean isBtnPresent = false;
        try {
            isBtnPresent = waitvisible.until(d -> getCopyBtnEle().isDisplayed());
        } catch (Exception ignored) {}

        if (!isBtnPresent) {
            GlobalMethods.printDebugInfo("Copy button not found, retrying navigation...");
            menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
            menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
           // strategyAcc = findClickableElementByXpath("//div[@id='pane-0']//div[./span[contains(text(),'"+ copier +"')]]");
            //strategyAcc.click();
            js.executeScript("arguments[0].click()", strategyAcc(copier));
            waitLoading();
        }
    }

    protected void collectCopyDetails(String copyMode) {
        String investment = getInputValue(getInvestmentInCopyDialogInputEle());
        copyAmount = investment;

        copyStrategyDetail.put("Return", getReturnInCopyDialogEle().getText());
        copyStrategyDetail.put("Settlement", getSettlementInCopyDialogEle().getText());
        copyStrategyDetail.put("Profit", getProfitSharingInCopyDialogEle().getText());
        copyStrategyDetail.put("Investment", investment);
        copyStrategyDetail.put("StopLoss", getStopLossSliderInCopyDialogEle().getAttribute("aria-valuetext"));
        copyStrategyDetail.put("CodeMode", getInputValue(getCodeModeInCopyDialogInputEle()));
        copyStrategyDetail.put("AvailableBalance", getAvailableBalanceInCopyDialogEle().getText());
        copyStrategyDetail.put("AvailableInvestment", getAvailableInvestmentInCopyDialogEle().getText());

        if (StringUtils.containsIgnoreCase(copyMode, "Fixed Lots")) {
            copyStrategyDetail.put("lots", getInputValue(getLotsInCopyDialogInputEle()));
        } else if (StringUtils.containsIgnoreCase(copyMode, "Fixed Multiples")) {
            copyStrategyDetail.put("multiples", getInputValue(getMultiplesInCopyDialogInputEle()));
        }
    }

    protected void handleOpenTradeSwitch(boolean isCopyOpenTrade) {
        waitLoadingInCopyTrading();
        boolean isCurrentlyChecked = getOpenTradeSwitchEle().getAttribute("class").contains("is-checked");
        if (isCopyOpenTrade != isCurrentlyChecked) {
            getOpenTradeSwitchEle().click();
            GlobalMethods.printDebugInfo("Adjusted copy open trades switch to: " + isCopyOpenTrade);
        }
    }

    protected boolean executeSubmitAndCheck() {
        getSubmitBtnEle().click();
        try {
            waitLoadingInCopyTrading();
            waitvisible.until(d -> getSuccessMsgEle().isDisplayed());
        } catch (Exception ex) {
            GlobalMethods.printDebugInfo("Success message did not appear.");
        }

        String submitMsg = getSuccessMsgEle().getText();
        boolean isSuccess = submitMsg.contains("Submit Successful");

        if (isSuccess) {
            getOkBtnEle().click();
            waitLoading();
        }
        return isSuccess;
    }

    private String getInputValue(WebElement element) {
        return (String) js.executeScript("return arguments[0].value;", element);
    }

    public String getStrategyAccName()
    {
        return strategyAccName;
    }

    public String getPageUrl()
    {
        waitLoading();
        return driver.getCurrentUrl();
    }

    public String getRecommendationTab()
    {
        return getRecommendationTabEle().getText();
    }

    public boolean verifyRecommendationContent() {
        List<WebElement> elements = getRecommendationContent();

        if (elements.size() < 3) {
            GlobalMethods.printDebugInfo("Not enough elements under recommendation tab found. Expected at least 3, but got: " + elements.size());
            return false;
        }

        String firstText  = elements.get(0).getText().trim().toLowerCase();
        String secondText = elements.get(1).getText().trim().toLowerCase();
        String thirdText  = elements.get(2).getText().trim().toLowerCase();

        boolean firstMatch  = firstText.contains("highest annual return".toLowerCase());
        boolean secondMatch = secondText.contains("low risk and stable return".toLowerCase());
        boolean thirdMatch  = thirdText.contains("high win rate".toLowerCase());

        if (!firstMatch) {
            System.out.println("First element under recommendation tab text mismatch: " + firstText);
        }
        if (!secondMatch) {
            System.out.println("Second element under recommendation tab text mismatch: " + secondText);
        }
        if (!thirdMatch) {
            System.out.println("Third element under recommendation tab text mismatch: " + thirdText);
        }

        return firstMatch && secondMatch && thirdMatch;
    }

    public List<WebElement> getRecommendationContent(){
        return getRecommendationContentEle();
    }

    public String getRankListTab(){
        return getRankListTabEle().getText();
    }

    public void clickRankListTab(){
       waitLoading();
       waitLoadingInCopyTrading();
       getRankListTabEle().click();
       waitLoading();
    }

    public void clickFirstImgInHighestReturnTab() {
        getFirstCardImageEle().click();
        waitLoading();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("click first img in highest return tab");
    }

    public void clickSecImgInHighestReturnTab() {
        getSecondCardImageEle().click();
        GlobalMethods.printDebugInfo("click second img in highest return tab");
    }

    public String getFirstStrategyNameInHighestReturnTab() {
        waitLoading();
        return getFirstCardInHighestReturnTabEle().getText();
    }

    public String getSecStrategyNameInHighestReturnTab() {
        waitLoading();
        return getSecondCardInHighestReturnTabEle().getText();
    }

    public String getStrategyDetailsDisplay() {
        waitLoading();
        return getStrategyUserInfoInDetailsEle().getText();
    }
    public String getsignalProviderInfoInSignalPrvPage() {
        waitLoading();
        return getSignalProviderInfoInSignalPrvPageEle().getText();
    }

    public boolean checkCopyDialogDisplay() {
       waitLoading();
       List<WebElement> elements = getCopyDialogTitleEle();
       return !elements.isEmpty();
    }
    public String getStrategyNameInCopyDialog(){
        waitLoading();
        return getStrategyNameInCopyDialogEle().getText();
    }

    public void closeCopyBtn(){
       try {
           getCloseBtnInCopyDialogEle().click();
       }
       catch (Exception e)
       {
           GlobalMethods.printDebugInfo("Close copy btn failed");
       }
    }

    public String getTabListInRankList(){
        return getTabListInRankListEle().getText();
    }

    public int getSizeOfTop3InRankList(){
        waitLoading();
       int size =  getTop3InRankListEle().size();
        GlobalMethods.printDebugInfo("Get size of top3 rating in rank list:" + size);
        return size;
    }

    public int getSizeOfTopRatingInRankList(){
        waitLoading();
        return getTopRatingInRankListEle().size();
    }

    public void clickViewMoreBtn(){
        getViewMoreBtnEle().click();
        waitLoading();
    }

    public String getTopStrategyAndProvider(){
        return getTopStrategyAndSiglePrvdEle().getText();
    }

    public String getSearchResult(){
        waitLoading();
        waitForCopyTrading(1000);
        String searchRlt = getSearchResultEle().getText();
        GlobalMethods.printDebugInfo("Search result is: " + searchRlt);
        return searchRlt;
    }
    public String getSearchResultInSignalProvider(){
        waitLoading();
        String searchRlt = getSearchResultInTopSignalPrvEle().getText();
        GlobalMethods.printDebugInfo("Search result in top signal provider is: " + searchRlt);
        return searchRlt;
    }

    public void backToDiscoverPage(){
        getBackToDiscoverEle().click();
        waitLoading();
    }
    //copy trading has to take between app and web, sometimes have to wait
    public void waitForCopyTrading(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
