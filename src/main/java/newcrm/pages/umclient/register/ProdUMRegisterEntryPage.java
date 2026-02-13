package newcrm.pages.umclient.register;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.LogUtils;

public class ProdUMRegisterEntryPage extends UMRegisterEntryPage{

    public ProdUMRegisterEntryPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.id("button"));
    }

    @Override
    protected WebElement getSendCodeBtnEle() {
        return assertElementExists(By.xpath("//button[@class='send_btn']"), "Send Code button");
    }

    //um prod have three new terms and conditions tickbox need to click
    public void checkTerms()
    {
        driver.findElement(By.xpath("//input[@class='from_text_ltd01']")).click();
        driver.findElement(By.xpath("//input[@class='from_text_ltd02']")).click();
        driver.findElement(By.xpath("//input[@class='from_text_ltd03']")).click();
    }

    @Override
    public boolean setUserType(GlobalProperties.USERTYPE type) {
        Select select = this.getUserTypeSelect();
        if(select == null) {
            LogUtils.info("WARNING ** RegisterEntryPage: Do not find user type element");
            return false;
        }
        select.selectByValue(type.toString());
        LogUtils.info("RegisterEntryPage: set user type to: " + type.toString());

        checkTerms();
        return true;
    }

    @Override
    public void checkAgreeCommunication()
    {

        WebElement checkbox = driver.findElement(By.xpath("//input[@id='agree_box']"));
        //checkbox.click();
        js.executeScript("arguments[0].click()", checkbox);
    }

    @Override
    public void sendCode(String code)
    {
        WebElement emailCode = driver.findElement(By.xpath("//input[@id='email_code']"));
        emailCode.sendKeys(code);
        waitLoading();
    }
}
