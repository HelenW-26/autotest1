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

public class IBClientReportPage extends Page {

    public IBClientReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getClientReportSearchInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'clearable search-type')]/input"), "Account Report - Search Input Box");
    }

    protected WebElement getClientReportSearchUpdateEle() {
        return assertElementExists(By.xpath("//div[@class='update_warp']/button[contains(@class,'update_btn')]"), "Account Report - Search Update");
    }

    protected WebElement getClientReportFirstRowFirstColumnEle(String tabName) {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div"), "Client Report - " + tabName + ": No Record");
    }

    //Date is located on the third column for 'New Clients' & 'FTD Clients' tabs
    protected WebElement getClientReportFirstRowThirdColumnEle(String tabName) {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[3]/div"), "Client Report - " + tabName + ": No Record");
    }

    protected List<WebElement> getClientReportLeadsTabNameEle() {
        return assertElementsExists(By.xpath("//tr[@class='el-table__row'][*]/td[3]/div"), "Account Report - Leads Tab: Name Column");
    }

    protected WebElement getNewClientsDatePickerEle() {
        return assertElementExists(By.xpath("//div[@data-testid='newClients'] //div[contains(@class, 'date-picker')]"), "New Clients Date Picker");
    }

    protected WebElement getFTDClientsDatePickerEle() {
        return assertElementExists(By.xpath("//div[@data-testid='FTDClients'] //div[contains(@class, 'date-picker')]"), "FTD Clients Date Picker");
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

    protected WebElement getLeadsAccountsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/clientLeads']"), "Leads Accounts Tab");
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

    protected WebElement getNewClientsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/newClients']"), "New Clients Tab");
    }

    protected WebElement getFTDClientsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/FTDClients']"), "FTD Clients Tab");
    }

    protected WebElement getArchivedClientsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/clientArchivedClients']"), "Archived Clients Tab");
    }

    protected List<WebElement> getAccountReportLeadsTabNameEle() {
        return assertElementsExists(By.xpath("//tr[@class='el-table__row'][*]/td[3]/div"), "Account Report - Leads Tab: Name Column");
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
        verifyDateFormat("Demo Accounts", getClientReportFirstRowFirstColumnEle("Demo Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabLeadsDropdownValueEle());
        verifyDateFormat("Leads", getClientReportFirstRowFirstColumnEle("Leads").getText());

        //New Clients
        triggerElementClickEvent_withoutMoveElement(this.getNewClientsTabEle());
        if(driver.findElements(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div")).isEmpty()){
            setDateRangeToLast365Days_NewClients();
            GlobalMethods.printDebugInfo("New Clients Tab - Set Date Range to Last 365 Days");
        }
        verifyDateFormat("New Clients", getClientReportFirstRowThirdColumnEle("New Clients").getText());

        //FTD Clients
        triggerElementClickEvent_withoutMoveElement(this.getFTDClientsTabEle());
        if(driver.findElements(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div")).isEmpty()){
            setDateRangeToLast365Days_FTDClients();
            GlobalMethods.printDebugInfo("FTD Clients Tab - Set Date Range to Last 365 Days");
        }
        verifyDateFormat("FTD Clients", getClientReportFirstRowThirdColumnEle("FTD Clients").getText());

        //Archived Client
        triggerElementClickEvent_withoutMoveElement(this.getArchivedClientsTabEle());
        String userID = this.getClientReportFirstRowFirstColumnEle("Archived Clients").getText();
        if (!userID.isEmpty()){
            GlobalMethods.printDebugInfo("Archived Clients First Record: " + userID);
        } else {
            Assert.fail("Archived Clients First Record is EMPTY!");
        }
    }

    //Verifying Demo Account is in Client Report's Leads Tab
    public List<String> retrieveDemoAccountNamesInClientReport(){
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());

        return getAccountReportLeadsTabNameEle().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void setDateRangeToLast365Days_NewClients(){
        triggerElementClickEvent_withoutMoveElement(this.getNewClientsDatePickerEle());

        triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());

        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    public void setDateRangeToLast365Days_FTDClients(){
        triggerElementClickEvent_withoutMoveElement(this.getFTDClientsDatePickerEle());

        triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());

        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

}
