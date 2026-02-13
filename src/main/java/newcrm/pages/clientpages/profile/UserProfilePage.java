package newcrm.pages.clientpages.profile;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class UserProfilePage extends Page {

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getProfileBoxContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.myProfile_box"), "Profile Info Content");
    }

    protected WebElement getProfileTabListEle() {
        return assertVisibleElementExists(By.cssSelector(".myProfile_box + div.ht-tabs"), "Profile Tab List");
    }

    protected WebElement getProfileSecurityMgmtContentEle() {
        return assertVisibleElementExists(By.cssSelector("#securityManagement"), "Security Management Content");
    }

    protected WebElement getProfileVerificationContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.verification_card"), "Verification Content");
    }

    protected WebElement getProfileVTabContentEle(String id, String desc) {
        return assertVisibleElementExists(By.cssSelector("#" + id), "Verification (" + desc + ") Content");
    }

    protected WebElement getProfileEmailEle() {
        return assertElementExists(By.cssSelector("div.myProfile_box svg.ht-icon-email + p"), "Profile Email");
    }

    protected WebElement getProfilePhoneEle() {
        return assertElementExists(By.cssSelector("div.myProfile_box svg.ht-icon-phone + p"), "Profile Phone");
    }

    protected WebElement getProfileSecurityMgmtTabEle() {
        return assertElementExists(By.cssSelector("[id='tab-/profile/securityManagement']"), "Security Management Tab");
    }

    protected WebElement getProfileVerificationTabEle() {
        return assertElementExists(By.cssSelector("[id='tab-/profile/verification']"), "Verification Tab");
    }

    protected WebElement getProfileVStandardTabEle() {
        return assertElementExists(By.cssSelector("[id='tab-standard']"), "Verification (Standard) Tab");
    }

    protected WebElement getProfileVPlusTabEle() {
        return assertElementExists(By.cssSelector("[id='tab-plus']"), "Verification (Plus) Tab");
    }

    protected List<WebElement> getProfileVTabContentListEle(WebElement parentEle, String desc) {
        return assertElementsExists(By.cssSelector("li"), "Verification (" + desc + ") Content Item", parentEle);
    }

    protected WebElement getProfileVTabContentItemTitleEle(WebElement parentEle, String desc) {
        return assertElementExists(By.cssSelector("div.verification_title"), "Verification (" + desc + ") Content Item Title", parentEle);
    }

    protected WebElement getProfileVTabContentItemVerifyBtnEle(WebElement parentEle, String desc, String title) {
        return checkElementExists(By.cssSelector("div.verification_btn button"), parentEle);
    }

    protected WebElement getProfileSecurityEmailEle() {
        return assertElementExists(By.cssSelector("#securityManagement div.email"), "Profile Email (Security)");
    }

    protected WebElement getProfileSecurityPhoneEle() {
        return assertElementExists(By.cssSelector("#securityManagement div.phone"), "Profile Phone (Security)");
    }

    public String getProfileEmail() {
        WebElement e = getProfileEmailEle();
        String val = e.getText().trim();
        LogUtils.info(String.format("Profile Email: %s", val));
        return val;
    }

    public String getProfilePhone() {
        WebElement e = getProfilePhoneEle();
        String val = e.getText().trim();
        LogUtils.info(String.format("Profile Phone: %s", val));
        return val;
    }

    public String getProfileSecurityEmail() {
        WebElement e = getProfileSecurityEmailEle();
        String val = e.getText().trim();
        LogUtils.info(String.format("Profile Email (Security): %s", val));
        return val;
    }

    public String getProfileSecurityPhone() {
        WebElement e = getProfileSecurityPhoneEle();
        String val = e.getText().trim();
        LogUtils.info(String.format("Profile Email (Phone): %s", val));
        return val;
    }

    public void clickProfileSecurityMgmtTab() {
        WebElement e = getProfileSecurityMgmtTabEle();
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Click Security Management Tab");
    }

    public void clickProfileVerificationTab() {
        WebElement e = getProfileVerificationTabEle();
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Click Verification Tab");
    }

    public WebElement clickProfileVStandardTab() {
        WebElement tabEle = getProfileVStandardTabEle();
        String cls = tabEle.getAttribute("class");

        if(!cls.contains("is-active")){
            triggerElementClickEvent_withoutMoveElement(tabEle);
            LogUtils.info("Click Verification - Standard Tab");
        }

        return tabEle;
    }

    public WebElement clickProfileVPlusTab() {
        WebElement tabEle = getProfileVPlusTabEle();
        String cls = tabEle.getAttribute("class");

        if(!cls.contains("is-active")){
            triggerElementClickEvent_withoutMoveElement(tabEle);
            LogUtils.info("Click Verification - Plus Tab");
        }

        return tabEle;
    }

    public void clickProfileVStandardTabVerifyBtn(int i) {
        clickProfileVTabVerifyBtn(i, "Standard", getProfileVStandardTabEle());
    }

    public void clickProfileVPlusTabVerifyBtn(int i) {
        clickProfileVTabVerifyBtn(i, "Plus", getProfileVPlusTabEle());
    }

    public void clickProfileVTabVerifyBtn(int i, String tabDesc, WebElement tabEle) {
        String tabId = tabEle.getAttribute("aria-controls");

        // Get tab content based on parent id
        WebElement tabContentEle = getProfileVTabContentEle(tabId, tabDesc);
        List<WebElement> contentItemList = getProfileVTabContentListEle(tabContentEle, tabDesc);

        WebElement contentItemEle = contentItemList.get(i);
        WebElement titleEle = getProfileVTabContentItemTitleEle(contentItemEle, tabDesc);
        String titleDesc = titleEle.getText().trim();

        WebElement verifyBtnEle = getProfileVTabContentItemVerifyBtnEle(contentItemEle, tabDesc, titleDesc);
        verifyBtnEle.click();
        LogUtils.info("Click Verification (" + tabDesc + ") " + titleDesc + " Verify Now button");
        waitLoading();
        waitButtonLoader();
        waitLoader();
    }

    public void waitLoadingProfileBoxContent() {
        getProfileBoxContentEle();
    }

    public void waitLoadingProfileTabListContent() {
        getProfileTabListEle();
    }

    public void waitLoadingProfileSecurityMgmtContent() {
        getProfileSecurityMgmtContentEle();
    }

    public void waitLoadingProfileVerificationContent() {
        getProfileVerificationContentEle();
    }

    public void checkProfileVStandardContent(boolean[] visibleFlags, boolean[] enableFlags) {
        String tabDesc = "Standard";

        System.out.println("***Check Verification (" + tabDesc + ") Content***");

        WebElement tabEle = clickProfileVStandardTab();
        String tabId = tabEle.getAttribute("aria-controls");

        // Get tab content based on parent id
        WebElement tabContentEle = getProfileVTabContentEle(tabId, tabDesc);
        List<WebElement> contentItemList = getProfileVTabContentListEle(tabContentEle, tabDesc);

        // Check tab content button behavior
        for (int i = 0; i < contentItemList.size() && i < enableFlags.length; i++) {
            boolean isVisible = visibleFlags[i];
            boolean isEnable = enableFlags[i];

            WebElement titleEle = getProfileVTabContentItemTitleEle(contentItemList.get(i), tabDesc);
            String titleDesc = titleEle.getText().trim();
            WebElement verifyBtnEle = getProfileVTabContentItemVerifyBtnEle(contentItemList.get(i), tabDesc, titleDesc);

            // Check button visibility
            if (isVisible && verifyBtnEle == null) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button not found");
            }

            if (!isVisible && verifyBtnEle != null) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button found. Expected: Hidden from UI");
            }

            // Check button enable status
            if (isEnable && verifyBtnEle != null && !verifyBtnEle.isEnabled()) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button is disabled");
            }

            LogUtils.info("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button, Visible Status: " + isVisible + ", Enable Status: " + isEnable);
        }
    }

    public void checkProfileVPlusContent(boolean[] visibleFlags, boolean[] enableFlags) {
        String tabDesc = "Plus";

        System.out.println("***Check Verification (" + tabDesc + ") Content***");

        WebElement tabEle = clickProfileVPlusTab();
        String tabId = tabEle.getAttribute("aria-controls");

        // Get tab content based on parent id
        WebElement tabContentEle = getProfileVTabContentEle(tabId, tabDesc);
        List<WebElement> contentItemList = getProfileVTabContentListEle(tabContentEle, tabDesc);

        // Check tab content button behavior
        for (int i = 0; i < contentItemList.size() && i < enableFlags.length; i++) {
            boolean isVisible = visibleFlags[i];
            boolean isEnable = enableFlags[i];

            WebElement titleEle = getProfileVTabContentItemTitleEle(contentItemList.get(i), tabDesc);
            String titleDesc = titleEle.getText().trim();
            WebElement verifyBtnEle = getProfileVTabContentItemVerifyBtnEle(contentItemList.get(i), tabDesc, titleDesc);

            // Check button visibility
            if (isVisible && verifyBtnEle == null) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button not found");
            }

            if (!isVisible && verifyBtnEle != null) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button found. Expected: Hidden from UI");
            }

            // Check button enable status
            if (isEnable && verifyBtnEle != null && !verifyBtnEle.isEnabled()) {
                Assert.fail("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button is disabled");
            }

            LogUtils.info("Verification (" + tabDesc + ") " + titleDesc + " Verify Now button, Visible Status: " + isVisible + ", Enable Status: " + isEnable);
        }
    }

}
