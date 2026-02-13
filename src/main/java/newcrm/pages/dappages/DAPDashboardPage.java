package newcrm.pages.dappages;

import cn.hutool.log.Log;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DAPDashboardPage extends Page {

    public DAPDashboardPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getProfilePanelEle() {
        return assertElementExists(By.xpath("//img[contains(@class,'profile-panel-popover')]"), "Profile Panel Icon");
    }

    protected WebElement getProfilePanelUsernameEle() {
        return assertElementExists(By.xpath("//span[@class='basic-info__username']"), "Profile Panel - Username");
    }

    protected WebElement getProfilePanelUserIDEle() {
        return assertElementExists(By.xpath("//div[@class='basic-info__userID']/span"), "Profile Panel - User ID");
    }

    protected WebElement getNavigateToMyProfileEle() {
        return assertElementExists(By.xpath("//div[@class='basic-info__arrow']"), "Navigate to My Profile Icon");
    }

    protected WebElement getProfilePanelLogoutButtonEle() {
        return assertElementExists(By.xpath("(//div[@class='profile-panel-actions'] //span[@class='profile-panel-actions__item-verification'])[2]"), "Profile Panel - Logout");
    }

    protected WebElement getSwitchToCPEle() {
        return assertElementExists(By.xpath("//div[@class='profile-panel__role-switch'] //div[@class='ht-multiple-switcher__item']"), "Switch to CP Button");
    }

    protected WebElement getLanguageDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='language-switcher__trigger']"), "Language Dropdown");
    }

    protected WebElement getActiveLanguageEle() {
        return assertElementExists(By.xpath("//div[@class='language-selector__item language-selector__item--active'] //span[@class='language-selector__item-label']"), "Active (Currently Selected) Language");
    }

    protected WebElement getRegisterTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-2']"), "Marketing Link - Register Tab");
    }

    protected WebElement getRegisterLinkEle() {
        return assertElementExists(By.xpath("//div[@id='pane-2'] //span[@class='copy-content__text copy-content__text--home']"), "Marketing Link - Register Link");
    }

    protected WebElement getReferralCodeEle() {
        return assertElementExists(By.xpath("//div[@class='links-card__field'] //span[@class='copy-content__text copy-content__text--home']"), "Marketing Link - Referral Code");
    }

    protected WebElement getMarketingMenuEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.spread']"), "Marketing Menu");
    }

    protected WebElement getDeepLinkEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.spread.deepLink']"), "Marketing Menu - Deep Link");
    }

    protected WebElement getClientEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.client']"), "Client Menu");
    }

    protected WebElement getClientListEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.client.clientList']"), "Client List Menu");
    }

    protected WebElement getCommissionEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.commission']"), "Commission Menu");
    }

    protected WebElement getCommissionReportEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.commission.report']"), "Commission Report Menu");
    }

    protected WebElement getCommissionPlanEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.commission.plan']"), "Commission Report Menu");
    }

    protected WebElement getSubCPAEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.agent']"), "Sub CPA Menu");
    }

    protected WebElement getMultiCommissionEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.agent.multi']"), "Multi Commission Menu");
    }

    protected WebElement getDashboardEle() {
        return assertElementExists(By.xpath("//ul[@role='menubar']/li[1]/span"), "Dashboard Menu");
    }

    protected WebElement getPaymentButtonEle() {
        return assertElementExists(By.xpath("//div[@class='balance-card__commission'] //button"), "Payment Button");
    }

    protected WebElement getPerformanceReportEle() {
        return assertElementExists(By.xpath("//li[@data-testid='menu.client.performanceReport']"), "Performance Report Menu");
    }

    protected List<WebElement> getClientJourneyRegistrationTimeEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[1]/div/span"), "Client Journey - Registration Time");
    }

    protected List<WebElement> getClientJourneyUIDEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[2]/div/span"), "Client Journey - User IDs");
    }

    protected List<WebElement> getClientJourneyNameEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[3]/div/span"), "Client Journey - Names");
    }

    protected List<WebElement> getClientJourneyStatusEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[4]/div/span/span"), "Client Journey - Status");
    }

    protected WebElement getStatisticsFilterCurrentStartDateEle() {
        return assertElementExists(By.xpath("//div[@class='performance__condition']/div/div/div/input[1]"), "Statistics Filter - Current Start Date");
    }

    protected WebElement getStatisticsFilterCurrentEndDateEle() {
        return assertElementExists(By.xpath("//div[@class='performance__condition']/div/div/div/input[2]"), "Statistics Filter - Current End Date");
    }

    protected WebElement getStatisticsFilterLastAvailableDateEle() {
        return assertElementExists(By.xpath("(//td[contains(@class,'available') and not(contains(@class,'today'))])[last()]"), "Statistics Filter - Last Available Date");
    }

    protected WebElement getStatisticsFilterTodayDateEle() {
        return assertElementExists(By.xpath("//td[contains(@class,'available today')]"), "Statistics Filter - Today's Date");
    }

    protected List<WebElement> getStatisticsPerformanceIndexItemNameListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'performance__index-item')] //div[@class='performance__index-name']"), "Statistics - Performance Index Item Name List");
    }

    protected List<WebElement> getStatisticsPerformanceIndexItemValueListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'performance__index-item')] //div[@class='performance__index-value']"), "Statistics - Performance Index Item Value List");
    }

    protected List<WebElement> getStatisticsPerformanceIndexItemRateListEle() {
        return assertElementsExists(By.xpath("//span[@class='performance__index-rate-zero' or @class='performance__index-rate--rise' or @class='performance__index-rate--fall']"), "Statistics - Performance Index Item Rate List");
    }

    protected WebElement getPostbackTrackerNotYetSetupButtonEle() {
        return assertElementExists(By.xpath("//div[@class='tracker-card__title_setting']"), "Postback Tracker Button (Not Yet Setup)");
    }

    protected List<WebElement> getPostbackTrackerAlreadySetupButtonsEle() {
        return assertElementsExists(By.xpath("//div[@class='tracker-card__content-item-url']"), "Postback Tracker Buttons (Already Setup)");
    }

    protected List<WebElement> getPostbackTrackerAlreadySetupTitleEle() {
        return assertElementsExists(By.xpath("//div[@class='tracker-card__content-item-url']/preceding-sibling::*"), "Postback Tracker Titles (Already Setup)");
    }

    protected WebElement getTotalCommissionAmountEle() {
        return assertElementExists(By.xpath("//div[@class='balance-card__amount']/span[contains(@class,'text')]"), "Total Commission - Amount");
    }

    protected WebElement getTotalCommissionCurrencyEle() {
        return assertElementExists(By.xpath("//div[@class='balance-card__amount']/span[contains(@class,'currency')]"), "Total Commission - Currency");
    }

    protected WebElement getAvailableBalanceAmountEle() {
        return assertElementExists(By.xpath("//div[@class='balance-card__value']/span[contains(@class,'text')]"), "Available Balance - Amount");
    }

    protected WebElement getAvailableBalanceCurrencyEle() {
        return assertElementExists(By.xpath("//div[@class='balance-card__value']/span[contains(@class,'currency')]"), "Available Balance - Currency");
    }

    protected List<WebElement> getSubCPAAccountIDListEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-right'] //tbody/tr/td[2]/div/span"), "Sub CPA Account ID");
    }

    protected List<WebElement> getSubCPANameListEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-right'] //tbody/tr/td[3]/div/span"), "Sub CPA Name");
    }

    protected List<WebElement> getTotalCommissionListEle() {
        return assertElementsExists(By.xpath("//div[@class='dashboard__additional-section-right'] //tbody/tr/td[4]/div/span"), "Total Commission(USD)");
    }

    public String retrieveProfilePanelUserID()  {
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@class,'profile-panel-popover')]")));
        triggerClickEvent_withoutMoveElement(getProfilePanelEle());
        LogUtils.info("Username: "+getProfilePanelUsernameEle().getText());
        String userID = getProfilePanelUserIDEle().getText();
        userID = userID.replace("UID: ","");
        LogUtils.info("User ID: "+userID);

        Assert.assertTrue(getNavigateToMyProfileEle().isDisplayed());
        LogUtils.info("Navigate to My Profile Icon is displayed.");

        //Close Profile Panel
        triggerClickEvent_withoutMoveElement(getProfilePanelEle());

        return userID;
    }

    public void dapLogout(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@class,'profile-panel-popover')]")));
        triggerClickEvent_withoutMoveElement(getProfilePanelEle());
        triggerClickEvent_withoutMoveElement(getProfilePanelLogoutButtonEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-testid='userName_login']")));
        checkUrlContains("login");
        LogUtils.info("DAP Logged out successfully.");
    }

    public void switchToCP(){
        String title = driver.getTitle();
        if(!title.toLowerCase().contains("partner")) {
            LogUtils.info("At CP portal. Do not need switch");
            return;
        }
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@class,'profile-panel-popover')]")));
        triggerClickEvent_withoutMoveElement(getProfilePanelEle());
        triggerClickEvent_withoutMoveElement(getSwitchToCPEle());
        fastwait.until(ExpectedConditions.titleContains("Client Portal"));
        checkUrlContains("secure");
    }

    public String getActiveLanguage(){
        triggerClickEvent_withoutMoveElement(getLanguageDropdownEle());
        return getActiveLanguageEle().getText();
    }

    public String getRegisterLiveAccLink(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tab-2']")));
        triggerClickEvent_withoutMoveElement(getRegisterTabEle());
        return getRegisterLinkEle().getText();
    }

    public String getReferralCode(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='links-card__field'] //span[@class='copy-content__text copy-content__text--home']")));
        triggerClickEvent_withoutMoveElement(getReferralCodeEle());
        return getReferralCodeEle().getText();
    }

    public void navigateToDashboardPage(){
        triggerClickEvent_withoutMoveElement(getDashboardEle());
        LogUtils.info("Navigated to DAP Dashboard page.");
    }

    public void navigateToDeepLinkPage(){
        triggerClickEvent_withoutMoveElement(getMarketingMenuEle());
        triggerClickEvent_withoutMoveElement(getDeepLinkEle());
        LogUtils.info("Navigated to DAP Deep Link page.");
    }

    public void navigateToClientListPage(){
        triggerClickEvent_withoutMoveElement(getClientEle());
        triggerClickEvent_withoutMoveElement(getClientListEle());
        LogUtils.info("Navigated to DAP Client List page.");
    }

    public void navigateToCommissionReportPage(){
        triggerClickEvent_withoutMoveElement(getCommissionEle());
        triggerClickEvent_withoutMoveElement(getCommissionReportEle());
        LogUtils.info("Navigated to DAP Commission Report page.");
    }

    public void navigateToCommissionPlanPage(){
        triggerClickEvent_withoutMoveElement(getCommissionEle());
        triggerClickEvent_withoutMoveElement(getCommissionPlanEle());
        LogUtils.info("Navigated to DAP Commission Plan page.");
    }

    public void navigateToMultiCommissionPage(){
        triggerClickEvent_withoutMoveElement(getSubCPAEle());
        triggerClickEvent_withoutMoveElement(getMultiCommissionEle());
        LogUtils.info("Navigated to DAP Commission Plan page.");
    }

    public void navigateToPaymentPage(){
//        triggerClickEvent_withoutMoveElement(getCommissionEle());
        triggerClickEvent_withoutMoveElement(getPaymentButtonEle());
        LogUtils.info("Navigated to DAP Commission Report page.");
    }

    public String navigateToPostbackTrackerPage(){
        String postbackEventType = "";
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@data-testid='menu.spread.deepLink']")));
        if(driver.findElements(By.xpath("//div[@class='tracker-card__title_setting']")).isEmpty()){
            //Already setup (Not Yet Setup gear button not exist)
            Random random = new Random();
            int randomIndex = random.nextInt(getPostbackTrackerAlreadySetupButtonsEle().size());
            WebElement e = getPostbackTrackerAlreadySetupButtonsEle().get(randomIndex);
            postbackEventType = getPostbackTrackerAlreadySetupTitleEle().get(randomIndex).getText();
            this.moveElementToVisible(e);
            triggerElementClickEvent(e);
            LogUtils.info("Navigated to DAP Postback Tracker page, selected event type: "+postbackEventType);
        }else{
            //Not Yet setup (Click Not Yet Setup gear button)
            triggerClickEvent_withoutMoveElement(getPostbackTrackerNotYetSetupButtonEle());
            LogUtils.info("Navigated to DAP Postback Tracker page (Not yet setup).");
        }
        return postbackEventType;
    }

    public List<String> getAllClientJourneyRegTime(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[1]/div/span")));
        ArrayList<String> regTime = new ArrayList<>();
        moveElementToVisible(this.getClientJourneyRegistrationTimeEle().get(0));
        List<WebElement> div = this.getClientJourneyRegistrationTimeEle();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText");
            if(acc!=null && acc.trim().length()>0) {
                regTime.add(acc);
            }
        }
        return regTime;
    }

    public List<String> getAllClientJourneyUID(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[2]/div/span")));
        ArrayList<String> uids = new ArrayList<>();
        moveElementToVisible(this.getClientJourneyUIDEle().get(0));
        List<WebElement> div = this.getClientJourneyUIDEle();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText");
            if(acc!=null && acc.trim().length()>0) {
                uids.add(acc);
            }
        }
        return uids;
    }

    public List<String> getAllClientJourneyName(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[3]/div/span")));
        ArrayList<String> name = new ArrayList<>();
        moveElementToVisible(this.getClientJourneyNameEle().get(0));
        List<WebElement> div = this.getClientJourneyNameEle();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText").toLowerCase();
            if(acc!=null && acc.trim().length()>0) {
                name.add(acc);
            }
        }
        return name;
    }

    public List<String> getAllClientJourneyStatus(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[4]/div/span")));
        ArrayList<String> status = new ArrayList<>();
        moveElementToVisible(this.getClientJourneyStatusEle().get(0));
        List<WebElement> div = this.getClientJourneyStatusEle();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText").toLowerCase();
            if(acc!=null && acc.trim().length()>0) {
                status.add(acc);
            }
        }
        return status;
    }

    public void verifyTopSubCPATable(Map<String, List<String>> commissionMap){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='dashboard__additional-section-left'] //tbody/tr/td[4]/div/span")));

        moveElementToVisible(this.getSubCPAAccountIDListEle().get(0));
        List<String> dashboardSubCPANameList = getSubCPANameListEle().stream().map(WebElement::getText).toList();
        List<String> dashboardSubCPAAccountIDList = getSubCPAAccountIDListEle().stream().map(WebElement::getText).toList();
        List<Double> dashboardTotalCommissionList = getTotalCommissionListEle().stream().map(WebElement::getText).map(String::trim).map(Double::valueOf).toList();

        //Sort By totalCommissionList (commission value)
        Map<String, List<String>> descendingcommissionMap =
                commissionMap.entrySet()
                        .stream()
                        .sorted(Comparator.comparing(
                                (Map.Entry<String, List<String>> e) -> Double.parseDouble(e.getValue().get(1))
                        ).reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        List<String> subCPANameList = new ArrayList<>(descendingcommissionMap.keySet());
        List<String> subCPAAccountIDList = new ArrayList<>();
        List<String> totalCommissionStrList = new ArrayList<>();

        descendingcommissionMap.values().forEach(pair -> {
            subCPAAccountIDList.add(pair.get(0));
            totalCommissionStrList.add(pair.get(1));
        });

        List<Double> totalCommissionList = totalCommissionStrList
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Double::valueOf)
                .toList();

        for (int acc = 0; acc <= dashboardSubCPANameList.size()-1; acc++) {

            String expectedCPAName = subCPANameList.get(acc);
            String actualCPAName = dashboardSubCPANameList.get(acc);

            Assert.assertTrue(expectedCPAName.equals(actualCPAName));

            String expectedCPAAccountID = subCPAAccountIDList.get(acc);
            String actualCPAAccountID = dashboardSubCPAAccountIDList.get(acc);

            Assert.assertTrue(expectedCPAAccountID.equals(actualCPAAccountID));

            Double expectedCommissionValue = totalCommissionList.get(acc);
            Double actualCommissionValue = dashboardTotalCommissionList.get(acc);

            Assert.assertTrue(expectedCommissionValue.equals(actualCommissionValue));

        }
    }

    public double getTotalCommission(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tab-2']")));
        return Double.parseDouble(getTotalCommissionAmountEle().getText());
    }

    public void verifyAvailableBalance(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tab-2']")));
        double availableBalanceAmount = Double.parseDouble(getAvailableBalanceAmountEle().getText());
        String availableBalanceCurrency = getAvailableBalanceCurrencyEle().getText();

        Assert.assertTrue(availableBalanceAmount>0,"Available Balance = "+availableBalanceAmount);
        Assert.assertTrue(availableBalanceCurrency.equalsIgnoreCase("usd"),"Available Balance Currency = " + availableBalanceCurrency + "; Expected = USD");
        LogUtils.info("Verified Available Balance on dashboard: " + getAvailableBalanceAmountEle().getText() +""+ availableBalanceCurrency);
    }

    public void verifyStatisticsDefaultDate(){
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='performance__condition']/div/div/div/input[1]")));
        String startDate = getStatisticsFilterCurrentStartDateEle().getAttribute("value");
        String endDate = getStatisticsFilterCurrentEndDateEle().getAttribute("value");

        Assert.assertTrue(currentDate.equals(startDate),"Statistics Default Start Date is not today. Expected: "+currentDate+" , Actual: "+startDate);
        Assert.assertTrue(currentDate.equals(endDate),"Statistics Default End Date is not today. Expected: "+currentDate+" , Actual: "+endDate);
        LogUtils.info("Statistics Table Default Date is correct: "+currentDate);
    }

    public void verifyStatisticsRate(){
        List<String> performanceIndexNameList = getStatisticsPerformanceIndexItemNameListEle().stream().map(WebElement::getText).toList();
        List<String> todayPerformanceIndexRateList = getStatisticsPerformanceIndexItemRateListEle().stream().map(WebElement::getText).toList();
        List<String> todayPerformanceIndexValueList = getStatisticsPerformanceIndexItemValueListEle().stream().map(WebElement::getText).toList();

        //Select yesterday's date
        triggerElementClickEvent(getStatisticsFilterCurrentStartDateEle());
        triggerElementClickEvent(getStatisticsFilterLastAvailableDateEle());
        triggerElementClickEvent(getStatisticsFilterLastAvailableDateEle());

        List<String> yesterdayPerformanceIndexValueList = getStatisticsPerformanceIndexItemValueListEle().stream().map(WebElement::getText).toList();

        for(int i=0;i<performanceIndexNameList.size();i++){
            String name = performanceIndexNameList.get(i);
            String todayRate = todayPerformanceIndexRateList.get(i);
            String todayValueStr = todayPerformanceIndexValueList.get(i).replaceAll(",","");
            String yesterdayValueStr = yesterdayPerformanceIndexValueList.get(i).replaceAll(",","");

            double todayValue = Double.parseDouble(todayValueStr);
            double yesterdayValue = Double.parseDouble(yesterdayValueStr);

            if(todayValue==0 && yesterdayValue!=0){
                Assert.assertTrue(todayRate.equals("-100.00%"),"Statistics Rate for "+name+" is incorrect. Today's value is "+todayValue+", yesterday is "+yesterdayValueStr+" , but actual rate is: "+todayRate);
            } else if(todayValue==0 && yesterdayValue==0) {
                Assert.assertTrue(todayRate.equals("0.00%"),"Statistics Rate for "+name+" is incorrect. Today's value is "+todayValue+", yesterday is "+yesterdayValueStr+" , but actual rate is: "+todayRate);
            } else {
                double expectedRate = ((todayValue - yesterdayValue) / yesterdayValue) * 100;
                String expectedRateStr = String.format("%.2f", expectedRate) + "%";

                Assert.assertTrue(todayRate.equals(expectedRateStr),"Statistics Rate for "+name+" is incorrect. Expected: "+expectedRateStr+" , Actual: "+todayRate);
            }

            LogUtils.info("Statistics Rate for "+name+" is correct. Today's Value: "+todayValueStr+" , Yesterday's Value: "+yesterdayValueStr+" , Rate: "+todayRate);
        }
    }

    public boolean checkPerformanceReportEntryPointExist(){
        triggerElementClickEvent(getClientEle());
        boolean perfReportExist = !driver.findElements(By.xpath("//li[@data-testid='menu.client.performanceReport']")).isEmpty();
        return perfReportExist;
    }



}
