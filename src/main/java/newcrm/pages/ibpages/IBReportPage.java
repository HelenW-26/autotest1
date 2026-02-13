package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.clientpages.MenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IBReportPage extends Page {

    public IBReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getTotalRebateCcyEle() {
        return assertElementExists(By.xpath("//div[@class='total_content'] //span[@class='currency']"), "Total Rebate Currency");
    }

    protected WebElement getTotalRebateAmtEle() {
        return assertElementExists(By.xpath("//div[@class='total_content'] //span[not(@class='currency')]"), "Total Rebate Amount");
    }

    public List<WebElement> getProductsRebateCcyEle() {
        return assertElementsExists(By.xpath("//div[@class='products'] //div[@class='formatNumber']/span[(@class='currency')]"), "Product-Level Rebate Currencies");
    }

    public List<WebElement> getProductsRebateAmtsEle() {
        return assertElementsExists(By.xpath("//div[@class='products'] //div[@class='formatNumber']/span[not(@class='currency')]"), "Product-Level Rebate Amounts");
    }

    protected WebElement getDateTabFirstRebateRecordDateEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div"), "Date Tab: First Rebate Record Date");
    }

    protected WebElement getDateTabFirstRebateRecordNotionalValueAmtEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[2]/div/span[1]"), "Date Tab: First Rebate Record Notional Value Amount");
    }

    protected WebElement getDateTabFirstRebateRecordNotionalValueCcyEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[2]/div/span[2]"), "Date Tab: First Rebate Record Notional Value Currency");
    }

    protected WebElement getDateTabFirstRebateRecordTotalRebateAmtEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[3]/div/span[1]"), "Date Tab: First Rebate Record Total Rebate Amount");
    }

    protected WebElement getDateTabFirstRebateRecordTotalRebateCcyEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[3]/div/span[2]"), "Date Tab: First Rebate Record Total Rebate Currency");
    }

    protected WebElement getAccountTabFirstRebateRecordNameEle() {
        return assertElementExists(By.xpath("//tr[@class='el-table__row'][1]/td[1]/div"), "Date Tab: First Rebate Record Name");
    }

    protected WebElement getDatePickerEle() {
        return assertElementExists(By.xpath("//div[@id='rebate_report'] //div[contains(@class, 'date-picker')]"), "Rebate Report Date Picker");
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
        return assertElementExists(By.xpath("//div[@id='rebate_report'] //button[@data-testid='loading-button']"), "Update Date Range Button");
    }

    protected WebElement getDateTabEle() {
        return assertElementExists(By.xpath("//div[@class='ht-switcher']/section/div[1]"), "Date Tab");
    }

    protected WebElement getAccountTabEle() {
        return assertElementExists(By.xpath("//div[@class='ht-switcher']/section/div[2]"), "Account Tab");
    }

    public void setDateRangeToLast365Days(){
        triggerElementClickEvent_withoutMoveElement(this.getDatePickerEle());
        triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    public void verifyRebateValuesCurrency(){
        // Collect the rebate amounts into a list
        List<Double> rebateAmounts = this.getProductsRebateAmtsEle()
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .map(text -> text.replaceAll("[^\\d.]", ""))
                .map(Double::parseDouble)
                .collect(Collectors.toList());

        // Sum the rebate amounts
        double sum = rebateAmounts.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Round the sum to 2 decimal places
        double roundedSum = Math.round(sum * 100.0) / 100.0;

        String totalRebateAmount = this.getTotalRebateAmtEle().getText().replaceAll("[^\\d.]", "").replaceAll(",", "");

        if(roundedSum != Double.parseDouble(totalRebateAmount)){
            Assert.fail("Sum of Product-Level Rebate Amounts does not equal Total Rebate Amount"
                    + roundedSum + "!=" + Double.parseDouble(totalRebateAmount));
        }

        String expectedCurrency = getTotalRebateCcyEle().getText();
        boolean allMatch = true;

        for (WebElement element : this.getProductsRebateCcyEle()) {
            if (!element.getText().equals(expectedCurrency)) {
                allMatch = false;
                break;
            }
        }

        if (!allMatch) {
            Assert.fail("Currency mismatch found. Expected: " + expectedCurrency);
        }

        GlobalMethods.printDebugInfo("Rebate Report Amounts and Currencies Verified");
    }

    public void verifyDateTabValues(){
        triggerElementClickEvent_withoutMoveElement(this.getDateTabEle());
        waitLoading();

        String date = this.getDateTabFirstRebateRecordDateEle().getText();
        if (date.matches(("^\\d{2}/\\d{2}/\\d{4}$"))){
            GlobalMethods.printDebugInfo("Date Tab: First Rebate Record Date format is correct");
        } else {
            Assert.fail("Date Tab: First Rebate Record Date format is incorrect");
        }
    }

    public void verifyAccountTabValues(){
        triggerElementClickEvent_withoutMoveElement(this.getAccountTabEle());
        waitLoading();

        String name = this.getAccountTabFirstRebateRecordNameEle().getText();
        if (!name.isEmpty()){
            GlobalMethods.printDebugInfo("Account Tab: First Rebate Record: " + name);
        } else {
            Assert.fail("Account Tab: First Rebate Record is empty");
        }
    }



}
