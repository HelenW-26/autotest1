package newcrm.pages.vjpclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class VJPRegisterDemoEntryPage extends RegisterDemoEntryPage {

    public VJPRegisterDemoEntryPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getTnCEle() {
        return assertElementExists(By.cssSelector("input#rulesAgreed"), "Terms of Use");
    }

    protected WebElement getPolicyEle() {
        return assertElementExists(By.cssSelector("input#InformationAgreed"), "Protection Policy");
    }

    protected WebElement getReceiveDealsEle() {
        return assertElementExists(By.cssSelector("input#receiveAgreed"), "Agree Receive Deals");
    }

    @Override
    public void agreeTnC() {
        getTnCEle().click();
        LogUtils.info("Agree Terms of Use");
    }

    @Override
    public void agreePolicy() {
        getPolicyEle().click();
        LogUtils.info("Agree Protection Policy");
    }

    @Override
    public void agreeReceiveDeals() {
        getReceiveDealsEle().click();
        LogUtils.info("Agree Receive Deals");
    }

}
