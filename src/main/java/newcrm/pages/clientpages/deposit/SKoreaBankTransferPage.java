package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.elements.DepositPageCommElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SKoreaBankTransferPage extends InterBankTransPage{
    public SKoreaBankTransferPage(WebDriver driver) {
        super(driver);
    }
    @Override
    public void setEmail(String email) {

        //WebElement amount_element = driver.findElement(By.xpath("//input[@id='attachVariables.0.value'] | //input[@data-testid='psp_account']"));
    	WebElement amount_element = this.findClickableElemntByTestId("email");
        amount_element.clear();
        amount_element.sendKeys(email);
        GlobalMethods.printDebugInfo("DepositBasePage: Set amount to: " + email);
    }
}
