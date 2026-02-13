package newcrm.pages.owspages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class OWSCPAPage extends Page {

    public OWSCPAPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getCPAAccountIDInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_account']"), "CPA Account ID Input");
    }

    protected WebElement getCPANameInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_userName']"), "CPA Name Input");
    }

    protected WebElement getUserIdInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_userId']"), "User ID Input");
    }

    protected WebElement getExpandSearchOptionsEle() {
        return assertElementExists(By.xpath("//span[@class='anticon anticon-down']/parent::span/parent::button"), "Expand Search Options Button");
    }

    protected WebElement getEmailInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_email']"), "Email Input");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//div[@class='ant-form-item-control-input'] //button[contains(@class,'ant-btn-color-primary')]"), "Search Button");
    }

    protected WebElement getFirstRowSaleEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1]/td[@class='ant-table-cell ant-table-cell-ellipsis'][6]"), "First Row - Sale");
    }

    protected WebElement getFirstRowCPAAccountEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1]/td[contains(@class,'ant-table-cell')][1]/span/span"), "First Row - CPA Account ID");
    }

    protected WebElement getFirstRowCPANameEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1]/td[contains(@class,'ant-table-cell')][2]/span/span"), "First Row - CPA Name");
    }

    protected WebElement getSettingTabEle() {
        return assertElementExists(By.xpath("(//div[@role='tablist'] //div[@role='tab'])[last()]"), "Setting Tab");
    }

    protected WebElement getPerformanceReportToggleEle() {
        return assertElementExists(By.xpath("//span[text()='Performance Report']/following-sibling::button"), "Setting - Performance Report Toggle");
    }

    protected WebElement getTotalItemCountEle() {
        return assertElementExists(By.xpath("//li[@class='ant-pagination-total-text']"), "CPA Search Table - Total Item Count");
    }

    protected List<WebElement> getEditIconListEle() {
        return assertElementsExists(By.xpath("//*[@data-icon='edit']"), "Edit Icon List");
    }

    protected WebElement getStatusEditIconEle() {
        return assertElementExists(By.xpath("//label[@for='status'] //span[@aria-label='edit']"), "Status - Edit Icon");
    }

    protected WebElement getNormalStatusEle() {
        return assertElementExists(By.xpath("//input[@value='NORMAL']"), "Status - Normal Radio Button");
    }

    protected WebElement getLockedStatusEle() {
        return assertElementExists(By.xpath("//input[@value='LOCKED']"), "Status - Locked Radio Button");
    }

    protected WebElement getConfirmEditStatusEle() {
        return assertElementExists(By.xpath("//div[@class='ant-modal-footer']/button[2]"), "Status - Confirm Button");
    }

    protected WebElement getCurrentStatusEle() {
        return assertElementExists(By.xpath("//label[@for='status']/parent::*/following-sibling::* //span[contains(@class,'index_word')]"), "Status - Current Value");
    }

    protected WebElement getStatisticsFilterDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='performanceForm_dateShortcutType']/parent::*/parent::*"), "Statistics Filter Dropdown Trigger");
    }

    protected WebElement getStatisticsFilterLast7DaysEle() {
        return assertElementExists(By.xpath("//div[@title='Last 7 days']"), "Statistics Filter - Last 7 Days");
    }

    protected List<WebElement> getStatisticsItemNameListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'style_performance')]/div[contains(@class,'ant-flex-vertical')]/div[contains(@class,'ant-row')] //span[contains(@class,'itemName')]"), "Statistics Item Name List");
    }

    protected List<WebElement> getStatisticsItemCountListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'style_performance')]/div[contains(@class,'ant-flex-vertical')]/div[contains(@class,'ant-row')] //span[contains(@class,'itemCount')]"), "Statistics Item Count List");
    }

    protected List<WebElement> getStatisticsLastCountListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class,'style_performance')]/div[contains(@class,'ant-flex-vertical')]/div[contains(@class,'ant-row')] //span[contains(@class,'lastCount')]"), "Statistics Last Count List");
    }

    protected List<WebElement> getStatisticsRateListEle() {
        return assertElementsExists(By.xpath("//span[(contains(@class,'lastRate') and not(contains(@class,'increRate')) and not(contains(@class,'reduceRate'))) or (contains(@class,'rateWord'))]"), "Statistics Rate List");
    }

    protected List<WebElement> getStatisticsRateArrowListEle() {
        return assertElementsExists(By.xpath("//span[contains(@class,'itemName')]/following-sibling::span"), "Statistics Rate Arrow List");
    }





    //Pass one of the three parameters will be sufficient
    public void searchCPA(String cpaAccount,String cpaName, String userID, String email){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='business_search_userId']")));
        setInputValue(getCPAAccountIDInputEle(),cpaAccount);
        setInputValue(getCPANameInputEle(),cpaName);
        setInputValue(getUserIdInputEle(),userID);

        if(!email.equals("")){
            triggerElementClickEvent_withoutMoveElement(getExpandSearchOptionsEle());
            fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='business_search_email']")));
            setInputValue(getEmailInputEle(),email);
        }

        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
    }

    public String getSalesAssigned(){
        fastwait.until(ExpectedConditions.visibilityOf(getFirstRowSaleEle()));
        return getFirstRowSaleEle().getText();
    }

    public Boolean togglePerformanceReportEntryPoint(){
        triggerElementClickEvent(getFirstRowCPAAccountEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@role='tablist'] //div[@role='tab'])[last()]")));
        triggerElementClickEvent(getSettingTabEle());

        String perfReportToggleBefore = getPerformanceReportToggleEle().getAttribute("aria-checked");
        triggerElementClickEvent(getPerformanceReportToggleEle());
        String perfReportToggleAfter = getPerformanceReportToggleEle().getAttribute("aria-checked");
        Assert.assertFalse(perfReportToggleBefore.equalsIgnoreCase(perfReportToggleAfter));

        if(perfReportToggleAfter.equalsIgnoreCase("true")){
            return true;
        }else{
            return false;
        }
    }

    public void verifyCPASearchTableAndOverviewPerformance(String cpaAccount,String cpaName, String userID, String email){
        String totalItemCountStr = getTotalItemCountEle().getText();
        totalItemCountStr = totalItemCountStr.replace("Total","").replace("items", "").trim();
        int totalItemCount = Integer.parseInt(totalItemCountStr);
        Assert.assertTrue(totalItemCount>0, "CPA Search Table has no records!");
        LogUtils.info("Verified CPA Search Table has "+totalItemCount+" records.");

        //Remove cpaName so that it's not passed to the search, to verify we can search using other parameter and the cpaName displayed is correct
        String checkCpaName = cpaName;
        cpaName = "";
        searchCPA(cpaAccount, cpaName, userID, email);

        String name = getFirstRowCPANameEle().getText();
        Assert.assertTrue(name.equals(checkCpaName));
        LogUtils.info("Verified CPA Search Table - First Row CPA Name matches searched CPA Name: "+checkCpaName);

        triggerElementClickEvent(getFirstRowCPAAccountEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@role='tablist'] //div[@role='tab'])[1]")));
        //Verify Overview page is editable
        Assert.assertTrue(getEditIconListEle().size()>0, "CPA Overview Page - Edit Icon not displayed!");

        String oriStatus = getCurrentStatusEle().getText();
        LogUtils.info("Original Status: "+oriStatus);
        triggerElementClickEvent(getStatusEditIconEle());
        triggerElementClickEvent(getLockedStatusEle());
        triggerElementClickEvent(getConfirmEditStatusEle());
        fastwait.until(ExpectedConditions.invisibilityOf(getNormalStatusEle()));
        String newStatus = getCurrentStatusEle().getText();
        LogUtils.info("New Status: "+newStatus);
        Assert.assertTrue(!oriStatus.equals(newStatus), "CPA Overview Page - Status not changed after edit!");

        triggerElementClickEvent(getStatusEditIconEle());
        triggerElementClickEvent(getNormalStatusEle());
        triggerElementClickEvent(getConfirmEditStatusEle());
        fastwait.until(ExpectedConditions.invisibilityOf(getNormalStatusEle()));

        LogUtils.info("Verified CPA Overview Page is editable.");

        triggerElementClickEvent(getStatisticsFilterDropdownEle());
        triggerElementClickEvent(getStatisticsFilterLast7DaysEle());

        List<String> itemNameList = getStatisticsItemNameListEle().stream().toList().stream().map(WebElement::getText).toList();
        List<String> itemCountList = getStatisticsItemCountListEle().stream().toList().stream().map(WebElement::getText).toList();
        List<String> lastCountList = getStatisticsLastCountListEle().stream().toList().stream().map(WebElement::getText).toList();
        List<String> rateList = getStatisticsRateListEle().stream().toList().stream().map(WebElement::getText).toList();

        for(int i=0;i<itemNameList.size();i++){
            String itemName = itemNameList.get(i);
            String todayRate = rateList.get(i);
            String todayValueStr = itemCountList.get(i).replaceAll("%","").replaceAll("[A-Za-z]", "");
            String yesterdayValueStr = lastCountList.get(i).replaceAll("%","");
            String rateArrow = getStatisticsRateArrowListEle().get(i).getAttribute("class");

//            if(rateArrow.contains("increRate")){
//                rateArrow = "increased";
//            }else if(rateArrow.contains("reduceRate")){
//                rateArrow = "reduced";
//            }

            double todayValue = Double.parseDouble(todayValueStr);
            double yesterdayValue = Double.parseDouble(yesterdayValueStr);

            if(todayValue==0 && yesterdayValue!=0){
                Assert.assertTrue((todayRate.equals("100%") && rateArrow.contains("increRate")),"Statistics Rate for "+itemName+" is incorrect. Today's value is "+todayValue+", yesterday is "+yesterdayValue+" , but actual rate is: "+todayRate);
            } else if(yesterdayValue==0) {
                Assert.assertTrue(todayRate.equals("--"),"Statistics Rate for "+itemName+" is incorrect. Today's value is "+todayValue+", yesterday is "+yesterdayValue+" , but actual rate is: "+todayRate);
            } else {
                double expectedRate = ((todayValue - yesterdayValue) / yesterdayValue) * 100;
                String expectedRateStr = String.format("%.0f", expectedRate);

                if(expectedRateStr.contains("-")){
                    Assert.assertTrue(rateArrow.contains("reduceRate"),"Statistics Rate Arrow for "+itemName+" is incorrect. Expected decrease but arrow indicates increase.");
                }else if (!expectedRateStr.contains("-")) {
                    Assert.assertTrue(rateArrow.contains("increRate"),"Statistics Rate Arrow for "+itemName+" is incorrect. Expected increase but arrow indicates decrease.");
                }
                expectedRateStr = expectedRateStr.replace("-","");
                Assert.assertTrue(todayRate.equals(expectedRateStr),"Statistics Rate for "+itemName+" is incorrect. Expected: "+expectedRateStr+" , Actual: "+todayRate);
            }
            LogUtils.info(itemNameList.get(i)+": ("+itemCountList.get(i)+"-"+lastCountList.get(i)+")/"+lastCountList.get(i)+"="+rateList.get(i)+"%");
        }



    }




}
