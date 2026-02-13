package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DAPMultiCommissionPage extends Page {

    public DAPMultiCommissionPage(WebDriver driver) {
        super(driver);
    }



    protected WebElement getPageSizeDropdownEle() {
        return assertElementExists(By.xpath("//span[@class='ht-pagination__sizes is-first']"), "Page Size Filter Dropdown");
    }

    protected WebElement get100PerPageDropdownOptionEle() {
        return assertElementExists(By.xpath("//div[@class='ht-select-dropdown'] //li[4]"), "100/Page Dropdown Option");
    }

    protected WebElement getStartDateInputEle() {
        return assertElementExists(By.xpath("(//div[@class='date-range-picker'] //input)[1]"), "Start Date Input");
    }

    protected WebElement getEndDateInputEle() {
        return assertElementExists(By.xpath("(//div[@class='date-range-picker'] //input)[2]"), "End Date Input");
    }

    protected WebElement getTodayEle() {
        return assertElementExists(By.xpath("//button[text()='Today']"), "Today Button");
    }

    protected WebElement getPreviousYearArrowEle() {
        return assertElementExists(By.xpath("//button[@aria-label='Previous Year']"), "Previous Year Arrow");
    }

    protected WebElement getNextYearArrowEle() {
        return assertElementExists(By.xpath("//button[@aria-label='Next Year']"), "Next Year Arrow");
    }

    protected WebElement getDateFilterFirstAvailableDateEle() {
        return assertElementExists(By.xpath("(//td[contains(@class,'available')])[1]"), "Date Filter - First Available Date");
    }

    protected WebElement getDateFilterLastAvailableDateEle() {
        return assertElementExists(By.xpath("(//td[contains(@class,'available')])[last()]"), "Date Filter - First Available Date");
    }

    protected WebElement getUpdateFilterEle() {
        return assertElementExists(By.xpath("//button[contains(@class,'apply-button')]"), "Update Filter");
    }

    protected List<WebElement> getSubCPAAccountIDListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[1]/div/span"), "Sub CPA Account ID List");
    }

    protected List<WebElement> getSubCPANameListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[2]/div/span"), "Sub CPA Name List");
    }

    protected List<WebElement> getQFTDListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[3]/div/span"), "QFTD List");
    }

    protected List<WebElement> getProjectedCommissionListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[4]/div/span"), "ProjectedCommission List");
    }


    public Map<String, List<String>> verifyMultiCommissionPage(){
        filterCommissionReport_OneYear();

        List<String> subCPAAccountIDList  = getSubCPAAccountIDListEle().stream().map(WebElement::getText).toList();
        List<String> subCPANameList  = getSubCPANameListEle().stream().map(WebElement::getText).toList();
        List<String> qftdList  = getQFTDListEle().stream().map(WebElement::getText).toList();

        List<String> projectedCommissionList  = getProjectedCommissionListEle().stream().map(WebElement::getText).toList();
        List<Double> projectedCommissionInts = projectedCommissionList.stream().map(String::trim).map(Double::parseDouble).toList();
        double totalProjectedCommission = projectedCommissionInts
                        .stream()
                        .mapToDouble(Double::doubleValue)
                        .sum();

        Assert.assertTrue(subCPAAccountIDList.size()>=1, "Sub CPA Account ID List is empty!");
        LogUtils.info("Verified Sub CPA Account ID List, total records: " + subCPAAccountIDList.size());

        Assert.assertTrue(subCPANameList.size()>=1, "Sub CPA Name List is empty!");
        LogUtils.info("Verified Sub CPA Account ID List, total records: " + subCPANameList.size());

        Assert.assertTrue(qftdList.size()>=1, "QFTD List is empty!");
        LogUtils.info("Verified Sub CPA Account ID List, total records: " + qftdList.size());

        Assert.assertTrue(totalProjectedCommission>0, "Total Projected Commission = 0");
        LogUtils.info("Verified Project Commission List, total Projected Commission = " + totalProjectedCommission);

        Map<String, List<String>> subCPACommissionData = new HashMap<>();

        for (int acc = 1; acc <= subCPAAccountIDList.size(); acc++) {
            subCPACommissionData.put(subCPANameList.get(acc-1), List.of((subCPAAccountIDList.get(acc-1)),(projectedCommissionList.get(acc-1))));
        }

//        Map<String, List<Double>> subCPADescendingCommissionData =
//                subCPACommissionData.entrySet()
//                        .stream()
//                        .sorted(
//                                Map.Entry.<Double, List<String>>comparingByKey(Comparator.reverseOrder())
//                                        .thenComparing(e -> e.getValue().get(1))
//                        )
//                        .collect(Collectors.toMap(
//                                Map.Entry::getKey,
//                                Map.Entry::getValue,
//                                (oldV, newV) -> oldV,
//                                LinkedHashMap::new
//                        ));

        return subCPACommissionData;
    }

    public void filterCommissionReport_OneYear(){
        //Filter by Date - From 1 years ago to today
        getStartDateInputEle().click();
        triggerElementClickEvent(getPreviousYearArrowEle());
        triggerElementClickEvent(getDateFilterFirstAvailableDateEle());
        triggerElementClickEvent(getNextYearArrowEle());
        triggerElementClickEvent(getDateFilterLastAvailableDateEle());
        triggerElementClickEvent(getUpdateFilterEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='ht-pagination__sizes is-first']")));
        triggerElementClickEvent(getPageSizeDropdownEle());
        triggerElementClickEvent(get100PerPageDropdownOptionEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='table-body-loading-overlay']")));
        waitLoading();
    }


}
