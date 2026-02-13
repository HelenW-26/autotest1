package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class AUAccountConfigurationPage extends AccountConfigurationPage {

    public AUAccountConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getCurrencyEles(){
        //return driver.findElements(By.xpath("//ul[@class='accountConfiguration currency_container']//li"));
        return driver.findElements(By.xpath("//ul[@class='accountConfiguration special_type']//li"));
    }

//    @Override
//    public String setCurrency(GlobalProperties.CURRENCY currency) {
//        triggerElementClickEvent(getCurrencyEle());
//        waitLoadingOpendElements();
//        triggerElementClickEvent_withoutMoveElement(getCurrencyItemEle(currency));
//        LogUtils.info("AccountConfigurationPage: set Currency to: " + currency.getCurrencyDesc());
//        return currency.getCurrencyDesc();
//    }

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
