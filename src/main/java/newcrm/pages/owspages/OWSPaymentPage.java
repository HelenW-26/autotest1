package newcrm.pages.owspages;

import cn.hutool.log.Log;
import newcrm.pages.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class OWSPaymentPage extends Page {

    public OWSPaymentPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getCPANameInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_cpaUserName']"), "CPA Name Input");
    }

    protected WebElement getClearCPANameButtonEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_cpaUserName']/following-sibling::*[1] //span[not(contains(@class,'hidden'))]/span[contains(@class,'circle')]/*/*"), "CPA Name Input - Clear Button");
    }

    protected WebElement getStatusDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_applyStatus']"), "Status Dropdown Trigger");
    }

    protected WebElement getPendingStatusOptionEle() {
        return assertElementExists(By.xpath("//div[text()='Pending']"), "Status Dropdown - Pending Option");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//div[@class='businessTable_search'] //button[contains(@class,'ant-btn-color-primary')]"), "Search Button");
    }

    protected WebElement getAuditButtonEle() {
        return assertElementExists(By.xpath("//tr[contains(@class,'ant-table-row')][1] //*[@*[contains(., 'icon-Audit1')]]/parent::*"), "First Result Audit Button");
    }

    protected WebElement getApprovalReasonInputEle() {
        return assertElementExists(By.xpath("(//textarea[contains(@class,'ant-input')])[1]"), "Approval Panel - Reason Input");
    }

    protected WebElement getApprovalDecisionDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='nodeStatus']"), "Approval Panel - Approval Decision Dropdown Trigger");
    }

    protected WebElement getApprovalDecisionApprovedEle() {
        return assertElementExists(By.xpath("//div[@class='rc-virtual-list'] //div[@title='Approved']"), "Approval Panel - Approval Decision - Approved Option");
    }

    protected WebElement getApprovalDecisionRejectedEle() {
        return assertElementExists(By.xpath("//div[@class='rc-virtual-list'] //div[@title='Rejected']"), "Approval Panel - Approval Decision - Rejected Option");
    }

    protected WebElement getApprovalConfirmEle() {
        return assertElementExists(By.xpath("//div[@class='ant-drawer-footer'] //button[contains(@class,'ant-btn-primary')]"), "Approval Panel - Approval Decision - Confirm Button");
    }

    public void searchPayment_Pending(String cpaName){

        if(!driver.findElements(By.xpath("//input[@id='business_search_cpaUserName']/following-sibling::*[1] //span[not(contains(@class,'hidden'))]/span[contains(@class,'circle')]/*/*")).isEmpty()){
            triggerElementClickEvent(getClearCPANameButtonEle());
        }
        setInputValue(getCPANameInputEle(),cpaName);
        if(driver.findElements(By.xpath("//span[@title='Pending']")).isEmpty()){
            triggerElementClickEvent(getStatusDropdownEle());
            triggerElementClickEvent(getPendingStatusOptionEle());
        }
        triggerElementClickEvent(getSearchButtonEle());
    }

    public void auditPayment_Approve(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[contains(@class,'ant-table-row')][1] //div[@class='ant-space-item']/button")));
        triggerElementClickEvent(getAuditButtonEle());
        setInputValue(getApprovalReasonInputEle(),"Test Auto - Approved");

        triggerElementClickEvent(getApprovalDecisionDropdownEle());
        triggerElementClickEvent(getApprovalDecisionApprovedEle());
        triggerElementClickEvent(getApprovalConfirmEle());

        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchButtonEle()));
        LogUtils.info("Payment audit approved");
    }

    public void auditPayment_Reject(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[contains(@class,'ant-table-row')][1] //div[@class='ant-space-item']/button")));
        triggerElementClickEvent(getAuditButtonEle());
        setInputValue(getApprovalReasonInputEle(),"Test Auto - Rejected");

        triggerElementClickEvent(getApprovalDecisionDropdownEle());
        //Unable to click Rejected option directly, so using keyboard events to select it
        //        triggerElementClickEvent(getApprovalDecisionRejectedEle());
        getApprovalDecisionDropdownEle().sendKeys(Keys.ARROW_DOWN);
        getApprovalDecisionDropdownEle().sendKeys(Keys.ENTER);

        triggerElementClickEvent(getApprovalConfirmEle());

        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchButtonEle()));
        LogUtils.info("Payment audit rejected");
    }



}
