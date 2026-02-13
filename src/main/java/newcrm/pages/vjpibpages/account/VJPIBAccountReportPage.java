package newcrm.pages.vjpibpages.account;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBAccountReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class VJPIBAccountReportPage extends IBAccountReportPage {

    public VJPIBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAccountReportSearchTypeDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='account-search'] //div[not(@role='combobox')]/div/input"), "Account Report - Search Type Dropdown");
    }

    protected WebElement getAccountReportSearchTypeCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'select-dropdown') and not(contains(@style,'display: none'))]/div[@class='el-scrollbar'] //li[5]"), "Account Report - Search Type: Campaign Source");
    }

    @Override
    protected WebElement getAccountReportFirstRowCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[@class='ht-table table_warp'] //tbody/tr[1]/td[6]/div"), "Account Report First Row - Campaign Source");
    }

    @Override
    protected WebElement getAccountReportSearchInputEle() {
        return assertElementExists(By.xpath("//div[@class='account-search'] //div[@role='combobox']/div/input"), "Account Report - Search Input Box");
    }


    @Override
    public void searchAccountReport_CampaignSource(String campaignSource){
        waitLoading();
        triggerElementClickEvent_withoutMoveElement(getAccountReportSearchTypeDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getAccountReportSearchTypeCampaignSourceEle());
        setInputValue(getAccountReportSearchInputEle(), campaignSource);
        triggerElementClickEvent_withoutMoveElement(getAccountReportSearchUpdateEle());
        waitLoading();
    }



}