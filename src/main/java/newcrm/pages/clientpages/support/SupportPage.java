package newcrm.pages.clientpages.support;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;

public class SupportPage extends Page {

    public SupportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSupportTicketContentEle() {
        return assertVisibleElementExists(By.cssSelector("div#support div.my_ticket"), "Support Ticket Content");
    }

    protected WebElement getCreateBtnEle() {
        return assertElementExists(By.cssSelector("button[data-testid='button']"), "Create New Ticket button", getSupportTicketContentEle());
    }

    protected WebElement getSubmitBtnEle() {
        return assertElementExists(By.cssSelector("div.ticket_detail_footer button[data-testid='button']"), "Submit New Ticket Request button", e -> e.getText().trim().toLowerCase().contains("Create New Ticket".toLowerCase()), getCreateTicketContentEle());
    }

    protected WebElement getCreateTicketDialogEle() {
        return assertElementExists(By.cssSelector("div.ht-dialog:not([style*='display: none']) > div.ticket_dialog"), "Create Support Ticket Dialog");
    }

    protected WebElement getCreateTicketContentEle() {
        return assertElementExists(By.cssSelector("div#ticket"), "Create Ticket Dialog Content", getCreateTicketDialogEle());
    }

    protected WebElement getTicketSubjectEle() {
        return assertElementExists(By.cssSelector("div.new_subject input[type='text']"), "Support Ticket Subject", getCreateTicketContentEle());
    }

    protected WebElement getTicketContentEle() {
        return assertElementExists(By.cssSelector("div.comment div.text-editor p"), "Support Ticket Content", getCreateTicketContentEle());
    }

    protected WebElement getTicketAttachmentEle() {
        return assertElementExists(By.cssSelector("div.comment div.upload_attachment input[type='file']"), "Support Ticket File Attachment", getCreateTicketContentEle());
    }

    protected WebElement getTicketAttachmentListEle() {
        return assertVisibleElementExists(By.cssSelector("div.comment div.upload_attachment ul.el-upload-list li"), "Support Ticket File Attachment");
    }

    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message");
    }

    public void waitLoadingSupportTicketContent() {
        getSupportTicketContentEle();
    }

    public void waitLoadingCreateTicketDialog() {
        getCreateTicketDialogEle();
        getCreateTicketContentEle();
    }

    public void clickCreateBtn() {
        getCreateBtnEle().click();
        LogUtils.info("Click Create New Ticket button");
    }

    public void clickSubmitBtn() {
        triggerElementClickEvent_withoutMoveElement(getSubmitBtnEle());
        LogUtils.info("Click Submit New Ticket Request button");

        // Check for error
        checkAlertMsg("Create Support Ticket");
    }

    public void setTicketSubject(String ticketSubject) {
        setInputValue(getTicketSubjectEle(), ticketSubject);
        LogUtils.info("Set Support Ticket Subject: " + ticketSubject);
    }

    public void setTicketContent(String ticketContent) {
        setInputValue(getTicketContentEle(), ticketContent);
        LogUtils.info("Set Support Ticket Content: " + ticketContent);
    }

    public void uploadAttachment(String fileAttachment) {
        // Upload attachment
        WebElement ele = getTicketAttachmentEle();
        ele.sendKeys(fileAttachment);
        LogUtils.info("Upload Support Ticket File Attachment: " + fileAttachment);

        // Check file upload error message
        checkAlertMsg("Support Ticket File Upload");

        // Get file uploaded
        getTicketAttachmentListEle();
    }

    public Map.Entry<Boolean, String> checkAlertMsg(String label) {
        return checkAlertMsg(this::getAlertMsgBy, label);
    }

    public Map.Entry<Boolean, String> checkAlertMsg(Supplier<By> elementSupplier, String label) {
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", label.replace(" ", ""));

        // Element path
        By locator  = elementSupplier.get();
        if (locator == null) return new AbstractMap.SimpleEntry<>(false, null);

        // Get element by locator
        WebElement alertMsgEle = checkElementExists(locator);
        if (alertMsgEle == null) return new AbstractMap.SimpleEntry<>(false, null);

        // Get element class
        String cls = alertMsgEle.getAttribute("class");
        if (cls == null || cls.isEmpty()) return new AbstractMap.SimpleEntry<>(false, null);
        cls = cls.trim().toLowerCase();

        // Get message content element text
        String alertMsg = alertMsgEle.findElement(By.tagName("p")).getText();
        LogUtils.info(label + " Resp Msg: " + alertMsg);

        boolean bIsSuccessMsg = cls.contains("success");
        boolean bIsErrorMsg = cls.contains("error") || cls.contains("warning");

        // Check empty on msg
        if (alertMsg == null || alertMsg.isEmpty()) {
            Assert.fail(String.format("%s %s but response message is empty", label, (bIsSuccessMsg ? "success" : "failed")));
        }

        // Check for error / warning msg
        if(bIsErrorMsg) {
            Assert.fail(label + " failed. Resp Msg: " + alertMsg);
        }

        // Check for success msg
        if(bIsSuccessMsg) {
            return new AbstractMap.SimpleEntry<>(true, alertMsg);
        }

        return new AbstractMap.SimpleEntry<>(false, null);
    }

}
