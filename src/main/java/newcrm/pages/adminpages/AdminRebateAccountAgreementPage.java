package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AdminRebateAccountAgreementPage extends Page {

    public AdminRebateAccountAgreementPage(WebDriver driver)
    {
        super(driver);
    }
    protected WebElement getFirstAgreementTabEle() {
        return assertElementExists(By.xpath("//div[@data-node-key='first']"), "First Agreement Tab");
    }

    protected WebElement getUpdatedAgreementTabEle() {
        return assertElementExists(By.xpath("//div[@data-node-key='updated']"), "Updated Agreement Tab");
    }

    protected WebElement getRebateAccountSearchInputEle() {
        return assertElementExists(By.xpath("//div[contains(@id,'rc-tabs-0') and contains(@class,'active')] //input[@id='business_search_mt4Account']"), "Rebate Account Search Input");
    }

    protected WebElement getUserIDSearchInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_userId']"), "User ID Search Input");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//div[contains(@id,'rc-tabs-0') and contains(@class,'active')] //button[contains(@class,'ant-btn-primary')]"), "Search Button");
    }

    protected WebElement getUpdateButtonEle(String ibAcc) {
        return assertElementExists(By.xpath("//div[@role='tabpanel' and not(contains(@class,'hidden'))]/div/div/div/div/div/div/div/div/div/table//td[@class='ant-table-cell' and text()='"+ibAcc+"']/following-sibling::td //span[@aria-label='to-top']"), "Update Agreement Button");
    }

    protected WebElement getChooseTemplatesEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-modal-body'] //button[contains(@class,'ant-btn-default')])[1]"), "Choose Templates Button");
    }

    protected WebElement getUploadFileEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-modal-body'] //button[contains(@class,'ant-btn-default')])[2]"), "Upload File Button");
    }

    protected WebElement getUploadFileInputEle() {
        return assertElementExists(By.xpath("//input[@id='fileUrl']"), "File Upload Input");
    }

    protected WebElement getSubmitFileUploadEle() {
        return assertElementExists(By.xpath("//div[@class='ant-modal-footer']/button[contains(@class,'ant-btn-primary')]"), "File Upload - Submit Button");
    }

    protected WebElement getOKFileUploadEle() {
        return assertElementExists(By.xpath("//div[@class='ant-modal-confirm-btns']/button[contains(@class,'ant-btn-primary')]"), "File Upload - OK Button");
    }

    protected WebElement getSearchTemplateEle() {
        return assertElementExists(By.xpath("//input[@id='templateId']"), "Search Template Input");
    }

    protected List<WebElement> getTemplateListEle() {
        return assertElementsExists(By.xpath("//div[@id='templateId_list']/following-sibling::div //div[contains(@class,'ant-select-item ant-select-item-option')]"), "Template Options List");
    }

    protected WebElement getGoToInputButtonEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'pdfViewerContainer')] //button[contains(@class,'ant-btn-primary')]"), "Go To Input Button");
    }

    protected List<WebElement> getTemplateInputsEle() {
        return assertElementsExists(By.xpath("//input[contains(@class,'ant-input-borderless')]"), "Agreement Template Input Fields");
    }

    protected WebElement getSaveAndUpdateToIBButtonEle() {
        return assertElementExists(By.xpath("//button[@type='submit']"), "Save and Update to IB Button");
    }

    protected WebElement getSaveAndUpdateToIBLoadingEle() {
        return assertElementExists(By.xpath("//button[@type='submit' and contains(@class,'ant-btn-loading')]"), "Save and Update to IB Loading");
    }

    protected WebElement getGeneratePDFFailureEle() {
        return assertElementExists(By.xpath("//div[@class='ant-message-notice ant-message-notice-error']"), "'The failure to generate a PDF' Message");
    }

    protected WebElement getCloseUploadAgreementDialogEle() {
        return assertElementExists(By.xpath("(//span[contains(@class,'close-icon')])[last()]"), "Close Upload Agreement Dialog");
    }

    protected WebElement getSuccessMessageEle() {
        return assertElementExists(By.xpath("//div[@class='ant-message-notice ant-message-notice-success']"), "Success Message");
    }

    public void updateRebateAccountAgreement(String ibAcc) throws InterruptedException {
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-node-key='updated']")));
        triggerClickEvent_withoutMoveElement(getUpdatedAgreementTabEle());
        setInputValue(getRebateAccountSearchInputEle(),ibAcc);
        triggerClickEvent_withoutMoveElement(getSearchButtonEle());
        waitLoading();

        triggerClickEvent_withoutMoveElement(getUpdateButtonEle(ibAcc));

        Integer templateAttempt = 0;
        Boolean invalidTemplate = true;
        while (invalidTemplate == true && templateAttempt < 5) {
            templateAttempt = templateAttempt + 1;
            invalidTemplate = updateAgreementTemplate_UploadNewTemplate(templateAttempt);
        }
    }

    public Boolean updateAgreementTemplate_ChooseTemplate(Integer templateAttempt) throws InterruptedException {
        fastwait.until(ExpectedConditions.visibilityOf(getChooseTemplatesEle()));
        triggerClickEvent_withoutMoveElement(getChooseTemplatesEle());
        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchTemplateEle()));
        triggerElementClickEvent_withoutMoveElement(getSearchTemplateEle());
        fastwait.until(ExpectedConditions.visibilityOf(getTemplateListEle().get(0)));
        String templateName = selectRandomDropDownOption_ElementClickEvent(getTemplateListEle());
        if(!driver.findElements(By.xpath("//div[contains(@class,'pdfViewerContainer')] //button[contains(@class,'ant-btn-primary')]")).isEmpty()) {

            for (WebElement input : getTemplateInputsEle()) {
                String randomString = GlobalMethods.getRandomString(8);
                input.clear();
                input.sendKeys(randomString);

            }
        }
        Thread.sleep(5000);
        triggerElementClickEvent(getSaveAndUpdateToIBButtonEle());
        fastwait.until(d -> d.findElements(By.xpath("//button[@type='submit' and contains(@class,'ant-btn-loading')]")).isEmpty());
        Thread.sleep(1000);
        //Check if generate PDF failed
        if(!driver.findElements(By.xpath("//div[@class='ant-message-notice ant-message-notice-error']")).isEmpty()){
            LogUtils.info("Attempt "+templateAttempt+" - INVALID Rebate Agreement Template: " +templateName);
            triggerElementClickEvent_withoutMoveElement(getCloseUploadAgreementDialogEle());
            return true;
        } else {
            fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-node-key='updated']")));
            LogUtils.info("Updated Rebate Agreement Template to: " +templateName);
            return false;
        }

    }

    public Boolean updateAgreementTemplate_UploadNewTemplate(Integer templateAttempt) throws InterruptedException {
        fastwait.until(ExpectedConditions.visibilityOf(getUploadFileEle()));
        triggerClickEvent_withoutMoveElement(getUploadFileEle());

        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileAgreement = Paths.get(parent.toString(), "Agreement Test Template.pdf").toString();


        getUploadFileInputEle().sendKeys(Paths.get(Utils.workingDir, fileAgreement).toString());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ant-upload-list-item ant-upload-list-item-done']")));

        triggerElementClickEvent_withoutMoveElement(getSubmitFileUploadEle());
        triggerElementClickEvent_withoutMoveElement(getOKFileUploadEle());

        LogUtils.info("Uploaded New Rebate Agreement Template");

        return false;
    }


}
