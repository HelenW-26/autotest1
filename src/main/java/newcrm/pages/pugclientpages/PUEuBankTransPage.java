package newcrm.pages.pugclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PUEuBankTransPage extends DepositBasePage {

    public PUEuBankTransPage(WebDriver driver) {
        super(driver);
    }

    public void agreeImportantNote() {
        WebElement importantNote_element = driver.findElement(By.xpath("//label[@data-testid='isAgreeImportantNote']"));

        // Select the checkbox when not selected. When checkbox is selected, 'is-checked' class is added to checkbox.
        String classAttribute = importantNote_element.getAttribute("class");
        if (classAttribute != null && !classAttribute.contains("is-checked")) {
            importantNote_element.click();
        }

        GlobalMethods.printDebugInfo("Agree on Important Notes");
    }

}
