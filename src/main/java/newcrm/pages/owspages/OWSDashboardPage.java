package newcrm.pages.owspages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;

public class OWSDashboardPage extends Page {

    public OWSDashboardPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getTaskDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@aria-controls,'task-popup')]"), "Task Dropdown");
    }

    protected WebElement getAccountRecordsDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@aria-controls,'task/records-popup')]"), "Task - Account Records Dropdown");
    }

    protected WebElement getKYCRecordsEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'task/records/clientsRecords')]"), "Task - Records - KYC Records");
    }

    protected WebElement getAdditionalRecordsEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'task/records/additionalAccountRecords')]"), "Task - Records - Additional Records");
    }

    protected WebElement getPartnerRecordsDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@aria-controls,'task/partnerRecords-popup')]"), "Task - Partner Records Dropdown");
    }

    protected WebElement getPartnerRecordsPaymentEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'task/partnerRecords/paymentRecord')]"), "Task - Partner Records  - Payment");
    }

    protected WebElement getPartnerManagementDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@aria-controls,'partnerManagement-popup')]"), "Partner Management Dropdown");
    }

    protected WebElement getConfigurationDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@aria-controls,'partnerManagement/configuration-popup') or contains(@aria-controls,'partnerManagement/settings-popup')]"), "Partner Management - Configuration Dropdown");
    }

    protected WebElement getCPAEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'partnerManagement/cpaManagement/list')]"), "Partner Management - CPA");
    }

    protected WebElement getCPAAllocationEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'partnerManagement/configuration/cpaAllocation')]"), "Partner Management - Configuration - CPA Allocation");
    }

    protected WebElement getCommissionEle() {
        return assertElementExists(By.xpath("//div[contains(@data-menu-id,'partnerManagement/commission')]"), "Partner Management - Commission");
    }

    protected WebElement getCommissionListEle() {
        return assertElementExists(By.xpath("//li[contains(@data-menu-id,'partnerManagement/commission/commissionManagement')]"), "Partner Management - Commission - Commission List");
    }

    public void navigateToKYCRecords(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'task-popup')]")));
        triggerElementClickEvent_withoutMoveElement(getTaskDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getAccountRecordsDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getKYCRecordsEle());
        LogUtils.info("Navigated to KYC Records page.");
    }

    public void navigateToAdditionalRecords(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'task-popup')]")));
        triggerElementClickEvent_withoutMoveElement(getTaskDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getAccountRecordsDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getAdditionalRecordsEle());
        LogUtils.info("Navigated to Additional Records page.");
    }

    public void navigateToPayment(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'task-popup')]")));
        if(driver.findElements(By.xpath("//div[contains(@aria-controls,'task/partnerRecords-popup')]")).isEmpty()){
            triggerElementClickEvent_withoutMoveElement(getTaskDropdownEle());
        }
        if(driver.findElements(By.xpath("//li[contains(@data-menu-id,'task/partnerRecords/paymentRecord')]")).isEmpty()){
            triggerElementClickEvent_withoutMoveElement(getPartnerRecordsDropdownEle());
        }
        triggerElementClickEvent_withoutMoveElement(getPartnerRecordsPaymentEle());
        LogUtils.info("Navigated to Partner Records - Payment page.");
    }

    public void navigateToCPA(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'partnerManagement-popup')]")));
        triggerElementClickEvent_withoutMoveElement(getPartnerManagementDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getCPAEle());
        LogUtils.info("Navigated to CPA page.");
    }

    public void navigateToCPAAllocation(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'partnerManagement-popup')]")));
        if(!getConfigurationDropdownEle().isDisplayed()){
            triggerElementClickEvent_withoutMoveElement(getPartnerManagementDropdownEle());
        }
        triggerElementClickEvent_withoutMoveElement(getConfigurationDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getCPAAllocationEle());
        LogUtils.info("Navigated to CPA Allocation page.");
    }

    public void navigateToCommissionList(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@aria-controls,'partnerManagement-popup')]")));
        triggerElementClickEvent_withoutMoveElement(getPartnerManagementDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getCommissionEle());
        triggerElementClickEvent_withoutMoveElement(getCommissionListEle());
        LogUtils.info("Navigated to Commission List page.");
    }



}
