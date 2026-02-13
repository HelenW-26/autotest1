package newcrm.pages.vtibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class VTIBReportPage extends IBReportPage {

    public VTIBReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getDateDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='query_content_main clearfix'] //input[@id='shortcut']"), "Rebate Report Date Shortcut Dropdown");
    }

    protected WebElement getLast30DaysEle() {
        return assertElementExists(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style, 'display: none'))] //span"), "Rebate Report Date - Last 30 Days",e -> "Last 30 days".equalsIgnoreCase(e.getText().trim()));
    }

    protected WebElement getTotalRebateAmtCcyEle() {
        return assertElementExists(By.xpath("//ul[@class='clearfix']/li[@class='special'] //div[@class='bottom']//p"), "Total Rebate Amount - Amount & Currency");
    }

    @Override
    public List<WebElement> getProductsRebateCcyEle() {
        return assertElementsExists(By.xpath("//ul[@class='clearfix']/li[not (@class='special')] //div[@class='bottom']/div/span"), "Product-Level Rebate Currencies");
    }

    @Override
    public List<WebElement> getProductsRebateAmtsEle() {
        return driver.findElements(By.xpath("//ul[@class='clearfix']/li[not(@class='special')]//div[@class='bottom']//p"));
    }

    @Override
    protected WebElement getUpdateDateRangeButtonEle() {
        return assertElementExists(By.xpath("//div[@class='query_content_main clearfix'] //div[@class='query_content_main_right']"), "Update Date Range Button");
    }

    public void setDateRangeToLast30Days(){
        triggerElementClickEvent_withoutMoveElement(this.getDateDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getLast30DaysEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    @Override
    public void verifyRebateValuesCurrency(){

        String totalRebateAmountCurrency = getTotalRebateAmtCcyEle().getText().trim();
        String totalRebateCurrency = totalRebateAmountCurrency.substring(totalRebateAmountCurrency.length()-3);
        String totalRebateAmount = totalRebateAmountCurrency.replaceAll("[^\\d.]", "").replaceAll(",", "");

        // Collect the rebate amounts into a list
        List<Double> rebateAmounts = new ArrayList<>();

        for (WebElement element : getProductsRebateAmtsEle()) {
            String text = element.getText();
            // Remove dollar sign, comma and trim whitespace
            String cleaned = text.replace("$", "").replace(",", "").trim();
            double amount = Double.parseDouble(cleaned);
            rebateAmounts.add(amount);
        }

        // Sum the rebate amounts
        double sum = rebateAmounts.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Round the sum to 2 decimal places
        double roundedSum = Math.round(sum * 100.0) / 100.0;

        if(roundedSum != Double.parseDouble(totalRebateAmount)){
            Assert.fail("Sum of Product-Level Rebate Amounts does not equal Total Rebate Amount"
                    + roundedSum + "!=" + Double.parseDouble(totalRebateAmount));
        }

        boolean allMatch = true;

        for (WebElement element : this.getProductsRebateCcyEle()) {
            if (!element.getText().equals(totalRebateCurrency)) {
                allMatch = false;
                break;
            }
        }
        if (!allMatch) {
            Assert.fail("Currency mismatch found. Expected: " + totalRebateCurrency);
        }
        GlobalMethods.printDebugInfo("Rebate Report Amounts and Currencies Verified");
    }

    @Override
    public void verifyAccountTabValues(){
        String name = this.getAccountTabFirstRebateRecordNameEle().getText();
        if (!name.isEmpty()){
            GlobalMethods.printDebugInfo("Account Tab: First Rebate Record: " + name);
        } else {
            Assert.fail("Account Tab: First Rebate Record is empty");
        }
    }
}
