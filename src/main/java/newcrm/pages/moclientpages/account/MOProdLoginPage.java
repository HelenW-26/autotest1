package newcrm.pages.moclientpages.account;

import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MOProdLoginPage extends LoginPage {

    public MOProdLoginPage(WebDriver driver, String url) {
        super(driver,url);
    }

    @Override
    public void submit() {
        WebElement btn = this.getSubmit();
        triggerElementClickEvent_withoutMoveElement(btn);
        LogUtils.info("Click Email Login button");
        this.refresh();
    }

}
