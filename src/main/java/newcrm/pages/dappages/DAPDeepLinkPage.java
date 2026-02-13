package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

public class DAPDeepLinkPage extends Page {

    public DAPDeepLinkPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getTargetPageDropdownEle() {
        return assertElementExists(By.xpath("(//div[@class='link-card__content'] //div[@class='ht-select ht-select--large'])[1]"), "Target Page Dropdown");
    }

    protected WebElement getRegisterOptionEle() {
        return assertElementExists(By.xpath("//span[text()='Register']"), "Register Dropdown Option");
    }

    protected WebElement getLanguageDropdownEle() {
        return assertElementExists(By.xpath("(//div[@class='link-card__content'] //div[@class='ht-select ht-select--large'])[2]"), "Language Dropdown");
    }

    protected WebElement getEnglishAPACELe() {
        return assertElementExists(By.xpath("//div[@class='ht-scrollbar'] //span[text()='English [APAC]']"), "English [APAC] Dropdown Option");
    }

    protected WebElement getWebLinkele() {
        return assertElementExists(By.xpath("//div[@class='link-card__content'] //span[@class='copy-content__text']"), "Web Link");
    }

    public String getLiveAccRegistrationDeepLink(){
        triggerClickEvent_withoutMoveElement(getTargetPageDropdownEle());
        triggerClickEvent_withoutMoveElement(getRegisterOptionEle());

        triggerClickEvent_withoutMoveElement(getLanguageDropdownEle());
        triggerClickEvent_withoutMoveElement(getEnglishAPACELe());

        return getWebLinkele().getText();
    }


}
