package newcrm.pages.clientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.LogUtils;

public class RegisterGoldEntryPage extends RegisterEntryPage {

    public RegisterGoldEntryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getSendCodeBtnEle() {
        return assertElementExists(By.xpath("//button[@data-testid='sendOtp']"), "Send Code button");
    }

    @Override
    public boolean setRegisterInterface(String registerInterface) {
        String value;
        Select select = this.getRegisterSelect();
        if(select == null) {
            LogUtils.info("WARNING ** RegisterEntryPage: Do not find regulator element");
            return false;
        }

//        value = "/web-api/api/client/registration/register";
        value = "/hgw/user-api/bsn/registration/goldRegister";

        select.selectByValue(value.trim());
        LogUtils.info("RegisterEntryPage: set registerInterface to: " + value.trim());
        return true;
    }

}
