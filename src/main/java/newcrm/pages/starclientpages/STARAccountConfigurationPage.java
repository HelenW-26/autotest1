package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class STARAccountConfigurationPage extends AccountConfigurationPage {

    public STARAccountConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getAccountTypeEles(){
        return driver.findElements(By.xpath("(//div[@class='box-inner'])[2]//ul/li"));
    }

    @Override
    protected List<WebElement> getCurrencyEles(){
        return driver.findElements(By.xpath("(//div[@class='box-inner'])[3]//ul/li"));
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
