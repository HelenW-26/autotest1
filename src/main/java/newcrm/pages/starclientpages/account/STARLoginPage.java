package newcrm.pages.starclientpages.account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PORTAL_LANGUAGE;
import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class STARLoginPage extends LoginPage {

    public STARLoginPage(WebDriver driver, String url) {
        super(driver,url);
    }

    @Override
    protected WebElement getCountryCodeSearchEle() {
        return assertElementExists(By.xpath("//input[@name='search']"), "Phone Country Code Search Box");
    }

    @Override
    protected WebElement getEmailTOTPSubmitBtnEle() {
        return assertElementExists(By.xpath("//div[@class='main_verification']//button"), "Email TOTP Submit button");
    }

    @Override
    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.el-message--error > p");
    }

    @Override
    public void setWebLanguage(PORTAL_LANGUAGE language) {
        // Get Language Icon
        WebElement languageIcon = getLanguageIconEle();
//        String ddlId = languageIcon.getDomAttribute("aria-describedby");

        String ddlId = (String) js.executeScript("return arguments[0].getAttribute('aria-describedby');", languageIcon);

        // Get Language List by id
        WebElement languageList = getLanguageListEle(ddlId);
        // Check if language list is opened
        String style = languageList.getAttribute("style");
        if(style.contains("display")){
            triggerElementClickEvent_withoutMoveElement(languageIcon);
        }

        // Check active language same as expected language
        WebElement activeLanguage = getActiveLanguageEle(languageList);
        if(activeLanguage.getAttribute("data-testid").trim().equalsIgnoreCase(language.getTestId())) {
            LogUtils.info(language.getDesc() + " language is in use. No need to switch.");
            return;
        }

        // Set language
        WebElement languageItem = getLanguageItemEle(languageList, language);
        triggerElementClickEvent(languageItem);
        LogUtils.info("Change to language: " + language.getDesc());
    }

}
