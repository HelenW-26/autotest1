package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMRegisterDemoEntryPage extends RegisterDemoEntryPage {

    public UMRegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getFirstNameInput() {
        return assertVisibleElementExists(By.id("name"), "Name");
    }

    @Override
    protected WebElement getOpenAccountVerifyBtn() {
        return assertElementExists(By.xpath("//button[@data-testid='button' and contains(@class, 'register_guide_btn')]"), "Verification button");
    }

    @Override
    public boolean setCountry(String country) {
        WebElement e = getCountryInput();
        e.click();
        setInputValue_withoutMoveElement(e, country);
        getCountryListItemEle(country).click();
        LogUtils.info("RegisterEntryPage: set Country to: " + country);
        return true;
    }


}
