package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;
import newcrm.pages.clientpages.elements.DepositPageCommElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class IndiaUPIPage extends DepositBasePage {

    public IndiaUPIPage(WebDriver driver){
        super(driver);
    }

    @Override
    public void goBack() {
        driver.navigate().back();
        driver.navigate().back();
        this.waitLoading();
    }

}
