package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinishPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class AUFinishPage extends FinishPage {
    public AUFinishPage(WebDriver driver) {
        super(driver);
    }
    @Override
    public String getResponse() {
        WebElement e = assertVisibleElementExists(By.xpath("//div[@datatestid='poaAuthentication']"), "Identity verification Review");
        String result = e.getText();
        LogUtils.info("FinishPage: response message:\n" + result);
        return result;
    }

}
