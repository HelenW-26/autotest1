package newcrm.pages.staribpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class STARIBReportPage extends IBReportPage {

    public STARIBReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getTotalRebateAmtDollarEle() {
        return assertElementExists(By.xpath("(//div[@class='total_content'] //span[not(@class='currency')])[1]"), "Total Rebate Amount - Dollar");
    }

    protected WebElement getTotalRebateAmtCentEle() {
        return assertElementExists(By.xpath("(//div[@class='total_content'] //span[not(@class='currency')])[2]"), "Total Rebate Amount - Cent");
    }

    @Override
    public List<WebElement> getProductsRebateCcyEle() {
        return assertElementsExists(By.xpath("(//div[@class='products'] //div[@class='formatNumber']/span[(@class='currency')][1])"), "Product-Level Rebate Currencies");
    }

    public List<WebElement> getProductsRebateAmtDollarsEle() {
        return assertElementsExists(By.xpath("(//div[@class='products'] //div[@class='formatNumber']/span[not(@class='currency')][1])"), "Product-Level Rebate Amount - Dollars");
    }

    public List<WebElement> getProductsRebateAmtCentsEle() {
        return assertElementsExists(By.xpath("(//div[@class='products'] //div[@class='formatNumber']/span[not(@class='currency')][1])"), "Product-Level Rebate Amount - Cents");
    }

    protected WebElement getStartDatePickerEle() {
        return assertElementExists(By.xpath("//div[@type='daterange']/input[1]"), "Rebate Report Start Date Picker");
    }

    protected WebElement getEndDatePickerEle() {
        return assertElementExists(By.xpath("//div[@type='daterange']/input[2]"), "Rebate Report End Date Picker");
    }

    @Override
    public void setDateRangeToLast365Days(){
        triggerElementClickEvent_withoutMoveElement(this.getStartDatePickerEle());
        triggerElementClickEvent_withoutMoveElement(this.getPreviousYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getFirstAvailableDayEle());
//        triggerElementClickEvent_withoutMoveElement(this.getEndDatePickerEle());
        triggerElementClickEvent_withoutMoveElement(this.getSubsequentYearArrowEle());
        triggerElementClickEvent_withoutMoveElement(this.getLastAvailableDayEle());
        triggerElementClickEvent_withoutMoveElement(this.getUpdateDateRangeButtonEle());
    }

    @Override
    public void verifyRebateValuesCurrency(){
        // Collect the rebate amounts into a list
        List<WebElement> dollarElements = this.getProductsRebateAmtDollarsEle();
//        List<WebElement> centElements = this.getProductsRebateAmtCentsEle();

        List<Double> combinedRebateAmounts = new ArrayList<>();

        for (int i = 0; i < dollarElements.size(); i++) {
            String dollarText = dollarElements.get(i).getText().trim();
//            String centText = centElements.get(i).getText().trim();

            String combinedText = dollarText;
            combinedText = combinedText.replace(",", "");
            double amount = Double.parseDouble(combinedText);
            combinedRebateAmounts.add(amount);
        }

        // Sum of all the combined rebate amounts
        double sum = combinedRebateAmounts.stream().mapToDouble(Double::doubleValue).sum();

        // Round the sum to 2 decimal places
        double roundedSum = Math.round(sum * 100.0) / 100.0;

        // Take TOTAL Rebate dollar and cents, combine them into a full amount string
        String dollarText = this.getTotalRebateAmtDollarEle().getText().trim();
        dollarText = dollarText.replace(",", "");
//        String centText = this.getTotalRebateAmtCentEle().getText().trim();

        String totalRebateText = dollarText;
        double totalRebateAmount = Double.parseDouble(totalRebateText);

        if (sum != totalRebateAmount) {
            Assert.fail("Sum of Product-Level Rebate Amounts does not equal Total Rebate Amount: "
                    + roundedSum + " != " + totalRebateAmount);
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
