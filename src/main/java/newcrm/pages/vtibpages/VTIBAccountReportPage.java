package newcrm.pages.vtibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.ibpages.IBAccountReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class VTIBAccountReportPage extends IBAccountReportPage {

    public VTIBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getAccountReportSearchInputEle() {
        return assertElementExists(By.xpath("//div[@class='content_calender']/div[@class='el-input el-input--prefix']/input"), "Account Report - Search Input Box");
    }

    @Override
    protected WebElement getAccountReportSearchUpdateEle() {
        return assertElementExists(By.xpath("//div[@class='content_calender']/div[@class='button-row']/button[contains(@class,'search_button')]"), "Account Report - Search Update");
    }

    @Override
    protected WebElement getAccountReportFirstRowCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'el-table__body-wrapper')] //tbody/tr[1]/td[6]/div"), "Account Report First Row - Campaign Source");
    }

    @Override
    protected WebElement getAccountReportFirstRowAccountNumberEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'el-table__body-wrapper')] //tbody/tr[1]/td[3]/div/u/a"), "Account Report First Row - Account Number");
    }

    protected WebElement getLeadsSectionFirstRowFirstColumnEle() {
        return assertElementExists(By.xpath("//li[@class='active']/div/div/div/table/tbody/tr[@class='el-table__row'][1]/td[1]/div"), "Account Report - Leads: No Record");
    }

    protected WebElement getDateDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='shortcut']"), "Opened Accounts Date Shortcut Dropdown");
    }

    protected WebElement getLast30DaysEle() {
        return assertElementExists(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style, 'display: none'))] //span[text()='Last 30 days']"), "Rebate Report Date - Last 30 Days");
    }

    @Override
    protected WebElement getUpdateDateRangeButtonEle() {
        return assertElementExists(By.xpath("//div[@class='query_content_main_right']"), "Update Date Range Button");
    }

    @Override
    protected WebElement getAccountsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[1]"), "Accounts Tab");
    }

    @Override
    protected WebElement getUnfundedAccountsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[2]"), "Unfunded Accounts Tab");
    }

    @Override
    protected WebElement getOpenedAccountsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[3]"), "Opened Accounts Tab");
    }

    @Override
    protected WebElement getPendingAccountsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[4]"), "Pending Accounts Tab");
    }

    @Override
    protected WebElement getLeadsAccountsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[5]"), "Leads Accounts Tab");
    }

    protected WebElement getLeadsAccountsTabDemoAccSectionEle() {
        return assertElementExists(By.xpath("//li[@data-testid='resetNormalPage']"), "Leads Tab - Demo Accounts Section");
    }

    protected WebElement getLeadsAccountsTabLeadsSectionEle() {
        return assertElementExists(By.xpath("//li[@data-testid='resetDemoPage']"), "Leads Tab - Leads Section");
    }

    protected WebElement getArchivedClientsTabEle() {
        return assertElementExists(By.xpath("//ul[@id='guide-account']/li[6]"), "Archived Clients Tab");
    }

    public void setDateRangeToLast30Days(){
        triggerElementClickEvent_withoutMoveElement(this.getDateDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLast30DaysEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    @Override
    //Verifying the first 4 Tabs within Account Report Page
    public void verifyAccountReportPage4Tabs(){
        triggerElementClickEvent_withoutMoveElement(this.getAccountsTabEle());
        verifyDateFormat("Accounts", getAccountReportFirstRowFirstColumnEle("Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getUnfundedAccountsTabEle());
        verifyDateFormat("Unfunded Accounts", getAccountReportFirstRowFirstColumnEle("Unfunded Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getOpenedAccountsTabEle());

        if(driver.findElements(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div")).isEmpty()){
            setDateRangeToLast30Days();
            GlobalMethods.printDebugInfo("Opened Accounts Tab - Set Date Range to Last 30 Days");
        }

        verifyDateFormat("Opened Accounts", getAccountReportFirstRowFirstColumnEle("Opened Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getPendingAccountsTabEle());
        verifyDateFormat("Pending Accounts", getAccountReportFirstRowFirstColumnEle("Pending Accounts").getText());


    }

    @Override
    //Verify the Archived Client Tab
    public void verifyLeadsAndArchivedClientsTab(){
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccSectionEle());
        verifyDateFormat("Demo Accounts", getAccountReportFirstRowFirstColumnEle("Demo Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabLeadsSectionEle());
        verifyDateFormat("Leads", getLeadsSectionFirstRowFirstColumnEle().getText());

        triggerElementClickEvent_withoutMoveElement(this.getArchivedClientsTabEle());
        String userID = this.getAccountReportFirstRowFirstColumnEle("Archived Clients").getText();
        if (!userID.isEmpty()){
            GlobalMethods.printDebugInfo("Archived Clients First Record: " + userID);
        } else {
            Assert.fail("Archived Clients First Record is EMPTY!");
        }
    }

}
