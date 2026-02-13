package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;

public class DAPCommissionReportPage extends Page {

    public DAPCommissionReportPage(WebDriver driver) {
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

    protected List<WebElement> getCommissionListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[4]/div/span"), "Commission List");
    }

    protected List<WebElement> getStatusListEle() {
        return assertElementsExists(By.xpath("//tr[@class='ht-table__row']/td[6]/div/span/span"), "Status List");
    }

    protected List<WebElement> getPageListEle() {
        return assertElementsExists(By.xpath("//ul[@class='ht-pager']/li"), "Page List");
    }

    public void filterCommissionReport_Today(){
        //Filter by Date - From 1 years ago to today
        getStartDateInputEle().click();
        triggerElementClickEvent(getTodayEle());
        triggerElementClickEvent(getUpdateFilterEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='ht-pagination__sizes is-first']")));
        triggerElementClickEvent(getPageSizeDropdownEle());
        triggerElementClickEvent(get100PerPageDropdownOptionEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='table-body-loading-overlay']")));
    }


    public void filterCommissionReport_OneYear(){
        //Filter by Date - From 1 years ago to today
        getStartDateInputEle().click();
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='Previous Year']")));
        fastwait.until(ExpectedConditions.elementToBeClickable(getPreviousYearArrowEle()));
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

    public List<Double> getCommissionList(){
        return getCommissionListEle().stream()
                .map(WebElement::getText)
                .map(s -> s.replace("$", "").trim())
                .map(Double::parseDouble)
                .toList();

    }

    public List<String> getStatusList(){
        return getStatusListEle().stream().map(WebElement::getText).toList();
    }

    public Map<String, Double> getStatusCommissionMap(){
        List<String> statusList = new ArrayList<>();
        List<Double> commissionList = new ArrayList<>();

        if(getPageListEle().size()>1){
            for (int page = 1; page <= getPageListEle().size(); page++) {
                triggerElementClickEvent(getPageListEle().get(page - 1));

                //Store statuses into list
                statusList.addAll(getStatusListEle().stream().map(WebElement::getText).collect(java.util.stream.Collectors.toList()));

                //Store commissions into list
                commissionList.addAll(getCommissionListEle().stream()
                        .map(WebElement::getText)
                        .map(s -> s.replace("$", "").trim())
                        .map(Double::parseDouble)
                        .collect(java.util.stream.Collectors.toList())) ;
            }
        }

        Map<String, Double> reportMap = new LinkedHashMap<>();

        int size = Math.min(statusList.size(), commissionList.size());
        for (int i = 0; i < size; i++) {
            String status = statusList.get(i);
            Double commission = commissionList.get(i);

//            reportMap.put(status, commission);
            reportMap.merge(status, commission, Double::sum);
        }

        return reportMap;
    }

}
