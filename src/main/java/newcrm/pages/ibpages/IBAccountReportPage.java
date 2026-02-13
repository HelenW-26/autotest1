package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.util.stream.Collectors;

public class IBAccountReportPage extends Page {

    public IBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAccountReportSearchInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'clearable search-type')]/input"), "Account Report - Search Input Box");
    }

    protected WebElement getAccountReportSearchUpdateEle() {
        return assertElementExists(By.xpath("//div[@class='update_warp']/button[contains(@class,'update_btn')]"), "Account Report - Search Update");
    }

    protected WebElement getAccountReportFirstRowCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[@class='ht-table table_warp'] //tbody/tr[1]/td[6]/div"), "Account Report First Row - Campaign Source");
    }

    protected WebElement getAccountReportFirstRowAccountNumberEle() {
        return assertElementExists(By.xpath("//div[@class='ht-table table_warp'] //tbody/tr[1]/td[3]/div/span"), "Account Report First Row - Account Number");
    }

    // First Column for Accounts/Unfunded Accounts/Opened Accounts/Pending Accounts/Leads Tab = Date
    // First Column for Arhived Clients = User ID
    protected WebElement getAccountReportFirstRowFirstColumnEle(String tabName) {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div"), "Account Report - " + tabName + ": No Record");
    }

//    protected WebElement getAccountReportLeadsTabEmailEle(String userID) {
//        return assertElementExists(By.xpath("//tr[@class='el-table__row'][*]/td[2]/div"), "Account Report - Leads Tab: Name Column",e -> email.equalsIgnoreCase(e.getText().trim()));
//    }

    protected List<WebElement> getAccountReportLeadsTabNameEle() {
        return assertElementsExists(By.xpath("//tr[@class='el-table__row'][*]/td[3]/div"), "Account Report - Leads Tab: Name Column");
    }

    protected WebElement getDatePickerEle() {
        return assertElementExists(By.xpath("//div[@data-testid='openedAccounts'] //div[contains(@class, 'date-picker')]"), "Rebate Report Date Picker");
    }

    protected WebElement getPreviousYearArrowEle() {
        return assertElementExists(By.xpath("//button[contains(@class, 'd-arrow-left')]"), "Previous Year Arrow");
    }

    protected WebElement getSubsequentYearArrowEle() {
        return assertElementExists(By.xpath("(//button[contains(@class, 'd-arrow-right')])[last()]"), "Subsequent Year Arrow");
    }

    protected WebElement getFirstAvailableDayEle() {
        return assertElementExists(By.xpath("(//table[@class='el-date-table']//td[not(contains(@class, 'disabled'))])[1]"), "First Available Day");
    }

    protected WebElement getLastAvailableDayEle() {
        return assertElementExists(By.xpath("(//table[@class='el-date-table'] //td[contains(@class, 'available')])[last()]"), "Last Available Day");
    }

    protected WebElement getUpdateDateRangeButtonEle() {
        return assertElementExists(By.xpath("//button[@data-testid='UPDATE']"), "Update Date Range Button");
    }

    protected WebElement getAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/ibaccounts']"), "Accounts Tab");
    }

    protected WebElement getUnfundedAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/unfundedaccounts']"), "Unfunded Accounts Tab");
    }

    protected WebElement getOpenedAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/openaccounts']"), "Opened Accounts Tab");
    }

    protected WebElement getPendingAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/pendingaccounts']"), "Pending Accounts Tab");
    }

    protected WebElement getLeadsAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/leads']"), "Leads Accounts Tab");
    }

    protected WebElement getLeadsAccountsTabSelectDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='search_warp flex-row-between'] //input[@placeholder='Select']"), "Leads Accounts Tab - Dropdown");
    }

    protected WebElement getLeadsAccountsTabDemoAccountsDropdownValueEle() {
        return assertElementExists(By.xpath("//span"), "Leads Accounts Tab - Dropdown value - Demo Accounts",e -> "Demo Accounts".equalsIgnoreCase(e.getText().trim()));
    }

    protected WebElement getLeadsAccountsTabLeadsDropdownValueEle() {
        return assertElementExists(By.xpath("//span[text()='Leads']"), "Leads Accounts Tab - Dropdown value - Leads");
    }

    protected WebElement getArchivedClientsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/archivedClients']"), "Archived Clients Tab");
    }

    public void searchAccountReport_CampaignSource(String campaignSource){
        waitLoading();
        setInputValue(getAccountReportSearchInputEle(), campaignSource);
        triggerElementClickEvent_withoutMoveElement(getAccountReportSearchUpdateEle());
        waitLoading();
    }

    public void verifyCampaignSource(String campaignSource, String tradingAcc){
        waitLoading();
        if(getAccountReportFirstRowCampaignSourceEle().getText().equals(campaignSource)){
            LogUtils.info("Campaign Source: " + campaignSource + " is in Account Report Page.");
        } else {
            Assert.fail("Campaign Source: " + campaignSource + " is NOT in Account Report Page!");
        }

        if(getAccountReportFirstRowAccountNumberEle().getText().equals(tradingAcc)){
            LogUtils.info("Trading Account: " + tradingAcc + " is in Account Report Page.");
        } else {
            Assert.fail("Trading Account: " + tradingAcc + " is NOT in Account Report Page!");
        }
    }

    public void setDateRangeToLast365Days(){
        triggerElementClickEvent_withoutMoveElement(this.getDatePickerEle());

            triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());

        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    //Verifying the first 4 Tabs within Account Report Page
    public void verifyAccountReportPage4Tabs(){
        triggerElementClickEvent_withoutMoveElement(this.getAccountsTabEle());
        verifyDateFormat("Accounts", getAccountReportFirstRowFirstColumnEle("Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getUnfundedAccountsTabEle());
        waitLoading();
        verifyDateFormat("Unfunded Accounts", getAccountReportFirstRowFirstColumnEle("Unfunded Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getOpenedAccountsTabEle());

        if(driver.findElements(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div")).isEmpty()){
            setDateRangeToLast365Days();
            GlobalMethods.printDebugInfo("Opened Accounts Tab - Set Date Range to Last 365 Days");
        }

        verifyDateFormat("Opened Accounts", getAccountReportFirstRowFirstColumnEle("Opened Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getPendingAccountsTabEle());
        verifyDateFormat("Pending Accounts", getAccountReportFirstRowFirstColumnEle("Pending Accounts").getText());

//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());
//        verifyDateFormat("Demo Accounts", getAccountReportFirstRowFirstColumnEle("Demo Accounts").getText());
//
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabLeadsDropdownValueEle());
//        verifyDateFormat("Leads", getAccountReportFirstRowFirstColumnEle("Leads").getText());
    }

//    //Verifying Demo Account is in Account Report's Leads Tab
//    public void verifyDemoAccountInAccountReport_Name(String name){
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
//        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());
//
//        if(!getAccountReportLeadsTabEmailEle(name).getText().equals(name)){
//            Assert.fail("Demo Account with Email: " + name + " is NOT found in Account Report's Leads Tab!");
//        }
//    }

    //Verifying Demo Account is in Account Report's Leads Tab
    public List<String> retrieveDemoAccountNamesInAccountReport(){
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());

        return getAccountReportLeadsTabNameEle().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    //Verifying Pending Account is in Account Report's Leads Tab
    public List<String> retrievePendingAccountNamesInAccountReport(String tradingAcc){
        triggerElementClickEvent_withoutMoveElement(this.getPendingAccountsTabEle());

        setInputValue(getAccountReportSearchInputEle(), tradingAcc);
        triggerElementClickEvent(getAccountReportLeadsTabNameEle().get(0));
        waitLoading();

        return getAccountReportLeadsTabNameEle().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void verifyDateFormat(String tabName, String date){
        waitLoading();

        if (date.matches(("^\\d{2}/\\d{2}/\\d{4}$"))){
            GlobalMethods.printDebugInfo(tabName + " Tab's Date format is correct");
        } else {
            Assert.fail(tabName + " Tab's Date format is INCORRECT!");
        }
    }

    public void verifyLeadsAndArchivedClientsTab(){
        //Leads
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());
        verifyDateFormat("Demo Accounts", getAccountReportFirstRowFirstColumnEle("Demo Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabLeadsDropdownValueEle());
        verifyDateFormat("Leads", getAccountReportFirstRowFirstColumnEle("Leads").getText());

        //Archived Client
        triggerElementClickEvent_withoutMoveElement(this.getArchivedClientsTabEle());
        String userID = this.getAccountReportFirstRowFirstColumnEle("Archived Clients").getText();
        if (!userID.isEmpty()){
            GlobalMethods.printDebugInfo("Archived Clients First Record: " + userID);
        } else {
            Assert.fail("Archived Clients First Record is EMPTY!");
        }
    }

}
