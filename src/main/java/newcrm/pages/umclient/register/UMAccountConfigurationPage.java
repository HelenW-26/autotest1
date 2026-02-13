package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class UMAccountConfigurationPage extends AccountConfigurationPage {

    public UMAccountConfigurationPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void tickBox() {
        WebElement e = getAgreementEle();
        String class_value = e.getAttribute("class");
        if(class_value.contains("active")) {
            LogUtils.info("AccountConfigurationPage: Already ticked agreement");
            return;
        }
        e.click();
        LogUtils.info("AccountConfigurationPage: Tick Agreement");
    }

}
