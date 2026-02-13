package newcrm.pages.staribpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBAccountReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class STARIBAccountReportPage extends IBAccountReportPage {

    public STARIBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getLeadsSectionFirstRowFirstColumnEle() {
        return assertElementExists(By.xpath("//li[@class='active']/div/div/div/table/tbody/tr[@class='el-table__row'][1]/td[1]/div"), "Account Report - Leads: No Record");
    }

    protected WebElement getStartDatePickerEle() {
        return assertElementExists(By.xpath("//div[@type='daterange']/input[1]"), "Account Report Start Date Picker");
    }

    protected WebElement getEndDatePickerEle() {
        return assertElementExists(By.xpath("//div[@type='daterange']/input[2]"), "Account Report End Date Picker");
    }

    protected WebElement getLeadsAccountsTabDemoAccSectionEle() {
        return assertElementExists(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown']/div/div/ul/li[1]"), "Leads Tab - Demo Accounts Section");
    }

    protected WebElement getLeadsAccountsTabLeadsSectionEle() {
        return assertElementExists(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown']/div/div/ul/li[2]"), "Leads Tab - Leads Section");
    }

    @Override
    protected WebElement getAccountReportFirstRowCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[@class='ht-table table_warp'] //tbody/tr[1]/td[6]/div"), "Account Report First Row - Campaign Source");
    }

    @Override
    public void setDateRangeToLast365Days(){
        triggerElementClickEvent_withoutMoveElement(this.getStartDatePickerEle());

//        if(!getFirstAvailableDayEle().isDisplayed()){
            triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());
//        }

        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
//        triggerElementClickEvent_withoutMoveElement(this.getEndDatePickerEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    @Override
    //Verifying the 4 Tabs within Account Report Page
    public void verifyAccountReportPage4Tabs(){
        triggerElementClickEvent_withoutMoveElement(this.getAccountsTabEle());
        verifyDateFormat("Accounts", getAccountReportFirstRowFirstColumnEle("Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getUnfundedAccountsTabEle());
        verifyDateFormat("Unfunded Accounts", getAccountReportFirstRowFirstColumnEle("Unfunded Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getOpenedAccountsTabEle());

        if(driver.findElements(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div")).isEmpty()){
            setDateRangeToLast365Days();
            GlobalMethods.printDebugInfo("Opened Accounts Tab - Set Date Range to Last 365 Days");
        }

        verifyDateFormat("Opened Accounts", getAccountReportFirstRowFirstColumnEle("Opened Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getPendingAccountsTabEle());
        verifyDateFormat("Pending Accounts", getAccountReportFirstRowFirstColumnEle("Pending Accounts").getText());
    }

    @Override
    //Verify the Archived Client Tab
    public void verifyLeadsAndArchivedClientsTab(){
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabDemoAccountsDropdownValueEle());
        verifyDateFormat("Demo Accounts", getAccountReportFirstRowFirstColumnEle("Demo Accounts").getText());

        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabSelectDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLeadsAccountsTabLeadsDropdownValueEle());
        verifyDateFormat("Leads", getAccountReportFirstRowFirstColumnEle("Leads").getText());

        triggerElementClickEvent_withoutMoveElement(this.getArchivedClientsTabEle());
        String userID = this.getAccountReportFirstRowFirstColumnEle("Archived Clients").getText();
        if (!userID.isEmpty()){
            GlobalMethods.printDebugInfo("Archived Clients First Record: " + userID);
        } else {
            Assert.fail("Archived Clients First Record is EMPTY!");
        }
    }

}
