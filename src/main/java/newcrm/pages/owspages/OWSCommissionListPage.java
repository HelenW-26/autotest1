package newcrm.pages.owspages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class OWSCommissionListPage extends Page {

    public OWSCommissionListPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAdjustmentButtonEle() {
        return assertElementExists(By.xpath("//div[@class='businessTable_operation'] //button"), "Adjustment Button");
    }

    protected WebElement getAdjustmentCPAAccountIDInputEle() {
        return assertElementExists(By.xpath("//input[@id='cpaAccount']"), "Adjustment Panel - CPA Account ID Input");
    }

    protected WebElement getAdjustmentCPAAccountIDFirstOptionEle() {
        return assertElementExists(By.xpath("(//div[@class='rc-virtual-list'] //div[@class='ant-select-item-option-content'])[1]"), "Adjustment Panel - CPA Account ID - First Option");
    }

    protected WebElement getAdjustmentCommissionTimeEle() {
        return assertElementExists(By.xpath("//input[@id='adjustTime']"), "Adjustment Panel - Commission Time Input");
    }

    protected WebElement getAdjustmentTodayEle() {
        return assertElementExists(By.xpath("//li[@class='ant-picker-now']"), "Adjustment Panel - Commission Time - Today");
    }

    protected WebElement getAdjustmentAmountEle() {
        return assertElementExists(By.xpath("//input[@id='adjustAmount']"), "Adjustment Panel - Adjustment Amount Input");
    }

    protected WebElement getAdjustmentCommissionTypeDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='settlementType']"), "Adjustment Panel - Commission Type Dropdown Trigger");
    }

    protected WebElement getAdjustmentOtherCommissionTypeEle() {
        return assertElementExists(By.xpath("//div[@title='Other']"), "Adjustment Panel - Commission Type Option - Other");
    }

    protected WebElement getAdjustmentRemarkEle() {
        return assertElementExists(By.xpath("//textarea[@id='remark']"), "Adjustment Panel - Remark Input");
    }

    protected WebElement getAdjustmentConfirmEle() {
        return assertElementExists(By.xpath("//div[@class='ant-drawer-footer'] //button[contains(@class,'ant-btn-color-primary')]"), "Adjustment Panel - Confirm Button");
    }


    public void commissionAdjustment(String cpaName){
        triggerElementClickEvent_withoutMoveElement(getAdjustmentButtonEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='cpaAccount']")));

        setInputValue(getAdjustmentCPAAccountIDInputEle(),cpaName);
        triggerClickEvent(getAdjustmentCPAAccountIDFirstOptionEle());

        triggerClickEvent(getAdjustmentCommissionTimeEle());
        triggerElementClickEvent(getAdjustmentTodayEle());

        triggerElementClickEvent(getAdjustmentCommissionTypeDropdownEle());
        triggerElementClickEvent(getAdjustmentOtherCommissionTypeEle());

        setInputValue(getAdjustmentAmountEle(),"1");

        setInputValue(getAdjustmentRemarkEle(),"Test Auto Adjustment (testPaymentAudit)");

        triggerClickEvent(getAdjustmentConfirmEle());
    }




}
