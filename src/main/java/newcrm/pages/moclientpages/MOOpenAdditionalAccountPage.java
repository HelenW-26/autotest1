package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.OpenAdditionalAccountPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MOOpenAdditionalAccountPage extends OpenAdditionalAccountPage {

    public MOOpenAdditionalAccountPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getAgreementEle() {
        return assertClickableElementExists(By.xpath("//label[@data-testid='byTicking']"), "Agreement tick box");
    }

    @Override
    protected WebElement getSubmitBtn() {
        return assertClickableElementExists(By.cssSelector("div.account_configuration_btns button"), "Submit button");
    }

    @Override
    protected WebElement getOpenAccountConfirmationEle() {
        return checkElementExists(By.xpath("//div[@id='openAccount' and not(contains(@style,'display'))]"));
    }

    @Override
    protected WebElement getOpenAccountResponseContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.result_info > div"), "Open Account Response Content");
    }

    @Override
    protected String getOpenAccountResponseMessage() {
        return "now able to start funding and trading";

    }

    @Override
    protected WebElement getOpenAccountResponseContentBtn() {
        return assertClickableElementExists(By.cssSelector("a[data-testid='depositNow'] > button"), "View My Accounts button");
    }

    @Override
    public void setNote(String note) {
        WebElement e =  getNoteEle();
        setInputValue_withoutMoveElement(e, note);
        LogUtils.info("OpenAdditionalAccountPage: set Note to: " + note);
    }

    @Override
    public void waitLoadingAccConfigContent() {
        assertVisibleElementExists(By.cssSelector("#openAccount div.account_configuration_container"),"Open Account Content");
    }

}
