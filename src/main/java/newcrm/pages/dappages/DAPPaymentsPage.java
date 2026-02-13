package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LogUtils;

import java.util.List;
import java.util.Map;

public class DAPPaymentsPage extends Page {

    public DAPPaymentsPage(WebDriver driver) {
        super(driver);
    }


    protected WebElement getApplicableCommissionDollarEle() {
        return assertElementExists(By.xpath("//span[@class='statistics-card__amount--large']"), "Applicable Commission - Dollar");
    }

    protected WebElement getApplicableCommissionCurrencyEle() {
        return assertElementExists(By.xpath("//span[@class='statistics-card__amount--currency']"), "Applicable Commission - Currency");
    }

    protected WebElement getCumulativeCommissionEle() {
        return assertElementExists(By.xpath("//div[@class='statistics-card__bottom']/div[1]/div[2]"), "Cumulative Commission");
    }

    protected WebElement getPaidCommissionEle() {
        return assertElementExists(By.xpath("//div[@class='statistics-card__bottom']/div[2]/div[2]"), "Paid Commission");
    }

    protected WebElement getRequestPaymentButtonEle() {
        return assertElementExists(By.xpath("//button[@class='ht-button ht-button--primary']"), "Request Payment Button");
    }

    protected WebElement getRequestPaymentFirstCheckboxEle() {
        return assertElementExists(By.xpath("(//table[@class='ht-table__body'] //span[@class='ht-checkbox__input'])[1]"), "Request Payment - First Checkbox");
    }

    protected WebElement getRequestPaymentSubmitButtonEle() {
        return assertElementExists(By.xpath("//div[@class='apply-drawer__footer']/button"), "Request Payment - Submit Button");
    }

    protected WebElement getRequestPaymentSuccessMessageEle() {
        return assertElementExists(By.xpath("//p[@class='ht-message__content']"), "Request Payment - Success Message");
    }


    public Map<String, Double> getTotalCommissionByType(){
        String applicableCommissionDollarStr = getApplicableCommissionDollarEle().getText();
        String applicableCommissionCurrencyStr = getApplicableCommissionCurrencyEle().getText();
        String cumulativeCommissionStr = getCumulativeCommissionEle().getText().replace("$","").trim();
        String paidCommissionStr = getPaidCommissionEle().getText().replace("$","").trim();

        Double applicableCommissionDollar = Double.parseDouble(applicableCommissionDollarStr);
        Double cumulativeCommission = Double.parseDouble(cumulativeCommissionStr);
        Double paidCommission = Double.parseDouble(paidCommissionStr);

        Map<String, Double> commissionMap = Map.of(
                "applicableCommissionDollar", applicableCommissionDollar,
                "cumulativeCommission", cumulativeCommission,
                "paidCommission", paidCommission
        );

        return commissionMap;
    }

    public void requestPayment(){
        triggerClickEvent_withoutMoveElement(getRequestPaymentButtonEle());
        triggerClickEvent_withoutMoveElement(getRequestPaymentFirstCheckboxEle());
        triggerClickEvent_withoutMoveElement(getRequestPaymentSubmitButtonEle());

        fastwait.until(ExpectedConditions.visibilityOf(getRequestPaymentSuccessMessageEle()));
        LogUtils.info("Successfully requested payment.");
    }


}
