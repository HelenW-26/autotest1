package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class VJPAccountConfigurationPage extends AccountConfigurationPage {

    public VJPAccountConfigurationPage(WebDriver driver) {
        super(driver);
    }

    JavascriptExecutor js = (JavascriptExecutor)driver;

    @Override
    protected List<WebElement> getCurrencyEles(){
        return driver.findElements(By.xpath("//ul[@class='accountConfiguration special_type']//li"));
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
